package com.example.projectcurie;

import androidx.annotation.NonNull;

import com.google.type.LatLng;

class intCountTrial extends Trial{

    private int Count;
    public intCountTrial() { }

    public intCountTrial(String experiment,String author,Integer count){
        this.Count = count;
    }
    public intCountTrial(String experiment, String author, Integer count, LatLng location){
        this.Count = count;
    }

    public int getCount() {
        return Count;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(Count);
    }


}