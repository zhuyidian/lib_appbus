package com.coocaa.x.downloadmanager;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadInfo implements Parcelable {
    private String name, packageName;
    private String url, iconUrl, savedir, savename;
    private String md5;
    private long current, length;
    private int progress;

    protected DownloadInfo(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        url = in.readString();
        iconUrl = in.readString();
        savedir = in.readString();
        savename = in.readString();
        md5 = in.readString();
        current = in.readLong();
        length = in.readLong();
        status = in.readInt();
        starttime = in.readLong();
        createtime = in.readLong();
        speed = in.readFloat();
        id = in.readLong();
        progress = in.readInt();
    }

    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel in) {
            return new DownloadInfo(in);
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private int status;
    private long starttime, createtime = System.currentTimeMillis();
    private float speed;
    private long id;

    public DownloadInfo() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getSavedir() {
        return savedir;
    }

    public void setSavedir(String savedir) {
        this.savedir = savedir;
    }

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", url='" + url + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", savedir='" + savedir + '\'' +
                ", savename='" + savename + '\'' +
                ", md5='" + md5 + '\'' +
                ", current=" + current +
                ", length=" + length +
                ", progress=" + progress +
                ", status=" + status +
                ", starttime=" + starttime +
                ", createtime=" + createtime +
                ", speed=" + speed +
                ", id=" + id +
                '}';
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        url = in.readString();
        iconUrl = in.readString();
        savedir = in.readString();
        savename = in.readString();
        md5 = in.readString();
        current = in.readLong();
        length = in.readLong();
        status = in.readInt();
        starttime = in.readLong();
        createtime = in.readLong();
        speed = in.readFloat();
        id = in.readLong();
        progress = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeString(url);
        dest.writeString(iconUrl);
        dest.writeString(savedir);
        dest.writeString(savename);
        dest.writeString(md5);
        dest.writeLong(current);
        dest.writeLong(length);
        dest.writeInt(status);
        dest.writeLong(starttime);
        dest.writeLong(createtime);
        dest.writeFloat(speed);
        dest.writeLong(id);
        dest.writeInt(progress);
    }
}
