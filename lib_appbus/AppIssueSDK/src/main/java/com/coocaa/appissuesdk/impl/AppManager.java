package com.coocaa.appissuesdk.impl;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.coocaa.appissuesdk.IAppListener;
import com.coocaa.appissuesdk.IAppManager;
import com.coocaa.appissuesdk.utils.ThreadManager;
import com.coocaa.x.downloadmanager.DownloadInfo;
import com.coocaa.appissuesdk.data.LaunchData;
import com.coocaa.appissuesdk.utils.Android;
import com.coocaa.appissuesdk.utils.AppChecker;
import com.coocaa.x.downloadmanager.IDownloadInterface;
import com.coocaa.x.downloadmanager.IDownloadListener;
import com.coocaa.x.downloadmanager.IInstallListener;
import com.coocaa.x.downloadmanager.IUnInstallListener;

import java.util.HashSet;
import java.util.Set;


public class AppManager implements IAppManager {
    private String TAG = "demo";
    private String action = "com.coocaa.x.downloadmanager";
    private IDownloadInterface iDownloadService;
    private IAppListener mListener;
    private Context mContext = null;
    private Set<String> mSet = new HashSet<>();

    @Override
    public void init(Context c, IAppListener listener) {
        Log.d(TAG, "AppManager  INIT");
        mContext = c;
        mListener = listener;
        bindService();
    }

    private void bindService() {
        ThreadManager.getInstance().ioThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "INIT  (iDownloadService == null):" + (iDownloadService == null));
                if (iDownloadService == null) {
                    Intent service = new Intent(action);
                    service.setPackage("com.tianci.appstore");
                    boolean res = mContext.bindService(service, connection, Service.BIND_AUTO_CREATE);
                    Log.d(TAG, "bindService res:" + res);
                }
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iDownloadService = IDownloadInterface.Stub.asInterface(iBinder);
//            Toast.makeText(mContext, "绑定成功", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "bind success");
            setAppListener();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iDownloadService = null;
            Log.i(TAG, "unbind success");
        }
    };

    @Override
    public boolean launchApp(LaunchData data) {
        PackageInfo info = null;
        try {
            info = Android.isInstalled(mContext, data.packageName);
            Log.d("launch", "(info != null):" + (info != null) + "  pkg:" + data.packageName);
        } catch (Exception e) {
        }
        if (info == null) {
            return false;
        }
        launchIntent(mContext, data.intent);
        return true;
    }

    @Override
    public boolean launchAppWithVersion(LaunchData data) {
        Intent intent = null;
        try {
            intent = AppChecker.checkLauncherIntent(mContext, data);
        } catch (Exception e) {
        }
        if (intent == null) {
            return false;
        }
        launchIntent(mContext, data.intent);
        return true;
    }

    @Override
    public void startDownload(String pkg) {
        try {
            mSet.add(pkg);
            iDownloadService.startDownload(pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseDownload(String pkg) {
        try {
            iDownloadService.pauseDownload(pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancleDownload(String pkg) {
        try {
            iDownloadService.cancleDownload(pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAppListener() {
        try {
            String pkg = mContext.getPackageName();
            iDownloadService.addDownloadListener(pkg,downloadListener);
            iDownloadService.addInstallListener(pkg,installListener);
            iDownloadService.addUnInstallListener(pkg,unInstallListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void removeAppListener() {
//        try {
//            iDownloadService.removeDownloadListener(downloadListener);
//            iDownloadService.removeInstallListener(installListener);
//            iDownloadService.removeUnInstallListener(unInstallListener);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public DownloadInfo getAppStatus(String pkg) {
        try {
            return iDownloadService.getDonwloadInfo(pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void installApp(String pkg, String filePath) {
        try {
            mSet.add(pkg);
            iDownloadService.install(pkg, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uninstallApp(String pkg) {
        try {
            mSet.add(pkg);
            iDownloadService.unInstall(pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchIntent(Context context, Intent intent) {
        try {
            context.startActivity(addFlag(context, intent));
        } catch (Exception e) {
            Log.d("launch", "intent error , start launch!!");
            String pkg = intent.getPackage();
            ComponentName component = Android.getLauncherActivity(context, pkg);
            Intent i = new Intent();
            i.setPackage(pkg);
            i.setComponent(component);
            context.startActivity(addFlag(context, i));
            e.printStackTrace();
        }
    }

    private Intent addFlag(Context context, Intent intent) {
        try {
            if (context instanceof Activity) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            String pkg = intent.getPackage();
            String cur_pkg = context.getPackageName();
            try {
                if (pkg.equals(cur_pkg)) {
                    intent.setFlags(intent.getFlags());
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
            } catch (Exception e) {
                intent.setFlags(intent.getFlags());
            }
        } catch (Exception e) {
        }
        return intent;
    }

    private IDownloadListener downloadListener = new IDownloadListener.Stub() {

        @Override
        public void onStart(DownloadInfo info) throws RemoteException {
            Log.d(TAG, "downloadListener pkg:" + info.getPackageName());
            if (mListener != null && isOwnPkg(info.getPackageName())) {
                mListener.onAppDownloadStart(info.getPackageName(), info.getUrl());
            }
        }

        @Override
        public void onProgress(DownloadInfo info, int progress) throws RemoteException {
            Log.d(TAG, "downloadListener onProgress pkg:" + info.getPackageName() + " process:" + progress);
            if (mListener != null && isOwnPkg(info.getPackageName())) {
                mListener.onAppDownloadProcess(info.getPackageName(), progress);
            }
        }

        @Override
        public void onSuccess(DownloadInfo info) throws RemoteException {
            Log.d(TAG, "downloadListener onSuccess pkg:" + info.getPackageName());
            if (mListener != null && isOwnPkg(info.getPackageName())) {
                mListener.onAppDownloadFinish(info.getPackageName(), true, info.getSavedir(), "");
            }
        }

        @Override
        public void onPaused(DownloadInfo info) throws RemoteException {
            Log.d(TAG, "downloadListener onPaused pkg:" + info.getPackageName());
            if (mListener != null && isOwnPkg(info.getPackageName())) {
                mListener.onAppDownloadPause(info.getPackageName());
            }
        }

        @Override
        public void onFailed(DownloadInfo info, String msg) throws RemoteException {
            Log.d(TAG, "downloadListener onFailed pkg:" + info.getPackageName());
            if (mListener != null && isOwnPkg(info.getPackageName())) {
                mListener.onAppDownloadFinish(info.getPackageName(), false, info.getSavedir(), msg);
                removePkg(info.getPackageName());
            }
        }

        @Override
        public void onCancle(DownloadInfo info, String msg) throws RemoteException {
            Log.d(TAG, "downloadListener onCancle pkg:" + info.getPackageName());
            if (mListener != null && isOwnPkg(info.getPackageName())) {
                mListener.onAppDownloadCancle(info.getPackageName(), info.getSavedir());
                removePkg(info.getPackageName());
            }
        }
    };

    private IInstallListener installListener = new IInstallListener.Stub() {
        @Override
        public void onInstallStart(String apkfile, String packageName) throws RemoteException {
            Log.d(TAG, "installListener start pkg:" + packageName);
            if (mListener != null && isOwnPkg(packageName)) {
                mListener.onAppInstallStart(packageName, apkfile);
            }
        }

        @Override
        public void onInstallEnd(String apkfile, String packageName, boolean success, String msg) throws RemoteException {
            Log.d(TAG, "installListener end pkg:" + packageName);
            if (mListener != null && isOwnPkg(packageName)) {
                mListener.onAppInstallFinish(packageName, success, apkfile, msg);
                removePkg(packageName);
            }
        }

    };

    private IUnInstallListener unInstallListener = new IUnInstallListener.Stub() {
        @Override
        public void onUnInstallStart(String packageName) throws RemoteException {
            Log.d(TAG, "unInstallListener start pkg:" + packageName);
            if (mListener != null && isOwnPkg(packageName)) {
                mListener.onAppUninstallStart(packageName);
            }
        }

        @Override
        public void onUnInstallEnd(String packageName, boolean success, String msg) throws RemoteException {
            Log.d(TAG, "unInstallListener end pkg:" + packageName);
            if (mListener != null && isOwnPkg(packageName)) {
                mListener.onAppUninstallFinish(packageName, success, msg);
                removePkg(packageName);
            }
        }

    };

    private boolean isOwnPkg(String pkg) {
        if (!TextUtils.isEmpty(pkg) && mSet.contains(pkg)) {
            return true;
        }
        return false;
    }

    private synchronized void removePkg(String pkg){
        if (!TextUtils.isEmpty(pkg) && mSet.contains(pkg)) {
            mSet.remove(pkg);
        }
    }

}
