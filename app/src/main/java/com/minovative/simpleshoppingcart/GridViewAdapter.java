package com.minovative.simpleshoppingcart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GridViewAdapter extends ArrayAdapter<ProductItem> {


    private List<ProductItem> itemList;

    private Context context;

    private Activity activity;


    private List<ShoppingCart> newItemOnCart = new ArrayList<>();
    public GridViewAdapter(Activity activity,List<ProductItem> list) {
        super(activity,0,list);
        this.context = activity;
        this.activity = activity;
    }
    public void updateCartItems(List<ShoppingCart> newItems) {

        this.newItemOnCart.addAll(newItems);
        notifyDataSetChanged();
    }
  

    static class ViewHolder {
        ImageView productImg;
        TextView productDetail;
        TextView productPrice;
        ImageView addCart;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView (int position,View convertView,@NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item,parent,false);

            holder = new ViewHolder();
            holder.productImg = convertView.findViewById(R.id.productImg);
            holder.productDetail = convertView.findViewById(R.id.productDet);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.addCart = convertView.findViewById(R.id.addBtn);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        ProductItem item = getItem(position);
        assert item != null;
        holder.productImg.setImageResource(item.getImgPath());
        holder.productDetail.setText(item.getItemName());
        holder.productPrice.setText(item.getPrice() + " €");

        holder.addCart.setOnClickListener(view -> {

            String currentProduct = item.getItemName();
            int productImg = item.getImgPath();
            int productPrice = item.getPrice();
            AtomicInteger itemQuantity = new AtomicInteger(1);

            new Thread(( ) -> {
                AppDatabase db = AppDatabase.getInstance(context);
                ShoppingCartDao shoppingCartDao = db.shoppingCartDao();

                List<ShoppingCart> itemOnCart = shoppingCartDao.getAllItems(currentProduct,productPrice);
                boolean exists = false;
                int updateQtt = 0;
                for (ShoppingCart it : itemOnCart){
                    updateQtt = it.getQuantity();
                    if (currentProduct.equals(it.getItemName())) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {

                    updateQtt++;
                    ShoppingCart updateCartItem = new ShoppingCart(currentProduct,productImg,productPrice,updateQtt);
                    Log.d("DEBUG", "Item is being update to cart. Current Item clicked : " + currentProduct + "current qtt: " + updateQtt);
                    shoppingCartDao.updateItemQtt(updateCartItem);

                }

                if (!exists) {
                    ShoppingCart newCartItem = new ShoppingCart(currentProduct,productImg,productPrice,itemQuantity.get());
                    Log.d("DEBUG", "Item is being added to cart");
                    shoppingCartDao.insertItem(newCartItem);
                }

            }).start();


        });
        return convertView;
    }


}