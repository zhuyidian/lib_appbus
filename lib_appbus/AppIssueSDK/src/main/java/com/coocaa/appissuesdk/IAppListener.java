package com.coocaa.appissuesdk;

public interface IAppListener {
    //download
    void onAppDownloadStart(String pkg,String url);
    void onAppDownloadProcess(String pkg,int percent);
    void onAppDownloadPause(String pkg);
    void onAppDownloadCancle(String pkg,String saveFile);
    void onAppDownloadFinish(String pkg,boolean res,String saveFile,String msg);

    //install
    void onAppInstallStart(String pkg,String saveFile);
    void onAppInstallFinish(String pkg,boolean res,String saveFile,String msg);

    //uninstall
    void onAppUninstallStart(String pkg);
    void onAppUninstallFinish(String pkg,boolean res,String msg);
}
