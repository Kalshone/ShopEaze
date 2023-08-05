package com.example.shopeaze;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListFragment extends Fragment implements AddProductDialog.OnProductAddedListener{
    private RecyclerView recyclerView;
    private ArrayList<Product> products;
    private ProductAdapter productAdapter;
    private DatabaseReference productsRef;
    private ProgressDialog progressDialog;
    private TextView textViewStoreName;
    private boolean productsFetched = false;

    @Override
    public void onProductAdded(Product product) {
        // Add the new product to the list and notify the adapter of the change
        products.add(product);
        productAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        textViewStoreName = view.findViewById(R.id.textViewStoreName);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Products...");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), products);

        fetchStoreName();

        recyclerView.setAdapter(productAdapter);
        productsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Products");


        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If products exist and haven't been fetched before, add the child event listener to fetch them
                    if (!productsFetched) {
                        productsFetched = true; // Mark that products have been fetched once
                        productsRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                                Product product = dataSnapshot.getValue(Product.class);
                                products.add(product);
                                productAdapter.notifyDataSetChanged();

                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                                // Handle changes to existing children if needed
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                // Handle removed children if needed
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                                // Handle moved children if needed
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Log.w("TAG", "Listen failed.", databaseError.toException());
                            }
                        });
                    }
                } else {
                    // If no products found, dismiss the progressDialog and display a message
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    showToast("No products found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.w("TAG", "Listen failed.", databaseError.toException());
            }
        });

        FloatingActionButton fabAddProduct = view.findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });

        ImageButton btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshProducts();
            }
        });

        return view;
    }

    private void openStoreProductDetailsFragment(String productID) {
        ProductDetailsFragment fragment = ProductDetailsFragment.newInstance(productID);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showAddProductDialog() {
        // Create and show a dialog to gather product information from the user
        AddProductDialog dialog = new AddProductDialog();
        dialog.setOnProductAddedListener(this);
        dialog.show(requireActivity().getSupportFragmentManager(), "AddProductDialog");
    }

    private void fetchStoreName() {
        DatabaseReference storeNameRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("StoreName");
        storeNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storeName = dataSnapshot.getValue(String.class);
                if (storeName != null) {
                    textViewStoreName.setText(storeName);
                } else {
                    showToast("Store name not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String errorMessage = "Error fetching store name: " + databaseError.getMessage();
                showToast(errorMessage);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void refreshProducts() {
        // Clear the existing products list and show the progress dialog
        products.clear();
        productAdapter.notifyDataSetChanged();
        progressDialog.show();

        // Attach the child event listener again to fetch the products
        productsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Product product = dataSnapshot.getValue(Product.class);
                products.add(product);
                productAdapter.notifyDataSetChanged();

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle changes to existing children if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle removed children if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle moved children if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.w("TAG", "Listen failed.", databaseError.toException());
            }
        });
    }
}
