package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    public static boolean hasSignedIn;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        icon = (ImageView) findViewById(R.id.imageView14);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity
            Intent intent = new Intent(SplashActivity.this , PaperOnboardingActivity.class);
            startActivity(intent);
            //Toast.makeText(SplashActivity.this, "First Run", Toast.LENGTH_LONG)


            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit();
        }
        else {

            SharedPreferences settings = getSharedPreferences(SignIn.PREFS_NAME, 0);
            boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

            if (hasLoggedIn) {
                Intent intent = new Intent(SplashActivity.this, MainFrame.class);
                startActivity(intent);
                SplashActivity.this.finish();


            } else {
                Intent intent = new Intent(SplashActivity.this, SignIn.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }

    }
}

