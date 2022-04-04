package com.maxwellthomasgrocerylistproject1.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.maxwellthomasgrocerylistproject1.api.viewmodels.UserViewModel;

public class SignUpActivity extends AppCompatActivity {
    UserViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        Button signUp = findViewById(R.id.signup);

        signUp.setOnClickListener((view) -> {
            viewModel.signUp(
                    email.getText().toString(),
                    password.getText().toString()
            );
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getUser().observe(this, (user) -> {
            if (user != null) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
