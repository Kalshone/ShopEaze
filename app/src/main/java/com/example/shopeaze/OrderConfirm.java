package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import com.example.shopeaze.databinding.OrderConfirmBinding;

public class OrderConfirm extends Fragment {

    private OrderConfirmBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrderConfirmBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


        ImageButton storesButton = rootView.findViewById(R.id.button_stores);
        storesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderConfirm.this);
                navController.navigate(R.id.action_Order_confirm_to_StoreList);
            }
        });

        ImageButton cartButton = rootView.findViewById(R.id.button_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderConfirm.this);
                navController.navigate(R.id.action_Order_confirm_to_Cart);
            }
        });


        return rootView;
    }
}