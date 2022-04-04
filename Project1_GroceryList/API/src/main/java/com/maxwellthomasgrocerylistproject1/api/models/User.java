package com.maxwellthomasgrocerylistproject1.api.models;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;


public class User {
    public String uid;
    public String email;

    public User(FirebaseUser user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
    }

}
