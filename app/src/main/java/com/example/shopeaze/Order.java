package com.example.shopeaze;

public class Order {
    private String orderId;
    private String status;

    public Order() {
    }

    public Order(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }
}
