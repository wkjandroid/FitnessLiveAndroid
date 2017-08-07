package com.example.wkj_pc.fitnesslive.tools;

import okhttp3.OkHttpClient;

/**
 * Created by wkj_pc on 2017/8/6.
 */

public class OkHttpClientFactory {
    private static OkHttpClient client;
    private OkHttpClientFactory(){
         client= new OkHttpClient();
    }
    public static OkHttpClient getOkHttpClientInstance(){
        if (null==client)
            new OkHttpClientFactory();
        return client;
    }
}
