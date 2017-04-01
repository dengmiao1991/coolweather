package com.example.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.util.HttpCallBacklistener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

/**
 * Created by dengm on 17-3-22.
 */

public class WeatherActivity extends Activity {
    private TextView mTitleTv,mPublishTimeTv,mSwitchCityTv,mRefrashTv,mTimeTv,mWeatherTv,mTempTv;
    private LinearLayout mWeatherLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        initView();
    }

    private void initView() {
        mPublishTimeTv= (TextView) findViewById(R.id.publishTimeTv);
        mRefrashTv= (TextView) findViewById(R.id.refrashTv);
        mSwitchCityTv= (TextView) findViewById(R.id.switch_cityTv);
        mTempTv= (TextView) findViewById(R.id.tmpTv);
        mTimeTv= (TextView) findViewById(R.id.timeTv);
        mTitleTv= (TextView) findViewById(R.id.titleTv);
        mWeatherTv= (TextView) findViewById(R.id.weatherTv);
        mWeatherLinear= (LinearLayout) findViewById(R.id.weatherinfo_layout);
        String countyCode=getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            mPublishTimeTv.setText("同步中...");
            mWeatherLinear.setVisibility(View.INVISIBLE);
            mTitleTv.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);

        }else{
           showWeather();
        }



    }

    private void showWeather() {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        mPublishTimeTv.setText("今天"+sp.getString("publish_time","")+"发布");
        mTitleTv.setText(sp.getString("city_name",""));
        mTempTv.setText(sp.getString("temp1","")+"~"+sp.getString("temp2",""));
        mTimeTv.setText(sp.getString("current_date",""));
        mWeatherTv.setText(sp.getString("weather_desp",""));
        mWeatherLinear.setVisibility(View.VISIBLE);
        mTitleTv.setVisibility(View.VISIBLE);


    }

    private void queryWeatherCode(String countycode) {
        String address="http://www.weather.com.cn/data/list3/city"+countycode+".xml";
        queryFromServer(address,"countyCode");
    }

    private void queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallBacklistener() {
            @Override
            public void onError(Exception e) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mPublishTimeTv.setText("同步失败!");
                     }
                 });
            }

            @Override
            public void onFinish(String response) {
                Log.e("onFinish", "onFinish: "+response);
                  if ("countyCode".equals(type)){
                      if(!TextUtils.isEmpty(response)){
                          String[]array=response.split("\\|");
                          if(array!=null&&array.length==2){
                              String weatherCode=array[1];
                              queryWeatherInfo(weatherCode);
                          }
                      }
                  }else if("weatherCode".equals(type)){
                      Utility.handleWeatherResponse(response,WeatherActivity.this);
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              showWeather();
                          }
                      });
                  }
            }
        });
    }

    private void queryWeatherInfo(String weatherCode) {
       String address="http://www.weather.com.cn/data/cityinfo"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }
}
