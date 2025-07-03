package com.minovative.simpleshoppingcart;

import static com.minovative.simpleshoppingcart.Helper.addItemToCart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<ProductItem> {

    private Context context;

    private OnItemClickListener listener;
    private List<ShoppingCart> newItemOnCart = new ArrayList<>();
    public GridViewAdapter(Activity activity,List<ProductItem> list, OnItemClickListener listener) {
        super(activity,0,list);
        this.context = activity;
        this.listener = listener;
    }
    // Livedata updating current item on the cart
    public void updateCartItems(List<ShoppingCart> newItems) {

        this.newItemOnCart.addAll(newItems);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(ProductItem currentPosition);
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
        holder.productPrice.setTextColor(ContextCompat.getColor(context,R.color.black));

        holder.productImg.setOnClickListener((v) -> {

            listener.onItemClick(item);
        });

        holder.addCart.setOnClickListener(view -> {

            addItemToCart(item, context);

            holder.productPrice.setText("Item added to cart");
            holder.productPrice.setTextColor(ContextCompat.getColor(context,R.color.colorSecondaryVariant));

            new android.os.Handler().postDelayed(( ) -> {
                holder.productPrice.setTextColor(ContextCompat.getColor(context,R.color.black));
                holder.productPrice.setText(item.getPrice() + " €");
            }, 4500);
        });

        return convertView;
    }
}