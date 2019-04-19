package com.example.lab2firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFoods;
    private List<DailyOffer> DailyFoods = new ArrayList<>();

    public interface DataStatus{
      void  DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys);
      void DataIsInserted();
      void DataIsUpdated();
      void DataIsDeleted();
    }

    public  FirebaseDatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFoods= mDatabase.getReference("DailyFoods");
    }
    public void readFoods (final DataStatus dataStatus){
        mReferenceFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DailyFoods.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    DailyOffer dailyOffer=keyNode.getValue(DailyOffer.class);
                    DailyFoods.add(dailyOffer);
                }
                dataStatus.DataIsLoaded(DailyFoods,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void addFoods(DailyOffer dailyOffer,final DataStatus dataStatus){
        String key = mReferenceFoods.push().getKey();
        mReferenceFoods.child(key).setValue(dailyOffer)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                  public void onSuccess( Void aVoid){
                        dataStatus.DataIsInserted();
                    }
                });
    }
    public void updateFoods(String key, DailyOffer dailyOffer,final DataStatus dataStatus){
        mReferenceFoods.child(key).setValue(dailyOffer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }
    public void deleteFoods(String key,final DataStatus dataStatus){
        mReferenceFoods.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }
}
