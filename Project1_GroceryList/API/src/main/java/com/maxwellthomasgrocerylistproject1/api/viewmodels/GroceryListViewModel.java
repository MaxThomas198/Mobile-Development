package com.maxwellthomasgrocerylistproject1.api.viewmodels;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maxwellthomasgrocerylistproject1.api.models.GroceryList;
import com.maxwellthomasgrocerylistproject1.api.models.User;
import com.maxwellthomasgrocerylistproject1.api.viewmodels.UserViewModel;

public class GroceryListViewModel extends ViewModel {


    public void addGroceryList(String name) {
        GroceryList newGroceryList = new GroceryList(name);
    }

}
