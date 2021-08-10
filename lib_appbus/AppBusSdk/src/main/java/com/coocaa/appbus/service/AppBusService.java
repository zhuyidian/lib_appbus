package com.coocaa.appbus.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.coocaa.appbus.AppBus;
import com.coocaa.appbus.traffic.AppBusAidl;
import com.coocaa.appbus.traffic.AppBusCallback;
import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.utils.LogUtil;

import java.util.List;

public abstract class AppBusService extends Service {

    protected abstract List<AppInfoBean> onGetAppInfo();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("service","onCreate"+", Thread="+Thread.currentThread().toString());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("service","onStartCommand intent"+intent+", flags="+flags+", startId="+startId);
        return START_STICKY/*super.onStartCommand(intent, flags, startId)*/;
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("service","onBind intent="+intent+", Thread="+Thread.currentThread().toString());
        return new AppBusAidlImpl();
    }

    @Override
    public void onDestroy() {
        LogUtil.d("service","onDestroy");
        AppBus.getInstance().killListener();
        super.onDestroy();
    }

    class AppBusAidlImpl extends AppBusAidl.Stub{
        @Override
        public Response run(Request request) throws RemoteException {
            return null;
        }

        @Override
        public void register(AppBusCallback cb, int pid) throws RemoteException {
            LogUtil.d("service","register callBack: cb="+cb+", client pid="+pid+", Thread="+Thread.currentThread().toString());
            if (cb != null) {
                AppBus.getInstance().registerListener(cb);
                cb.asBinder().linkToDeath(/*mDeathRecipient*/new DeathRecipientListen(cb),0);
            }
        }

        @Override
        public void unregister(AppBusCallback cb) throws RemoteException {
            LogUtil.d("service","unregister callBack: cb="+cb+", Thread="+Thread.currentThread().toString());
            if (cb != null) {
                AppBus.getInstance().unregisterListener(cb);
            }
        }

        @Override
        public List<AppInfoBean> getAppInfo() throws RemoteException {
            LogUtil.d("service","get app info"+", Thread="+Thread.currentThread().toString());
            List<AppInfoBean> infoList = onGetAppInfo();
            if (infoList == null) {
                LogUtil.d("service","get app infoList:null");
                return null;
            }
            LogUtil.d("service","get app infoList:"+infoList.size());
            return infoList;
        }
    }

    class DeathRecipientListen implements IBinder.DeathRecipient{
        private AppBusCallback cb = null;

        public DeathRecipientListen(AppBusCallback cb){
            this.cb = cb;
        }

        @Override
        public void binderDied() {
            LogUtil.d("service","callback is binderDied!!!!!");
            AppBus.getInstance().unregisterListener(cb);
        }
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            LogUtil.d("service","callback is binderDied!!!!!");
        }
    };

//    private boolean isCallbackAlive() {
//        return mHost != null && mHost.asBinder().isBinderAlive();
//    }
}
