package com.minovative.simpleshoppingcart;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, ShoppingCart.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ShoppingCartDao shoppingCartDao();
    private static AppDatabase INSTANCE = null;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            Log.d("AppDatabase","Creating new DB instance");
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"my-database")
                    .fallbackToDestructiveMigration().build();
        } else {
                Log.d("AppDatabase", "Reusing existing DB instance");

        } return INSTANCE;
    }


}
