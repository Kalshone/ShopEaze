package com.example.shopeaze;

import java.io.Serializable;
import java.util.UUID;
public class Product implements Serializable {
    // Fields
    private String productID;
    private String productName;
    private String productBrand;
    private String productDescription;
    private double productPrice;
    private String status;
    private String storeID;
    private int quantity;
    private String imageURL;

    public Product() {}

    // Constructor

    public Product(String name, String brand, double price, String description, int quantity, String status, String storeID, String productID){
        this.productID = productID;
        this.productName = name;
        this.productBrand = brand;
        this.productPrice = price;
        this.productDescription = description;
        this.status = status;
        this.quantity = quantity;
        this.storeID = storeID;
    }

    public Product(String name, String brand, double price, int quantity, String status, String storeID, String productID, String imageURL){
        this.productID = productID;
        this.productName = name;
        this.productBrand = brand;
        this.productPrice = price;
        this.status = status;
        this.quantity = quantity;
        this.storeID = storeID;
        this.imageURL = imageURL;
    }

    public Product(String name, String brand, double price,
                   String description, int quantity, String imageURL,
                   String status, String storeName, String storeID){


        this.productID = generateProductID();
        this.productName = name;
        this.productBrand = brand;
        this.productPrice = price;
        this.productDescription = description;
        this.status = status;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.storeID = storeID;
    }

    private String generateProductID() {
        String rand = UUID.randomUUID().toString();
        return rand;
    }

    // Getters
    public String getProductID() { return productID; }
    public String getName() {
        return productName;
    }
    public String getBrand() {
        return productBrand;
    }
    public String getDescription() { return productDescription; }
    public double getPrice() {
        return productPrice;
    }
    //    public int getQuantity() { return cartQuantity; }
    public String getImage() { return imageURL; }
    public String getStatus() { return status; }
    public int getQuantity() { return quantity; }
    public String getImageURL() { return imageURL; }
    public String getStoreID() { return storeID; }

    // Setters
    public void setProductID(String id) { this.productID = id; }
    public void setName(String name) { this.productName = name; }
    public void setPrice(double price) { this.productPrice = price; }
    public void setDescription(String description) { this.productDescription = description; }
    public void setBrand(String brand) { this.productBrand = brand; }
    //    public void setQuantity(int quantity) { this.cartQuantity = quantity; }
    public void setImage(String imageURL) { this.imageURL = imageURL; }
    public void setStatus(String status) { this.status = status; }
    public void setStoreID(String storeName) { this.storeID = storeID; }
    public void setQuantity(int quantity) { this.quantity = quantity; }


}