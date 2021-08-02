package com.coocaa.appbus.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Author:Administrator
 * Date:2021/7/30 17:35
 * Description:ViewUtil
 */
public class ViewUtil {
    /**
     * 将本地资源图片大小缩放
     *
     * @param resId:本地资源id
     * @param w:需要缩放的宽度
     * @param h:需要缩放的高度
     * @return 缩放处理后的Bitmap
     */
    public static Bitmap zoomImageBitmap(Context mContext, int resId, int w, int h) {
        Resources res = mContext.getResources();
        Bitmap oldBmp = BitmapFactory.decodeResource(res, resId);
        Bitmap newBmp = Bitmap.createScaledBitmap(oldBmp, w, h, true);
        return newBmp;
    }

    /**
     *
     * @param drawable
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
