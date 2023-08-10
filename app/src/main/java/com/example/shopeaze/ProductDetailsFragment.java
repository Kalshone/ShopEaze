package com.example.shopeaze;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsFragment extends Fragment {
    private static final String ARG_PRODUCT = "product_id";
    private Product product;
    ImageView imageViewProduct;
    TextView textViewProductName;
    TextView textViewProductBrand;
    TextView textViewProductPrice;
    TextView textViewProductDescription;
    TextView textViewProductQuantity;
    ImageButton buttonChangeQuantity;
    ImageButton buttonChangePrice;
    ImageButton buttonChangeDescription;
    TextView buttonRemoveProduct;
    View rootView;

    public static ProductDetailsFragment newInstance(Product product, String productImageURL) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        args.putString("productImageURL", productImageURL);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_details, container, false);

        imageViewProduct = rootView.findViewById(R.id.imageViewProduct);
        textViewProductName = rootView.findViewById(R.id.textViewProductName);
        textViewProductBrand = rootView.findViewById(R.id.textViewProductBrand);
        textViewProductPrice = rootView.findViewById(R.id.textViewProductPrice);
        textViewProductDescription = rootView.findViewById(R.id.textViewProductDescription);
        textViewProductQuantity = rootView.findViewById(R.id.textViewProductQuantity);
        buttonChangePrice = rootView.findViewById(R.id.buttonChangePrice);
        buttonChangeDescription = rootView.findViewById(R.id.buttonChangeDescription);
        buttonChangeQuantity = rootView.findViewById(R.id.buttonChangeQuantity);
        buttonRemoveProduct = rootView.findViewById(R.id.buttonRemoveProduct);

        Bundle arguments = getArguments();
        if (arguments != null) {
            product = (Product) arguments.getSerializable(ARG_PRODUCT);
            if (product != null) {
                initializeViews(rootView);
            } else {
                Toast.makeText(requireContext(), "Product data is null.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No arguments passed to the fragment.", Toast.LENGTH_SHORT).show();
        }

        String productImageURL = arguments.getString("productImageURL");

        if (product != null) {
            // Load and display the image using Glide
            Glide.with(requireContext())
                    .load(productImageURL)
                    .placeholder(R.drawable.placeholder_image) // Replace with a placeholder image resource
                    .error(R.drawable.error_image) // Replace with an error image resource
                    .into(imageViewProduct);
            // Rest of your code to initialize other views
        } else {
            Toast.makeText(requireContext(), "Product data is null.", Toast.LENGTH_SHORT).show();
        }

        Button buttonGoBack = rootView.findViewById(R.id.buttonGoBack);
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment
                        .findNavController(ProductDetailsFragment.this);
                navController.navigate(R.id.action_product_details_to_product_list);
            }
        });

        TextView buttonRemoveProduct = rootView.findViewById(R.id.buttonRemoveProduct);
        buttonRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveProductDialog();
            }
        });

        return rootView;
    }

    private void initializeViews(View rootView) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            product = (Product) arguments.getSerializable(ARG_PRODUCT);
            String productImageURL = arguments.getString("productImageURL");

            if (product != null) {
                // Load and display the image using Glide
                Glide.with(requireContext())
                        .load(productImageURL)
                        .placeholder(R.drawable.placeholder_image) // Replace with a placeholder image resource
                        .error(R.drawable.error_image) // Replace with an error image resource
                        .into(imageViewProduct);

                // Rest of your code to initialize other views
            } else {
                Toast.makeText(requireContext(), "Product data is null.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No arguments passed to the fragment.", Toast.LENGTH_SHORT).show();
        }

        imageViewProduct.setImageResource(R.drawable.sample);
        textViewProductName.setText(product.getName());
        textViewProductBrand.setText(product.getBrand());
        textViewProductPrice.setText("$ " + String.valueOf(product.getPrice()));
        textViewProductDescription.setText(product.getDescription());
        textViewProductQuantity.setText("Inventory Stock: " +String.valueOf(product.getQuantity()));

        buttonChangePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePriceDialog();
            }
        });

        buttonChangeDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeDescriptionDialog(textViewProductDescription.getText().toString());
            }
        });

        buttonChangeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeQuantityDialog();
            }
        });
    }

    private void showChangePriceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialog);
        builder.setTitle("Change Price")
                .setView(R.layout.dialog_change_price)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog dialogView = (Dialog) dialog;
                        EditText editTextNewPrice = dialogView.findViewById(R.id.editTextNewPrice);
                        String newPrice = editTextNewPrice.getText().toString();
                        if (!newPrice.isEmpty()) {
                            updateProductPrice(newPrice);
                        } else {
                            showToast("No changes applied.");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        alertDialog.getWindow().setAttributes(layoutParams);

    }

    private void showChangeDescriptionDialog(String initialDescription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialog);
        builder.setTitle("Change Description")
                .setView(R.layout.dialog_change_description)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog dialogView = (Dialog) dialog;
                        EditText editTextNewDescription = dialogView.findViewById(R.id.editTextNewDescription);
                        String newDescription = editTextNewDescription.getText().toString();
                        if (!newDescription.isEmpty()) {
                            updateProductDescription(newDescription);
                            textViewProductDescription.setText(newDescription);
                        } else {
                            showToast("No changes applied.");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                // .show();
        // Set the initial text of the EditText to the current description
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_description, null);
        EditText editTextNewDescription = dialogView.findViewById(R.id.editTextNewDescription);
        editTextNewDescription.setText(initialDescription);

        builder.setView(dialogView).show();
    }

    private void updateProductPrice(String newPrice) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Products");

        Query productQuery = productRef
                .orderByChild("name")
                .equalTo(product.getName());

        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (productSnapshot.child("brand").getValue(String.class).equals(product.getBrand())) {
                        productSnapshot.getRef().child("price").setValue(Double.parseDouble(newPrice));
                    }
                }
                showToast("Product price updated successfully.");
                textViewProductPrice.setText("$ " + newPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to update product price.");
            }
        });
    }

    private void updateProductDescription(String newDescription) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Products");

        Query productQuery = productRef.orderByChild("name").equalTo(product.getName());

        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean updateSuccessful = false;
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (productSnapshot.child("brand").getValue(String.class).equals(product.getBrand())) {
                        productSnapshot.getRef().child("description").setValue(newDescription);
                    }
                    updateSuccessful = true;
                }
                if (updateSuccessful) {
                    showToast("Product description updated successfully.");
                    textViewProductDescription.setText(newDescription);
                } else {
                    showToast("Failed to update product description.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to update product description.");
            }
        });
    }


    private void showChangeQuantityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialog);
        builder.setTitle("Change Quantity")
                .setView(R.layout.dialog_change_quantity)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog dialogView = (Dialog) dialog;
                        EditText editTextNewQuantity = dialogView.findViewById(R.id.editTextNewQuantity);
                        String newQuantity = editTextNewQuantity.getText().toString();
                        if (!newQuantity.isEmpty()) {
                            updateProductQuantity(newQuantity);
                        } else {
                            showToast("No changes applied.");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        alertDialog.getWindow().setAttributes(layoutParams);
    }

    private void updateProductQuantity(String newQuantity) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Products");

        Query productQuery = productRef.orderByChild("name").equalTo(product.getName());

        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (productSnapshot.child("brand").getValue(String.class).equals(product.getBrand())) {
                        productSnapshot.getRef().child("quantity").setValue(Double.parseDouble(newQuantity));
                    }
                }
                showToast("Product quantity updated successfully.");
                textViewProductQuantity.setText("Inventory Stock: " + newQuantity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to update product quantity.");
            }
        });
    }


    private void showRemoveProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Removal")
                .setMessage("Are you sure you want to remove this product?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeProductFromFirebase();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void removeProductFromFirebase() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Products");

        Query productQuery = productRef.orderByChild("name").equalTo(product.getName());

        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (productSnapshot.child("brand").getValue(String.class).equals(product.getBrand())) {
                        productSnapshot.getRef().removeValue();
                    }
                }
                NavController navController = NavHostFragment.findNavController(ProductDetailsFragment.this);
                navController.navigate(R.id.action_product_details_to_product_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to remove product.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

