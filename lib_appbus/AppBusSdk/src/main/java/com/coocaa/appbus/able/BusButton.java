package com.coocaa.appbus.able;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import java.io.Serializable;

@SuppressLint("AppCompatCustomView")
public class BusButton extends Button implements Serializable {

    public BusButton(Context context) {
        super(context);
    }

    public BusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
