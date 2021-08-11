package com.coocaa.appissuesdk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Log;

import com.coocaa.appissuesdk.data.LaunchData;

/**
 * Created by C0der on 2017/7/27.
 */
public class AppChecker {

    public static Intent checkLauncherIntent(Context context, LaunchData data) throws Exception {
        if (data == null || TextUtils.isEmpty(data.packageName) || data.intent == null) {
            Log.e("launch", "Launcher: launch param err!");
            throw new Exception("errorLaunchData");
        }
        PackageInfo info = Android.isInstalled(context, data.packageName);
        Log.d("launch", "(info != null):" + (info != null) + "  pkg:" + data.packageName);
        if (info != null) {
            if (checkVersionCode(data, info.versionCode)) {
                data.intent.setPackage(data.packageName);
                return data.intent;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static boolean checkVersionCode(LaunchData appData, int localVersion) {
        if (appData == null) {
            Log.e("launch", "Launcer: checkVersionCode appData == null");
            return false;
        }
        Log.d("launch", "checkVersionCode appData.versionCode:" + appData.versionCode + " localVersion:" + localVersion);
        return appData.versionCode <= localVersion;
    }

}