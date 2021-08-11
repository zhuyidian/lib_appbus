package com.coocaa.appissuesdk.data;

import android.content.Context;
import android.content.Intent;

/**
 * Created by XuZeXiao on 2017/2/18.
 * 主页运营位传进来的参数
 */
public class LaunchData {

    /**
     * Default constructor
     */
    public LaunchData() {
    }

    /**
     * 主页运营位指定的versionCode
     */
    public int versionCode;

    /**
     * 主页运营位apk包名
     */
    public String packageName;

    /**
     * 打开运营位所配的参数
     */
    public Intent intent;

}