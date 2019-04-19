package com.example.lab2firebase;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailedItemActivity extends AppCompatActivity implements Serializable{
    ListView itemsListView;
    TextView orderNumber;
    TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);

        itemsListView = (ListView) findViewById(R.id.itemsListView);
        orderNumber = (TextView) findViewById(R.id.orderNb_textView);
        totalPrice = (TextView) findViewById(R.id.totalPrice_textView);

        Intent myIntent = getIntent();
        Order current_order = (Order) myIntent.getSerializableExtra("Order_clicked");

        ArrayList<ItemOrdered> items = current_order.getAllItems();
        DetailedItemAdapter detailedItemAdapter = new DetailedItemAdapter(getApplicationContext(), items);
        itemsListView.setAdapter(detailedItemAdapter);

        orderNumber.setText("Order n°" + current_order.getId());

        totalPrice.setText("Total: " + current_order.getTotalPrice() + "€");

            /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.orderDetail_fragment, frag2)
                .commit();

        frag2.updateView((Order) current_order);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.orderDetail_fragment, frag2)
                .commit();*/


    }
}
