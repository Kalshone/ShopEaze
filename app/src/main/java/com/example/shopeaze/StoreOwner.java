package com.example.shopeaze;

import com.example.shopeaze.AccountOwner;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class StoreOwner extends Account {
    String storeName;


    private ArrayList<Product> products;


    public StoreOwner(String username, String password, String storeName){
        this.storeName = storeName;
        products = new ArrayList<>();
        //orders = new ArrayList<>();
    }

    public String getStoreName(){
        return storeName;
    }

    @Override
    public boolean login(String username, String password) {
        return false;
    }

    public Object getProducts() {
        return products;
    }
}
