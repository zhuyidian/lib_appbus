package com.coocaa.x.downloadmanager;

interface IInstallListener {
   void onInstallStart(String apkfile, String packageName);
   void onInstallEnd(String apkfile, String packageName,boolean success,String msg);
}
