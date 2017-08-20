package com.example.wkj_pc.fitnesslive.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.po.User;
import com.example.wkj_pc.fitnesslive.tools.GsonUtils;
import com.example.wkj_pc.fitnesslive.tools.LoginUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginService extends Service {
    private String longRequestUrl;
    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }
    @Override
    public void onCreate() {
        longRequestUrl=getResources().getString(R.string.app_longrequest_url);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginUtils.longRequestServer(longRequestUrl, MainApplication.loginUser.getAccount(), MainApplication.cookie, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        MainApplication.loginUser=null;
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        User loginUser = GsonUtils.getGson().fromJson(responseBody, User.class);
                        MainApplication.loginUser=loginUser;
                    }
                });
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime= SystemClock.elapsedRealtime()+25*60*1000;
        Intent intent1=new Intent(this,LoginService.class);
        PendingIntent pi=PendingIntent.getService(this,0,intent1,0);
        if (Build.VERSION.SDK_INT>=23) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, triggerTime, pi);
        }
        else {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, triggerTime, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
