package com.example.wkj_pc.fitnesslive;

import android.app.Application;
import android.content.Context;
import com.example.wkj_pc.fitnesslive.po.User;
import com.example.wkj_pc.fitnesslive.tools.LogUtils;

import org.litepal.LitePal;

/**
 * Created by wkj_pc on 2017/6/11.
 */

public class MainApplication extends Application {
    public static String cookie;   //qq用户的登录信息
    public static User loginUser;   //普通用户的登录信息

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(getApplicationContext());
        LitePal.getDatabase();
    }

    public static Context getContext(){
        return getContext();
    }

}
