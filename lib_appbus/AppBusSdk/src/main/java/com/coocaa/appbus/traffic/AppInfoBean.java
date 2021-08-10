package com.coocaa.appbus.traffic;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:zhuyidian
 * Date:2021/7/30 17:53
 * Description:AppInfoBean
 */
public class AppInfoBean implements Parcelable {
    private Bitmap mIcon;
    private String label;
    //private Intent launchIntent;
    //private PackageInfo packageInfo;
    private String mPackage;
    private String mClass;
    public static final Creator<AppInfoBean> CREATOR = new Creator<AppInfoBean>() {
        public AppInfoBean createFromParcel(Parcel in) {
            return new AppInfoBean(in);
        }

        public AppInfoBean[] newArray(int size) {
            return new AppInfoBean[size];
        }
    };

    public AppInfoBean(){}

    public AppInfoBean(Bitmap icon,String label,String mPackage, String mClass) {
        this.mIcon = icon;
//        this.packageInfo = info;
        this.label = label;
//        this.launchIntent = intent;
        this.mPackage = mPackage;
        this.mClass = mClass;
    }

    private AppInfoBean(Parcel in) {
        if (in.readInt() != 0) {
            this.mIcon = (Bitmap)Bitmap.CREATOR.createFromParcel(in);
        }
        this.label = in.readString();
//        if (in.readInt() != 0) {
//            this.packageInfo = (PackageInfo)PackageInfo.CREATOR.createFromParcel(in);
//        }
        this.mPackage = in.readString();
        this.mClass = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (this.mIcon == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            this.mIcon.writeToParcel(dest, 0);
        }
        dest.writeString(this.label);
//        if(this.launchIntent == null){
//            dest.writeInt(0);
//        }else{
//            dest.writeInt(1);
//            this.launchIntent.writeToParcel(dest, 0);
//        }
//        if(this.packageInfo == null){
//            dest.writeInt(0);
//        }else{
//            dest.writeInt(1);
//            this.packageInfo.writeToParcel(dest, 0);
//        }
        dest.writeString(this.mPackage);
        dest.writeString(this.mClass);
    }

    public Bitmap getIcon() {
        return this.mIcon;
    }

    public void setIcon(Bitmap bitmap) {
        this.mIcon = bitmap;
    }

//    public PackageInfo getPackageInfo() {
//        return this.packageInfo;
//    }
//
//    public void setPackageInfo(PackageInfo info) {
//        this.packageInfo = info;
//    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
//
//    public Intent getLaunchIntent() {
//        return launchIntent;
//    }
//
//    public void setLaunchIntent(Intent intent) {
//        this.launchIntent = intent;
//    }

    public String getPackageName() {
        return mPackage;
    }

    public void setPackageName(String packageName) {
        this.mPackage = packageName;
    }

    public String getClassName() {
        return mClass;
    }

    public void setClassName(String className) {
        this.mClass = className;
    }
}
