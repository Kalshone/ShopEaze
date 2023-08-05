package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ProductsOfferedFragment extends Fragment implements ProductsOfferedAdapter.OnItemClickListener, StoreList.OnStoresLoadedListener, ProductList.OnProductsLoadedListener {
    private static final String TAG = "ProductsOfferedFragment";
    private static final String ARG_STORE_ID = "store_id";
    private List<Product> products;
    private ProductsOfferedAdapter adapter;
    private RecyclerView recyclerViewProducts;
    private StoreList storeList;
    private ProductList productList;

    public static ProductsOfferedFragment newInstance(String storeID) {
        Log.d(TAG, "Creating new ProductsOfferedFragment for store ID: " + storeID);
        ProductsOfferedFragment fragment = new ProductsOfferedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORE_ID, storeID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Creating view for ProductsOfferedFragment");
        View rootView = inflater.inflate(R.layout.activity_products_offered, container, false);

        recyclerViewProducts = rootView.findViewById(R.id.recyclerViewProductsOffered);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        String storeID = getArguments().getString(ARG_STORE_ID);
        Log.d(TAG, "Store ID: " + storeID);
        storeList = new StoreList();
        storeList.setOnStoresLoadedListener(this);

        productList = new ProductList(storeID);
        productList.setOnProductsLoadedListener(this);

        return rootView;
    }

    @Override
    public void onStoresLoaded(List<Store> stores) {
        String storeID = getArguments().getString(ARG_STORE_ID);
        Store store = null;
        try {
            store = getStoreByID(storeID, stores);
            if (store != null) {
                Log.d(TAG, "Store found: " + store.getStoreName());
            } else {
                Log.d(TAG, "Store not found");
            }
        } catch (AppExceptions.StoreNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "getStoreDetails store not found ");
        }
    }

    @Override
    public void onProductsLoaded(List<Product> products) {
        adapter = new ProductsOfferedAdapter(products, this);
        recyclerViewProducts.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Product product) {
        Log.d(TAG, "Product clicked: " + product.getName());
        openStoreProductDetailsFragment(product.getProductID());
    }

    private void openStoreProductDetailsFragment(String productID) {
        Log.d(TAG, "Opening StoreProductDetailsFragment for product ID: " + productID);
        StoreProductDetailsFragment fragment = StoreProductDetailsFragment.newInstance(productID);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private Store getStoreByID(String storeID, List<Store> stores) throws AppExceptions.StoreNotFoundException {
        for (Store store : stores) {
            if (store.getStoreID().equals(storeID)) {
                return store;
            }
        }
        throw new AppExceptions.StoreNotFoundException("Store with ID " + storeID + " not found.");
    }
    private Store getStoreDetails(String storeID) {
        StoreList storeList = new StoreList();
        try {
            return storeList.getStoreByID(storeID);
        } catch (AppExceptions.StoreNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
