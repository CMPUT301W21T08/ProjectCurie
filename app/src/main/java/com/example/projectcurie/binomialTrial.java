package com.example.projectcurie;

import androidx.annotation.NonNull;

import com.google.type.LatLng;

class BinomialTrial extends Trial{

    private Boolean Success;
    public BinomialTrial() { }

    public BinomialTrial(String experiment, String author, Boolean success) {
        this.Success = success;
    }
    public BinomialTrial(String experiment, String author, Boolean success, LatLng location){
        this.Success = success;
    }
    public Boolean getSuccess() {
        return Success;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(Success);
    }

}
