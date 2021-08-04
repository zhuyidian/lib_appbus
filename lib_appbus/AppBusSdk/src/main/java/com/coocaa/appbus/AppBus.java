package com.coocaa.appbus;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;

import com.coocaa.appbus.able.INotify;
import com.coocaa.appbus.able.IUserData;
import com.coocaa.appbus.core.Client;
import com.coocaa.appbus.joor.Reflect;
import com.coocaa.appbus.service.AppBusService;
import com.coocaa.appbus.thread.ThreadManager;
import com.coocaa.appbus.traffic.AppBusAidl;
import com.coocaa.appbus.traffic.AppBusCallback;
import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.NotifyAidl;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.utils.AndroidUtil;
import com.coocaa.appbus.utils.LogUtil;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class AppBus {
    public static final boolean IS_BUNDLE_DEBUG = true;
    private static AppBus instance = new AppBus();
    private Context mContext;

    public static AppBus getInstance() {
        return instance;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();
    }

    public void destroy(){
        mContext = null;
    }

    //-------------------------------server---------------------------------------------------------
    private Map<Class<?>, Class<?>> mRegisterClassMap = new HashMap<>();
    private IUserData userData;
    private final RemoteCallbackList<AppBusCallback> mRemoteCallbacks
            = new RemoteCallbackList<AppBusCallback>();

    /**
     * 注册需要被调用的class.
     *
     * @param face 接口
     * @param impl 实现类
     */
    public void register(Class<?> face, Class<?> impl) {
        mRegisterClassMap.put(face, impl);
        LogUtil.d("server","register: 接口:"+face + ", 类:" + impl + ", map size:" + mRegisterClassMap.size());
    }

    public <T> T getCreateObject(Class<?> face) {
        if (mRegisterClassMap.containsKey(face)) {
            Class<?> aClass = mRegisterClassMap.get(face);
            try {
                LogUtil.d("server","get map class: 接口:"+face+", 类:" + aClass);
                // TODO: 处理单例的类
                // TODO: 处理静态函数
                // TODO: 避免重复创建new
                return Reflect.on(aClass).create().get();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d("server","get map class: e="+e);
            }
        }
        LogUtil.d("server","map size: " + mRegisterClassMap.size());
        return null;
    }

    public void setUserImpl(IUserData userData){
        LogUtil.d("service","set UserImpl:"+userData);
        this.userData = userData;
    }

    public IUserData getUserImpl(){
        if (userData == null) {
            throw new RuntimeException(
                    "Impl未设置!");
        }
        return userData;
    }

    public void registerListener(AppBusCallback cb){
        if(mRemoteCallbacks!=null)mRemoteCallbacks.register(cb);
    }

    public void unregisterListener(AppBusCallback cb){
        if(mRemoteCallbacks!=null)mRemoteCallbacks.unregister(cb);
    }

    public void killListener(){
        if(mRemoteCallbacks!=null)mRemoteCallbacks.kill();
    }

    public void update(final List<AppInfoBean> appInfoList){
        LogUtil.d("service","update appInfoList:"+(appInfoList!=null?appInfoList.size():"null"));
        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("service","[Notify] update: mRemoteCallbacks size="+mRemoteCallbacks.getRegisteredCallbackCount());
                //这里需要判断控制中心进程有灭有起来?如果没有起来，那么要bindService远程拉起进程，将数据传递过去
                //添加数据到队列
                queue.offer(appInfoList);
                //检查NotifyService，目前检查mRemoteCallbacks来确定控制中心有没有连接
                if(mRemoteCallbacks.getRegisteredCallbackCount()<=0){
                    bindNotifyService(mContext);
                }
                try {
                    final int N = mRemoteCallbacks.beginBroadcast();
                    for (int i = 0; i < N; i++) {
                        try {
                            mRemoteCallbacks.getBroadcastItem(i).update(appInfoList);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                            LogUtil.d("service", "update" + ", e=" + e);
                        }
                    }
                    mRemoteCallbacks.finishBroadcast();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //---------------------------------------notify-------------------------------------------------
    private static final String ACTION_NOTIFY="com.coocaa.os.controlcenter.NOTIFY";
    //队列大小
    private final int QUEUE_LENGTH =100*10;
    //基于内存的阻塞队列
    private BlockingQueue<List<AppInfoBean>> queue =new LinkedBlockingQueue<List<AppInfoBean>>(QUEUE_LENGTH);
    private NotifyAidl mNotifyAidl;
    private void bindNotifyService(final Context mContext){
        if(mNotifyAidl==null){
            ThreadManager.getInstance().ioThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(ACTION_NOTIFY);
                        Intent choice = AndroidUtil.createExplicitFromImplicitIntent(mContext, intent);
                        Intent eintent = null;
                        if (choice == null) {
                            //这里没有找到对应的service，怎么处理？
                        } else {
                            eintent = new Intent(choice);
                            boolean res = mContext.bindService(eintent, mNotifyConnection, Service.BIND_AUTO_CREATE);
                            LogUtil.d("service","[notify] bindNotifyService: bind service is over eintent="+eintent+", result res="+res);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        LogUtil.d("service","[notify] bindNotifyService: e="+e);
                    }
                }
            });
        }else{
            List<AppInfoBean> bean = queue.poll();
            LogUtil.d("service","[Notify] bindNotifyService: bean="+bean);
            if(bean!=null){
                try {
                    mNotifyAidl.notify(bean);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void unbindNotifyService(){
        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mContext.unbindService(mNotifyConnection);
                    mNotifyAidl = null;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private ServiceConnection mNotifyConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mNotifyAidl = NotifyAidl.Stub.asInterface(service);
            LogUtil.d("service","[Notify] onServiceConnected: mNotifyAidl="+mNotifyAidl);
            try {
                service.linkToDeath(mDeathRecipientNotify, 0);
            } catch (RemoteException e) {
                LogUtil.d("service","[Notify] onServiceConnected: e1="+e);
            }
            //这里进行数据传输
            List<AppInfoBean> bean = queue.poll();
            LogUtil.d("service","[Notify] onServiceConnected: list size="+(bean!=null?bean.size():"null"));
            if(bean!=null){
                try {
                    mNotifyAidl.notify(bean);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("service","[Notify] onServiceDisconnected");
        }
    };
    private IBinder.DeathRecipient mDeathRecipientNotify = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            LogUtil.d("service","[Notify] binderDied!!!!!: mNotifyAidl="+mNotifyAidl);
            if (mNotifyAidl == null) {
                return;
            }
            mNotifyAidl.asBinder().unlinkToDeath(mDeathRecipientNotify, 0);
            mNotifyAidl = null;
        }
    };
}
