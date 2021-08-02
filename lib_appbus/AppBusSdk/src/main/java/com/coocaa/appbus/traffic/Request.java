package com.coocaa.appbus.traffic;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 按照JSON的标准格式
 * {
 * "status":  // 接口访问成功或者失败的状态码
 * "message": // 接口访问错误的时候返回的错误提示文字，访问成功的时候为空字符串
 * "data":{   // 服务端实际返回的数据
 * }
 * }
 */
public class Request implements Parcelable {
    public static final String ARGS_KEY = "args";
    public static final String METHOD_KEY = "method";
    public static final String CLAZZ_KEY = "clazz";

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    private String data;
    private Bundle bundle; // 考虑是否可使用 Bundle来完成参数的传送，不使用JSON序列化.

    public Request() {
    }

    protected Request(Parcel in) {
        data = in.readString();
        bundle = in.readBundle();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Request{" +
                "data='" + data + '\'' +
                ", bundle=" + bundle +
                '}';
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public static class ClassData {
        @SerializedName("clazz")
        public
        String clazz; // 完整路径的类名
        @SerializedName("method")
        public
        String method; // 方法名称
        @SerializedName("params")
        public
        String params; // args 参数

        @Override
        public String toString() {
            return "ClassData{" +
                    "clazz='" + clazz + '\'' +
                    ", method='" + method + '\'' +
                    ", params='" + params + '\'' +
                    '}';
        }
    }

}
