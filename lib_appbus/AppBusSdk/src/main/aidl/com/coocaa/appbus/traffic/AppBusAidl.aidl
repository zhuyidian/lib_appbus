// AppBusAidl.aidl
package com.coocaa.appbus.traffic;

import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.AppBusCallback;

interface AppBusAidl {
    Response run(in Request request);
    void register(AppBusCallback cb, int pid);
    void unregister(AppBusCallback cb);
    List<AppInfoBean> getAppInfo();
}
