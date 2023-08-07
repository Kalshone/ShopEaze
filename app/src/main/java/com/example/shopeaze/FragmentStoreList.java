package com.example.shopeaze;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

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
        Button logoutButton = rootView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                navController.navigate(R.id.action_StoreList_to_logout);
            }
        });

        ImageButton storesButton = rootView.findViewById(R.id.button_stores);

        storesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.StoreList) {
                    // User is already on StoreList fragment, do nothing
                    return;
                }
            }
        });

        ImageButton ordersButton = rootView.findViewById(R.id.button_orders);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                navController.navigate(R.id.action_StoreList_to_OrderFragment);
            }
        });

        ImageButton cartButton = rootView.findViewById(R.id.button_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                navController.navigate(R.id.action_StoreList_to_Cart);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                ImageButton storesButton = view.findViewById(R.id.button_stores);
                ImageButton cartButton = view.findViewById(R.id.button_cart);
                ImageButton ordersButton = view.findViewById(R.id.button_orders);
                TextView storesText = view.findViewById(R.id.textViewStoresText);
                TextView cartText = view.findViewById(R.id.textViewCartText);
                TextView ordersText = view.findViewById(R.id.textViewOrdersText);
                ImageView storeIcon = view.findViewById(R.id.storeIcon);
                ImageView cartIcon = view.findViewById(R.id.cartIcon);
                ImageView ordersIcon = view.findViewById(R.id.ordersIcon);
                if (destination.getId() == R.id.StoreList) {
                    storesButton.setImageResource(R.drawable.focused_nav_button);
                    cartButton.setImageResource(R.drawable.nav_gradient);
                    ordersButton.setImageResource(R.drawable.nav_gradient);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    storeIcon.setImageResource(R.drawable.black_store);
                    cartIcon.setImageResource(R.drawable.white_cart);
                    ordersIcon.setImageResource(R.drawable.white_orders);
                } else if (destination.getId() == R.id.Cart) {
                    storesButton.setImageResource(R.drawable.nav_gradient);
                    cartButton.setImageResource(R.drawable.focused_nav_button);
                    ordersButton.setImageResource(R.drawable.nav_gradient);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    storeIcon.setImageResource(R.drawable.white_store);
                    cartIcon.setImageResource(R.drawable.black_cart);
                    ordersIcon.setImageResource(R.drawable.white_orders);
                } else if (destination.getId() == R.id.OrderFragment) {
                    storesButton.setImageResource(R.drawable.nav_gradient);
                    cartButton.setImageResource(R.drawable.nav_gradient);
                    ordersButton.setImageResource(R.drawable.focused_nav_button);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    storeIcon.setImageResource(R.drawable.white_store);
                    cartIcon.setImageResource(R.drawable.white_cart);
                    ordersIcon.setImageResource(R.drawable.black_orders);
                }
            }
        });
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
