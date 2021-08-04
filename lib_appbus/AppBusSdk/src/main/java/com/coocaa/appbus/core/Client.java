package com.coocaa.appbus.core;

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
import android.os.RemoteException;
import android.text.TextUtils;

import com.coocaa.appbus.AppBus;
import com.coocaa.appbus.able.INotify;
import com.coocaa.appbus.service.AppBusService;
import com.coocaa.appbus.thread.ThreadManager;
import com.coocaa.appbus.traffic.AppBusAidl;
import com.coocaa.appbus.traffic.AppBusCallback;
import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.utils.AndroidUtil;
import com.coocaa.appbus.utils.LogUtil;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Author:zhuyidian
 * Date:2021/8/4 15:50
 * Description:Client
 */
public class Client {
    public static final String ACTION="com.coocaa.os.controlcenter.APP_INFO";
    private static Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }

    //-------------------------------client--------------------------------------------------------
    private AppBusAidl mXBusAidl;
    private INotify notify;

    public void init(Context context, INotify notify) {
        this.notify = notify;
        bind(context, "", AppBusService.class);
    }

    public void init(Context context, String packageName,INotify notify) {
        this.notify = notify;
        bind(context, packageName, AppBusService.class);
    }

    public void init(Context context, Class<? extends Service> service, INotify notify) {
        this.notify = notify;
        bind(context, "", service);
    }

    public boolean init(Context context,String packageName,String action,INotify notify){
        this.notify = notify;
        return bind(context,packageName,action);
    }

    private boolean bind(final Context context,final String packageName,final String action){
        boolean result = false;

        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("client","bind service: packageName="+packageName+", action="+action+
                        ", Thread="+Thread.currentThread().toString());
                Intent service = new Intent(action);
                service.setPackage(packageName);

                Intent intent = new Intent(action);
                Intent choice = AndroidUtil.createExplicitFromImplicitIntent(context,intent);
                Intent eintent = null;
                if(choice==null){

                }else{
                    eintent = new Intent(choice);

                    boolean res = context.bindService(eintent, mServiceConnection, Service.BIND_AUTO_CREATE);
                }
            }
        });

        return result;
    }

    private void bind(final Context context, final String packageName, final Class<? extends Service> service) {
        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (TextUtils.isEmpty(packageName)) {
                    intent = new Intent(context, service);
                } else { // getName:com.coocaa.appbus.service.XBusService
                    intent = new Intent();
                    intent.setClassName(packageName, service.getName());
                }
                context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    public void disconnect(Context context) {
        unbind(context);
        LogUtil.d("client","unbind service");
    }

    private void unbind(final Context context) {
        ThreadManager.getInstance().ioThread(new Runnable() {

            @Override
            public void run() {
                if(mXBusAidl!=null){
                    try {
                        mXBusAidl.unregister(mCallback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                context.unbindService(mServiceConnection);
                mXBusAidl = null;
                notify = null;
            }
        });
    }
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            LogUtil.d("client","binderDied!!!!!mXBusAidl="+mXBusAidl);
            if (mXBusAidl == null) {
                return;
            }
            mXBusAidl.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mXBusAidl = null;
            if(notify!=null){
                notify.serverKill();
            }
            notify=null;
            //是否需要重新绑定服务
            //bind();
        }
    };
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mXBusAidl = AppBusAidl.Stub.asInterface(service);
            if(mXBusAidl!=null && notify!=null){
                notify.connectStatus(true);
            }
            LogUtil.d("client","onServiceConnected: mXBusAidl="+mXBusAidl+", Thread="+Thread.currentThread().toString());
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                LogUtil.d("client","onServiceConnected: e1="+e);
            }
            // 注册回调.
            ThreadManager.getInstance().ioThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LogUtil.d("client","onServiceConnected register callback: client PID="+ Process.myPid()+
                                ", Thread="+Thread.currentThread().toString());
                        mXBusAidl.register(mCallback, Process.myPid());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        LogUtil.d("client","onServiceConnected: e2="+e);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("client","onServiceDisconnected: ComponentName:" + name);
            if(notify!=null){
                notify.connectStatus(false);
            }
            //交给mDeathRecipient去释放
            //mXBusAidl = null;
        }
    };
    /**
     * 注册回调，用于有回调的函数处理.
     */
    private AppBusCallback mCallback = new AppBusCallback.Stub() {
        @Override
        public void update(List<AppInfoBean> appInfoList) throws RemoteException {
            if(notify!=null){
                notify.update(appInfoList);
            }
        }

        /**
         * 销毁相关的回调记录.
         */
//        @Override
//        public void gc() throws RemoteException {
//        }
    };

    /**
     * 测试调用情况.
     */
    public <T> T getCreateCall(Class<T> tClass) {
        try {
            Class<?>[] interfaces = new Class[]{tClass};
            XBusHandler handler = new XBusHandler(tClass, mXBusAidl);
            Object proxy = Proxy.newProxyInstance(tClass.getClassLoader(), interfaces, handler);
            LogUtil.d("client","get proxy: proxy="+proxy);
            return (T) proxy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 动态代理的 InvocationHandler invoke 处理.
     */
    class XBusHandler implements InvocationHandler {

        private Gson gson = new Gson();

        private Class<?> clazz;
        private AppBusAidl xBusAidl;

        public XBusHandler(Class<?> clazz, AppBusAidl xBusAidl) {
            this.clazz = clazz;
            this.xBusAidl = xBusAidl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            String clazzName = clazz.getName();
            LogUtil.d("client","invoke: clazzName="+clazzName);
            LogUtil.d("client","invoke: methodName="+methodName);
            LogUtil.d("client","invoke: param="+args);

            if ("Object".equals(clazzName)) {
                return null;
            }

            Request request = new Request();
            try{
                if (args == null || args[0] == null) {

                }else{
                    // 尝试获取所有参数，判断是否拥有接口.
                    int length = args.length;
                    for (int i = 0; i < length; ++i) {
                        Class<?>[] classes = method.getParameterTypes();
                        if (classes[i].isInterface()) {
                            Object parameter = args[i];
                        }
                    }
                }

                if (AppBus.IS_BUNDLE_DEBUG) {
                    // bundle 传递 args. 方法，类名
                    Bundle bundle = new Bundle();
                    // bundle.putInt("args", (Integer) args[0]);
                    bundle.putSerializable(Request.ARGS_KEY, args);
                    bundle.putString(Request.METHOD_KEY, methodName);
                    bundle.putString(Request.CLAZZ_KEY, clazzName);
                    request.setBundle(bundle);
                } else {
                    // 保存类名，方法，参数值. json 方式传递
                    Request.ClassData classData = new Request.ClassData();
                    classData.clazz = clazzName;
                    classData.method = methodName;
                    classData.params = null != args ? gson.toJson(args) : null;
                    request.setData(gson.toJson(classData));
                }
                // 3. AIDL 远程调用 （函数执行过程)
                LogUtil.d("client","Bp(run)--->: request="+request.toString());
                Response response = xBusAidl.run(request);
                LogUtil.d("client","Bp(run)<---: response="+response.toString());
                // 4. 处理 返回值.
                if (AppBus.IS_BUNDLE_DEBUG) {
                    Bundle bundle1 = response.getBResult();
                    if (null != bundle1) {
                        bundle1.setClassLoader(getClass().getClassLoader());
                        //return bundle1.getSerializable(Response.RESULT_KEY);
                        return (AppInfoBean)bundle1.getParcelable(Response.RESULT_KEY_PARCELABLE);
                    }
                }
                return gson.fromJson(response.getResult(), method.getReturnType());
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d("client","invoke: e="+e);
            }

            return null;
        }
    }

    /**
     * 检查是否安装SDK
     * @param context
     * @return
     */
    public boolean isInstallSdk(Context context){
        boolean result = false;

        try {
            String localPackageName = context.getPackageName();
            LogUtil.d("client","localPackageName="+localPackageName);
            PackageManager pm = context.getPackageManager();
            List<ApplicationInfo> list = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            LogUtil.d("client","list.size="+(list!=null?list.size():0));
            for(ApplicationInfo info : list){
                LogUtil.d("client","packageName="+info.packageName);
                if(info.metaData!=null && !localPackageName.equals(info.packageName)){
                    boolean value = info.metaData.getBoolean("com.coocaa.appbus.get",false);
                    LogUtil.d("client","metadata:packageName="+info.packageName+", value="+value);
                    if(value==true){
                        result = true;
                        break;
                    }
                }
            }
//            ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            String value = appInfo.metaData.getString("api_key");
        }
//        catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public List<AppInfoBean> getAppInfo() throws RemoteException {
        if(mXBusAidl==null){
            return null;
        }
        return mXBusAidl.getAppInfo();
    }
}
