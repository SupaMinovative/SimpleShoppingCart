package com.minovative.simpleshoppingcart;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"itemName"})
public class ShoppingCart {
    @NonNull
    @ColumnInfo(name ="itemName")
    private String itemName;
    @ColumnInfo(name ="imgPath")
    private int imgPath;
    @ColumnInfo(name ="price")
    private int price;
    @ColumnInfo(name ="quantity")
    private int quantity;

    public ShoppingCart(String itemName, int imgPath,int price, int quantity) {
        this.itemName = itemName;
        this.imgPath = imgPath;
        this.price = price;
        this.quantity = quantity;
    }

    public String getItemName( ) {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImgPath( ) {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public int getPrice( ) {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity( ) {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
