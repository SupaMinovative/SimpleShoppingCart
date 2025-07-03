package com.minovative.simpleshoppingcart;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, ShoppingCart.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ShoppingCartDao shoppingCartDao();
    private static AppDatabase INSTANCE = null;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"my-database")
                    .fallbackToDestructiveMigration().build();
        } return INSTANCE;
    }


}
