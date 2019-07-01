package com.avalonglobalresearch.creatives;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageSelect extends AppCompatActivity {

    public static ImageView clickedImage;
    Button uploadButton;
    Bitmap bitmap , bitmap2;
    EditText caption;
    String photoPath = Environment.getExternalStorageDirectory() + "/photo.jpg" , downloadUrl;
    String[] country = { "India", "USA", "China", "Japan", "Other"};
    public static final String Database_Path = "All_Image_Uploads_Database", Storage_Path = "All_Image_Uploads/";
    private SlidrInterface slidr;
    public static final String KEY_EXTRA = "com.example.yourapp.KEY_BOOK";
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    Uri imageUri , FilePathUri;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference(Storage_Path);

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Storage_Path);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(ImageSelect.this);

        clickedImage = (ImageView) findViewById(R.id.imageView);
        uploadButton = (Button) findViewById(R.id.upload);
        uploadButton.setEnabled(false);
        caption = (EditText) findViewById(R.id.caption);
        slidr = Slidr.attach(this);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        if(CameraActivity.i == "1"  )
        {
            imageUri = getIntent().getData();
            clickedImage.setImageURI(imageUri);
        }

        else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(photoPath, options);

            File f = new File(photoPath);
            imageUri = getIntent().getData();

            clickedImage.setImageURI(imageUri);
        }

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);



        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //stuff here to handle item selection
                String spinnerResult = spinner.getSelectedItem().toString().trim();
                Log.d("TAG" , "SPINNERRESULT   = "+spinnerResult);
                if(spinnerResult != "Categories"){
                    uploadButton.setEnabled(true);
                    Toast.makeText(getApplicationContext() , "Please select a valid Category" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.i("GTOUTOUT", "Nothing Selected");
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImageFileToFirebaseStorage();
                //uploadFile(bitmap2);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Setting up bitmap selected image into ImageView.
                clickedImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                //ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

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


    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (imageUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));

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

                                    // Getting image name from EditText and store into string variable.
                                    String TempImageName = caption.getText().toString().trim();

                                    @SuppressWarnings("VisibleForTests")
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, downloadUrl);


                                    // Getting image upload ID.
                                    String ImageUploadId = databaseReference.push().getKey();

                                    // Adding image upload id s child element into databaseReference.
                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);

                                }
                            });



                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();



                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(ImageSelect.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(ImageSelect.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    private void uploadFile(Bitmap bitmap) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final StorageReference ImagesRef = storageRef.child(Storage_Path);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = ImagesRef.putBytes(data);



        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("whatTheFuck:",exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.i("problem", task.getException().toString());
                        }

                        return ImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

                            Log.i("seeThisUri", downloadUri.toString());// This is the one you should store

                            ref.child("imageURL").setValue(downloadUri.toString());


                        } else {
                            Log.i("wentWrong","downloadUri failure");
                        }
                    }
                });
            }
        });

    }

}



