package com.example.shopeaze;

import java.util.List;

public class Order {
    private String orderNumber;
    private String status;

    public Order(String orderNumber, String status) {
        this.orderNumber = orderNumber;
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

