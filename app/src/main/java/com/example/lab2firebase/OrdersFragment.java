package com.example.lab2firebase;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    @Nullable

    ListView orderListView;
    //private ViewGroup container;
    //private LayoutInflater inflater;
    private OrdersFragmentListener listener;
    ArrayList<Order> current_orders;


    public interface OrdersFragmentListener {
        void onOrderClicked(Object detailed_order);
    }

    /*public View initializeUserInterface(){
        View view;

        if(container != null){
            container.removeAllViewsInLayout();
        }

        int orientation = getActivity().getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            view = inflater.inflate(R.layout.fragment1layout, container, false);
        }else{

        }
    }*/


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ordersfragment_layout, container, false);


        orderListView = (ListView) v.findViewById(R.id.orderListView);



        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CurrentOrders mainActivity = (CurrentOrders) getActivity();
        current_orders = (ArrayList<Order>) mainActivity.getOrders();

        OrderAdapter itemAdapter = new OrderAdapter(getActivity(), current_orders);
        orderListView.setAdapter(itemAdapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order orderClicked = (Order) parent.getItemAtPosition(position);
                listener.onOrderClicked(orderClicked);
                Toast.makeText(getActivity(), "Order n°"+orderClicked.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OrdersFragmentListener){
            listener = (OrdersFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString() + "must implement Fragment1Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
