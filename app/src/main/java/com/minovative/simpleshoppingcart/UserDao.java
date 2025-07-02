package com.minovative.simpleshoppingcart;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);
    @Query("SELECT * FROM user WHERE email =:email AND password =:password")
    List<User> getUserLogin(String email,String password);

    @Query("SELECT * FROM user WHERE email =:email")

    List<User> getAllUsers(String email);
}
