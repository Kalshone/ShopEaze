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
    private int cartQuantity;
    private int image;
    private String status;
    private String storeName;
    private int quantity;
    private String imageURL;

    public Product() {}

    // Constructor
    public Product(String name, String brand, double price, String description, int cartQuantity, int image, String imageURL, String status, String storeName){

        this.productID = generateProductID();
        this.productName = name;
        this.productBrand = brand;
        this.productPrice = price;
        this.productDescription = description;
        this.cartQuantity = cartQuantity;
        this.image = image;
        this.status = status;
        this.storeName = storeName;
        this.quantity = quantity;
        this.imageURL = imageURL;
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
    public int getQuantity() { return cartQuantity; }
    public int getImage() { return image; }
    public String getStatus() { return status; }
    public String getStoreName() { return storeName; }
    public int getQuantity() { return quantity; }
    public String getImageURL() { return imageURL; }
    // Setters
    public void setProductID(String id) { this.productID = id; }
    public void setName(String name) { this.productName = name; }
    public void setPrice(double price) { this.productPrice = price; }
    public void setDescription(String description) { this.productDescription = description; }
    public void setBrand(String brand) { this.productBrand = brand; }
    public void setQuantity(int quantity) { this.cartQuantity = quantity; }
    public void setImage(int image) { this.image = image; }
    public void setStatus(String status) { this.status = status; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

}