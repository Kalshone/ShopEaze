package com.example.shopeaze;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProductDialog extends DialogFragment {

    public interface OnProductAddedListener {
        void onProductAdded(Product product);
    }

    private OnProductAddedListener onProductAddedListener;

    public void setOnProductAddedListener(OnProductAddedListener onProductAddedListener) {
        this.onProductAddedListener = onProductAddedListener;
    }

    private void addProduct(final Product newProduct) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference storeOwnerRef = usersRef.child("StoreOwner").child(currentUserUid);
        storeOwnerRef.child("Products").push().setValue(newProduct);

        if (onProductAddedListener != null) {
            onProductAddedListener.onProductAdded(newProduct);
        }
    }

    private void checkProductExistence(final Product newProduct) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(currentUserUid)
                .child("Products");

        productsRef.orderByChild("name")
                .equalTo(newProduct.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean productExists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Product product = snapshot.getValue(Product.class);
                            if (product != null && product.getBrand().equals(newProduct.getBrand())) {
                                productExists = true;
                                break;
                            }
                        }

                        if (productExists) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Product Exists")
                                    .setMessage("The product with the same name and brand already exists.")
                                    .setPositiveButton("OK", null)
                                    .show();
                        } else {
                            addProduct(newProduct);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error if the query is canceled
                    }
                });


    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        final EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        final EditText editTextProductBrand = dialogView.findViewById(R.id.editTextProductBrand);
        final EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        final EditText editTextProductDescription = dialogView.findViewById(R.id.editTextProductDescription);
        final EditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);

        builder.setTitle("Add Product")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String productName = editTextProductName.getText().toString().trim();
                        String productBrand = editTextProductBrand.getText().toString().trim();
                        // Check if productName and productBrand are empty
                        if (productName.isEmpty() || productBrand.isEmpty()) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Uh oh!")
                                    .setMessage("Product name and brand cannot be empty.")
                                    .setPositiveButton("OK", null)
                                    .show();
                        } else {
                            double productPrice = 0.0;
                            String productPriceStr = editTextProductPrice.getText().toString().trim();
                            if (!productPriceStr.isEmpty()) {
                                productPrice = Double.parseDouble(productPriceStr);
                            }

                            String productDescription = "Default description";
                            String enteredDescription = editTextProductDescription.getText().toString().trim();
                            if (!enteredDescription.isEmpty()) {
                                productDescription = enteredDescription;
                            }

                            int productQuantity = 1;
                            String productQuantityStr = editTextProductQuantity.getText().toString().trim();
                            if (!productQuantityStr.isEmpty()) {
                                productQuantity = Integer.parseInt(productQuantityStr);
                            }
                            Product newProduct = new Product();
                            newProduct.setName(productName);
                            newProduct.setBrand(productBrand);
                            newProduct.setPrice(productPrice);
                            newProduct.setDescription(productDescription);
                            newProduct.setQuantity(productQuantity);

                            checkProductExistence(newProduct);
                        }
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