package com.example.shopeaze;


import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class OwnerOrders extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    // Declare a ListView to display the orders and a Button for refreshing the orders.
    private ListView ownerordersListView;
    private Button refreshButton;
    private TextView textViewStoreName;


    // Declare an ArrayAdapter to handle the list of orders.
    private ArrayAdapter<Order> ownerordersAdapter;


    // Declare a List to hold the orders data.
    private List<String> ownerordersList;
    Query ordersCollection;
    DatabaseReference ref;
    List<Product> productList;


    List<Order> orderList;
    String storeName;
    String shopperEmail;
    Order order;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_orders, container, false);


        ImageButton inventoryButton = view.findViewById(R.id.button_inventory);

        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OwnerOrders.this);
                navController.navigate(R.id.action_OwnerOrders_to_ProductList);
            }
        });

        ImageButton ordersButton = view.findViewById(R.id.button_orders);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OwnerOrders.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.OwnerOrders) {
                    // User is already on OwnerOrders fragment, do nothing
                    return;
                }

            }
        });

        textViewStoreName = view.findViewById(R.id.textViewStoreName);

        ref = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        orderList = new ArrayList<>();
        ownerordersListView = view.findViewById(R.id.ownerordersListView);
        fetchStoreName();
        loadOrders();
        return view;
    }


    private void loadOrders() {
        String userId = mAuth.getUid();
        DatabaseReference ordersRef = ref.child("Users").child("StoreOwner").child(userId).child("Orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String orderID = orderSnapshot.getKey();
                    productList = new ArrayList<>();
                    for (DataSnapshot productSnapshot : orderSnapshot.getChildren()) {
                        String productID = productSnapshot.getKey();
                        Log.d("loadOrders()", "ProductID is: " + productID);
                        String name = productSnapshot.child("cartProductName").getValue(String.class);
                        String brand = productSnapshot.child("cartProductBrand").getValue(String.class);
                        double price = productSnapshot.child("cartProductPrice").getValue(Double.class);
                        String description = productSnapshot.child("cartProductDescription").getValue(String.class);
                        int quantity = productSnapshot.child("cartQuantity").getValue(Integer.class);
                        String status = productSnapshot.child("status").getValue(String.class);
                        Product product = new Product(name, brand, price, description, quantity, status, userId, productID);
                        productList.add(product);

                /*storeName = snapshot.getValue(String.class);
                Log.d("StoreName", storeName);
                com.google.firebase.database.Query ordersRef = storeRef.child("Orders");
                ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                            Log.d("OrderID", orderSnapshot.getKey());
                            String orderID = orderSnapshot.getKey();
                            String shopperEmail = orderSnapshot.child("Shopper Email").getValue(String.class);
                            productList = new ArrayList<>();
                            for(DataSnapshot productSnapshot : orderSnapshot.getChildren()){
                                String storeId = productSnapshot.child("storeID").getValue(String.class);
                                String brand = productSnapshot.child("cartProductBrand").getValue(String.class);
                                String cartproductID = productSnapshot.child("cartProductID").getValue(String.class);
                                String name = productSnapshot.child("cartProductName").getValue(String.class);
                                int quantity = productSnapshot.child("cartQuantity").getValue(Integer.class);
                                String status = productSnapshot.child("status").getValue(String.class);
                                double price = productSnapshot.child("cartProductPrice").getValue(Double.class);
                                String image = null;
                                Product product = new Product(name, brand, price, null, quantity, status, null, storeId, cartproductID);
                                productList.add(product);

                            }
                            if(productList.size() > 0){
                                orderList.add(new Order(shopperEmail, null, productList));
                            }
                            printOrderList();
                        }
                        displayProducts();*/

                    }
                    if (productList.size() > 0) {
                        orderList.add(new Order(orderID, productList));
                    }
                    printOrderList();
                }
                displayProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void printOrderList() {
        Log.d("OrderList", orderList.toString());
    }


    private void displayProducts() {
        // Create a custom adapter to display the list of orders and their products
        ownerordersAdapter = new ArrayAdapter<Order>(getContext(), R.layout.order_list_view_item, orderList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_list_view_item, parent, false);
                }
                // Get the order at the current position
                Order order = getItem(position);
                if (order != null) {
                    // Display order information
                    TextView orderTextView = convertView.findViewById(R.id.orderTextView);
                    //CheckBox orderCheckBox = convertView.findViewById(R.id.orderCheckBox);
                    StringBuilder textBuilder = new StringBuilder();
                    if (order.getProducts().size() == 1) {
                        Product product = order.getProducts().get(0);
                        textBuilder.append("• ").append(product.getName());
                    } else if (order.getProducts().size() > 1) {
                        for (int i = 0; i < 2; i++) {
                            Product product = order.getProducts().get(i);
                            textBuilder.append("\n• ").append(product.getName());
                        }
                    }
                    if (order.getProducts().size() > 2) {
                        textBuilder.append("•••").append("\n");
                    }
                    orderTextView.setText(textBuilder.toString());
                    //orderCheckBox.setChecked(order.isComplete());
                }
                return convertView;
            }
        };

        // Set the custom adapter to the ListView
        ownerordersListView.setAdapter(ownerordersAdapter);

        // Set item click listener to show detailed product information when an order is clicked
        ownerordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected order
                Order selectedOrder = ownerordersAdapter.getItem(position);
                // Show the product details
                showProductDetails(selectedOrder);
            }
        });
    }


    private void showProductDetails(Order order) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_order_product_details);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.scrollview_background);

        TextView productTitleTextView = dialog.findViewById(R.id.productTitleTextView);
        LinearLayout productsLinearLayout = dialog.findViewById(R.id.productsLinearLayout);

        productTitleTextView.setText("Order Details for " + order.getOrderNumber());

        for (Product product : order.getProducts()) {
            View productView = LayoutInflater.from(getContext()).inflate(R.layout.owner_order_item, productsLinearLayout, false);
            CheckBox productCheckBox = productView.findViewById(R.id.productCheckBox);
            TextView productDetailsTextView = productView.findViewById(R.id.productDetailsTextView);

            StringBuilder detailsBuilder = new StringBuilder();
            detailsBuilder.append("Product: ").append(product.getName())
                    .append("\nBrand: ").append(product.getBrand())
                    .append("\nPrice: ").append(product.getPrice())
                    .append("\nDescription: ").append(product.getDescription())
                    .append("\nQuantity: ").append(product.getQuantity()).append("\n");
            // .append("\nProductID: ").append(product.getProductID());
            //.append("\nStatus: ").append(product.getStatus());

            if ("Complete".equals(product.getStatus())) {
                productCheckBox.setChecked(true);
            } else {
                productCheckBox.setChecked(false);
            }

            productCheckBox.setText(product.getName());
            productCheckBox.setTextColor(getResources().getColor(R.color.light_gray));
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.light_gray));
            productCheckBox.setButtonTintList(colorStateList);
            productDetailsTextView.setTextColor(getResources().getColor(R.color.gray));
            productDetailsTextView.setPadding(20, 5, 0, 0);

            productCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String userId = mAuth.getUid();
                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Users").child("StoreOwner").child(userId).child("Orders").child(order.getOrderNumber()).child(product.getProductID());
                    if (isChecked) {
                        // Update the status to "Complete"
                        productRef.child("status").setValue("Complete");
                        // Update the status on the shopper's side
                        Log.d("If statement", "OrderNumber: " + order.getOrderNumber() + " ProductID: " + product.getProductID());
                        queryShoppers(order.getOrderNumber(), product.getProductID(), "Ready for Pickup");
                    } else {
                        // Update the status to "Received"
                        productRef.child("status").setValue("Received");
                        queryShoppers(order.getOrderNumber(), product.getProductID(), "Received");
                    }
                }
            });

            productDetailsTextView.setText(detailsBuilder.toString());

            productsLinearLayout.addView(productView);
        }

        CheckBox completeCheckBox = dialog.findViewById(R.id.markAsCompleteCheckBox);
        completeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Delete the order from the database
                    String userId = mAuth.getUid();
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Users").child("StoreOwner").child(userId).child("Orders").child(order.getOrderNumber());
                    orderRef.removeValue();

                    // Update the data source of the ListView
                    orderList.remove(order);
                    ownerordersAdapter.notifyDataSetChanged();
                }
            }
        });



        dialog.show();
    }


    //Create an example  order

    /*private void createExampleOrder(){
        Log.d("Create Order", "Creating Order");
        //Go to a specific Shopper and then push to
        String userID = mAuth.getUid();
        DatabaseReference shopperRef = ref.child("Users").child("StoreOwner").child(userID);
        DatabaseReference orderRef = shopperRef.child("Orders");
        DatabaseReference newOrderRef = orderRef.push();
        String orderID = newOrderRef.getKey();
        DatabaseReference statusRef = newOrderRef.child("Status");
        //User id
        DatabaseReference shopperEmailRef = newOrderRef.child("User ID");
        shopperEmailRef.setValue(userID);
        statusRef.setValue("Pending");
        DatabaseReference itemsRef = newOrderRef.child("Items");
        DatabaseReference newItemRef = itemsRef.push();
        DatabaseReference nameRef = newItemRef.child("Name");
        nameRef.setValue("Banana");
        DatabaseReference brandRef = newItemRef.child("Brand");
        brandRef.setValue("Banana Republic");
        DatabaseReference priceRef = newItemRef.child("Price");
        priceRef.setValue(1.99);
        DatabaseReference descriptionRef = newItemRef.child("Description");
        descriptionRef.setValue("A delicious banana");
        DatabaseReference quantityRef = newItemRef.child("Quantity");
        quantityRef.setValue(1);
        DatabaseReference storeNameRef = newItemRef.child("Store Name");
        storeNameRef.setValue("Banana Republic");
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                ImageButton inventoryButton = view.findViewById(R.id.button_inventory);
                ImageButton ordersButton = view.findViewById(R.id.button_orders);
                TextView inventoryText = view.findViewById(R.id.textViewInventoryText);
                TextView ordersText = view.findViewById(R.id.textViewOrderText);
                ImageView inventoryIcon = view.findViewById(R.id.inventoryIcon);
                ImageView ordersIcon = view.findViewById(R.id.orderIcon);
                if (destination.getId() == R.id.ProductList) {
                    inventoryButton.setImageResource(R.drawable.focused_nav_button);
                    ordersButton.setImageResource(R.drawable.nav_gradient);
                    inventoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    inventoryIcon.setImageResource(R.drawable.black_store);
                    ordersIcon.setImageResource(R.drawable.white_orders);
                } else if (destination.getId() == R.id.OwnerOrders) {
                    inventoryButton.setImageResource(R.drawable.nav_gradient);
                    ordersButton.setImageResource(R.drawable.focused_nav_button);
                    inventoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    inventoryIcon.setImageResource(R.drawable.white_store);
                    ordersIcon.setImageResource(R.drawable.black_orders);
                }
            }
        });
    }

    private void fetchStoreName() {
        DatabaseReference storeNameRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("StoreName");
        storeNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storeName = dataSnapshot.getValue(String.class);
                if (storeName != null) {
                    textViewStoreName.setText(storeName);
                } else {
                    showToast("Store name not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String errorMessage = "Error fetching store name: " + databaseError.getMessage();
                showToast(errorMessage);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void queryShoppers(String orderID, String productID, String tag){
        Log.d("In order query", "OrderID is " + orderID);
        Log.d("In product query", "ProductID is " + productID);
        DatabaseReference shopperRef = FirebaseDatabase.getInstance().getReference("Users").child("Shoppers");
        shopperRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String shopperId = snapshot.getKey();
                    Log.d("Checking shopper", "Checking shopper with ID: " + shopperId);
                    DatabaseReference ordersRef = shopperRef.child(shopperId).child("Orders");
                    ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String tempOrderID = snapshot.getKey();
                                Log.d("Checking order", "Checking order with ID: " + tempOrderID);
                                if (tempOrderID.equals(orderID)) {
                                    // Found the order, now query for the product
                                    Log.d("Found order", "Found order with ID: " + orderID + " for shopper with ID: " + shopperId);
                                    DatabaseReference productRef = shopperRef.child(shopperId).child("Orders").child(orderID);
                                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String tempProductID = snapshot.getKey();
                                                Log.d("Checking products", "Checking product with ID: " + tempProductID);
                                                if (tempProductID.equals(productID)) {
                                                    Log.d("Found product", "Found product with ID: " + productID + " for order with ID: " + orderID + " and shopper with ID: " + shopperId);
                                                    productRef.child(productID).child("status").setValue(tag);
                                                    return;
                                                }
                                            }}

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("DatabaseError", databaseError.getMessage());
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("DatabaseError", databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });
    }

}

