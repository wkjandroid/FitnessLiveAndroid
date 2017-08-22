package com.example.wkj_pc.fitnesslive.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LiveService extends Service {
    public LiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
