package com.coocaa.appissuesdk;

import android.content.Context;

import com.coocaa.x.downloadmanager.DownloadInfo;
import com.coocaa.appissuesdk.data.LaunchData;
import com.coocaa.appissuesdk.impl.AppManager;

public interface IAppManager {
    IAppManager MANAGER = new AppManager();

    void init(Context c,IAppListener listener);  //初始化
    boolean launchApp(LaunchData data);
    boolean launchAppWithVersion(LaunchData data);
    void startDownload(String pkg);
    void pauseDownload(String pkg);
    void cancleDownload(String pkg);
//    void setAppListener();
//    void removeAppListener();
    DownloadInfo getAppStatus(String pkg);
    void installApp(String pkg,String filePath);
    void uninstallApp(String pkg);

}
