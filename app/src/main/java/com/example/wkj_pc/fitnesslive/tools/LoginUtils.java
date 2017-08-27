package com.example.wkj_pc.fitnesslive.tools;

import java.io.IOException;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wkj_pc on 2017/6/13.
 */

public class LoginUtils {


    public static void getUserInfoWithWeibo(String url, Callback callback){
         Request request=new Request.Builder().url(url)
                 .build();
         OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }

    public static void toRequestServerForLogin(String requestUrl, String userinfo, String cookie, Callback callback) {
        Request request=null;
        RequestBody body=new FormBody.Builder()
                .add("user",userinfo).build();
        if (null == cookie) {
            request = new Request.Builder().url(requestUrl).post(body).build();
        } else {
            request = new Request.Builder().addHeader("cookie", cookie)
                    .url(requestUrl).post(body).build();
        }
        OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }
    /** 登录成功后定期获取用户信息*/
    public static void longRequestServer(String path,String account,String cookie, Callback callback) {
        RequestBody body=new FormBody.Builder()
                .add("account",account).build();
        Request request = new Request.Builder().url(path)
                    .post(body).addHeader("cookie",cookie).build();
        OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }
    /*退出登录请求*/
    public static void toRequestQuitLogin(String requestUrl,String content, String cookie) {
        RequestBody body=new FormBody.Builder()
                .add("user",content).build();
        Request request=null;
        if (null != cookie){
            request = new Request.Builder().url(requestUrl)
                    .post(body).addHeader("cookie",cookie).build();
        }else {
            request = new Request.Builder().url(requestUrl)
                    .post(body).build();
            try {
                OkHttpClientFactory.getOkHttpClientInstance().newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /** 获取主页用户的的直播信息进行展示 */
    public static void longGetUserLiveInfosFromServer(String getHomeLiveUserInfoUrl, Callback callback) {
        Request request=new Request.Builder().url(getHomeLiveUserInfoUrl)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }
    /** 获取主页用户的的直播风格进行展示 */
    public static void longGetUserLiveTagFromServer(String getHomeLiveTagUrl, Callback callback) {
        Request request=new Request.Builder().url(getHomeLiveTagUrl)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }
    /** 请求服务器获取信息验证码 */
    public static void getMobileVerifyCodeFromServer(String getVerifycodeUrl, Callback callback) {
        Request request=new Request.Builder().url(getVerifycodeUrl)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }
    /** 用户修改密码 */
    public static void updateUserPassword(String getVerifycodeUrl ,String mobileNum,String password,Callback callback) {
        RequestBody body=new FormBody.Builder()
                .add("mobilenum",mobileNum)
                .add("password",password)
                .build();
        Request request=new Request.Builder().url(getVerifycodeUrl)
                .post(body)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }

    public static void registerUser(String registerUserUrl, String mobileNum, String password, Callback callback) {
        RequestBody body=new FormBody.Builder()
                .add("mobilenum",mobileNum)
                .add("password",password)
                .build();
        Request request=new Request.Builder().url(registerUserUrl)
                .post(body)
                .build();
        OkHttpClientFactory.getOkHttpClientInstance().newCall(request).enqueue(callback);
    }
}
