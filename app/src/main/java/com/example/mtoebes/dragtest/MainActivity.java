package com.example.mtoebes.dragtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This adds the JewelView programmatically to the layout
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FrameLayout previewLayout = (FrameLayout) findViewById(R.id.preview_layout);
                JewelView jewelView = new JewelView(MainActivity.this);
                previewLayout.addView(jewelView);
            }
        });
    }
}


