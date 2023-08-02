package com.example.shopeaze;

import android.provider.ContactsContract;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

/*

NOTE: this method provides methods to add, remove, and retrieve products from the db, while
maintaining a local copy of the products for efficient access and display w/i the app.

 */

public class ProductList {
    private DatabaseReference databaseReference;
    public List<Product> products;

    public ProductList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("products");
        products = new ArrayList<>();
        loadProductsFromFirebase();
    }

    public void addProduct(Product product) {
        String productID = databaseReference.push().getKey();
        product.setProductID(productID);

        databaseReference.child(productID).setValue(product);
    }

    public void removeProduct(Product product) {
        databaseReference.child(product.getProductID()).removeValue();
    }

    public List<Product> getAllProducts() { return products; }

    private void loadProductsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    products.add(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error loading products from Firebase: " + error.getMessage());
            }
        });
    }

    // Get the product by its ID (search method)
    public Product getProductByID(String productID) throws AppExceptions.ProductNotFoundException {
        for (Product product : products) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }

        // if the product is not found
        throw new AppExceptions.ProductNotFoundException("Product with ID " + productID + " not found.");
    }
}
