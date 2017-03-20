package com.example.coolweather.model;

/**
 * Created by dengm on 17-3-20.
 */

public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;


    public int getId(){
        return this.id;
    }
    public String getProvinceName(){
        return this.provinceName;
    }
    public String getProvinceCode(){
        return this.provinceCode;
    }
    public void setId(int id){
        this.id=id;

    }
    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }
    public void setProvinceCode(String provinceCode){
        this.provinceCode=provinceCode;
    }
}

