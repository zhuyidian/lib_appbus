package com.coocaa.appbus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.coocaa.appbus.AppBus;
import com.coocaa.appbus.traffic.AppBusCallback;
import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.NotifyAidl;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.utils.LogUtil;

import java.util.List;

public abstract class NotifyService extends Service {

    protected abstract void onNotifyAppInfo(List<AppInfoBean> list);

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("client","onCreate[Notify]"+", Thread="+Thread.currentThread().toString());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("client","onStartCommand[Notify] intent"+intent+", flags="+flags+", startId="+startId);
        return START_STICKY/*super.onStartCommand(intent, flags, startId)*/;
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("client","onBind[Notify] intent="+intent+", Thread="+Thread.currentThread().toString());
        return new NotifyAidlImpl();
    }

    @Override
    public void onDestroy() {
        LogUtil.d("client","onDestroy[Notify]");
        super.onDestroy();
    }

    class NotifyAidlImpl extends NotifyAidl.Stub{
        @Override
        public void notify(List<AppInfoBean> appInfoList) throws RemoteException {
            //这里需要保存到数据库
            LogUtil.d("client","notify[Notify] list.size="+(appInfoList!=null?appInfoList.size():"null")+", Thread="+Thread.currentThread().toString());
            onNotifyAppInfo(appInfoList);
        }
    }
}
