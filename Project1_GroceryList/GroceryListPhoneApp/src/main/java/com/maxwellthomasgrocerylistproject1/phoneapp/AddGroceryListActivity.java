package com.maxwellthomasgrocerylistproject1.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.maxwellthomasgrocerylistproject1.api.models.User;
import com.maxwellthomasgrocerylistproject1.api.viewmodels.GroceryListViewModel;
import com.maxwellthomasgrocerylistproject1.api.viewmodels.UserViewModel;

public class AddGroceryListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery_list);

        findViewById(R.id.submitname).setOnClickListener((view) -> {
            EditText addGroceryListName = findViewById(R.id.grocerylistname);
            UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            viewModel.getUser().observe(this, (user) -> {
                if (user != null)
                    viewModel.addGroceryListToUser(addGroceryListName.getText().toString());

            });
            GroceryListViewModel viewModel1 = new GroceryListViewModel();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
