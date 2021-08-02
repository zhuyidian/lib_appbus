package com.dunn.logger.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.coocaa.appbus.AppBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //AppBus.getInstance().update();
    }

    public void click(View view){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
