package com.minovative.simpleshoppingcart;

public class ProductItem {
    private int imgPath;
    private String itemName;
    private double price;
    private String itemDescription;

    public ProductItem(int imgPath,String itemName,double price,String itemDescription) {
        this.imgPath = imgPath;
        this.itemName = itemName;
        this.price = price;
        this.itemDescription = itemDescription;
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

    public double getPrice( ) {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemDescription( ) {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
