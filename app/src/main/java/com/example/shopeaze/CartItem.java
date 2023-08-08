package com.example.shopeaze;

public class CartItem {
    //Represents an item in cart
    private int cartQuantity;
    private String cartProductID;
    private String cartProductName;
    private String cartProductBrand;
    private double cartProductPrice;
    private String status;
    private String storeID;
    private String imageURL;

    public CartItem() {
    }

    public CartItem(Product product) {
        this.cartProductID = product.getProductID();
        this.cartProductName = product.getName();
        this.cartProductBrand = product.getBrand();
        this.cartProductPrice = product.getPrice();
        this.cartQuantity = product.getQuantity();
        this.status = product.getStatus();
        this.storeID = product.getStoreID();
        this.imageURL = String.valueOf(product.getImage());
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

    public String getStoreID() {
        return storeID;
    }

    //Necessary setters:
    public void setcartProductID(String productID) {
        this.cartProductID = productID;
    }

    public void setcartProductName(String productName) {
        this.cartProductName = productName;
    }

    public void setcartProductBrand(String productBrand) {
        this.cartProductBrand = productBrand;
    }

    public void setcartProductPrice(double productPrice) {
        this.cartProductPrice = productPrice;
    }

    public void setImage(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setQuantity(int quantity) {
        this.cartQuantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
