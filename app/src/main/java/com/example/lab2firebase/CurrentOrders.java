package com.example.lab2firebase;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CurrentOrders extends AppCompatActivity implements OrdersFragment.OrdersFragmentListener {
    ListView orderListView;
    Order order1, order2, order3, order4;
    ItemOrdered item1, item2, item3, item4, item5, item6, item7, item8;
    ArrayList<ItemOrdered> list1, list2, list3, list4;

    ArrayList<Order> totalorders;

    private OrdersFragment fragment1;
    private DetailedItemFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();

        totalorders = new ArrayList<>();

        item1 = new ItemOrdered("Muffin", 3.60, 3);
        item2 = new ItemOrdered("Hamburger", 5.45, 1);
        item3 = new ItemOrdered("Coke", 2.30, 2);
        item4 = new ItemOrdered("Cheeseburger", 6.50, 1);
        item5 = new ItemOrdered("French fries", 3.30, 2);
        item6 = new ItemOrdered("Ketchup", 0.25, 4);
        item7 = new ItemOrdered("Big Mac", 7.35, 1);
        item8 = new ItemOrdered("Ice cream", 4.20, 3);

        list1.addAll(Arrays.asList(item1, item3, item7));
        list2.addAll(Arrays.asList(item2, item4, item1, item8));
        list3.addAll(Arrays.asList(item5, item2, item4, item6, item7));
        list4.addAll(Arrays.asList(item7, item5));

        order1 = new Order(12345, "John", "Add salt please", list1);
        order2 = new Order(52949, "Paul", list2);
        order3 = new Order(28474, "Johanna","A lot of sauce", list3);
        order4 = new Order(84762, "Hermann", list4);

        totalorders.addAll(Arrays.asList(order1,order2,order3,order4));

        /****** WE NOW CREATE THE VIEW DEPENDING ON THE DEVICE AND ORIENTATION *****/

        fragment1 = new OrdersFragment();
        fragment2 = new DetailedItemFragment();

        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_current_orders);
            if (findViewById(R.id.fragment_container)!= null){
                if (savedInstanceState != null){
                    return;
                }
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.orderTitle_fragment, fragment1)
                    .replace(R.id.orderDetail_fragment, fragment2)
                    .commit();*/
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment1)
                        .commit();
            }
        }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_current_orders_landscape);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.orderTitle_fragment, fragment1)
                    .replace(R.id.orderDetail_fragment, fragment2)
                    .commit();
        }




    }

    @Override
    public void onOrderClicked(Object detailed_order) {
        DetailedItemFragment f2 = (DetailedItemFragment) getSupportFragmentManager().findFragmentById(R.id.orderDetail_fragment);

        if(f2 != null && f2.isVisible()){
            fragment2.updateView((Order) detailed_order);
            getSupportFragmentManager().beginTransaction().replace(R.id.orderDetail_fragment, fragment2)
                    .commit();
        }else{
            //fragment2.updateView((Order) detailed_order);
            Intent intent_act2 = new Intent(getApplicationContext(), DetailedItemActivity.class);
            intent_act2.putExtra("Order_clicked", (Order) detailed_order);
            startActivity(intent_act2);
        }



    }

    public ArrayList<Order> getOrders(){
        return totalorders;
    }

}
