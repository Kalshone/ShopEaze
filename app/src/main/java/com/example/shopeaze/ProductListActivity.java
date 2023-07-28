package com.example.shopeaze;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        ProductList productList = new ProductList();
        products = productList.getAllProducts();

        ProductAdapter adapter = new ProductAdapter(products);
        recyclerViewProducts.setAdapter(adapter);
    }
}
