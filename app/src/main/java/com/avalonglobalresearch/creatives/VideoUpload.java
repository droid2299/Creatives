package com.avalonglobalresearch.creatives;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.File;

public class VideoUpload extends AppCompatActivity {

    private Uri fileUri;
    private static final int VIDEO_CAPTURE = 101;
    VideoView videoView;
    FirebaseAuth auth;
    Button upload;
    StorageReference storageRef;
    DatabaseReference databaseReference , databaseReference2 ,  databaseReference3 , databaseReference4 , databaseReference5;
    String Storage_Path = "All_Uploads/" , downloadUrl , fileUrl , name2 , bio2 , imageUrl , uid;
    String spinnerResult;
    EditText caption;
    Uri FilePathUri;
    ProgressDialog progressDialog ;
    private SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);


        auth = FirebaseAuth.getInstance();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Storage_Path);
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        videoView =(VideoView)findViewById(R.id.videoView);
        upload = (Button) findViewById(R.id.upload);
        caption = (EditText) findViewById(R.id.caption);
        progressDialog = new ProgressDialog(VideoUpload.this);
        upload.setEnabled(false);
        slidr = Slidr.attach(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                    R.array.spinner_options, android.R.layout.simple_spinner_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //stuff here to handle item selection
                    spinnerResult = spinner.getSelectedItem().toString().trim();
                    Log.d("TAG" , "SPINNERRESULT   = "+spinnerResult);
                    if(!spinnerResult.equals("Categories") ){
                        Storage_Path += spinnerResult + "/Videos";
                        upload.setEnabled(true);
                        Log.d("TAG" , "PATH = "+Storage_Path);
                        //Toast.makeText(getApplicationContext() , "Please select a valid Category" , Toast.LENGTH_SHORT).show();
                    }
                    else{
                        upload.setEnabled(false);
                        Toast.makeText(getApplicationContext() , "Please select a valid Category" , Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    Log.i("GTOUTOUT", "Nothing Selected");
                    upload.setEnabled(false);
                    Toast.makeText(getApplicationContext() , "Please select a valid Category" , Toast.LENGTH_SHORT).show();
                }
        });


        databaseReference3.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
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

                uid = user.getUid();
                Log.d("TAG" , "UID = " +uid);


                imageUrl = (String) dataSnapshot.child("profilePic").getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        File mediaFile = new
                File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/myvideo.mp4");

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //fileUri = Uri.fromFile(mediaFile);
        fileUri = FileProvider.getUriForFile(VideoUpload.this , BuildConfig.APPLICATION_ID + ".provider" , mediaFile );

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY , 0);
        long maxVideoSize = 5 * 1024 * 1024;
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT , maxVideoSize);
        startActivityForResult(intent, VIDEO_CAPTURE);



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadVideo();
                uploadVideoToFirebase();
            }
        });

    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                //Toast.makeText(this, "Video has been saved to:\n" +
                       // data.getData(), Toast.LENGTH_LONG).show();
                MediaController mediaController= new MediaController(this);
                mediaController.setAnchorView(videoView);
                Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/myvideo.mp4");

                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
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

    public void uploadVideo(){

        final StorageReference VideoRef = storageRef.child(Storage_Path);
        UploadTask uploadTask = VideoRef.putFile(fileUri);



        uploadTask = storageRef.child(Storage_Path).putFile(fileUri );

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {





            }
        });


    }

    public void uploadVideoToFirebase(){
        storageRef = FirebaseStorage.getInstance().getReference(Storage_Path);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user2 = auth.getCurrentUser();
        final StorageReference storageReference2nd = storageRef.child(System.currentTimeMillis() + "." + GetFileExtension(fileUri));

        if(fileUri != null) {

            storageReference2nd.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.setTitle("Video is Uploading...");
                            progressDialog.show();

                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("TAG", "onSuccess: uri= " + uri.toString());
                                    FilePathUri = uri;
                                    downloadUrl = FilePathUri.toString();
                                    Log.d("TAG", "DOWN = " + downloadUrl);

                                    storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("TAG", "onSuccess: uri= "+ uri.toString());
                                            FilePathUri = uri;
                                            downloadUrl = FilePathUri.toString();
                                            Log.d("TAG" , "DOWN = " +downloadUrl);

                                            // Getting image name from EditText and store into string variable.
                                            String TempImageName = caption.getText().toString().trim();

                                            @SuppressWarnings("VisibleForTests")
                                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, downloadUrl , name2 , imageUrl , uid);
                                            Log.d("TAGGGGGGGGG" , ""+imageUploadInfo);


                                            // Getting image upload ID.
                                            String ImageUploadId = databaseReference.push().getKey();
                                            String ImageUploadId2 = databaseReference2.push().getKey();
                                            String ImageUploadId3 = databaseReference5.push().getKey();

                                            // Adding image upload id s child element into databaseReference.
                                            databaseReference.child(spinnerResult).child("/Videos").child(ImageUploadId).setValue(imageUploadInfo);
                                            databaseReference2.child("/Videos").child(ImageUploadId).setValue(imageUploadInfo);
                                            databaseReference5.child("users").child(user2.getUid()).child("/Videos").child(ImageUploadId).setValue(imageUploadInfo);
                                        }
                                    });

                                }
                            });


                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Video Uploaded Successfully ", Toast.LENGTH_LONG).show();


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(VideoUpload.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            //progressDialog.setTitle("Image is Uploading...");
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                            progressDialog.setTitle("Image is Uploading... " + progress + "% done");

                        }
                    });
        }
        else{
            Toast.makeText(VideoUpload.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }
}



