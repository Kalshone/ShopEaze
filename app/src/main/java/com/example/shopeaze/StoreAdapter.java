package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> stores;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Store store);
    }

    public StoreAdapter(List<Store> stores, OnItemClickListener itemClickListener) {
        this.stores = stores;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view, itemClickListener, stores);
    }


    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.textViewStoreName.setText(store.getStoreName());
        Log.d("StoreAdapter", " StoreAdpater Binding store name: " + store.getStoreName());
        // String logoUrl = store.getLogoUrl();
        /*if (logoUrl != null && !logoUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(logoUrl)
                    .into(holder.imageViewStoreLogo);
        }*/
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStoreName;
        ImageView imageViewStoreLogo;
        OnItemClickListener itemClickListener;
        List<Store> stores;

        public StoreViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener, List<Store> stores) {
            super(itemView);
            this.itemClickListener = itemClickListener;
            this.stores = stores;
            textViewStoreName = itemView.findViewById(R.id.textViewStoreName);
            //imageViewStoreLogo = itemView.findViewById(R.id.imageViewStoreLogo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d("StoreAdapter", "Clicked item at position: " + position);
                    if (position != RecyclerView.NO_POSITION) {
                        Store store = stores.get(position);
                        Log.d("StoreAdapter", "Clicked storeID: " + store.getStoreID());
                        itemClickListener.onItemClick(store);
                    }
                }
            });
        }
    }

}
