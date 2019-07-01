package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static boolean hasSignedIn;
    //ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        SharedPreferences settings = getSharedPreferences(SignIn.PREFS_NAME, 0);

        //icon = (ImageView) findViewById(R.id.imageView14);


        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(hasLoggedIn)
        {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();


        }
        else{
            Intent intent = new Intent(SplashActivity.this, SignIn.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }

    }
}

