package com.avalonglobalresearch.creatives;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseInstance;
    String name2 , email2 , bio2 , downloadUrl , Storage_Path = "ProfilePictures/" , uid;
    EditText name , email , bio;
    private SlidrInterface slidr;
    ImageView profilePic;
    final int PICK_IMAGE_REQUEST = 71;
    Uri imageUri ,  FilePathUri;
    Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = auth.getCurrentUser();
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        bio = (EditText) findViewById(R.id.bio);
        profilePic = (ImageView) findViewById(R.id.profilepic);
        saveButton = (Button) findViewById(R.id.save);
        slidr = Slidr.attach(this);
        mFirebaseInstance = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference(Storage_Path);
        final String userID = user.getUid();
        Log.d("TAG" , "USERID = "+userID );
        uid = userID;

        databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                name2 = (String) dataSnapshot.child("name").getValue();
                String gender = (String) dataSnapshot.child("gender").getValue();
                email2 = (String) dataSnapshot.child("email").getValue();
                bio2 = (String) dataSnapshot.child("bio").getValue();
                Log.d("TAG", "Name: " +name2);
                Log.d("TAG", "Email: " +email);
                Log.d("TAG", "Gender: " +gender);
                name.setText(name2);
                email.setText(email2);
                bio.setText(bio2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTest = name.getText().toString();
                String emailTest = email.getText().toString();
                String bioTest = bio.getText().toString();

                Log.d("TAG" , "NAMETEST = "+nameTest);

                mFirebaseDatabase.child("users").child(userID).child("name").setValue(nameTest);
                mFirebaseDatabase.child("users").child(userID).child("bio").setValue(bioTest);
                mFirebaseDatabase.child("users").child(userID).child("email").setValue(emailTest);
                UploadImageFileToFirebaseStorage();
                //mFirebaseDatabase.child("users").child(userID).child("Profile").setValue(imageUri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                profilePic.setImageURI(imageUri);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (imageUri != null) {


            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(uid+ "." + GetFileExtension(imageUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("TAG", "onSuccess: uri= "+ uri.toString());
                                    FilePathUri = uri;
                                    downloadUrl = FilePathUri.toString();
                                    Log.d("TAG" , "DOWN = " +downloadUrl);
                                    String userID2 = auth.getCurrentUser().getUid();



                                    // Getting image upload ID.
                                    String ImageUploadId = databaseReference.push().getKey();

                                    // Adding image upload id s child element into databaseReference.
                                    databaseReference.child("user").child("ProfilePic").setValue(downloadUrl);
                                    mFirebaseDatabase.child("users").child(userID2).child("profilePic").setValue(downloadUrl);

                                }
                            });

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Profile Picture Updated Successfully", Toast.LENGTH_LONG).show();



                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Showing exception erro message.
                            Toast.makeText(EditProfile.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                        }
                    });
        }
        else {

            Toast.makeText(EditProfile.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

}
