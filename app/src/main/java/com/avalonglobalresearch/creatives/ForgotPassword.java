package com.avalonglobalresearch.creatives;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;


public class ForgotPassword extends AppCompatActivity {

    TextView forgotPassword;
    EditText emailID    , newPassword , confirmPassword;
    Button update;
    String emailid , uid , conPass;
    FirebaseAuth auth;
    private SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        slidr = Slidr.attach(this);
        auth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotpassword);
        emailID = (EditText)findViewById(R.id.email);

        update = (Button) findViewById(R.id.updateButton);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailid = emailID.getText().toString();


                if (TextUtils.isEmpty(emailid)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.sendPasswordResetEmail(emailid)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });



        TextPaint paint = forgotPassword.getPaint();
        float width = paint.measureText("X Football");

        Shader textShader = new LinearGradient(0, 0, width, forgotPassword.getTextSize(),
                new int[]{
                        Color.parseColor("#6699ff"),
                        Color.parseColor("#9900ff"),

                }, null, Shader.TileMode.CLAMP);
        forgotPassword.getPaint().setShader(textShader);
    }
}
