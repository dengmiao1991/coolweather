package com.example.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;
import com.example.coolweather.model.City;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpCallBacklistener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengm on 17-3-20.
 */

public class ChooseAreaActivity extends Activity implements AdapterView.OnItemClickListener {
    public static int LEVEL_PROVINCE=0;
    public static int LEVEL_CITY=1;
    public static int LEVEL_COUNTY=2;
    private TextView mTitleTv;
    private ListView mAreaLv;
    private List<Province>provinceList;
    private List<City>cityList;
    private List<County>countyList;
    private ArrayAdapter<String>mAdapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String>dataList=new ArrayList<String>();
    private int currentLevel;
    private Province selectedProvince;
    private City selectedCity;
    private ProgressDialog progressDialog;

    private  static int flag=0;
    private int dif;
    private String str;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean("city_selected",false)){
            startActivity(new Intent(this,WeatherActivity.class));
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        mAreaLv= (ListView) findViewById(R.id.area_Lv);
        mTitleTv= (TextView) findViewById(R.id.titleTv);
        mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        mAreaLv.setAdapter(mAdapter);
        coolWeatherDB=CoolWeatherDB.getInstance(this);
        mAreaLv.setOnItemClickListener(this);
        queryProvinces();
    }

    private void queryProvinces() {
        provinceList=coolWeatherDB.loadProvinces();
        if (provinceList.size()>0){
            dataList.clear();
            for(Province pro:provinceList){
                dataList.add(pro.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mAreaLv.setSelection(0);
            mTitleTv.setText("中国");
            currentLevel=LEVEL_PROVINCE;

        }else{
            //load from server
            queryFromServer(null,"province");
        }

    }
    private void queryCities(){
        cityList=coolWeatherDB.loadCities(selectedProvince.getId());
        if(cityList.size()>0){
            dataList.clear();
            for (City c:cityList){
                dataList.add(c.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mAreaLv.setSelection(0);
            mTitleTv.setText(selectedProvince.getProvinceName());
            currentLevel=LEVEL_CITY;
        }else{
          queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }

    private void queryCounties(){
        countyList=coolWeatherDB.loadCounties(selectedCity.getId());
        if(countyList.size()>0){
            dataList.clear();
            for (County c:countyList){
                Log.e("queryCounties", "queryCounties: "+c.getCountyName());
                dataList.add(c.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mTitleTv.setText(selectedCity.getCityName());
            mAreaLv.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    private void queryFromServer(String code, final String type) {
        String address;
        if(!TextUtils.isEmpty(code)){
            address="http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else{
            address="http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallBacklistener() {
            @Override
            public void onError(Exception e) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         closeProgressDialog();
                         Toast.makeText(ChooseAreaActivity.this,"加载失败！",Toast.LENGTH_LONG).show();
                     }
                 });
            }

            @Override
            public void onFinish(String response) {
                Log.e("onFinish", "onFinish: "+response);
                boolean result=false;
                if ("province".equals(type)){
                    result= Utility.handleProvinceResponse(response,coolWeatherDB);

                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(response,coolWeatherDB,selectedProvince.getId());

                }else if("county".equals(type)){
                    result=Utility.handleCountyResponse(response,coolWeatherDB,selectedCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    private void showProgressDialog() {
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           if(currentLevel==LEVEL_PROVINCE){
               selectedProvince=provinceList.get(position);
               queryCities();
           }else if(currentLevel==LEVEL_CITY){
               selectedCity=cityList.get(position);
               queryCounties();
           }else if(currentLevel==LEVEL_COUNTY){
               String countyCode=countyList.get(position).getCountyCode();
               Intent i=new Intent(this,WeatherActivity.class);
               i.putExtra("county_code",countyCode);
               startActivity(i);

           }
    }

    @Override
    public void onBackPressed() {
       if(currentLevel==LEVEL_COUNTY){
           queryCities();
       }else if(currentLevel==LEVEL_CITY){
           queryProvinces();
       }else{
           finish();
       }

    }

}
