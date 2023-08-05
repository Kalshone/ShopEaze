package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProductDetailsFragment extends Fragment {
    private static final String ARG_PRODUCT_ID = "product_id";

    private String productID;

    public static ProductDetailsFragment newInstance(String productID) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productID = getArguments().getString(ARG_PRODUCT_ID);
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_details, container, false);

        TextView textViewProductName = rootView.findViewById(R.id.textViewProductName);
        TextView textViewProductBrand = rootView.findViewById(R.id.textViewProductBrand);
        TextView textViewProductPrice = rootView.findViewById(R.id.textViewProductPrice);

        String productID = getArguments() != null ? getArguments().getString(ARG_PRODUCT_ID) : null;
        if (productID != null) {
            Product product = getProductDetails(productID);

            if (product != null) {
                textViewProductName.setText(product.getName());
                textViewProductBrand.setText(product.getBrand());
                textViewProductPrice.setText(String.valueOf(product.getPrice()));
                // Set other TextViews with respective product details
            } else {
                showErrorMessage("Product not found");
            }
        } else {
            showErrorMessage("Product ID is null");
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

    private void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

