package com.coocaa.appbus.traffic;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Response implements Parcelable {
    public static final String RESULT_KEY = "result";
    public static final String RESULT_KEY_PARCELABLE = "result_parcelable";

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @SerializedName("result")
    private String result;
    private Bundle bResult;
    private int status;
    private String message;

    public Response() {

    }

    protected Response(Parcel in) {
        result = in.readString();
        bResult = in.readBundle();
        message = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeBundle(bResult);
        dest.writeString(message);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result='" + result + '\'' +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Bundle getBResult() {
        return bResult;
    }

    public void setBResult(Bundle result) {
        this.bResult = result;
    }

}
