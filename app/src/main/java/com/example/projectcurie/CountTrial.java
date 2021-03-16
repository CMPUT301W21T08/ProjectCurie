package com.example.projectcurie;

import androidx.annotation.NonNull;

import com.google.type.LatLng;

import java.util.Date;

class CountTrial extends Trial {

    private int Count;
    public CountTrial() { }

    public int getCount() {
        return Count;
    }

    public CountTrial(String experiment, String author, Integer count) {
        this.Count = count;
    }
    public CountTrial(String experiment, String author, Integer count, LatLng location){
        this.Count = count;
    }
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(Count);
    }
}