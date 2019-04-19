package com.example.lab2firebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerView_Config {

    private Context mContext;
    private FoodsAdapter mFoodsAdapter;
    public void setConfig (RecyclerView recyclerView, Context context,List<DailyOffer> dailyOffers, List <String> keys){
        mContext=context;
        mFoodsAdapter = new FoodsAdapter(dailyOffers,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFoodsAdapter);
    }

    class DailyOfferItemView extends RecyclerView.ViewHolder {
        private TextView mFoodname;
        private TextView mPrice;
        private TextView mDiscount;
        private TextView mShortdescription;
        private TextView mAvailablequantity;
        private ImageView mImgfood;
        private Uri image_uri;
        private String key;
        public DailyOfferItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.food_list_item,parent,false));
            mFoodname= (TextView) itemView.findViewById(R.id.txt_FoodName);
            mPrice= (TextView) itemView.findViewById(R.id.txt_Price);
            mDiscount= (TextView) itemView.findViewById(R.id.txt_Discount);
            mAvailablequantity= (TextView) itemView.findViewById(R.id.txt_AvailableQuantity);
            mShortdescription= (TextView) itemView.findViewById(R.id.txt_ShortDescription);
            mImgfood= (ImageView) itemView.findViewById(R.id.img_Food);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,FoodDetailsActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("foodName",mFoodname.getText().toString());
                    intent.putExtra("price",mPrice.getText().toString());
                    intent.putExtra("discount",mDiscount.getText().toString());
                    intent.putExtra("availableQuantity",mAvailablequantity.getText().toString());
                    intent.putExtra("shortDescription",mShortdescription.getText().toString());
                  //  intent.putExtra("image",image_uri);

                    mContext.startActivity(intent);
                }
            });

        }
        public void bind(DailyOffer dailyOffer, String key) {

            mFoodname.setText(dailyOffer.getName());
            mPrice.setText(dailyOffer.getPrice());
            mDiscount.setText(dailyOffer.getDiscount());
            mShortdescription.setText(dailyOffer.getShortdescription());
            mAvailablequantity.setText(dailyOffer.getAvailablequantity());

            Picasso.get()
                    .load(dailyOffer.getImageUrl())
                    .placeholder(R.drawable.default_food)
                    .fit()
                    .centerCrop()
                    .into(mImgfood);
            this.key = key;
        }
    }
    class FoodsAdapter extends RecyclerView.Adapter<DailyOfferItemView>{
        private List<DailyOffer> mDailyOfferList;
        private List <String> mKeys;

        public FoodsAdapter(List <DailyOffer> mDailyOfferList , List <String> mKeys){
            this.mDailyOfferList = mDailyOfferList;
            this.mKeys = mKeys;
        }


        @Override
        public DailyOfferItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DailyOfferItemView(parent);
        }

        @Override
        public void onBindViewHolder(DailyOfferItemView holder, int position) {
            holder.bind(mDailyOfferList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mDailyOfferList.size();
        }
    }

}

