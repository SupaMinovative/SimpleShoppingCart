package com.minovative.simpleshoppingcart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemListAdapter extends ArrayAdapter<ShoppingCart> {


    private Context context;
    private List<ShoppingCart> itemList;
    private AppDatabase db;
    private ShoppingCartDao shoppingCartDao;

    public ItemListAdapter(Context context, List<ShoppingCart> itemList) {

        super(context, R.layout.item_list,itemList );
        this.context = context;
        this.itemList = itemList;
    }

    // Update current item on cart from livedata
    public void updateItems(List<ShoppingCart> newItems) {

        this.itemList.clear();
        this.itemList.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position,View converView,ViewGroup parent) {

        if (converView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            converView = inflater.inflate(R.layout.item_list, parent, false);
        }

        db = AppDatabase.getInstance(context);
        shoppingCartDao = db.shoppingCartDao();

        ImageView itemImg = converView.findViewById(R.id.itemImg);
        TextView itemName = converView.findViewById(R.id.itemName);
        TextView itemQtt = converView.findViewById(R.id.qtt);
        TextView itemPrice = converView.findViewById(R.id.priceText);
        ImageView deleteBtn = converView.findViewById(R.id.deleteItemBtn);
        ImageView addBtn = converView.findViewById(R.id.addItemBtn);

        ShoppingCart currentItemOnCart = itemList.get(position);
        AtomicInteger currentQtt = new AtomicInteger(currentItemOnCart.getQuantity());

        // Removing items on cart
        deleteBtn.setOnClickListener(view -> {
            if(currentItemOnCart.getQuantity() == 0) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                        .setTitle("")
                        .setMessage("Do you want to remove item from the cart?");
                dialog.setNegativeButton("No",(d,i) -> d.cancel());
                dialog.setPositiveButton("Yes",(d,i) -> {

                    new Thread(( ) -> {
                        shoppingCartDao.deleteItem(currentItemOnCart.getItemName());
                    }).start();
                });
                dialog.show();
            }
            else {
                currentQtt.getAndDecrement();
                setRemoveAndAddIT(currentQtt,currentItemOnCart);
                setQttText(currentItemOnCart,itemQtt);
            }
        });

        // Adding item to cart
        addBtn.setOnClickListener(view -> {
            currentQtt.getAndIncrement();
            setRemoveAndAddIT(currentQtt, currentItemOnCart);
            setQttText(currentItemOnCart,itemQtt);
        });

        // Setting resource to cart's item
        itemImg.setImageResource(currentItemOnCart.getImgPath());
        itemName.setText(currentItemOnCart.getItemName());
        itemPrice.setText(currentItemOnCart.getPrice() + " €");
        setQttText(currentItemOnCart,itemQtt);

        return converView;
    }
    private void setQttText(ShoppingCart currentIt, TextView itemQtt){
        if (currentIt.getQuantity() <= 1) {
            itemQtt.setText("Quantity: " + currentIt.getQuantity());
        } else itemQtt.setText("Quantities: " + currentIt.getQuantity());
    }
    private void setRemoveAndAddIT(AtomicInteger currentQtt,ShoppingCart currentIt){
        new Thread(()-> {
            int newQtt =  currentQtt.get();
            currentIt.setQuantity(newQtt);
            shoppingCartDao.updateItemQtt(currentIt);
        }).start();
    }
}
