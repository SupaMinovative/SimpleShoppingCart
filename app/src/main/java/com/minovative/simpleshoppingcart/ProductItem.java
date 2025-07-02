package com.minovative.simpleshoppingcart;

public class ProductItem {
    private int imgPath;
    private String itemName;
    private int price;

    public ProductItem(int imgPath,String itemName,int price) {
        this.imgPath = imgPath;
        this.itemName = itemName;
        this.price = price;
    }

    public int getImgPath( ) {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public String getItemName( ) {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice( ) {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
