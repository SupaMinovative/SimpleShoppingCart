package com.minovative.simpleshoppingcart;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"email"})
public class User {

    @NonNull
    @ColumnInfo(name ="username")
    private String username;
    @NonNull
    @ColumnInfo(name ="email")
    private String email;
    @ColumnInfo(name = "password")
    private String password;

    public User(@NonNull String username,@NonNull String email,String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getUsername( ) {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getEmail( ) {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getPassword( ) {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
