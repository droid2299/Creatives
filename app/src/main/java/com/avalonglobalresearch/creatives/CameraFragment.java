package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.camerakit.CameraKitView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {

    View view;
    final int PICK_IMAGE_REQUEST = 71;
    private CameraKitView cameraKitView;
    Button button;
    static Uri filepath;
    TextView uploadText;
    ImageView shutterButton , gallery , videoCamera;
    private SlidrInterface slidr;
    public static String i = "0";
    Uri imageUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageUri = data.getData();
            //String test = filepath.toString();
            //Log.d("CameraActivity" , " Path is = " +test);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byte[] byteArray = stream.toByteArray();
                i = "1";
                Intent intent = new Intent(getActivity() , ImageSelect.class);
                intent.setData(imageUri);
                startActivity(intent);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    public void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_camera, container, false);



        cameraKitView = (CameraKitView) view.findViewById(R.id.camera);
        final MediaPlayer cameraclick = MediaPlayer.create(getContext(), R.raw.cameraclick);
        shutterButton = (ImageView) view.findViewById(R.id.shutter);
        gallery = (ImageView) view.findViewById(R.id.gallery1);
        videoCamera = (ImageView) view.findViewById(R.id.video);
        uploadText = (TextView) view.findViewById(R.id.uploadText);

        slidr = Slidr.attach(getActivity());

        shutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CameraActivity" , "Button pressed");
                cameraclick.start();
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                        File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                        imageUri = Uri.fromFile(savedPhoto);
                        try {
                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(capturedImage);
                            outputStream.close();
                            Intent intent = new Intent(getActivity() , ImageSelect.class);
                            intent.setData(imageUri);
                            startActivity(intent);
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        videoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , VideoUpload.class);
                startActivity(intent);
            }
        });

        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , TextUpload.class);
                startActivity(intent);
            }
        });

        return view;
    }

}

