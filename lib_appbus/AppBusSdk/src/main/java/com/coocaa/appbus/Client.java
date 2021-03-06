package com.coocaa.appbus;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;

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

    private Client(){

    }

    public static class ClientHolder{
        private static volatile Client instance = new Client();
    }

    public static Client getInstance() {
        return ClientHolder.instance;
    }

    //-------------------------------client--------------------------------------------------------
    private Context mContext;
    private AppBusAidl mXBusAidl;
    private INotify notify;

    public void init(Context context, INotify notify) {
        this.mContext = context;
        this.notify = notify;
        bind(context, "", AppBusService.class);
    }

    public void init(Context context, String packageName,INotify notify) {
        this.mContext = context;
        this.notify = notify;
        bind(context, packageName, AppBusService.class);
    }

    public void init(Context context, Class<? extends Service> service, INotify notify) {
        this.mContext = context;
        this.notify = notify;
        bind(context, "", service);
    }

    public boolean init(Context context,String packageName,String action,INotify notify){
        this.mContext = context;
        this.notify = notify;
        return bind(context,packageName,action);
    }

    public void destroy(Context context) {
        unbind(context);
    }

    private boolean bind(final Context context,final String packageName,final String action){
        boolean result = false;

        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtil.d("client", "bind service: packageName=" + packageName + ", action=" + action);

                    Intent intent = new Intent(action);
                    final Intent choice = AndroidUtil.createExplicitFromImplicitIntent(context, intent);
                    //Intent eintent = null;
                    if (choice == null) {
                        //???????????????????????????service??????????????????
                    } else {
                        //????????????????????????
                        ThreadManager.getInstance().ioThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    LogUtil.d("client", "bind service again: mXBusAidl="+mXBusAidl);
                                    if (mXBusAidl == null) {
                                        LogUtil.d("client", "bind service again: timeout!!!!!!!");
                                        context.unbindService(mServiceConnection);
                                        boolean res = context.bindService(choice, mServiceConnection, Service.BIND_AUTO_CREATE);
                                        LogUtil.d("client", "bind service again: success choice=" + choice + ", result res=" + res);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    LogUtil.d("client", "bind service again: e="+e);
                                }
                            }
                        }, 5000);
                        boolean res = context.bindService(choice, mServiceConnection, Service.BIND_AUTO_CREATE);
                        LogUtil.d("client", "bind service: success choice=" + choice + ", result res=" + res);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d("client", "bind service: e1=" + e);
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

    private void unbind(final Context context) {
        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtil.d("client","unbind service: mXBusAidl="+mXBusAidl);
                    if (mXBusAidl != null) {
                        try {
                            mXBusAidl.unregister(mCallback);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    context.unbindService(mServiceConnection);
                    mXBusAidl = null;
                    notify = null;
                    mContext = null;
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d("client","unbind service: e="+e);
                }
            }
        });
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            try {
                LogUtil.d("client", "binderDied!!!!!mXBusAidl=" + mXBusAidl);
                if (mXBusAidl != null) mXBusAidl.asBinder().unlinkToDeath(mDeathRecipient, 0);
                if (notify != null) {
                    notify.serverKill();
                }
                //????????????????????????
                if (mContext != null) {
                    unbind(mContext);
                }
                //???????????????????????????????
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d("client", "binderDied!!!!!e=" + e);
            }
        }
    };

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mXBusAidl = AppBusAidl.Stub.asInterface(service);
                if (mXBusAidl != null && notify != null) {
                    notify.connectStatus(true);
                }
                LogUtil.d("client", "onServiceConnected: mXBusAidl=" + mXBusAidl);
                service.linkToDeath(mDeathRecipient, 0);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d("client", "onServiceConnected: e1=" + e);
            }
            // ????????????.
            ThreadManager.getInstance().ioThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LogUtil.d("client","onServiceConnected register callback: client PID="+ Process.myPid());
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
            //??????mDeathRecipient?????????
        }
    };

    /**
     * ?????????????????????????????????????????????.
     */
    private AppBusCallback mCallback = new AppBusCallback.Stub() {
        @Override
        public void update(List<AppInfoBean> appInfoList) throws RemoteException {
            if(notify!=null){
                notify.update(appInfoList);
            }
        }

        /**
         * ???????????????????????????.
         */
//        @Override
//        public void gc() throws RemoteException {
//        }
    };

    /**
     * ????????????????????????
     * ITestData testData = Client.getInstance().getCreateCall(ITestData.class);
     * // ?????????????????????
     * //result = testData.testtesttest(10, "????????????");
     * result = testData.testtesttest();
     *
     * // ?????????????????????????????????
     * result = testData.testtesttest(520, "??????????????????", new OtherMode());
     *
     * // ??????????????????.
     * result = testData.testtesttest(780, "????????????", new ITestCallBack() {
     *      @Override
     *      public void callback(int progress) {
     *             Toast.makeText(BActivity.this, "??????????????????:" + progress, Toast.LENGTH_LONG).show();
     *      }
     * });
     *
     * // ??? drawable, bitmap ?????????
     * final Drawable drawable = new TestDrawable();
     * final Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
     * result = testData.testBitmapAndDrawable(2000, "????????????", drawable, bitmap, null);
     */
    /**
     * ??????????????????.
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
     * ??????????????? InvocationHandler invoke ??????.
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
                    // ???????????????????????????????????????????????????.
                    int length = args.length;
                    for (int i = 0; i < length; ++i) {
                        Class<?>[] classes = method.getParameterTypes();
                        if (classes[i].isInterface()) {
                            Object parameter = args[i];
                        }
                    }
                }

                if (AppBus.IS_BUNDLE_DEBUG) {
                    // bundle ?????? args. ???????????????
                    Bundle bundle = new Bundle();
                    // bundle.putInt("args", (Integer) args[0]);
                    bundle.putSerializable(Request.ARGS_KEY, args);
                    bundle.putString(Request.METHOD_KEY, methodName);
                    bundle.putString(Request.CLAZZ_KEY, clazzName);
                    request.setBundle(bundle);
                } else {
                    // ?????????????????????????????????. json ????????????
                    Request.ClassData classData = new Request.ClassData();
                    classData.clazz = clazzName;
                    classData.method = methodName;
                    classData.params = null != args ? gson.toJson(args) : null;
                    request.setData(gson.toJson(classData));
                }
                // 3. AIDL ???????????? ?????????????????????)
                LogUtil.d("client","Bp(run)--->: request="+request.toString());
                Response response = xBusAidl.run(request);
                LogUtil.d("client","Bp(run)<---: response="+response.toString());
                // 4. ?????? ?????????.
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
     * ??????????????????SDK
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
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public List<AppInfoBean> getAppInfo() throws RemoteException {
        if(mXBusAidl == null || mXBusAidl.asBinder().isBinderAlive()==false){
            if (notify != null) {
                notify.serverKill();
            }
            return null;
        }
        return mXBusAidl.getAppInfo();
    }
}
