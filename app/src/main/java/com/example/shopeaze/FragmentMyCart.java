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

        ImageButton ordersButton = rootView.findViewById(R.id.button_orders);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentMyCart.this);
                navController.navigate(R.id.action_Cart_to_Orders);
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
//package com.example.shopeaze;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.NavDestination;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
////public class FragmentMyCart extends Fragment implements CartAdapter.OnItemClickListener, StoreList.OnStoresLoadedListener, ProductList.OnProductsLoadedListener {
//public class FragmentMyCart extends Fragment{
//
//    private ProductList productList;
//    private static final String TAG = "FragmentMyCart";
//
//    FirebaseFirestore database;
//    FirebaseAuth auth;
//
//    RecyclerView recyclerView;
//    MyCartAdapter cartAdapter;
//    List<MyCartModel> cartModelList;
//
//
//    public static FragmentMyCart newInstance(Store store) {
//        FragmentMyCart fragment = new FragmentMyCart();
//        Bundle args = new Bundle();
//        return fragment;
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG, "Creating view for FragmentMyCart");
//        View rootView = inflater.inflate(R.layout.activity_cart, container, false);
//
//
//
////        database = FirebaseFirestore.getInstance();
////        auth = FirebaseAuth.getInstance();
////        recyclerView = rootView.findViewById(R.id.recyclerCartView);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////
////        cartModelList = new ArrayList<>();
////        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
////        recyclerView.setAdapter(cartAdapter);
////
////        // go into the firebase database, entering "Users", then "Shoppers", then the current user's ID, then "Cart"
////        database.collection("Users").document(auth.getCurrentUser().getUid())
////                .collection("Shoppers").get()
////                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////
////            @Override
////            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                if (task.isSuccessful()){
////                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
////                        MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
////                        cartModelList.add(cartModel);
////                        cartAdapter.notifyDataSetChanged();
////                    }
////                }
////            }
////
////        });
//
//
//
//
//
//        ImageButton storesButton = rootView.findViewById(R.id.button_stores);
//        storesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavController navController = NavHostFragment.findNavController(FragmentMyCart.this);
//                navController.navigate(R.id.action_Cart_to_StoreList);
//            }
//        });
//
//        ImageButton cartButton = rootView.findViewById(R.id.button_cart);
//        cartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavController navController = NavHostFragment.findNavController(FragmentMyCart.this);
//                NavDestination currentDestination = navController.getCurrentDestination();
//                if (currentDestination != null && currentDestination.getId() == R.id.Cart) {
//                    // User is already on Cart fragment, do nothing
//                    return;
//                }
//            }
//        });
//        return rootView;
//    }
//
//
//
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        final NavController navController = NavHostFragment.findNavController(this);
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                ImageButton storesButton = view.findViewById(R.id.button_stores);
//                ImageButton cartButton = view.findViewById(R.id.button_cart);
//                TextView storesText = view.findViewById(R.id.textViewStoresText);
//                TextView cartText = view.findViewById(R.id.textViewCartText);
//                ImageView storeIcon = view.findViewById(R.id.storeIcon);
//                ImageView cartIcon = view.findViewById(R.id.cartIcon);
//                if (destination.getId() == R.id.StoreList) {
//                    storesButton.setImageResource(R.drawable.focused_nav_button);
//                    cartButton.setImageResource(R.drawable.nav_gradient);
//                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
//                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
//                    storeIcon.setImageResource(R.drawable.black_store);
//                    cartIcon.setImageResource(R.drawable.white_cart);
//                } else if (destination.getId() == R.id.Cart) {
//                    storesButton.setImageResource(R.drawable.nav_gradient);
//                    cartButton.setImageResource(R.drawable.focused_nav_button);
//                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
//                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
//                    storeIcon.setImageResource(R.drawable.white_store);
//                    cartIcon.setImageResource(R.drawable.black_cart);
//                }
//            }
//        });
//    }
//
//}
