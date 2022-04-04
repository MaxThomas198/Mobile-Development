package com.maxwellthomasgrocerylistproject1.phoneapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maxwellthomasgrocerylistproject1.api.GroceryListAdapter;
import com.maxwellthomasgrocerylistproject1.api.models.GroceryList;
import com.maxwellthomasgrocerylistproject1.api.viewmodels.GroceryListViewModel;
import com.maxwellthomasgrocerylistproject1.api.viewmodels.UserViewModel;

public class MainActivity extends OnStartActivity {
    UserViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUser().observe(this, (user) -> {
            if (user != null) {
                viewModel.storeUserSpecificData();
            }
        });

        findViewById(R.id.logout_button).setOnClickListener((view) -> {
            viewModel.signOut();
        });

        RecyclerView groceryLists = findViewById(R.id.groceryLists);
        ObservableArrayList<GroceryList> grocerylists = viewModel.getGroceryLists();
        GroceryListViewModel groceryListViewModel = new ViewModelProvider(this).get(GroceryListViewModel.class);

        GroceryListAdapter adapter = new GroceryListAdapter(grocerylists, (grocerylist) -> {
            Intent intent = new Intent(this, AddGroceryListActivity.class);
            startActivity(intent);
        });
        groceryLists.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        findViewById(R.id.fab).setOnClickListener((view) -> {
            Intent intent = new Intent(this, AddGroceryListActivity.class);
            startActivity(intent);
            finish();
        });
    }


}

