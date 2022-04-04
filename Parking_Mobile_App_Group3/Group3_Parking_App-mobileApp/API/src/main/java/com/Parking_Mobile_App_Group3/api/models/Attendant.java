package com.Parking_Mobile_App_Group3.api.models;

import com.google.firebase.auth.FirebaseUser;

public class Attendant extends User{
    String assignedParkingLotId;

    public Attendant(FirebaseUser user){
        super(user);
    }
}
