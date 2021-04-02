package com.example.projectcurie;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        // Add a marker in Sydney and move the camera
        LatLng jasperAve = new LatLng(53.540967, -113.524325);
        LatLng victoriaPark = new LatLng(53.533516, -113.522431);
        mMap.addMarker(new MarkerOptions().position(jasperAve).title("Count Electric Cars"));
        mMap.addMarker(new MarkerOptions().position(victoriaPark).title("Count Monarch Butterflies"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jasperAve, 14));
    }

    @Override
    public void notifyDataChanged(QuerySnapshot data, int returnCode) {
        /* Get Trials */
        for (DocumentSnapshot document : data) {
            trials.add(trialFactory.getTrial(document));
        }

        /* Confirm That Trials Are Successfully Fetched */
        for (Trial trial : trials) {
            Log.i("Trial Coordinates", String.format(Locale.CANADA, "Lat: %f   Long: %f", trial.getLatitude(), trial.getLongitude()));
        }

    }
}