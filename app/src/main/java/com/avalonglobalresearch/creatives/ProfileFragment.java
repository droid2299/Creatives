package com.avalonglobalresearch.creatives;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.r0adkll.slidr.model.SlidrInterface;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    FirebaseAuth auth;
    DatabaseReference databaseReference;
    TextView nameTextView , bioTextView;
    String name2 , bio2;
    Uri imageUri , FilePathUri;
    ImageView imageList , videoList ;
    TextView textList;
    private SlidrInterface slidr;
    CircleImageView profilepic;
    StorageReference storageReference;
    String Storage_Path = "ProfilePictures/" , downloadUrl;
    Context context;
    View view;
    ImageView settings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = auth.getCurrentUser();
        nameTextView = (TextView) view.findViewById(R.id.name);
        bioTextView = (TextView) view.findViewById(R.id.bio);
        settings = (ImageView) view.findViewById(R.id.settings);
        imageList = (ImageView) view.findViewById(R.id.imageList);
        videoList = (ImageView) view.findViewById(R.id.videoList);
        textList = (TextView) view.findViewById(R.id.textList);


        profilepic = (CircleImageView) view.findViewById(R.id.profilepic);
        storageReference = FirebaseStorage.getInstance().getReference();

        final Context finalContext = context;
        databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                name2 = (String) dataSnapshot.child("name").getValue();
                bio2 = (String) dataSnapshot.child("bio").getValue();

                //imageUri = (URL) dataSnapshot.child("profilePic").getValue();
                String gender = (String) dataSnapshot.child("gender").getValue();
                String email = (String) dataSnapshot.child("email").getValue();

                Log.d("TAG", "Name: " +name2);
                Log.d("TAG", "Email: " +email);
                Log.d("TAG", "Gender: " +gender);
                Log.d("TAG" , "Profile Pic URI = "+imageUri);
                nameTextView.setText(name2);
                bioTextView.setText(bio2);
                String uid = user.getUid();
                Log.d("TAG" , "UID = " +uid);
                storageReference.child("ProfilePictures").child(uid+".jpg");
                String imageUrl = (String) dataSnapshot.child("profilePic").getValue();
                GlideApp.with(getActivity()).load(imageUrl).into(profilepic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , Settings.class);
                startActivity(intent);
            }
        });

        Log.d("TAG" , "NAME OUTSIDE " +name2);

        loadFragment(new ProfileFragmentImagesFragment());

        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileFragmentImagesFragment());
            }
        });

        videoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileFragmentVideosFragment());
            }
        });

        textList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileFragmentTextsFragment());
            }
        });

        return view;
    }

    public boolean loadFragment(Fragment fragment){

        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return  false;
    }
}
