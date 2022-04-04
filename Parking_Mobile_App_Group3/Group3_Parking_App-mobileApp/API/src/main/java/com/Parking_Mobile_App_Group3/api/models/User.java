package com.Parking_Mobile_App_Group3.api.models;
import com.google.firebase.auth.FirebaseUser;

public class User {
    String uid;
    String email;
    String userType;
    double balance;

    public User(FirebaseUser user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
    }
}
