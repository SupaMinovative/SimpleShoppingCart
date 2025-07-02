package com.minovative.simpleshoppingcart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CartViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final ShoppingCartDao shoppingCartDao;

    public CartViewModelFactory(Application application ,ShoppingCartDao shoppingCartDao) {
        this.application = application;
        this.shoppingCartDao = shoppingCartDao;
    }

    @NonNull
    @Override

    public <T extends ViewModel> T create(@NonNull Class<T> modellClass) {

        if (modellClass.isAssignableFrom(CartViewModel.class)) {

            return (T) new CartViewModel(application ,shoppingCartDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
