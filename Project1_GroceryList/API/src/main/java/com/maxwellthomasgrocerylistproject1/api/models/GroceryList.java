package com.maxwellthomasgrocerylistproject1.api.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class GroceryList implements Serializable {
    String name;

    @Exclude
    public String id;

    public GroceryList() {
        this.name = "GroceryList";
    }

    public GroceryList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}