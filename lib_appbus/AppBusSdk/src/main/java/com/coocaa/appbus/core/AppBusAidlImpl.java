package com.coocaa.appbus.core;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;

import com.coocaa.appbus.AppBus;
import com.coocaa.appbus.able.IUserData;
import com.coocaa.appbus.joor.Reflect;
import com.coocaa.appbus.traffic.AppBusAidl;
import com.coocaa.appbus.traffic.AppBusCallback;
import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.utils.LogUtil;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppBusAidlImpl extends AppBusAidl.Stub {
    private Gson gson = new Gson();
    // 用于保存回调，与pid对应
    private ConcurrentHashMap<Integer, AppBusCallback> mCallbacks = new ConcurrentHashMap<Integer, AppBusCallback>();
    private final RemoteCallbackList<AppBusCallback> mRemoteCallbacks
            = new RemoteCallbackList<AppBusCallback>();

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            LogUtil.d("service","binderDied!!!!!");
        }
    };

    @Override
    public Response run(Request request) throws RemoteException {
        if (AppBus.IS_BUNDLE_DEBUG) {
            return runBundle(request);
        }
        return runJson(request);
    }

    /**
     * 注册回调，用于处理带回调的函数.
     */
    @Override
    public void register(final AppBusCallback cb, int pid) throws RemoteException {
        LogUtil.d("service","register callBack: cb="+cb+", client pid="+pid+", Thread="+Thread.currentThread().toString());
        //mCallbacks.put(pid, cb);
        if (cb != null) {
            mRemoteCallbacks.register(cb);
            //cb.asBinder().linkToDeath(mDeathRecipient,0);
        }
    }

    @Override
    public void unregister(AppBusCallback cb) throws RemoteException {
        LogUtil.d("service","unregister callBack: cb="+cb+", Thread="+Thread.currentThread().toString());
        if (cb != null) mRemoteCallbacks.unregister(cb);
    }

    public void clearRegister(){
        LogUtil.d("service","clearServer mRemoteCallbacks kill:"+mRemoteCallbacks);
        mRemoteCallbacks.kill();
    }

    @Override
    public List<AppInfoBean> getAppInfo() throws RemoteException {
        LogUtil.d("service","get app info"+", Thread="+Thread.currentThread().toString());
        IUserData userData = AppBus.getInstance().getUserImpl();
        List<AppInfoBean> list = null;
        if(userData!=null){
            list = userData.getAppInfo();
        }
        return list/*XAppList.getAppList(AppBusApplication.mContext)*/;
    }

    /**
     * 使用 Bundle 处理 请求参数以及返回
     */
    private Response runBundle(Request request) {
        Bundle bundle = request.getBundle();
        Object[] bargs = (Object[]) bundle.getSerializable(Request.ARGS_KEY);
        String clazzName = bundle.getString(Request.CLAZZ_KEY);
        String method = bundle.getString(Request.METHOD_KEY);
        LogUtil.d("service","Bn(run)<---: clazzName= " + clazzName);
        LogUtil.d("service","Bn(run)<---: methodName= " + method);
        LogUtil.d("service","Bn(run)<---: param= " + bargs);

        Response response = new Response();
        try {
            Class<?> iclazz = Reflect.on(clazzName).get();
            // 如果有回调存在，添加 动态代理支持.
            // Object[] mParameters 参考 Hermes->Receiver->setParameters
            Object createObject = AppBus.getInstance().getCreateObject(iclazz);
            Object result = Reflect.on(createObject)
                                   .call(method, bargs).get();
            LogUtil.d("service","Bn(run)<---: return result=" + result+", result.class="+result.getClass());
            //bundle.putSerializable(Response.RESULT_KEY, (Serializable) result);
            bundle.setClassLoader(AppInfoBean.class.getClassLoader());
            bundle.putParcelable(Response.RESULT_KEY_PARCELABLE, (Parcelable) result);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("service","Bn(run)<---: e= " + e);
        }

        // 失败处理
        response.setBResult(bundle);
        LogUtil.d("service","Bn(run)<---: return response= " + response.toString());
        return response;
    }

    /**
     * 使用 JSON 处理 请求参数以及返回
     */
    private Response runJson(Request request) {
        Response response = new Response();
        String data = request.getData();
        Request.ClassData classData = gson.fromJson(data, Request.ClassData.class);
        //
        try {
            List<Object> args = new ArrayList<>();
            // 类
            Class<?> iclazz = Class.forName(classData.clazz);
            Object createObject = AppBus.getInstance().getCreateObject(iclazz);
            // 找到对应函数，后续需要一一对应类型.
            Method method = null;
            for (Method m : iclazz.getMethods()) {
                if (m.getName().equals(classData.method)) {
                    method = m;
                    break;
                }
            }
            // 将参数值与类型 保存到 args.
            if (!TextUtils.isEmpty(classData.params)) {
                Class<?>[] types = method.getParameterTypes(); // 获取参数列表
                JSONArray array = new JSONArray(classData.params);
                for (int i = 0; i < array.length(); i++) {
                    String o = array.getString(i);
                    // 比如 10, int  "testtest", String
                    args.add(gson.fromJson(o, types[i]));
                }
            }
            // 传入 args 参数，执行 method 函数
            Object result = Reflect.on(createObject)
                                   .call(classData.method, args.toArray(new Object[0])).get();
            // 返回
            response.setResult(gson.toJson(result));
//            Log.d(TAG, "XBusAidlImpl run iclazz:" + iclazz + " createObject:" + createObject + " result:" + result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 失败处理
        return response;
    }

    public void update(List<AppInfoBean> appInfoList) throws RemoteException {
        LogUtil.d("service","update"+", Thread="+Thread.currentThread().toString());
//        if(mCallbacks!=null){
//            Iterator<Map.Entry<Integer, AppBusCallback>> entries = mCallbacks.entrySet().iterator();
//            while (entries.hasNext()) {
//                Map.Entry<Integer, AppBusCallback> entry = entries.next();
//                LogUtil.d("service","update listener: key="+entry.getKey()+", value="+entry.getValue());
//                if(entry.getValue()!=null){
//                    entry.getValue().update(appInfoList);
//                }
//            }
//        }
        final int N = mRemoteCallbacks.beginBroadcast();
        for (int i=0; i<N; i++) {
            try {
                mRemoteCallbacks.getBroadcastItem(i).update(appInfoList);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mRemoteCallbacks.finishBroadcast();
    }
}
