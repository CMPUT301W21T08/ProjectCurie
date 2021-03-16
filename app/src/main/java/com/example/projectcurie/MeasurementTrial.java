package com.example.projectcurie;

import androidx.annotation.NonNull;

import com.google.type.LatLng;

class MeasurementTrial extends Trial{
    public MeasurementTrial() { }

    private double Measurement;

    public MeasurementTrial(String experiment, String author, Double measurement) {
        this.Measurement = measurement;
    }
    public MeasurementTrial(String experiment, String author, Double measurement, LatLng location){
        this.Measurement = measurement;
    }
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(Measurement);
    }}

