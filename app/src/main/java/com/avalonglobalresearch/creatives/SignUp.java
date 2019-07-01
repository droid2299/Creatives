package com.avalonglobalresearch.creatives;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class SignUp extends AppCompatActivity {

    ImageView backButton;
    Button signup;
    CardView male, female;
    String gender = "", password, Name, email ;
    EditText passWord, name, eMail;
    TextView txtDetails;
    FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private SlidrInterface slidr;
    //private DatabaseReference mDatabase;

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        backButton = (ImageView) findViewById(R.id.backButton);
        signup = (Button) findViewById(R.id.signup);
        male = (CardView) findViewById((R.id.Male));
        female = (CardView) findViewById((R.id.Female));
        passWord = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.Name);
        eMail = (EditText) findViewById(R.id.Email);
        slidr = Slidr.attach(this);
        //txtDetails = (TextView) findViewById(R.id.txt_user);


        // get reference to 'users' node
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // store app title to 'app_title' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        // app_title change listener
        mFirebaseInstance.getReference("app_title").setValue("User Profile");

        DatabaseError error;
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Signup", "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("TAG" , "Failed to read app title value.", error.toException());
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent
                );
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Male!", Toast.LENGTH_SHORT).show();
                gender = "male";

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Female!", Toast.LENGTH_SHORT).show();
                gender = "female";

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString().trim();
                password = passWord.getText().toString().trim();
                email = eMail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(getApplicationContext(), "Enter your Name !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.trim().length() < 8) {
                    passWord.setError("Passowrd must be of atleast 8 characters.");
                }




                Log.d("UID,PASS,EM,GENDER", "UID = " + Name + " PASS = " + password + " EM = " + email + " Gender = " + gender);



                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (TextUtils.isEmpty(userId)) {
                                    createUser(Name, email);
                                } else {
                                    updateUser(Name, email);
                                }

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignUp.this, MainActivity.class));
                                    finish();
                                }

                            }
                        });

            }

            //Creating new user node under 'users'
            private void createUser(String name, String email) {
                // TODO
                // In real apps this userId should be fetched
                // by implementing firebase auth
                if (TextUtils.isEmpty(userId)) {
                    userId = mFirebaseDatabase.push().getKey();
                }

                User user = new User(Name, email , gender);

                mFirebaseDatabase.child(userId).setValue(user);

                addUserChangeListener();
            }

            //User data change listener
            private void addUserChangeListener() {
                // User data change listener
                mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    // User data change listener
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        // Check for null
                        if (user == null) {
                            Log.e("TAG" , "User data is null!");
                            return;
                        }

                        Log.e("TAG", "User data is changed!" + user.name + ", " + user.email);

                        // Display newly updated name and email
                        //txtDetails.setText(user.name + ", " + user.email);

                        // clear edit text
                        eMail.setText("");
                        name.setText("");


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.e("TAG" , "Failed to read user", error.toException());
                    }
                });
            }
            private void updateUser(String name, String email) {
                // updating the user via child nodes
                if (!TextUtils.isEmpty(name))
                    mFirebaseDatabase.child(userId).child("name").setValue(name);

                if (!TextUtils.isEmpty(email))
                    mFirebaseDatabase.child(userId).child("email").setValue(email);
            }


        });
    }

}

