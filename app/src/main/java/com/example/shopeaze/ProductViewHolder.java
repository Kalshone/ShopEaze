package com.example.shopeaze;


import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.annotation.NonNull;
import com.example.shopeaze.R;


import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    TextView textViewProductName, textViewProductBrand, textViewProductPrice;

    ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewProductPrice = itemView.findViewById(R.id.textViewProductName); // Changed to product_name
        textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice); // Changed to product_price
        textViewProductBrand = itemView.findViewById(R.id.textViewProductBrand); // Changed to product_description
    }
}