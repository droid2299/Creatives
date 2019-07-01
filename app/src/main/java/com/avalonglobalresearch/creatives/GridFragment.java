package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class GridFragment extends Fragment {

    CardView culinary , music , literature , design , handicrafts , photography , drawing , drama , miscellanous;
    View view;
    private SlidrInterface slidr;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_grid, container, false);

        culinary = view.findViewById(R.id.culinary);
        photography = (CardView) view.findViewById(R.id.photography);
        literature = (CardView) view.findViewById(R.id.literature);
        drama = (CardView) view.findViewById(R.id.drama);
        music = (CardView) view.findViewById(R.id.music);
        miscellanous = (CardView) view.findViewById(R.id.miscellanous);
        design = (CardView) view.findViewById(R.id.design);
        handicrafts = (CardView) view.findViewById(R.id.handicrafts);
        drawing = (CardView) view.findViewById(R.id.drawing);

        slidr = (SlidrInterface) Slidr.attach(getActivity());

        culinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Culinary.class);
                startActivity(intent);
            }
        });


        photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Photography.class);
                startActivity(intent);
            }
        });

        literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Literature.class);
                startActivity(intent);
            }
        });

        drama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Acting.class);
                startActivity(intent);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Music.class);
                startActivity(intent);
            }
        });

        miscellanous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Miscellanous.class);
                startActivity(intent);
            }
        });

        drawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Drawing.class);
                startActivity(intent);
            }
        });

        handicrafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Handicrafts.class);
                startActivity(intent);
            }
        });

        return view;


    }
}
