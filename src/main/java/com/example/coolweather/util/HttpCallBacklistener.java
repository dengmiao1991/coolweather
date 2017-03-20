package com.example.coolweather.util;

/**
 * Created by dengm on 17-3-20.
 */

public interface HttpCallBacklistener{
    void onError(Exception e);
    void onFinish(String response);
}
