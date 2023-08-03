package com.example.shopeaze;

public class Order {
    private String orderId;
    private String userId;
    private String status;

    // Empty constructor needed for Firestore
    public Order() {}

    public Order(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    // Getter methods
    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    // Setter methods
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
