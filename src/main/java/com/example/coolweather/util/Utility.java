package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.model.City;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

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
                    String []array=s.split("\\|");
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
}
