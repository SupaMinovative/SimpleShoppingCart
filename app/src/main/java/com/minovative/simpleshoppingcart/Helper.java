package com.minovative.simpleshoppingcart;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Helper {

    static void saveData(String email,String password,String currentUser,
                         boolean accStatus,CheckBox remember, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveData",Context.MODE_PRIVATE);
        if (remember.isChecked()) {
            accStatus = true;
        } else {
            accStatus = false;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key name",email);
        editor.putString("key password",password);
        editor.putString("key username",currentUser);
        editor.putBoolean("key remember",accStatus);
        editor.apply();
    }

    static void clearSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    static void addItemToCart(ProductItem itemPosition, Context context) {
        String currentProduct = itemPosition.getItemName();
        int productImg = itemPosition.getImgPath();
        double productPrice = itemPosition.getPrice();
        AtomicInteger itemQuantity = new AtomicInteger(1);

        new Thread(( ) -> {
            AppDatabase db = AppDatabase.getInstance(context);
            ShoppingCartDao shoppingCartDao = db.shoppingCartDao();

            List<ShoppingCart> itemOnCart = shoppingCartDao.getAllItems(currentProduct,productPrice);
            boolean exists = false;
            int updateQtt = 0;

            for (ShoppingCart it : itemOnCart){
                updateQtt = it.getQuantity();
                if (currentProduct.equals(it.getItemName())) {
                    exists = true;
                    break;
                }
            }

            // Updating existing item to database
            if (exists) {

                updateQtt++;
                ShoppingCart updateCartItem = new ShoppingCart(currentProduct,productImg,productPrice,updateQtt);
                shoppingCartDao.updateItemQtt(updateCartItem);

            }

            // Insert new item to database
            if (!exists) {
                ShoppingCart newCartItem = new ShoppingCart(currentProduct,productImg,productPrice,itemQuantity.get());
                shoppingCartDao.insertItem(newCartItem);
            }

        }).start();
    }

    static void setErrorText(TextView t,String str) {

        t.setVisibility(View.VISIBLE);
        t.setText(str);

        new android.os.Handler().postDelayed(( ) -> {

            setEmptyText(t);

        },2000);
    }

    static void setEmptyText(TextView t) {
        t.setText("");
    }


}
