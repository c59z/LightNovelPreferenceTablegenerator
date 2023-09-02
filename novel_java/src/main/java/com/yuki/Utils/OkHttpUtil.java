package com.yuki.Utils;

import okhttp3.*;

import java.io.IOException;

public class OkHttpUtil {
    // get请求
    public static String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(url)
            .build();
        
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // post请求
    public static String post(String url, String params) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), params);
        Request request = new Request.Builder()
            .url(url) 
            .post(body)
            .build();
            
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
