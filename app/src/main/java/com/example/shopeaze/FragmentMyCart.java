package com.example.shopeaze;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentMyCart extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_cart, container, false);

        ImageButton storesButton = rootView.findViewById(R.id.button_stores);
        storesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentMyCart.this);
                navController.navigate(R.id.action_Cart_to_StoreList);
            }
        });

        ImageButton cartButton = rootView.findViewById(R.id.button_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentMyCart.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.Cart) {
                    // User is already on Cart fragment, do nothing
                    return;
                }
            }
        });
        return rootView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                ImageButton storesButton = view.findViewById(R.id.button_stores);
                ImageButton cartButton = view.findViewById(R.id.button_cart);
                TextView storesText = view.findViewById(R.id.textViewStoresText);
                TextView cartText = view.findViewById(R.id.textViewCartText);
                ImageView storeIcon = view.findViewById(R.id.storeIcon);
                ImageView cartIcon = view.findViewById(R.id.cartIcon);
                if (destination.getId() == R.id.StoreList) {
                    storesButton.setImageResource(R.drawable.focused_nav_button);
                    cartButton.setImageResource(R.drawable.nav_gradient);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    storeIcon.setImageResource(R.drawable.black_store);
                    cartIcon.setImageResource(R.drawable.white_cart);
                } else if (destination.getId() == R.id.Cart) {
                    storesButton.setImageResource(R.drawable.nav_gradient);
                    cartButton.setImageResource(R.drawable.focused_nav_button);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    storeIcon.setImageResource(R.drawable.white_store);
                    cartIcon.setImageResource(R.drawable.black_cart);
                }
            }
        });
    }

}
