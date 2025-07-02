package com.minovative.simpleshoppingcart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    private GridView gridView;
    private GridViewAdapter adapter;
    private TextView welcomeText;
    private Button logoutBtn;
    private ImageView cart;
    private AppDatabase db;
    private UserDao userDao;
    private List<User> userOnDb;
    private String currentEmail;
    private String currentUser;
    private String currentPass;
    private List<ProductItem> testList;
    private TextView itemCount;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping);


        logoutBtn = findViewById(R.id.logoutBtn);
        welcomeText = findViewById(R.id.welcomeTextView);
        cart = findViewById(R.id.shoppingCart);
        itemCount = findViewById(R.id.itemCount);
        testList = new ArrayList<>();

        testList.add(new ProductItem(R.drawable.chocolate, "100% Dark Chocolate", 10));
        testList.add(new ProductItem(R.drawable.egg, "BIO Egg", 5));
        testList.add(new ProductItem(R.drawable.wine, "Red Wine", 15 ));
        testList.add(new ProductItem(R.drawable.bread, "Baguette", 5 ));
        testList.add(new ProductItem(R.drawable.cola, "Coco Caca", 5 ));
        testList.add(new ProductItem(R.drawable.pizza, "Frozen Pizza", 15 ));
        testList.add(new ProductItem(R.drawable.cheese, "Premium Cheese", 10 ));
        testList.add(new ProductItem(R.drawable.tomato, "Fresh Organic Tomato", 5 ));
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        currentEmail = getIntent().getStringExtra("EMAIL");
        currentUser = getIntent().getStringExtra("USERNAME");
        welcomeText.setText("Hello " + currentUser);

        gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(2);

        adapter = new GridViewAdapter(this ,testList);
        gridView.setAdapter(adapter);
        CartViewModel cartViewModel = new ViewModelProvider(this,
                new CartViewModelFactory(getApplication(),db.shoppingCartDao()))
                .get(CartViewModel.class);

        cartViewModel.getFinalItems().observe(this, existingItems -> {

            if (existingItems != null) {

                adapter.updateCartItems(existingItems);

                int total = existingItems.stream().mapToInt(ShoppingCart::getQuantity).sum();
                itemCount.setText(total + "");
            }
        });


        cart.setOnClickListener(view -> {
            Intent i = new Intent(ShoppingActivity.this, CartSummary.class);
            Log.d("DEBUG", "Starting intent from shopping activity");
            startActivity(i);
        });

        logoutBtn.setOnClickListener(view -> {
            boolean isLogout = true;
            Intent intent = new Intent(ShoppingActivity.this, MainActivity.class);
            intent.putExtra("ACCOUNT_STATUS",isLogout);
            startActivity(intent);
        });
    }
    /*
    public void setAdapter() {
        gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(2);

        adapter = new GridViewAdapter(this ,testList);
        gridView.setAdapter(adapter);
    }*/
}