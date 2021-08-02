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
import android.os.RemoteException;
import android.text.TextUtils;

import com.coocaa.appbus.able.INotify;
import com.coocaa.appbus.able.IUserData;
import com.coocaa.appbus.core.AppBusAidlImpl;
import com.coocaa.appbus.joor.Reflect;
import com.coocaa.appbus.service.AppBusService;
import com.coocaa.appbus.thread.ThreadManager;
import com.coocaa.appbus.traffic.AppBusAidl;
import com.coocaa.appbus.traffic.AppBusCallback;
import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.utils.LogUtil;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AppBus {
    public static final boolean IS_BUNDLE_DEBUG = true;
    private static AppBus instance = new AppBus();

    //-------------------------------client--------------------------------------------------------
    private AppBusAidl mXBusAidl;
    private INotify notify;
    public static AppBus getInstance() {
        return instance;
    }

    public void init(Context context,INotify notify) {
        this.notify = notify;
        bind(context, "", AppBusService.class);
    }

    public void init(Context context, String packageName,INotify notify) {
        this.notify = notify;
        bind(context, packageName, AppBusService.class);
    }

    public void init(Context context, Class<? extends Service> service,INotify notify) {
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
                Intent choice = createExplicitFromImplicitIntent(context,intent);
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

    /***
     * 绑定service检查
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     *
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     *
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    public Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        LogUtil.d("client","createExplicitFromImplicitIntent: action="+implicitIntent.getAction());
        LogUtil.d("client","createExplicitFromImplicitIntent: localpackageName="+context.getPackageName());
        LogUtil.d("client","createExplicitFromImplicitIntent: resolveInfo.size="+(resolveInfo!=null?resolveInfo.size():"null"));
        for(ResolveInfo info : resolveInfo){
            LogUtil.d("client","createExplicitFromImplicitIntent: packageName="+info.serviceInfo.packageName+
                    ", className="+info.serviceInfo.name);
        }

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() <=0) {
            return null;
        }

        Intent explicitIntent = new Intent(implicitIntent);
        boolean result = false;
        //过滤本地
        for(ResolveInfo info : resolveInfo){
            if(!info.serviceInfo.packageName.equals(context.getPackageName())){
                // Get component info and create ComponentName
//                ResolveInfo serviceInfo = resolveInfo.get(0);
                ResolveInfo serviceInfo = info;
                String packageName = serviceInfo.serviceInfo.packageName;
                String className = serviceInfo.serviceInfo.name;
                LogUtil.d("client","createExplicitFromImplicitIntent: !!!bind!!! packageName="+packageName+", className="+className);
                ComponentName component = new ComponentName(packageName, className);

                // Set the component to be explicit
                explicitIntent.setComponent(component);
                result = true;
                break;
            }
        }



        // Create a new intent. Use the old one for extras and such reuse
//        Intent explicitIntent = new Intent(implicitIntent.getAction());
//        explicitIntent.setAction(implicitIntent.getAction());
//        // Set the component to be explicit
//        explicitIntent.setComponent(component);
//        explicitIntent.setPackage(packageName);

        if(result){
            return explicitIntent;
        }else{
            return null;
        }
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
                context.unbindService(mServiceConnection);
            }
        });
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mXBusAidl = AppBusAidl.Stub.asInterface(service);
            LogUtil.d("client","onServiceConnected: mXBusAidl="+mXBusAidl+", Thread="+Thread.currentThread().toString());
            // 注册回调.
            ThreadManager.getInstance().ioThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LogUtil.d("client","onServiceConnected register callback: client PID="+Process.myPid()+
                                ", Thread="+Thread.currentThread().toString());
                        mXBusAidl.register(mCallback, Process.myPid());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("client","onServiceDisconnected: ComponentName:" + name);
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

                if (IS_BUNDLE_DEBUG) {
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
                if (IS_BUNDLE_DEBUG) {
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
        return mXBusAidl.getAppInfo();
    }

    //-------------------------------server--------------------------------------------------------
    private Map<Class<?>, Class<?>> mRegisterClassMap = new HashMap<>();
    private AppBusAidlImpl appBusAidlImpl;
    private IUserData userData;
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

    public AppBusAidlImpl createAppBusImpl(){
        appBusAidlImpl = new AppBusAidlImpl();
        LogUtil.d("service","create AppBusImpl:"+appBusAidlImpl);
        return appBusAidlImpl;
    }

    public void update(final List<AppInfoBean> appInfoList){
        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                if(appBusAidlImpl!=null){
                    try {
                        appBusAidlImpl.update(appInfoList);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
