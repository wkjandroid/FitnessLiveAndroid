package com.example.wkj_pc.fitnesslive;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.example.wkj_pc.fitnesslive.po.User;
import com.example.wkj_pc.fitnesslive.receiver.NetWorkReceiver;

import org.litepal.LitePal;

/**
 * Created by wkj_pc on 2017/6/11.
 */

public class MainApplication extends Application {
    public static String cookie;
    public static User loginUser;   //普通用户的登录信息
    public static Boolean networkinfo;  //网络状态情况
    private NetWorkReceiver netWorkReceiver=new NetWorkReceiver();
    public static final String THEMID = "themId";
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(getApplicationContext());
        LitePal.getDatabase();
        //注册网络广播监听事件
        IntentFilter filter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(netWorkReceiver,filter);

    }

    public static Context getContext(){
        return getContext();
    }

}
