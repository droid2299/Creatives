package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    TextView title, signup, forgotPassword;
    EditText eMail, passWord;
    String email, password, uid;
    Button signin;
    FirebaseAuth auth;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SharedPreferences settings = getSharedPreferences(SignIn.PREFS_NAME, 0);
        SplashActivity sa = new SplashActivity();
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned


        auth = FirebaseAuth.getInstance();


        forgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        eMail = (EditText) findViewById(R.id.Email);
        passWord = (EditText) findViewById(R.id.Password);
        title = (TextView) findViewById(R.id.title);
        title.setText("Creatives".toUpperCase());
        signup = (TextView) findViewById(R.id.Signup);
        signin = (Button) findViewById(R.id.LoginButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(intent);

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = eMail.getText().toString();
                password = passWord.getText().toString();

                //Log.d("UID , PASSWORD", "UID = " + username + " PASS = " + password);

                auth.signInWithEmailAndPassword(email , password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        passWord.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SignIn.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    SharedPreferences settings = getSharedPreferences(SignIn.PREFS_NAME, 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putBoolean("hasLoggedIn", true);
                                    editor.commit();
                                    SplashActivity.hasSignedIn = true;
                                    Intent intent = new Intent(SignIn.this, MainFrame.class);
                                    startActivity(intent);
                                    SignIn.this.finish();


                                }
                            }
                        });
            }
        });


        TextPaint paint = title.getPaint();
        float width = paint.measureText("Creatives");

        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#6699ff"),
                        Color.parseColor("#9900ff"),

                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);


    }

}