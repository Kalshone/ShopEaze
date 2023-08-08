package com.example.shopeaze;

public class MyCartModel {
    String productname;
    String productprice;
    String productquantity;


    public MyCartModel() {
        //
    }

    public MyCartModel(String productname, String productprice, String productquantity) {
        this.productname = productname;
        this.productprice = productprice;
        this.productquantity = productquantity;
    }

    // make setters and getters for all the fields

    public String getProductName() {
        return productname;
    }

    public void setProductName(String productname) {
        this.productname = productname;
    }

    public String getProductPrice() {
        return productprice;
    }

    public void setProductPrice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductQuantity() {
        return productquantity;
    }

    public void setProductQuantity(String productquantity) {
        this.productquantity = productquantity;
    }



}
