package com.example.coolweather.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.model.City;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by dengm on 17-3-20.
 */

public class Utility {
    public  static boolean handleProvinceResponse(String response, CoolWeatherDB coolWeatherDB){
        if(!TextUtils.isEmpty(response)){
            String []allProvinces=response.split(",");
            if(allProvinces!=null&&allProvinces.length>0){
                for (String pro:allProvinces){
                    String[]array=pro.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }

        }
        return false;

    }
    public static boolean handleCityResponse(String response,CoolWeatherDB coolWeatherDB,int provinceId){
        if (!TextUtils.isEmpty(response)){
            String []allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for(String c:allCities){
                    String[]array=c.split("\\|");
                    City city=new City();
                    city.setProvinceId(provinceId);
                    city.setCityName(array[1]);
                    city.setCityCode(array[0]);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;

    }

    public static boolean handleCountyResponse(String response,CoolWeatherDB coolWeatherDB,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[]allCounties=response.split(",");
            if(allCounties!=null&&allCounties.length>0){
                for(String s:allCounties){
                    Log.e("handleCountyResponse", "handleCountyResponse: "+s);
                    String []array=s.split("\\|");
                    Log.e("handleCountyResponse", "handleCountyResponse: "+array[0]+"   "+array[1]);
                    County county=new County();
                    county.setCityId(cityId);
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(String response, Context context){
        try {
            JSONObject jsonobjiect=new JSONObject(response);
            JSONObject weatherinfo=jsonobjiect.getJSONObject("weatherinfo");
            String cityName=weatherinfo.getString("city");
            String weatherCode=weatherinfo.getString("cityid");
            String temp1=weatherinfo.getString("temp1");
            String temp2=weatherinfo.getString("temp2");
            String weatherDesp=weatherinfo.getString("weather");
            String publishTime=weatherinfo.getString("ptime");
           saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.N)
    public static void saveWeatherInfo(Context context, String cityname, String weathercode, String temp1, String temp2, String weatherdesp, String ptime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityname);
        editor.putString("weather_code",weathercode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherdesp);
        editor.putString("publish_time",ptime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();

    }
}
