package com.example.shopeaze;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder>{

    Context context;
    ArrayList<CartItem> cartItems;

    public MyCartAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public MyCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new MyCartAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.MyViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Log.d("MyCartAdapter", "Name: " + cartItem.getcartProductName());
        Log.d("MyCartAdapter", "Price: " + cartItem.getcartProductPrice());
        Log.d("MyCartAdapter", "Brand: " + cartItem.getcartProductBrand());
        Log.d("MyCartAdapter", "Quantity: " + cartItem.getCartQuantity());
        Log.d("MyCartAdapter", "ID: " + cartItem.getcartProductID());
        Log.d("MyCartAdapter", "Status: " + cartItem.getStatus());
        Log.d("MyCartAdapter", "StoreID: " + cartItem.getStoreID());

        holder.cartProductName.setText(cartItem.getcartProductName());
        holder.cartProductPrice.setText("$ " + String.valueOf(cartItem.getcartProductPrice()));
        holder.cartProductBrand.setText(cartItem.getcartProductBrand());
        holder.cartProductQuantity.setText(String.valueOf(cartItem.getCartQuantity()));

        Log.d("MyCartAdapter", "Image URL: " + cartItem.getImage());
        Glide.with(context)
                .load(cartItem.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.imageView);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem cartItem = cartItems.get(position);
                    removeCartItem(cartItem);
                    cartItems.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // increment the quantity of the item in the cart
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem cartItem = cartItems.get(position);
                    cartItem.setQuantity(cartItem.getCartQuantity() + 1);
                    Log.d("MyCartAdapter", "Quantity: " + cartItem.getCartQuantity());
                    addCartItem(cartItem);
                    holder.cartProductQuantity.setText(String.valueOf(cartItem.getCartQuantity()));
                }
            }
        });

        holder.subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // increment the quantity of the item in the cart
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem cartItem = cartItems.get(position);
                    cartItem.setQuantity(cartItem.getCartQuantity() - 1);
                    Log.d("MyCartAdapter", "Quantity: " + cartItem.getCartQuantity());
                    subtractCartItem(cartItem);
                    holder.cartProductQuantity.setText(String.valueOf(cartItem.getCartQuantity()));
                }
            }
        });

    }

    private void addCartItem(CartItem cartItem) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference shopperRef = usersRef.child("Shoppers").child(userID);
        DatabaseReference cartRef = shopperRef.child("Cart");

        cartRef.orderByChild("cartProductID").equalTo(cartItem.getcartProductID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CartItem existingCartItem = snapshot.getValue(CartItem.class);
                        int newQuantity = existingCartItem.getCartQuantity() + 1;
                        snapshot.getRef().child("cartQuantity").setValue(newQuantity);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyCartAdapter", "onCancelled", databaseError.toException());
            }
        });

    }

    private void subtractCartItem(CartItem cartItem) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference shopperRef = usersRef.child("Shoppers").child(userID);
        DatabaseReference cartRef = shopperRef.child("Cart");

        cartRef.orderByChild("cartProductID").equalTo(cartItem.getcartProductID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CartItem existingCartItem = snapshot.getValue(CartItem.class);
                        int newQuantity = existingCartItem.getCartQuantity() - 1;
                        if (newQuantity <= 0){
                            removeCartItem(cartItem);
                            cartItems.remove(cartItem);
                            notifyDataSetChanged();
                        }
                        snapshot.getRef().child("cartQuantity").setValue(newQuantity);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyCartAdapter", "onCancelled", databaseError.toException());
            }
        });

    }

    private void removeCartItem(CartItem cartItem) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("Shoppers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Cart");

        Query productQuery = productRef.orderByChild("cartProductID").equalTo(cartItem.getcartProductID());

        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    productSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MyCartAdapter", "onCancelled", error.toException());
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cartProductName, cartProductPrice, cartProductBrand, cartProductQuantity;
        Button removeButton;
        Button addButton;
        Button subtractButton;

        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartProductPrice = itemView.findViewById(R.id.cartProductPrice);
            cartProductBrand = itemView.findViewById(R.id.cartProductBrand);
            cartProductQuantity = itemView.findViewById(R.id.productQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
            addButton = itemView.findViewById(R.id.addButton);
            subtractButton = itemView.findViewById(R.id.subtractButton);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}