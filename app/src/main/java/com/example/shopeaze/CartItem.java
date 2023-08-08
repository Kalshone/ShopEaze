package com.example.shopeaze;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartItem {
//Represents an item in cart
    private int cartQuantity;
    private String cartProductID;
    private String cartProductName;
    private String cartProductBrand;
    private double cartProductPrice;
    private int image;
    private String status;
    private String storeName;
    private String imageURL;


    public CartItem(){

    }
    public CartItem(Product product) {
        this.cartProductID = product.getProductID();
        this.cartProductName = product.getName();
        this.cartProductBrand = product.getBrand();
        this.cartProductPrice = product.getPrice();
        this.cartQuantity = product.getQuantity();
        this.image = product.getImage();
        this.status = product.getStatus();
        this.storeName = product.getStoreName();
        this.imageURL = product.getImage();
    }

    //Getters
    public String getcartProductID() {
        return cartProductID;
    }
    public String getStatus() {
        return status;
    }
    public String getcartProductName() {
        return cartProductName;
    }
    public String getcartProductBrand() {
        return cartProductBrand;
    }
    public double getcartProductPrice() {
        return cartProductPrice;
    }
    public int getCartQuantity() {
        return cartQuantity;
    }
    public String getImage() {
        return imageURL;
    }
    public String getStoreName() {
        return storeName;
    }

    //Necessary setters:
    public String setcartProductID(String productID) {
        return this.cartProductID = productID;
    }
    public String setcartProductName(String productName) {
        return this.cartProductName = productName;
    }
    public String setcartProductBrand(String productBrand) {
        return this.cartProductBrand = productBrand;
    }
    public double setcartProductPrice(double productPrice) {
        return this.cartProductPrice = productPrice;
    }
    public int setImage(int image) {
        return this.image = image;
    }
    public void setQuantity(int quantity){
    this.cartQuantity = quantity;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}
