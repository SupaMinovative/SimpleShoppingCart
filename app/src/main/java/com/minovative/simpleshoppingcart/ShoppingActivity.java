package com.minovative.simpleshoppingcart;

import static com.minovative.simpleshoppingcart.Helper.addItemToCart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity implements GridViewAdapter.OnItemClickListener {
    private GridView gridView;
    private GridViewAdapter adapter;
    private TextView welcomeText;
    private ImageView cart;
    private ImageView searchIcon;
    private LinearLayout searchBox;
    private AppDatabase db;
    private UserDao userDao;
    private String currentEmail;
    private String currentUser;
    private EditText searchField;
    private List<ProductItem> testList;
    private TextView itemCount;
    private LinearLayout rootLayout;
    private LinearLayout gridViewRoot;
    private ImageView accIcon;
    private User currentUserFromDb;
    private List<ProductItem> filteredList = new ArrayList<>();
    private ScrollView fullScreen;
    private ImageView fullProductImg;
    private TextView productName;
    private TextView fullProductPrice;
    private TextView productDes;
    private Button backBtn;
    private Button addToCart;
    private LinearLayout bottomBar;
    private ProductItem itemPosition;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping);

        welcomeText = findViewById(R.id.welcomeTextView);
        cart = findViewById(R.id.shoppingCart);
        searchField = findViewById(R.id.searchEditText);
        itemCount = findViewById(R.id.itemCount);
        searchIcon = findViewById(R.id.searchIcon);
        searchBox = findViewById(R.id.searchBox);
        rootLayout = findViewById(R.id.rootLayout);
        gridViewRoot = findViewById(R.id.gridViewRoot);
        accIcon = findViewById(R.id.accIcon);
        testList = new ArrayList<>();
        fullScreen = findViewById(R.id.fullScreenDisplay);
        fullProductImg = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        fullProductPrice = findViewById(R.id.productPrice);
        productDes = findViewById(R.id.productDescription);
        backBtn = findViewById(R.id.backButton);
        addToCart = findViewById(R.id.addToCartButton);
        bottomBar = findViewById(R.id.bottomBar);


        testList.add(new ProductItem(R.drawable.chocolate,"Dark Chocolate 100% (80g)",2.99,
                "Rich, intense dark chocolate made with 100% cocoa. " +
                        "Perfect for true chocolate lovers seeking a bold, bitter flavor with no added sugar. Vegan and gluten-free.\n" +
                        "Ingredients: Cocoa mass. May contain traces of nuts and milk.\n" +
                        "Store in a cool, dry place."));
        testList.add(new ProductItem(R.drawable.egg,"Organic Eggs 6-Pack (BIO)",3.29,
                "6 fresh, certified organic eggs from free-range hens. Rich in flavor and perfect for cooking or baking. " +
                        "Sourced from sustainable German farms.\n" +
                        "Class A • Size M–L\n" +
                        "Refrigerated item"));
        testList.add(new ProductItem(R.drawable.wine,"Red Wine Merlot 750ml",6.49,
                "Smooth and fruity Merlot with notes of blackberry and plum. " +
                        "Ideal with pasta, red meats, or cheese platters.\n" +
                        "Alcohol content: 13% vol\n" +
                        "Country: Italy\n" +
                        "Contains sulfites • 18+ only"));
        testList.add(new ProductItem(R.drawable.pizza,"Frozen Pizza Rucola e Olive",3.79,
                "Stone-baked pizza topped with fresh rucola, black olives, and mozzarella. " +
                        "Crispy crust with a tangy tomato base.\n" +
                        "Ready in 10–12 min at 220°C\n" +
                        "Vegetarian • 400g"));
        testList.add(new ProductItem(R.drawable.cheese,"Aged Gouda Cheese (200g)",4.59,
                "Matured for 12 months for a nutty, slightly caramelized flavor. " +
                        "Sliced and ready to enjoy with bread, wine, or crackers.\n" +
                        "Milk origin: Netherlands\n" +
                        "Storage: Refrigerate after opening\n" +
                        "\n"));
        testList.add(new ProductItem(R.drawable.tomato,"Fresh Tomatoes (250g, BIO)",2.49,
                "Juicy, organic tomatoes grown without synthetic pesticides. Ideal for salads, sandwiches, or cooking.\n" +
                        "Origin: Germany or Spain (depending on season)\n" +
                        "Store: At room temperature\n" +
                        "\n"));
        testList.add(new ProductItem(R.drawable.cookies,"Choco Chip Cookies 200g",2.29,
                "Crunchy and chewy chocolate chip cookies made with real butter and Belgian chocolate chips.\n" +
                        "Palm oil-free • Contains wheat, milk, soy, eggs\n" +
                        "Allergen info: May contain nuts"));
        testList.add(new ProductItem(R.drawable.croissant,"Butter Croissant 2-Pack",1.99,
                "Flaky, buttery croissants baked fresh daily. Perfect for breakfast or as a snack.\n" +
                        "Keep in a cool, dry place\n" +
                        "Heat briefly in oven for best taste\n" +
                        "\n"));
        testList.add(new ProductItem(R.drawable.butter,"Organic Butter 250g",2.69,
                "Creamy organic butter made from 100% German milk. Ideal for spreading or baking.\n" +
                        "FAT: min. 82%\n" +
                        "Storage: Refrigerate at 4°C"));
        testList.add(new ProductItem(R.drawable.cucumber,"Fresh Cucumber (1 piece)",0.69,
                "Crisp and refreshing cucumber – ideal for salads, dips, or sandwiches.\n" +
                        "Origin: Germany/Netherlands\n" +
                        "Store: In fridge drawer (5–7°C)"));
        testList.add(new ProductItem(R.drawable.milk,"Whole Milk 1L (3.5%)",0.99,
                "Fresh whole milk with 3.5% fat. Rich in calcium and protein.\n" +
                        "Pasteurized • Homogenized\n" +
                        "Refrigerated item • Consume within 3 days after opening"));
        testList.add(new ProductItem(R.drawable.pasta,"Durum Wheat Pasta 500g",1.19,
                "Classic Italian pasta made from 100% durum wheat semolina. Holds sauce well, ideal for any pasta dish.\n" +
                        "Cooking time: 9–11 minutes\n" +
                        "Vegan • No additives"));
        testList.add(new ProductItem(R.drawable.cake,"Chocolate Cake Slice",2.99,
                "Moist and rich chocolate cake slice layered with cocoa cream. A perfect treat for dessert lovers.\n" +
                        "Contains: Milk, wheat, eggs, soy\n" +
                        "Best before: See label"));

        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        currentEmail = getIntent().getStringExtra("EMAIL");
        currentUser = getIntent().getStringExtra("USERNAME");
        welcomeText.setText("Hello " + currentUser);

        gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(2);

        adapter = new GridViewAdapter(this,testList, this);
        gridView.setAdapter(adapter);
        CartViewModel cartViewModel = new ViewModelProvider(this,
                new CartViewModelFactory(getApplication(),db.shoppingCartDao()))
                .get(CartViewModel.class);

        GridViewAdapter filteredAdapter = new GridViewAdapter(ShoppingActivity.this, filteredList, this);
        cartViewModel.getFinalItems().observe(this,existingItems -> {

            if (existingItems != null) {

                adapter.updateCartItems(existingItems);

                int total = existingItems.stream().mapToInt(ShoppingCart::getQuantity).sum();
                itemCount.setText(total + "");
            }
        });

        backBtn.setOnClickListener(view -> {
            gridView.setVisibility(View.VISIBLE);
            fullScreen.setVisibility(View.GONE);
            bottomBar.setVisibility(View.VISIBLE);
        });

        addToCart.setOnClickListener(view -> {
            addItemToCart(itemPosition, this);
        });

        cart.setOnClickListener(view -> {
            Intent i = new Intent(ShoppingActivity.this,CartSummary.class);
            startActivity(i);
        });

        rootLayout.setOnClickListener(view -> {
            searchField.clearFocus();
            searchField.setText("");
            searchBox.setVisibility(View.GONE);
        });

        gridViewRoot.setOnClickListener(view -> {
            searchField.setText("");
        });

        if (searchIcon != null) {
            searchIcon.setOnClickListener(view -> {
                searchBox.setVisibility(View.VISIBLE);
            });
        }

        searchField.setOnFocusChangeListener((v,hasFocus) -> {
            if (!hasFocus) {
                searchBox.setVisibility(View.GONE);
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,int start,int count,int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count) {
                filteredList.clear();
                String query = s.toString().toLowerCase();

                for (ProductItem it : testList) {
                    if (it.getItemName().toLowerCase().contains(query)) {
                        filteredList.add(it);

                    }
                }
                gridView.setAdapter(filteredAdapter);
                filteredAdapter.notifyDataSetChanged();
                new Handler().postDelayed(( ) -> {
                    searchBox.setVisibility(View.GONE);
                },60_000);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        accIcon.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Do you want to log out?");
            dialog.setNegativeButton("No",(d,i) -> d.cancel());
            dialog.setPositiveButton("Yes",(d,i) -> {
                boolean isLogout = true;

                new Thread(( ) -> {
                    currentUserFromDb = userDao.getCurrentUser(currentEmail);

                    runOnUiThread(( ) -> {
                        currentUserFromDb.setStatus(false);
                    });

                    userDao.updateAccStatus(currentUserFromDb);
                }).start();

                Intent intent = new Intent(ShoppingActivity.this,MainActivity.class);
                intent.putExtra("ACCOUNT_STATUS",isLogout);
                startActivity(intent);
                finish();
            });
            dialog.show();
        });
    }

    @Override
    public void onItemClick(ProductItem currentPosition) {
        itemPosition = currentPosition;
        gridView.setVisibility(View.GONE);
        fullScreen.setVisibility(View.VISIBLE);
        bottomBar.setVisibility(View.GONE);
        fullProductImg.setImageResource(currentPosition.getImgPath());
        productName.setText(currentPosition.getItemName());
        fullProductPrice.setText("Price: " + currentPosition.getPrice() + " €");
        productDes.setText("Description: " + currentPosition.getItemDescription());
    }
}