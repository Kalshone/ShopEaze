package com.example.shopeaze;

import java.util.List;

public class Order {
    private List<Products> products;

    public List<Products> getProducts() {
        return products;
    }

    public static class Products {
        private String status;
        private String userId;

        public String getStatus() {
            return status;
        }

        public String getUserId() {
            return userId;
        }
    }
}


