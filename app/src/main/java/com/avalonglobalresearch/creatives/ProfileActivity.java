package com.avalonglobalresearch.creatives;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    TextView nameTextView , bioTextView , emailID;
    public String name2;
    public String bio2;
    public static String uidString;
    Uri imageUri , FilePathUri;
    Button logOut , editProfile;
    private SlidrInterface slidr;
    CircleImageView profilepic;
    StorageReference storageReference;
    String Storage_Path = "ProfilePictures/" , downloadUrl;
    Context context;

    ImageView imageList , videoList ;
    TextView textList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = auth.getCurrentUser();
        nameTextView = (TextView) findViewById(R.id.name);
        bioTextView = (TextView) findViewById(R.id.bio);
        emailID = (TextView) findViewById(R.id.emailID);
        logOut = (Button) findViewById(R.id.logout2);
        editProfile = (Button) findViewById(R.id.editprofile);
        slidr = Slidr.attach(this);
        profilepic = (CircleImageView) findViewById(R.id.profilepic);
        storageReference = FirebaseStorage.getInstance().getReference();
        imageList = (ImageView) findViewById(R.id.imageList);
        videoList = (ImageView) findViewById(R.id.videoList);
        textList = (TextView) findViewById(R.id.textList);

        Intent intent = getIntent();
        Bundle uid = intent.getExtras();
        uidString = uid.getString("UID");
        Log.d("UID OF USER" , "UID OF USER = "+uidString);

        final Context finalContext = context;
        databaseReference.child("users").child(uidString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name2 = (String) dataSnapshot.child("name").getValue();
                bio2 = (String) dataSnapshot.child("bio").getValue();


                String gender = (String) dataSnapshot.child("gender").getValue();
                final String email = (String) dataSnapshot.child("email").getValue();

                Log.d("TAG", "Name: " + name2);
                Log.d("TAG", "Email: " + email);
                Log.d("TAG", "Gender: " + gender);
                Log.d("TAG", "Profile Pic URI = " + imageUri);
                nameTextView.setText(name2);
                bioTextView.setText(bio2);
                emailID.setPaintFlags(emailID.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                emailID.setText(email);

                emailID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    }
                });


                storageReference.child("ProfilePictures").child(uidString+".jpg");
                String imageUrl = (String) dataSnapshot.child("profilePic").getValue();
                GlideApp.with(ProfileActivity.this).load(imageUrl).into(profilepic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        loadFragment(new ProfileImagesFragment());

        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileImagesFragment());
            }
        });

        videoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileVideosFragment());
            }
        });

        textList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileTextsFragment());
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
