/*package com.minovative.simpleshoppingcart;

import android.app.Application;

public class Helper {

    public static void saveItemToCart(ShoppingCart item, Application application) {

        new Thread(() -> {

            AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
            ShoppingCartDao shoppingCartDao = db.shoppingCartDao();
            ShoppingCart currentItem = shoppingCartDao.insertItem(item);

            if (currentState == null) {

                currentState = new GameState();
                currentState.setStarCount(starCount);
                gameStateDao.insertGameState(currentState);

            } else {
                currentState.setStarCount(starCount);
                gameStateDao.insertGameState(currentState);
            }
        }).start();
    }

}
*/