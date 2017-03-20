package com.example.coolweather.model;

/**
 * Created by dengm on 17-3-20.
 */

public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    public int getId(){
        return this.id;
    }
    public String getCountyName(){
        return this.countyName;
    }
    public String getCountyCode(){
        return this.countyCode;
    }
    public int getCityId(){
        return this.cityId;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setCountyName(String countyName){
        this.countyName=countyName;
    }
    public void setCountyCode(String countyCode){
        this.countyCode=countyCode;
    }
    public void setCityId(int cityId){
        this.cityId=cityId;
    }

}
