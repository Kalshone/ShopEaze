
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

    private static final String TAG = "StoreList";
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

                Log.d("StoreList", "onDataChange called"); // Add this log statement
                stores.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Store store = snapshot.getValue(Store.class);
                    if (store != null) {
                        String storeId = snapshot.getKey();
                        store.setStoreID(storeId);
                        stores.add(store);
                    }

                }
                Log.d("StoreList", "Loaded " + stores.size() + " stores from Firebase");
                if (onStoresLoadedListener != null) {
                    onStoresLoadedListener.onStoresLoaded(stores);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("StoreList", "onCancelled called with error: " + error.getMessage()); // Add this log statement
                System.err.println("Error loading stores from Firebase: " + error.getMessage());
            }
        });

}

    public Store getStoreByID(String storeID) throws AppExceptions.StoreNotFoundException {
        for (Store store : stores) {
            Log.d("StoreList", "Now comparing" + storeID + " with store ID " + store.getStoreID());
            if (store.getStoreID().equals(storeID)) {
                Log.d("StoreList", "Found the store with ID " + storeID);
                return store;
            }
        }

        // if the store is not found
        Log.d("StoreList", "Did not find the store with ID " + storeID);
        throw new AppExceptions.StoreNotFoundException("Store with ID " + storeID + " not found.");
    }
}
