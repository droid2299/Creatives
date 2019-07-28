package com.avalonglobalresearch.creatives;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.r0adkll.slidr.model.SlidrInterface;

public class DiscoverFragment extends Fragment {

    ImageView imageList , videoList ;
    TextView textList;
    View view;

    private SlidrInterface slidr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        view = inflater.inflate(R.layout.fragment_discover, container, false);

        imageList = (ImageView) view.findViewById(R.id.imageList);
        videoList = (ImageView) view.findViewById(R.id.videoList);
        textList = (TextView) view. findViewById(R.id.textList);

        loadFragment(new DiscoverImageFragment());

        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new DiscoverImageFragment());
            }
        });

        videoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tagg" , "BUTTON PRESSED");
                loadFragment(new DiscoverVideosFragment());
            }
        });

        textList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tagg" , "BUTTON PRESSED");
                loadFragment(new DiscoverTextsFragment());
            }
        });
        return view;
    }

    public boolean loadFragment(Fragment fragment){

        if (fragment != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return  false;
    }
}
