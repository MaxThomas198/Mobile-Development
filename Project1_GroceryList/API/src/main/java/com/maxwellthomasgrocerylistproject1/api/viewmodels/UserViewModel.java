package com.maxwellthomasgrocerylistproject1.api.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maxwellthomasgrocerylistproject1.api.models.GroceryList;
import com.maxwellthomasgrocerylistproject1.api.models.User;

public class UserViewModel extends ViewModel {
    FirebaseAuth auth;
    DatabaseReference database;
    MutableLiveData<User> user = new MutableLiveData<>();
    //    MutableLiveData<RuntimeException> loginError = new MutableLiveData<>();
    public UserViewModel() {
        this.auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = auth.getCurrentUser();
//                loginError.setValue(null);
        if (fbUser == null) {
            user.setValue(null);
        } else {
            user.setValue(new User(fbUser));
        }
        this.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = auth.getCurrentUser();
//                loginError.setValue(null);
                if (fbUser == null) {
                    user.setValue(null);
                } else {
                    user.setValue(new User(fbUser));
                }
            }
        });
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void signUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password);
//        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                AuthResult result = task.getResult();
//                if (result.getUser() == null) {
//                    loginError.setValue(new RuntimeException("Signup failed"));
//                }
//            }
//        });
    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password);
    }

    public void signOut() {
        auth.signOut();
    }

    public void storeUserSpecificData() {
        if (user.getValue() == null) return;
        database.child("userData").child(user.getValue().uid);
    }

    public void addGroceryListToUser(String name){
        if (user.getValue() == null) return;
        GroceryList newGroceryList = new GroceryList(name);
        database.child("/Shopping List").child(user.getValue().uid).push().setValue(newGroceryList);
        database.child("/Shopping List").child(user.getValue().uid).keepSynced(true);
    }
    private ObservableArrayList<GroceryList> groceryLists;
    private DatabaseReference db;
    public void GroceryListViewModel() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public ObservableArrayList<GroceryList> getGroceryLists() {
        if (groceryLists == null) {
            groceryLists = new ObservableArrayList<GroceryList>();
            loadContacts();
        }
        return groceryLists;
    }

    private void loadContacts() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uId = firebaseUser != null ? firebaseUser.getUid() : null;
        assert uId != null;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GroceryList grocerylist = snapshot.getValue(GroceryList.class);
                assert grocerylist != null;
                grocerylist.id = snapshot.getKey();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("CHANGED", "A contact was changed");
                GroceryList grocerylist = snapshot.getValue(GroceryList.class);
                assert grocerylist != null;
                grocerylist.id = snapshot.getKey();
                int index = groceryLists.indexOf(grocerylist);
                groceryLists.set(index, grocerylist);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GroceryList grocerylist = snapshot.getValue(GroceryList.class);
                assert grocerylist != null;
                grocerylist.id = snapshot.getKey();
                groceryLists.remove(grocerylist);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addGroceryList(String name) {
        if (user.getValue() == null) return;
        GroceryList newGroceryList = new GroceryList(name);
        database.child("/Shopping List").child(user.getValue().uid).push().setValue(newGroceryList);
    }

}