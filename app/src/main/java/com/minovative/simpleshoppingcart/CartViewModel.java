package com.minovative.simpleshoppingcart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private final ShoppingCartDao shoppingCartDao;
    private final LiveData<List<ShoppingCart>> shoppingCartList;
    public CartViewModel(@NonNull Application application, ShoppingCartDao shoppingCartDao) {
        super(application);
        this.shoppingCartDao = shoppingCartDao;
        shoppingCartList = shoppingCartDao.getFinalItems();
    }

    public LiveData<List<ShoppingCart>> getFinalItems() {
        return shoppingCartDao.getFinalItems();
    }
}
