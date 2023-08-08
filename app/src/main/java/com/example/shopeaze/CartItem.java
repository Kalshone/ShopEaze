package com.example.shopeaze;

public class CartItem {
//Represents an item in cart
    private int quantity;
    private String cartProductID;
    private String cartProductName;
    private String cartProductBrand;
    private double cartProductPrice;
    private String imageURL;

    public CartItem(){

    }
    public CartItem(Product product) {
        this.cartProductID = product.getProductID();
        this.cartProductName = product.getName();
        this.cartProductBrand = product.getBrand();
        this.cartProductPrice = product.getPrice();
        this.imageURL = product.getImage();
    }

    //Getters
    public String getcartProductID() {
        return cartProductID;
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
    public int getQuantity() {
        return quantity;
    }
    public String getImage() {
        return imageURL;
    }

    //Necessary setters:
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

}
