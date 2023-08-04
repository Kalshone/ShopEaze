package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductsOfferedFragment extends Fragment implements ProductAdapter.OnItemClickListener {

    private static final String ARG_STORE_ID = "store_id";
    private List<Product> products;
    private ProductAdapter adapter;

    public static ProductsOfferedFragment newInstance(String storeID) {
        ProductsOfferedFragment fragment = new ProductsOfferedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORE_ID, storeID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_products_offered, container, false);

        RecyclerView recyclerViewProducts = rootView.findViewById(R.id.recyclerViewProductsOffered);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));

        String storeID = getArguments().getString(ARG_STORE_ID);
        Store store = getStoreDetails(storeID);
        if (store != null) {
            products = store.getProductList().getAllProducts();
            adapter = new ProductAdapter(products, this);
            recyclerViewProducts.setAdapter(adapter);
        }

        return rootView;
    }

    @Override
    public void onItemClick(Product product) {
        openStoreProductDetailsFragment(product.getProductID());
    }

    private void openStoreProductDetailsFragment(String productID) {
        StoreProductDetailsFragment fragment = StoreProductDetailsFragment.newInstance(productID);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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
