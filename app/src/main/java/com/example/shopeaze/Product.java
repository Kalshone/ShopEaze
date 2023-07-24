package com.example.shopeaze;

public class Product {
    private String productName;
    private String productBrand;
    double productPrice;

    public Product(){
    }

    public Product(String name, String brand, double price) {
        this.productName = name;
        this.productBrand = brand;
        this.productPrice = price;
    }

    public String getName() {
        return productName;
    }

    public String getBrand() {
        return productBrand;
    }

    public double getPrice() {
        return productPrice;
    }

}