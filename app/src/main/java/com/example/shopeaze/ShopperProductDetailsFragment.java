package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopperProductDetailsFragment extends Fragment {
    private static final String ARG_STORE_ID = "store_id";
    private static final String ARG_PRODUCT = "product";
    private Product product;

    Button addToCartButton;         //new
    private static final String TAG = "ShopperProductDetails";  //new

    public static ShopperProductDetailsFragment newInstance(String storeID, Product product) {
        ShopperProductDetailsFragment fragment = new ShopperProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORE_ID, storeID);
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_shopper_product_details, container, false);

        // Retrieve storeID and product from arguments
        String storeID = getArguments().getString(ARG_STORE_ID);
        product = (Product) getArguments().getSerializable(ARG_PRODUCT);

        // Display product details
        TextView productNameTextView = rootView.findViewById(R.id.textViewProductName);
        productNameTextView.setText(product.getName());

        TextView brandNameTextView = rootView.findViewById(R.id.textViewBrandName);
        brandNameTextView.setText(String.valueOf(product.getBrand()));

        TextView productPriceTextView = rootView.findViewById(R.id.textViewProductPrice);
        productPriceTextView.setText("$ " + String.valueOf(product.getPrice()));

        TextView productDescriptionTextView = rootView.findViewById(R.id.textViewProductDescription);
        productDescriptionTextView.setText(product.getDescription());
//new:
        addToCartButton = rootView.findViewById(R.id.buttonAddToCart);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product);
            }
        });
//new ends^^
        return rootView;
    }

//new:
    private void addToCart(Product product) {
        CartItem cartItem = new CartItem(product);
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference shopperRef = usersRef.child("Shoppers").child(userID);
        DatabaseReference cartRef = shopperRef.child("Cart");

        //Count the number of a specific product
        cartRef.orderByChild("cartProductID").equalTo(cartItem.getcartProductID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //The product is already in the cart, update the quantity       (TEMPORARY, this will be done in actual cart)
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CartItem existingCartItem = snapshot.getValue(CartItem.class);
                        int newQuantity = existingCartItem.getQuantity() + 1;
                        snapshot.getRef().child("quantity").setValue(newQuantity);
                        Toast.makeText(getActivity(), "Items in cart: " + newQuantity, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Product is not in Cart, add it with an initial quantity of 1
                    cartItem.setQuantity(1);
                    cartRef.push().setValue(cartItem)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Item Added.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Failed to add item to cart.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that might occur during the query
                Log.e(TAG, "Error checking if product exists in cart: " + databaseError.getMessage());
            }
        });
    }
}
