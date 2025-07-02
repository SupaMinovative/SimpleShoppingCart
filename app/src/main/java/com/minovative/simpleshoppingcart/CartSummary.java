package com.minovative.simpleshoppingcart;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class CartSummary extends AppCompatActivity {
    private ListView listView;
    private ItemListAdapter adapter;
    private TextView totalAmount;
    private TextView backBtn;

    private AppDatabase db;
    private List<ShoppingCart> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_summary);

        listView = findViewById(R.id.listView);
        totalAmount = findViewById(R.id.totalText);
        backBtn = findViewById(R.id.backToShop);

        backBtn.setOnClickListener(view ->
                super.onBackPressed());


         db = AppDatabase.getInstance(this);
        ShoppingCartDao shoppingCartDao = db.shoppingCartDao();

        CartViewModel cartViewModel = new ViewModelProvider(this,
                new CartViewModelFactory(getApplication(),db.shoppingCartDao()))
                .get(CartViewModel.class);
        adapter = new ItemListAdapter(CartSummary.this, itemList);
        listView.setAdapter(adapter);
        cartViewModel.getFinalItems().observe(this, existingItems -> {

            if (existingItems != null) {

                adapter.updateItems(existingItems);

                double total = existingItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
                totalAmount.setText("Total " + String.format("%.2f",total) + " €");
            }
        });

/*

        new Thread(( ) -> {

            try {
                AppDatabase db = AppDatabase.getInstance(this);
                ShoppingCartDao shoppingCartDao = db.shoppingCartDao();
                LiveData<List<ShoppingCart>> itemList = shoppingCartDao.getFinalItems();

                double total = itemList.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
                runOnUiThread(() -> {

                        adapter = new ItemListAdapter(CartSummary.this, itemList);
                        listView.setAdapter(adapter);
                        totalAmount.setText("Total " + String.format("%.2f",total) + " €");

                });

            } catch (Exception e) {
                Log.d("DEBUG", "App is crashing due to DB");
            }
        }).start();*/
    }
}
