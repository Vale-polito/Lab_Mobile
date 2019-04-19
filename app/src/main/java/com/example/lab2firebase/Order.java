package com.example.lab2firebase;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private int id;
    private int nbItem;
    private double totalPrice;
    private String customer;
    private String comments;
    private ArrayList<ItemOrdered> items;

    public Order(int id, String customer, String comments, ArrayList<ItemOrdered> items){
        this.id = id;
        this.comments = comments;
        this.customer = customer;
        this.items = items;
    }

    public Order(int id, String customer, ArrayList<ItemOrdered> items){
        this.id = id;
        this.comments = "";
        this.customer = customer;
        this.items = items;
    }

    public int getId(){
        return id;
    }

    public double getTotalPrice(){
        totalPrice = 0.0;
        int i;
        for (i=0; i<items.size();i++){
            totalPrice += (items.get(i).getPrice())*(items.get(i).getQuantity());
        }

        totalPrice = (double)(Math.round(totalPrice*100)) / 100;
        return totalPrice;
    }

    public int getNbItem(){
        nbItem = 0;
        int i;
        for (i=0; i<items.size();i++){
            nbItem += items.get(i).getQuantity();
        }
        return nbItem;
    }

    public String getCustomer(){
        return customer;
    }
    public String getComments(){
        return comments;
    }

    public ItemOrdered getItem(int position){
        return items.get(position);
    }

    public ArrayList<ItemOrdered> getAllItems(){
        return items;
    }



}
