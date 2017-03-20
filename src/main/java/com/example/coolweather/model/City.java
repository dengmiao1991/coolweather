package com.example.coolweather.model;

/**
 * Created by dengm on 17-3-20.
 */

public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public int getId(){
        return this.id;
    }
    public String getCityName(){
        return this.cityName;
    }
    public String getCityCode(){
        return this.cityCode;
    }
    public int getProvinceId(){
        return this.provinceId;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setCityName(String cityName){
        this.cityName=cityName;
    }
    public void setCityCode(String cityCode){
        this.cityCode=cityCode;
    }
    public void setProvinceId(int provinceId){
        this.provinceId=provinceId;
    }
}
