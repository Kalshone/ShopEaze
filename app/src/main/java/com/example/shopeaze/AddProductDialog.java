package com.example.shopeaze;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class AddProductDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        final EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        final EditText editTextProductBrand = dialogView.findViewById(R.id.editTextProductBrand);
        final EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);

        builder.setTitle("Add Product")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the entered product information
                        String productName = editTextProductName.getText().toString().trim();
                        String productBrand = editTextProductBrand.getText().toString().trim();
                        double productPrice = Double.parseDouble(editTextProductPrice.getText().toString().trim());

                        // Create a new product object
                        Product newProduct = new Product();
                        newProduct.setName(productName);
                        newProduct.setBrand(productBrand);
                        newProduct.setPrice(productPrice);

                        // Add the new product to the Firebase Realtime Database
                        ProductList productList = new ProductList();
                        productList.addProduct(newProduct);

                        // Add the new product to the list in the fragment and refresh the RecyclerView
                        ProductListFragment productListFragment = (ProductListFragment) getParentFragment();
                        productListFragment.addProduct(newProduct);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled the dialog, do nothing
                    }
                });

        return builder.create();
    }
}
