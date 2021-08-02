package com.coocaa.appbus.able;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.coocaa.appbus.traffic.AppInfoBean;

import java.util.List;

/**
 * 由服务端实现
 */
public interface IUserData {
    public List<AppInfoBean> getAppInfo();
}
