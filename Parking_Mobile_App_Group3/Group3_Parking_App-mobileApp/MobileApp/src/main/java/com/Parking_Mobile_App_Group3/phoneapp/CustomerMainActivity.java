package com.Parking_Mobile_App_Group3.phoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CustomerMainActivity extends ActivityWithUser {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        findViewById(R.id.logout).setOnClickListener((view) -> {
            userViewModel.signOut();
        });
    }
}