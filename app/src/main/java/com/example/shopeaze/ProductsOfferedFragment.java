package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductsOfferedFragment extends Fragment implements ProductsOfferedAdapter.OnItemClickListener, StoreList.OnStoresLoadedListener, ProductList.OnProductsLoadedListener {
    private static final String TAG = "ProductsOfferedFragment";
    private static final String ARG_STORE = "store";
    private List<Product> products;
    private ProductsOfferedAdapter adapter;
    private RecyclerView recyclerViewProducts;
    private StoreList storeList;
    private ProductList productList;

    public static ProductsOfferedFragment newInstance(Store store) {
        Log.d(TAG, "Creating new ProductsOfferedFragment for store: " + store.getStoreName());
        ProductsOfferedFragment fragment = new ProductsOfferedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORE, store);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Creating view for ProductsOfferedFragment");
        View rootView = inflater.inflate(R.layout.activity_products_offered, container, false);

        recyclerViewProducts = rootView.findViewById(R.id.recyclerViewProductsOffered);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        Store store = (Store) getArguments().getSerializable(ARG_STORE);
        Log.d(TAG, "Store: " + store.getStoreName());

        TextView textViewStoreName = rootView.findViewById(R.id.textViewStoreName);
        textViewStoreName.setText(store.getStoreName());

        Button backToStoresButton = rootView.findViewById(R.id.buttonBackToStores);
        backToStoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProductsOfferedFragment.this).popBackStack();
                //NavHostFragment.findNavController(ShopperProductDetailsFragment.this)
                //         .navigate(R.id.action_ShopperProductDetails_to_ProductsOffered);
            }
        });

        storeList = new StoreList();
        storeList.setOnStoresLoadedListener(this);

        productList = new ProductList(store.getStoreID());
        productList.setOnProductsLoadedListener(this);

        return rootView;
    }

    @Override
    public void onStoresLoaded(List<Store> stores) {
        Store store = (Store) getArguments().getSerializable(ARG_STORE);
        if (store != null) {
            Log.d(TAG, "Store found: " + store.getStoreName());
        } else {
            Log.d(TAG, "Store not found");
        }
    }

    @Override
    public void onProductsLoaded(List<Product> products) {
        Log.d(TAG, "onProductsLoaded: products size: " + products.size());
        adapter = new ProductsOfferedAdapter(products, this);
        recyclerViewProducts.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Product product) {
        Log.d(TAG, "Product clicked: " + product.getName());

        Store store = (Store) getArguments().getSerializable(ARG_STORE);

        openShopperProductDetailsFragment(store, product);
    }

    private void openShopperProductDetailsFragment(Store store, Product product) {
        Log.d(TAG, "Opening ShopperProductDetails for product: " + product.getName());

        Bundle bundle = new Bundle();
        bundle.putSerializable("store", store);
        bundle.putSerializable("product", product);

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_ProductsOffered_to_ProductDetails, bundle);
    }
}