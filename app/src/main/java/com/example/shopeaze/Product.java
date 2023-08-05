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

    public Product() {}

    // Constructor
    public Product(String name, String brand, double price, String description) {
        this.productID = generateProductID();
        this.productName = name;
        this.productBrand = brand;
        this.productPrice = price;
        this.productDescription = description;
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

    // Setters
    public void setProductID(String id) { this.productID = id; }
    public void setName(String name) { this.productName = name; }
    public void setPrice(double price) { this.productPrice = price; }
    public void setDescription(String description) { this.productDescription = description; }
    public void setBrand(String brand) { this.productBrand = brand; }
}