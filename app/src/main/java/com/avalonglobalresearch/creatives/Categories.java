package com.avalonglobalresearch.creatives;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.r0adkll.slidr.model.SlidrInterface;

public class Categories extends AppCompatActivity {

    ImageView linear , grid;
    private SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        linear = (ImageView) findViewById(R.id.linear);
        grid = (ImageView) findViewById(R.id.grid);

        loadFragment(new LinearFragment());

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG" , "Button pressed");
                loadFragment(new GridFragment());
            }
        });

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG" , "Button pressed");
                loadFragment(new LinearFragment());
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
