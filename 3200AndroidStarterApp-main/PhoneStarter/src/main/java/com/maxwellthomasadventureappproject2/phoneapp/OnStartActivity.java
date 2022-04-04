package com.maxwellthomasadventureappproject2.phoneapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.maxwellthomasadventureappproject2.api.viewmodels.UserViewModel;

public abstract class OnStartActivity extends AppCompatActivity{
    protected UserViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int RECORD_CODE = 1;
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RECORD_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getUser().observe(this, (user) -> {
            if (user == null) {
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}