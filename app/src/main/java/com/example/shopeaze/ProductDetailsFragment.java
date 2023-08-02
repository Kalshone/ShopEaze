package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProductDetailsFragment extends Fragment {
    private static final String ARG_PRODUCT_ID = "product_id";

    public static ProductDetailsFragment newInstance(String productID) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_details, container, false);

        TextView textViewProductName = rootView.findViewById(R.id.textViewProductName);
        TextView textViewProductBrand = rootView.findViewById(R.id.textViewProductBrand);
        TextView textViewProductPrice = rootView.findViewById(R.id.textViewProductPrice);

        String productID = getArguments().getString(ARG_PRODUCT_ID);
        Product product = getProductDetails(productID);

        if (product != null) {
            textViewProductName.setText(product.getName());
            textViewProductBrand.setText(product.getBrand());
            textViewProductPrice.setText(String.valueOf(product.getPrice()));
            // Set other TextViews with respective product details
        }

        return rootView;
    }

    private Product getProductDetails(String productID) {
        ProductList productList = new ProductList();
        try {
            return productList.getProductByID(productID);
        } catch (AppExceptions.ProductNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
