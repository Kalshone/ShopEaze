package com.example.shopeaze;

public class AppExceptions {
    public static class ProductNotFoundException extends Exception {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    // add any other exceptions here

}
