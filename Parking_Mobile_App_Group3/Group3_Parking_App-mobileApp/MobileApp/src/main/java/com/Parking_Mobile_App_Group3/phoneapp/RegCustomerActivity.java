package com.Parking_Mobile_App_Group3.phoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.Parking_Mobile_App_Group3.api.viewmodels.UserViewModel;

public class RegCustomerActivity extends AppCompatActivity {

    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_customer);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button signIn = findViewById(R.id.login);
        Button signUp = findViewById(R.id.signup);
        Button back = findViewById(R.id.back);

        signIn.setOnClickListener((view) -> {
            viewModel.signIn(
                    email.getText().toString(),
                    password.getText().toString()
            );
        });

        signUp.setOnClickListener(view -> {
            viewModel.signUp(
                    email.getText().toString(),
                    password.getText().toString()
            );
        });

        back.setOnClickListener((view) -> {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getUser().observe(this, (user) -> {
            if (user != null) {
                Intent intent = new Intent(this, CustomerMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}