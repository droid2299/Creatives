package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class UploadType extends AppCompatActivity {

    Button image , text , video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_type);

        image = (Button) findViewById(R.id.image);
        text = (Button) findViewById(R.id.text);
        video = (Button) findViewById(R.id.video);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadType.this , CameraActivity.class);
                startActivity(intent);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadType.this , TextUpload.class);
                startActivity(intent);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadType.this , VideoUpload.class);
                startActivity(intent);
            }
        });
    }
}
