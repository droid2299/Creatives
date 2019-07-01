package com.avalonglobalresearch.creatives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class Acting extends AppCompatActivity {

    private SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acting);

        slidr = Slidr.attach(this);
    }
}
