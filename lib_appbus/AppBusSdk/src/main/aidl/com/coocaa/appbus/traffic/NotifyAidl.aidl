// AppBusAidl.aidl
package com.coocaa.appbus.traffic;

import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appbus.traffic.Response;
import com.coocaa.appbus.traffic.Request;
import com.coocaa.appbus.traffic.AppBusCallback;

interface NotifyAidl {
     void notify(in List<AppInfoBean> appInfoList);
}
