package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ShopperProductDetailsFragment extends Fragment {
    private static final String ARG_STORE_ID = "store_id";
    private static final String ARG_PRODUCT = "product";
    private Product product;

    public static ShopperProductDetailsFragment newInstance(String storeID, Product product) {
        ShopperProductDetailsFragment fragment = new ShopperProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORE_ID, storeID);
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_shopper_product_details, container, false);

        // Retrieve storeID and product from arguments
        String storeID = getArguments().getString(ARG_STORE_ID);
        product = (Product) getArguments().getSerializable(ARG_PRODUCT);

        // Display product details
        TextView productNameTextView = rootView.findViewById(R.id.textViewProductName);
        productNameTextView.setText(product.getName());

        TextView brandNameTextView = rootView.findViewById(R.id.textViewBrandName);
        brandNameTextView.setText(String.valueOf(product.getBrand()));

        TextView productPriceTextView = rootView.findViewById(R.id.textViewProductPrice);
        productPriceTextView.setText("$ " + String.valueOf(product.getPrice()));

        TextView productDescriptionTextView = rootView.findViewById(R.id.textViewProductDescription);
        productDescriptionTextView.setText(product.getDescription());

        return rootView;
    }
}
