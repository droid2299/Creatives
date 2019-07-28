package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class Settings extends AppCompatActivity {

    Button logOut , editProfile , contactUs;
    TextView aboutUs;
    private SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editProfile = (Button) findViewById(R.id.editprofile);
        logOut = (Button) findViewById(R.id.logout2);
        contactUs = (Button) findViewById(R.id.contactUs);
        aboutUs = (TextView) findViewById(R.id.aboutUs);
        slidr = Slidr.attach(this);

        aboutUs.setText("Creatives App v1.0");


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(SignIn.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
                Intent intent = new Intent(Settings.this, SignIn.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this , EditProfile.class);
                startActivity(intent);
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"agr22299@gmail.com"});
                startActivity(Intent.createChooser(i, "Send mail..."));
            }
        });

    }
}
