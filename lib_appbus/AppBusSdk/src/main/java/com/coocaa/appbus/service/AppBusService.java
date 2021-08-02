package com.coocaa.appbus.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.coocaa.appbus.AppBus;
import com.coocaa.appbus.core.AppBusAidlImpl;
import com.coocaa.appbus.utils.LogUtil;

public class AppBusService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("service","onCreate"+", Thread="+Thread.currentThread().toString());
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("service","onBind intent="+intent+", Thread="+Thread.currentThread().toString());
        return AppBus.getInstance().createAppBusImpl();
    }

    public static class NewXBusService extends AppBusService {
    }

}
