package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ListView ordersListView;
    private ArrayAdapter<String> ordersAdapter;
    private List<String> ordersList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ordersListView = view.findViewById(R.id.ordersListView);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadOrders();

        return view;
    }

    private void loadOrders() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ordersList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            ordersList.add("Order ID: " + order.getOrderId() + "\nStatus: " + order.getStatus());
                        }
                        ordersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ordersList);
                        ordersListView.setAdapter(ordersAdapter);
                    } else {
                        Toast.makeText(getContext(), "Error getting orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
