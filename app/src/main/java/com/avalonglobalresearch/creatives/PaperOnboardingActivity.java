package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnChangeListener;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;


public class PaperOnboardingActivity extends AppCompatActivity {



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.onboarding_main_layout);



        PaperOnboardingEngine engine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView), getDataForOnboarding(), getApplicationContext());



        engine.setOnChangeListener(new PaperOnboardingOnChangeListener() {

            @Override

            public void onPageChanged(int oldElementIndex, int newElementIndex) {

                //Toast.makeText(getApplicationContext(), "Swiped from " + oldElementIndex + " to " + newElementIndex, Toast.LENGTH_SHORT).show();

            }

        });



        engine.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {

            @Override

            public void onRightOut() {

                // Probably here will be your exit action

                //Toast.makeText(getApplicationContext(), "Swiped out right", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(SignIn.PREFS_NAME, 0);
                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

                if (hasLoggedIn) {
                    Intent intent = new Intent(PaperOnboardingActivity.this, MainActivity.class);
                    startActivity(intent);
                    PaperOnboardingActivity.this.finish();


                } else {
                    Intent intent = new Intent(PaperOnboardingActivity.this, SignIn.class);
                    startActivity(intent);
                    PaperOnboardingActivity.this.finish();
                }

            }

        });



    }



    // Just example data for Onboarding

    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {

        // prepare data

        //PaperOnboardingPage scr1 = new PaperOnboardingPage("View", "All hotels and hostels are sorted by hospitality rating",

                //Color.parseColor("#678FB4"), R.drawable.hotels, R.drawable.key);

        PaperOnboardingPage scr1 = new PaperOnboardingPage("Creatives", "A place where you can express your creativity to the fullest",

                Color.parseColor("#9B90BC"), R.mipmap.ic_launcher, R.drawable.wallet);

        PaperOnboardingPage scr2 = new PaperOnboardingPage("Share", "Your beautiful work with the world",

                Color.parseColor("#65B0B4"), R.drawable.share, R.drawable.wallet);

        PaperOnboardingPage scr3 = new PaperOnboardingPage("Discover", "Creativty where the only limit is you.",

                Color.parseColor("#9B90BC"), R.drawable.discover5 , R.drawable.wallet);


        PaperOnboardingPage scr5 = new PaperOnboardingPage("Inspire", "People and make the world a better place to live",

                Color.parseColor("#65B0B4"), R.drawable.inspire, R.drawable.wallet);




        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();

        elements.add(scr1);

        elements.add(scr2);

        elements.add(scr3);

        //elements.add(scr4);

        elements.add(scr5);

        return elements;

    }

}
