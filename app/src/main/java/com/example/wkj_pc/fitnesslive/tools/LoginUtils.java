package com.example.wkj_pc.fitnesslive.tools;

import java.io.IOException;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wkj_pc on 2017/6/13.
 */

public class LoginUtils {
    private static OkHttpClient client=new OkHttpClient();

    public static void getUserInfoWithWeibo(String url,Callback callback){
         Request request=new Request.Builder().url(url)
                 .build();
         client.newCall(request).enqueue(callback);
    }


    public static void toRequestServerForLogin(String requestUrl, String userinfo, String cookie, Callback callback) {
        Request request;
        RequestBody body=new FormBody.Builder()
                .add("user",userinfo).build();
        if (null == cookie) {
            request = new Request.Builder().url(requestUrl).post(body).build();
        } else {
            request = new Request.Builder().addHeader("cookie", cookie)
                    .url(requestUrl).post(body).build();
        }
        client.newCall(request).enqueue(callback);
    }

    public static void longRequestServer(String path,String cookie) throws IOException {
        Request request=new Request.Builder().url(path).build();
        client.newCall(request).execute();
    }

    public static void toRequestQuitLogin(String requestUrl,String content, String cookie) {
        RequestBody body=new FormBody.Builder()
                .add("user",content).build();
        Request request=new Request.Builder().url(requestUrl)
                .post(body).addHeader("cookie",cookie).build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
