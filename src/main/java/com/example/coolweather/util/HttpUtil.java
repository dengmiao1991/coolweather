package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by dengm on 17-3-20.
 */

public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpCallBacklistener listener){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 HttpURLConnection conn=null;
                 try {
                     URL url=new URL(address);
                     conn= (HttpURLConnection) url.openConnection();
                     conn.setRequestMethod("GET");
                     conn.setConnectTimeout(8000);
                     conn.setReadTimeout(8000);
                     InputStream in=conn.getInputStream();
                     StringBuilder sb=new StringBuilder();
                     BufferedReader br=new BufferedReader(new InputStreamReader(in));
                     String line;
                     while ((line=br.readLine())!=null){
                         sb.append(line);
                     }
                     if (listener!=null){
                         listener.onFinish(sb.toString());
                     }
                 } catch (IOException e) {
                    if(listener!=null){
                        listener.onError(e);
                    }
                 }finally {
                     if(conn!=null)
                         conn.disconnect();
                 }
             }
         }).start();
    }

}
