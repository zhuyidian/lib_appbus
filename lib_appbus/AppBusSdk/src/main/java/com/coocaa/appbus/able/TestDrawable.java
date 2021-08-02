package com.coocaa.appbus.able;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import java.io.Serializable;

public class TestDrawable extends GradientDrawable implements Serializable {

    public TestDrawable() {
        setStroke(2, Color.BLACK);
    }

    @Override
    protected void onBoundsChange(Rect r) {
        super.onBoundsChange(r);
        setCornerRadius(Math.min(r.width(), r.height()) / 2);
    }

}
