package com.example.shopeaze;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Store implements Serializable {

    private String storeId;
    private String StoreName;
    private String logoUrl;
    private ProductList productList;

    // constructors

    public Store() {
        // Default constructor required for calls to DataSnapshot.getValue(Store.class)
    }
    public Store(String storeId, String name, String email, String logoUrl) {
        this.StoreName = name;
        this.storeId = generateStoreID();
        this.logoUrl = logoUrl;
        this.productList = new ProductList();
    }

    private String generateStoreID() {
        String rand = UUID.randomUUID().toString();
        return rand;
    }

    // getters
    public String getStoreID() {
        //Log.d("Store class", "Just returned storeId to be" + storeId);
        return storeId;
    }

    public String getStoreName() {
        return StoreName;
    }


    public String getLogoUrl() {
        return logoUrl;
    }

    public ProductList getProductList() {
        return productList;
    }

    // setters
    public void setStoreID(String id) {
        //Log.d("Store class", "Just set storeId to be" + id);
        this.storeId = id;
    }

    public void setName(String name) {
        this.StoreName = name;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

}
