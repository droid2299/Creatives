package com.avalonglobalresearch.creatives;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextUpload extends AppCompatActivity {
    EditText text;
    Button upload;
    String textResult, textPath = Environment.getExternalStorageDirectory() + "textfile.txt", downloadUrl;
    String Storage_Path = "All_Uploads/";
    String spinnerResult , imageUrl , name2 , bio2 , uid;
    Uri fileUri, FilePathUri;
    File gpxfile;
    EditText caption;
    File mediaFile;
    StorageReference storageRef;
    DatabaseReference databaseReference , databaseReference2 ,  databaseReference3 , databaseReference4;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_upload);

        text = (EditText) findViewById(R.id.text);
        upload = (Button) findViewById(R.id.upload);
        caption = (EditText) findViewById(R.id.caption);
        progressDialog = new ProgressDialog(TextUpload.this);
        databaseReference = FirebaseDatabase.getInstance().getReference(Storage_Path);
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = auth.getCurrentUser();


        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

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
                    Storage_Path += spinnerResult + "/Texts";
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

        mediaFile = new
                File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/textfile.txt");

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
                Log.d("TAG" , "UID OF USER = " +uid);

                imageUrl = (String) dataSnapshot.child("profilePic").getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textResult = text.getText().toString();

                try {
                    writeToFile(textResult, TextUpload.this);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                uploadTextToFireBase();
            }
        });

    }

    private void writeToFile(String data, Context context) throws FileNotFoundException {

         mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!mediaFile.exists()) {
            mediaFile.mkdir();
        }
        byte[] data2 = data.getBytes();

        File txtFile = new File(mediaFile, "textfile.txt");
        FileOutputStream f = new FileOutputStream(txtFile );
        try {
            f.write(data2);
            f.flush();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileUri =  Uri.fromFile(txtFile);
        Log.d("TAHH" , "URI = "+fileUri);
    }


    /*public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }*/

    public void uploadTextToFireBase(){
        storageRef = FirebaseStorage.getInstance().getReference(Storage_Path);
        final StorageReference storageReference2nd = storageRef.child(System.currentTimeMillis() + ".txt" );
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user2 = auth.getCurrentUser();

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
                                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, text.getText().toString().trim() , name2 , imageUrl , uid);
                                            Log.d("TAGGGGGGGGG" , ""+imageUploadInfo);


                                            // Getting image upload ID.
                                            String ImageUploadId = databaseReference.push().getKey();

                                            // Adding image upload id s child element into databaseReference.
                                            databaseReference.child(spinnerResult).child("/Texts").child(ImageUploadId).setValue(imageUploadInfo);
                                            databaseReference2.child("/Texts").child(ImageUploadId).setValue(imageUploadInfo);
                                            databaseReference4.child("users").child(user2.getUid()).child("/Texts").child(ImageUploadId).setValue(imageUploadInfo);
                                        }
                                    });

                                }
                            });


                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Text Uploaded Successfully ", Toast.LENGTH_LONG).show();


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(TextUpload.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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
                            progressDialog.setTitle("Text is Uploading... " + progress + "% done");

                        }
                    });
        }
        else{
            Toast.makeText(TextUpload.this, "Please try again", Toast.LENGTH_LONG).show();
        }
    }


}

