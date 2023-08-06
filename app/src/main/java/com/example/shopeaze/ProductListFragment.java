package com.example.shopeaze;

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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
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

public class ProductListFragment extends Fragment implements AddProductDialog.OnProductAddedListener,
        ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Product> products;
    private ProductAdapter productAdapter;
    private DatabaseReference productsRef;
    private TextView textViewStoreName;

    @Override
    public void onProductAdded(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            productAdapter.notifyDataSetChanged();
        } else {
            showToast("Product already exists");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        textViewStoreName = view.findViewById(R.id.textViewStoreName);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), products, this);

        fetchStoreName();

        recyclerView.setAdapter(productAdapter);
        productsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Products");

        productsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Product product = dataSnapshot.getValue(Product.class);
                if (!isProductDuplicate(product)) {
                    products.add(product);
                    productAdapter.notifyDataSetChanged();
                }
            }

            private boolean isProductDuplicate(Product newProduct) {
                for (Product product : products) {
                    if (product.getName().equals(newProduct.getName()) && product.getBrand().equals(newProduct.getBrand())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle changes to existing children if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Product removedProduct = dataSnapshot.getValue(Product.class);
                if (removedProduct != null) {
                    products.remove(removedProduct);
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle moved children if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

    @Override
    public void onItemClick(Product product) {
        openProductDetailsFragment(product);
    }

    private void openProductDetailsFragment(Product product) {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(
                R.id.action_product_list_to_product_details,
                ProductDetailsFragment.newInstance(product).getArguments()
        );
    }

    private void showAddProductDialog() {
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
        // Clear the existing products list
        products.clear();
        productAdapter.notifyDataSetChanged();
        // Fetch the products again from the Firebase database
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (!products.contains(product)) {
                        products.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Refresh failed.", databaseError.toException());
                showToast("Failed to refresh products.");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

