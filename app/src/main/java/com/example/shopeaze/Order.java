package com.example.shopeaze;

import java.util.List;

public class Order {
    private String orderNumber;
    private String status;
    private String cartProductBrand;
    private Double cartProductPrice;
    private int cartQuantity;

    private List<Product> products;

    // Constructor
    public Order(String orderNumber, String status, String cartProductBrand,
                 Double cartProductPrice, int cartQuantity) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.cartProductBrand = cartProductBrand;
        this.cartProductPrice = cartProductPrice;
        this.cartQuantity = cartQuantity;
    }

    public Order(String orderNumber, String status, List<Product> products) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.products = products;
    }

    // Getters
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getCartProductBrand() {
        return cartProductBrand;
    }

    public Double getCartProductPrice() {
        return cartProductPrice;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    // Setters
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCartProductBrand(String cartProductBrand) {
        this.cartProductBrand = cartProductBrand;
    }

    public void setCartProductPrice(Double cartProductPrice) {
        this.cartProductPrice = cartProductPrice;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public List<Product> getProducts() {
        return products;
    }
}