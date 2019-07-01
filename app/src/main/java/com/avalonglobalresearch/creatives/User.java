package com.avalonglobalresearch.creatives;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String email;
    public String gender;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email , String gender) {
        this.name = name;
        this.email = email;
        this.gender = gender;
    }
}
