package com.example.lab2firebase;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailedItemFragment extends Fragment {
    @Nullable

    ListView itemsListView;
    TextView orderNumber;
    TextView totalPrice;
    Context thiscontext;
    protected Activity mActivity;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detaileditemfragment_layout, container, false);

        itemsListView = (ListView) v.findViewById(R.id.itemsListView);
        orderNumber = (TextView) v.findViewById(R.id.orderNb_textView);
        totalPrice = (TextView) v.findViewById(R.id.totalPrice_textView);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //thiscontext = (Context) getActivity();

    }

    public void updateView(Order current_order){
        ArrayList<ItemOrdered> items = current_order.getAllItems();
        if(mActivity!=null) {
            DetailedItemAdapter detailedItemAdapter = new DetailedItemAdapter(mActivity, items);
            itemsListView.setAdapter(detailedItemAdapter);

            orderNumber.setText("Order n°" + current_order.getId());

            totalPrice.setText("Total: " + current_order.getTotalPrice() + "€");
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }


}
