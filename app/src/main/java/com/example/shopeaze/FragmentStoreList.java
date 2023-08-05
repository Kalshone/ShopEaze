package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FragmentStoreList extends Fragment implements StoreAdapter.OnItemClickListener {

    private List<Store> stores;
    private StoreAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_store_list, container, false);

        final RecyclerView recyclerViewStores = rootView.findViewById(R.id.recyclerViewStores);
        recyclerViewStores.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        final StoreList storeList = new StoreList();
        storeList.setOnStoresLoadedListener(new StoreList.OnStoresLoadedListener() {
            @Override
            public void onStoresLoaded(List<Store> stores) {
                FragmentStoreList.this.stores = stores;
                adapter = new StoreAdapter(stores, FragmentStoreList.this);
                recyclerViewStores.setAdapter(adapter);
                Log.d("FragmentStoreList", "Loaded " + stores.size() + " stores from StoreList");
            }
        });

        // TEMPORARY FOR TESTING PURPOSES
        Button logoutButton = rootView.findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                navController.navigate(R.id.action_StoreList_to_logout);
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(Store store) {
        if (store == null) {
            Log.e("FragmentStoreList", "Store object is null");
        } else {
            Log.d("FragmentStoreList", "FragmentStoreList StoreID is" + store.getStoreID());
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("store", store);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_StoreList_to_ProductsOffered, bundle);
    }

}
