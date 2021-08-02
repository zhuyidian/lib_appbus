package com.coocaa.appbus.able;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.coocaa.appbus.traffic.AppInfoBean;

// @ClassId("TestData")
public interface ITestData {
    public String testtesttest(int i, String s, ITestCallBack cb);
    public String testtesttest(int i, String s, OtherMode otherMode);
    public String testBitmapAndDrawable(int i, String s, Drawable drawable, Bitmap bitmap, View view);
    public String testtesttest(int i, String s);
    public String testtesttest();
    public Bitmap testtesttest(int i);
    public AppInfoBean testtesttest(int i, int j);
}
