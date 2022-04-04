package com.maxwellthomasadventureappproject2.watchapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.maxwellthomasadventureappproject2.api.Verify;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Verify.verifyWatchApp();
    }
}