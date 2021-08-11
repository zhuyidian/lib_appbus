package com.coocaa.x.downloadmanager;

import com.coocaa.x.downloadmanager.DownloadInfo;
interface IDownloadListener {
   void onStart(in DownloadInfo info);
   void onProgress(in DownloadInfo info,int progress);
   void onSuccess(in DownloadInfo info);
   void onPaused(in DownloadInfo info);
   void onFailed(in DownloadInfo info,String msg);
   void onCancle(in DownloadInfo info,String msg);
}
