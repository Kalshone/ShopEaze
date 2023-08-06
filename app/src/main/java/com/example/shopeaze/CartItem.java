package com.example.shopeaze;

public class CartItem {
//Represents an item in cart
    private int quantity;
    private String cartProductID;
    private String cartProductName;
    private String cartProductBrand;
    private double cartProductPrice;
    private int image;

    public CartItem(){

    }
    public CartItem(Product product) {
        this.cartProductID = product.getProductID();
        this.cartProductName = product.getName();
        this.cartProductBrand = product.getBrand();
        this.cartProductPrice = product.getPrice();
        this.image = product.getImage();
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
    public int getImage() {
        return image;
    }

    //Necessary setters:
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

}
