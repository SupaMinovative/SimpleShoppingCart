package com.minovative.simpleshoppingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends ArrayAdapter<ShoppingCart> {


    private Context context;
    private List<ShoppingCart> itemList;

    public ItemListAdapter(Context context, List<ShoppingCart> itemList) {

        super(context, R.layout.item_list,itemList );
        this.context = context;
        this.itemList = itemList;
    }
    public void updateItems(List<ShoppingCart> newItems) {

        this.itemList.addAll(newItems);
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position,View converView,ViewGroup parent) {

        if (converView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            converView = inflater.inflate(R.layout.item_list, parent, false);
        }

        ImageView itemImg = converView.findViewById(R.id.itemImg);
        TextView itemName = converView.findViewById(R.id.itemName);
        TextView itemQtt = converView.findViewById(R.id.qtt);
        TextView itemPrice = converView.findViewById(R.id.priceText);



        ShoppingCart currentItemOnCart = itemList.get(position);

        itemImg.setImageResource(currentItemOnCart.getImgPath());
        itemName.setText(currentItemOnCart.getItemName());
        itemPrice.setText(currentItemOnCart.getPrice() + ".00 €");
        if(currentItemOnCart.getQuantity() <= 1) {
            itemQtt.setText("Quantity: " + currentItemOnCart.getQuantity());
        } else itemQtt.setText("Quantities: " + currentItemOnCart.getQuantity());
        return converView;
    }
}
