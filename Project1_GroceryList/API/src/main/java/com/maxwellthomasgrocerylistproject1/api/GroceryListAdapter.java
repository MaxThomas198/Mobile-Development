package com.maxwellthomasgrocerylistproject1.api;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.maxwellthomasgrocerylistproject1.api.models.GroceryList;
import com.maxwellthomasgrocerylistproject1.api.CustomAdapter;

public class GroceryListAdapter extends CustomAdapter<GroceryList> {
    GroceryListClicked listener;
    public GroceryListAdapter(ObservableArrayList<GroceryList> data, GroceryListClicked listener) {
        super(data);
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.grocerylist;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroceryList groceryList = data.get(position);
        CheckBox groceryListNameDisplay = holder.getItemView().findViewById(R.id.grocerylist);
        groceryListNameDisplay.setText(groceryList.getName());
        groceryListNameDisplay.setOnClickListener(view -> {
            listener.onClick(groceryList);
        });
    }
}
