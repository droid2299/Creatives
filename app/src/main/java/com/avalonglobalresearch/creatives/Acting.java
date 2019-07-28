package com.avalonglobalresearch.creatives;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class Acting extends AppCompatActivity {

    ImageView imageList , videoList ;
    TextView textList;


    private SlidrInterface slidr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acting);

        imageList = (ImageView) findViewById(R.id.imageList);
        videoList = (ImageView) findViewById(R.id.videoList);
        textList = (TextView) findViewById(R.id.textList);
        slidr = Slidr.attach(this);

        loadFragment(new ActingImagesFragment());

        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ActingImagesFragment());
            }
        });

        videoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ActingVideosFragment());
            }
        });

        textList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ActingTextsFragment());
            }
        });


    }

    public boolean loadFragment(Fragment fragment){

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return  false;
    }
}
