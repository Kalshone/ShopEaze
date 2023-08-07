package com.example.shopeaze;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class OrderDetailDialog extends DialogFragment {

    private Order order;

    public OrderDetailDialog(Order order) {
        this.order = order;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_order_detail, null);

        // Assuming you have TextViews with these IDs in dialog_order_detail.xml
        TextView brandTextView = view.findViewById(R.id.brand_text);
        TextView priceTextView = view.findViewById(R.id.price_text);
        TextView quantityTextView = view.findViewById(R.id.quantity_text);

        brandTextView.setText(order.getCartProductBrand());
        priceTextView.setText(String.valueOf(order.getCartProductPrice()));
        quantityTextView.setText(String.valueOf(order.getCartQuantity()));

        builder.setView(view)
                .setTitle("Order Detail")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close dialog
                    }
                });

        return builder.create();
    }
}
