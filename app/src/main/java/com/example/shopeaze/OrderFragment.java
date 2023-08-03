package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    // Declare Firebase Auth and Firestore instances.
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Declare a ListView to display the orders and a Button for refreshing the orders.
    private ListView ordersListView;
    private Button refreshButton;

    // Declare an ArrayAdapter to handle the list of orders.
    private ArrayAdapter<String> ordersAdapter;

    // Declare a List to hold the orders data.
    private List<String> ordersList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersListView = view.findViewById(R.id.ordersListView);

        // Initialize the refresh Button and set a click listener.
        Button refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> loadOrders());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadOrders();

        return view;
    }


    // Function to load orders from Firestore.
    private void loadOrders() {
        // Get the current user's ID.
        String userId = mAuth.getCurrentUser().getUid();

        // Query Firestore for orders where the userId matches the current user's ID.
        db.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    // If the query is successful...
                    if (task.isSuccessful()) {
                        // Initialize the ordersList.
                        ordersList = new ArrayList<>();

                        // Loop through each document in the results.
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert the document into an Order object.
                            Order order = document.toObject(Order.class);

                            // Add the order's ID and status to the ordersList.
                            ordersList.add("Order ID: " + order.getOrderId() + "\nStatus: " + order.getStatus());
                        }

                        // Create an ArrayAdapter with the ordersList and set it as the adapter for ordersListView.
                        ordersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ordersList);
                        ordersListView.setAdapter(ordersAdapter);
                    } else {
                        // If the query is not successful, display a toast with an error message.
                        Toast.makeText(getContext(), "Error getting orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
