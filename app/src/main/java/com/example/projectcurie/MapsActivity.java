package com.example.projectcurie;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.shape.MarkerEdgeTreatment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLngOrBuilder;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This class implements an activity for implementing Google Maps for Geolocation purposes.
 * Markers are representative of trial locations and their associated experiments.
 *
 * Tutorial for basic implementation of Google Maps API FROM :https://developers.google.com/maps/documentation/android-sdk/config
 *
 * @author Kevin Zhu
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DatabaseListener{

    private GoogleMap mMap;
    private final ArrayList<Trial> trials = new ArrayList<>();
    private TrialFactory trialFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* Grab Experiment From Intent */
        Experiment experiment = (Experiment) getIntent().getSerializableExtra("experiment");
        trialFactory = new TrialFactory(experiment);

        /* Grab Trials For This Experiment */
        DatabaseController.getInstance().fetchTrials(experiment, this, 0);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void notifyDataChanged(QuerySnapshot data, int returnCode) {
        /* Get Trials */
        for (DocumentSnapshot document : data) {
            trials.add(trialFactory.getTrial(document));
        }

        /* Iterate over Trials to create markers */
        if (trials.size() > 0) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for(Trial trial: trials){
                double latitude = trial.getLatitude();
                double longitude = trial.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                boundsBuilder.include(latLng);
                mMap.addMarker(new MarkerOptions().position(latLng).title(trial.getAuthor()));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 15));
        }
    }
}