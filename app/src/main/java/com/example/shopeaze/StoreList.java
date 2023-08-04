// StoreList.java
package com.example.shopeaze;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoreList {

    public interface OnStoresLoadedListener {
        void onStoresLoaded(List<Store> stores);
    }

    private DatabaseReference databaseReference;
    private List<Store> stores;
    private OnStoresLoadedListener onStoresLoadedListener;

    public StoreList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users").child("StoreOwner");
        stores = new ArrayList<>();
        loadStoresFromFirebase();
    }

    public void setOnStoresLoadedListener(OnStoresLoadedListener listener) {
        this.onStoresLoadedListener = listener;
    }

    public List<Store> getAllStores() {
        return stores;
    }

    private void loadStoresFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stores.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Store store = snapshot.getValue(Store.class);
                    stores.add(store);
                }
                Log.d("StoreList", "Loaded " + stores.size() + " stores from Firebase");
                if (onStoresLoadedListener != null) {
                    onStoresLoadedListener.onStoresLoaded(stores);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error loading stores from Firebase: " + error.getMessage());
            }
        });
    }

    public Store getStoreByID(String storeID) throws AppExceptions.StoreNotFoundException {
        for (Store store : stores) {
            if (store.getStoreID().equals(storeID)) {
                return store;
            }
        }

        // if the store is not found
        throw new AppExceptions.StoreNotFoundException("Store with ID " + storeID + " not found.");
    }
}
