package com.coocaa.x.downloadmanager;

interface IUnInstallListener {
   void onUnInstallStart(String packageName);
   void onUnInstallEnd(String packageName,boolean success,String msg);
}
