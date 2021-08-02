package com.coocaa.appbus.able;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.coocaa.appbus.traffic.AppInfoBean;


// @ClassId("TestData")
public class TestData implements ITestData {

    @Override
    public String testtesttest(int i, String s, OtherMode otherMode) {
        return "其它参数的测试数据==> i:" + i + " s:" + s + " otherMode:" + otherMode;
    }

    @Override
    public String testBitmapAndDrawable(int i, String s, Drawable drawable, Bitmap bitmap, View view) {
        return "带 bitmap drawable 的测试数据==> i:" + i + " s:" + s + " Drawable:" + drawable + " bitmap:" + bitmap;
    }

    @Override
    public String testtesttest(int i, String s, ITestCallBack cb) {
        for (int j = 0; j < 10; j++) {
            cb.callback(j);
        }
        return "回调的测试数据==> i:" + i + " s:" + s;
    }

    @Override
    public String testtesttest(int i, String s) {
        return "正常的测试数据==> i:" + i + " s:" + s;
    }

    @Override
    public String testtesttest() {
        return "无参函数调用返回";
    }

    @Override
    public Bitmap testtesttest(int i) {
        return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
    }

    @Override
    public AppInfoBean testtesttest(int i, int j) {
        //return new AppInfoBean("dunn",Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888));
        return null;
    }
}
