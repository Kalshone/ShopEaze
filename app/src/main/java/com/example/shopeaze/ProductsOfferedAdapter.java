package com.example.shopeaze;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductsOfferedAdapter extends RecyclerView.Adapter<ProductsOfferedAdapter.ViewHolder> {
    private static final String TAG = "ProductsOfferedAdapter";
    private List<Product> products;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductsOfferedAdapter(List<Product> products, OnItemClickListener listener) {
        Log.d(TAG, "Creating new ProductAdapter with " + products.size() + " products");
        this.products = products;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products_offered, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private TextView productPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            productPriceTextView = itemView.findViewById(R.id.textViewProductPrice);
        }

        public void bind(final Product product, final OnItemClickListener listener) {
            productNameTextView.setText(product.getName());
            productPriceTextView.setText("$ " + String.valueOf(product.getPrice()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
        }
    }
}
