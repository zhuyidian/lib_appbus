package com.coocaa.appbus.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.coocaa.appbus.traffic.AppInfoBean;
import com.coocaa.appinfo.R;

import java.util.ArrayList;
import java.util.List;

public class XAppList {
    public static List<AppInfoBean> getAppList(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            List<AppInfoBean> packages = new ArrayList<AppInfoBean>();

            List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES |
                    PackageManager.GET_SERVICES);
//        int count = 0;
            if (ListUtil.isEmpty(packageInfos)) return packages;
            for (PackageInfo pi : packageInfos) {
                if (isSystemApp(pi)) {
                    continue;
                }
                AppInfoBean info = new AppInfoBean();
                info.setLabel(getPackageLabel(pi.packageName, pm));
                info.setLaunchIntent(pm.getLaunchIntentForPackage(pi.packageName));
                try {
                    info.setIcon(ViewUtil.drawableToBitmap(pm.getApplicationIcon(pi.packageName)));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
                packages.add(info);
//            count++;
//            if (count == 3)
//                break;
            }
//        AppInfoBean info1 = new AppInfoBean();
//        info1.setLabel("更多");
//        Intent intent = new Intent("coocaa.intent.action.APP_STORE_MYAPPS");
//        intent.putExtra("oneKeyExit", Boolean.TRUE.toString());
//        info1.setLaunchIntent(intent);
//        Drawable able = ContextCompat.getDrawable(context, R.mipmap.source_atv_no_focus);
//        info1.setIcon(ViewUtil.drawableToBitmap(able));
//        packages.add(info1);
            return packages;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String getPackageLabel(String packageName, PackageManager pm) {
        try {
            ApplicationInfo i = pm.getApplicationInfo(packageName, 0);
            return i.loadLabel(pm).toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return packageName;
    }

    private static boolean isSystemApp(PackageInfo pInfo) {
        boolean isSysUpdate = (pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
        boolean isSysApp = (pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        return isSysApp || isSysUpdate;
    }
}
