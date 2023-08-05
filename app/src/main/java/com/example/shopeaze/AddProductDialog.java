package com.example.shopeaze;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductDialog extends DialogFragment {

    public interface OnProductAddedListener {
        void onProductAdded(Product product);
    }

    private OnProductAddedListener onProductAddedListener;

    public void setOnProductAddedListener(OnProductAddedListener onProductAddedListener) {
        this.onProductAddedListener = onProductAddedListener;
    }

    public static AddProductDialog newInstance(ProductListFragment productListFragment) {
        AddProductDialog dialog = new AddProductDialog();
        dialog.setTargetFragment(productListFragment, 0);
        return dialog;
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
                        // Get the entered product information
                        String productName = editTextProductName.getText().toString().trim();
                        String productBrand = editTextProductBrand.getText().toString().trim();
                        double productPrice = Double.parseDouble(editTextProductPrice.getText().toString().trim());
                        String productDescription = editTextProductDescription.getText().toString().trim();
                        int productQuantity = Integer.parseInt(editTextProductQuantity.getText().toString().trim());

                        // Create a new product object
                        Product newProduct = new Product();
                        newProduct.setName(productName);
                        newProduct.setBrand(productBrand);
                        newProduct.setPrice(productPrice);
                        newProduct.setDescription(productDescription);
                        newProduct.setQuantity(productQuantity);

                        // Get the current user's UID
                        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        // Get the reference to the "Users" node in the Firebase database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

                        // Get the reference to the "StoreOwner" node under the current user's UID
                        DatabaseReference storeOwnerRef = usersRef.child("StoreOwner").child(currentUserUid);

                        // Add the product under the "StoreOwner" node
                        storeOwnerRef.child("Products").push().setValue(newProduct);

                        if (onProductAddedListener != null) {
                            onProductAddedListener.onProductAdded(newProduct);
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
