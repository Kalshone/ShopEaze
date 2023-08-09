package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    // Declare a ListView to display the orders and a Button for refreshing the orders.
    private Button refreshButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private RecyclerView orderList;
    private List<Order> orders;
    private OrdersAdapter ordersAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Shoppers");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        orderList = view.findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(getContext()));

        orders = new ArrayList<>();

        fetchOrders();

        Button refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            fetchOrders();
        });

        ImageButton ordersButton = view.findViewById(R.id.button_orders);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderFragment.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.OrderFragment) {
                    // User is already on OrderFragment, do nothing
                    return;
                }
            }
        });

        ImageButton storesButton = view.findViewById(R.id.button_stores);
        storesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderFragment.this);
                navController.navigate(R.id.action_OrderFragment_to_StoreList);
            }
        });

        ImageButton cartButton = view.findViewById(R.id.button_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderFragment.this);
                navController.navigate(R.id.action_OrderFragment_to_Cart);
            }
        });

        return view;
    }

    private void fetchOrders() {
        Log.d("OrderFragment", "Starting to fetch orders...");
        mDatabase.child(currentUserID).child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear();
                int orderNumber = 1;
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) { // Looping through OrderId's
                    String orderId = orderSnapshot.getKey();

                    for (DataSnapshot productSnapshot : orderSnapshot.getChildren()) { // Looping through ProductId's
                        String productId = productSnapshot.getKey();
                        String status = productSnapshot.child("status").getValue(String.class);
                        String brand = productSnapshot.child("cartProductBrand").getValue(String.class);
                        Double price = productSnapshot.child("cartProductPrice").getValue(Double.class);
                        Integer quantity = productSnapshot.child("cartQuantity").getValue(Integer.class);
                        String name = productSnapshot.child("cartProductName").getValue(String.class);

                        orders.add(new Order(String.valueOf(orderNumber), status, brand, price, quantity, name));
                        orderNumber++;
                        Log.d("OrderFragment", "OrderID: " + orderId + ", ProductID: " + productId + ", Status: " + status); // Logging each order's status
                    }
                }

                ordersAdapter = new OrdersAdapter(orders);
                orderList.setAdapter(ordersAdapter);
                ordersAdapter.notifyDataSetChanged();

                if (orders.size() > 0) {
                    Log.d("OrderFragment", "Successfully fetched " + orders.size() + " orders.");
                } else {
                    Log.d("OrderFragment", "No orders found for the user.");
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.e("OrdersFragment", "onCancelled", databaseError.toException());
            }
        });
    }




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
}