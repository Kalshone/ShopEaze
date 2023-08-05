package com.example.shopeaze;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

/*

NOTE: this method provides methods to add, remove, and retrieve products from the db, while
maintaining a local copy of the products for efficient access and display w/i the app.

 */

public class ProductList {
    public interface OnProductsLoadedListener{
        void onProductsLoaded(List<Product> products);
    }
    private static final String TAG = "ProductList";
    private DatabaseReference databaseReference;
    public List<Product> products;
    private OnProductsLoadedListener onProductsLoadedListener;

    public ProductList() {
        Log.d(TAG, "Creating new ProductList");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("products");
        products = new ArrayList<>();
        loadProductsFromFirebase();
    }

    public ProductList(String storeID) {
        Log.d(TAG, "In ProductList.java: Creating ProductList with storeID: " + storeID);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users").child("StoreOwner").child(storeID).child("Products");
        products = new ArrayList<>();
        loadProductsFromFirebase();
    }

    public void setOnProductsLoadedListener(OnProductsLoadedListener listener) {
        this.onProductsLoadedListener = listener;
    }

    public void addProduct(Product product) {
        Log.d(TAG, "In ProductList.java: Adding new product: " + product.getName());
        String productID = databaseReference.push().getKey();
        product.setProductID(productID);

        databaseReference.child(productID).setValue(product);
    }

    public void removeProduct(Product product) {
        Log.d(TAG, "In ProductList.java: Removing product: " + product.getName());
        databaseReference.child(product.getProductID()).removeValue();
    }

    public List<Product> getAllProducts() {
        Log.d(TAG, "Getting all products");
        return products;
    }

    private void loadProductsFromFirebase() {
        Log.d(TAG, "Loading products from Firebase");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Data changed in Firebase, updating local list of products");
                products.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        String productId = snapshot.getKey();
                        product.setProductID(productId);
                        products.add(product);
                    }
                }
                Log.d("ProductList", "Loaded " + products.size() + " products from Firebase");
                if (onProductsLoadedListener != null) {
                    onProductsLoadedListener.onProductsLoaded(products);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error loading products from Firebase: " + error.getMessage());
                System.err.println("Error loading products from Firebase: " + error.getMessage());
            }
        });
    }

    // Get the product by its ID (search method)
    public Product getProductByID(String productID) throws AppExceptions.ProductNotFoundException {
        Log.d(TAG, "Getting product by ID: " + productID);
        for (Product product : products) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }

        // if the product is not found
        Log.e(TAG, "Product with ID " + productID + " not found.");
        throw new AppExceptions.ProductNotFoundException("Product with ID " + productID + " not found.");
    }
}