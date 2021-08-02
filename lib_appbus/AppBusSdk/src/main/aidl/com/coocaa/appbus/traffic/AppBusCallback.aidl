// AppBusCallback.aidl
package com.coocaa.appbus.traffic;

import com.coocaa.appbus.traffic.AppInfoBean;

interface AppBusCallback {
    void update(out List<AppInfoBean> appInfoList);
}
