package com.minovative.simpleshoppingcart;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingCartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertItem(ShoppingCart item);
    @Update
    void updateItemQtt(ShoppingCart item);

    @Query("SELECT * FROM shoppingCart WHERE itemName =:itemName AND price =:price")
    List<ShoppingCart> getAllItems(String itemName, int price);

    @Query("SELECT * FROM shoppingCart")
    LiveData<List<ShoppingCart>> getFinalItems();

}
