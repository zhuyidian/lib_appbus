package com.coocaa.appbus.able;

import com.coocaa.appbus.traffic.AppInfoBean;

import java.util.List;

/**
 * 由客户端实现
 */
public interface INotify {
    public void update(List<AppInfoBean> appInfoList);
    public void serverKill();
    public void connectStatus(boolean status);
}
