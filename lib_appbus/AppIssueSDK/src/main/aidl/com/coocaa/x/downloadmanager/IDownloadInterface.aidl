package com.coocaa.x.downloadmanager;

import com.coocaa.x.downloadmanager.IDownloadListener;
import com.coocaa.x.downloadmanager.IInstallListener;
import com.coocaa.x.downloadmanager.IUnInstallListener;
import com.coocaa.x.downloadmanager.DownloadInfo;
interface IDownloadInterface {
    boolean startDownload(String pkg);
    boolean pauseDownload(String pkg);
    boolean cancleDownload(String pkg);
    DownloadInfo getDonwloadInfo(String pkg);
    boolean install(String pkg,String path);
    boolean unInstall(String pkg);
    void addDownloadListener(String pkg,in IDownloadListener listener);
    void removeDownloadListener(String pkg,in IDownloadListener listener);
    void addInstallListener(String pkg,in IInstallListener listener);
    void removeInstallListener(String pkg,in IInstallListener listener);
    void addUnInstallListener(String pkg,in IUnInstallListener listener);
    void removeUnInstallListener(String pkg,in IUnInstallListener listener);
}
