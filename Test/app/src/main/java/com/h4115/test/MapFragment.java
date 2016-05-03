package com.h4115.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment {

    protected MapView mMapView;
    protected GoogleMap googleMap;
    protected Location  lastKnownLocation;
    protected ArrayList<MainActivity.Happening> happenings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location_info, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

        happenings = ((MainActivity)getActivity()).getHappeningList();
        for(MainActivity.Happening hpg : happenings){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(hpg.getLatitude(), hpg.getLongitude())).draggable(true));
        }
        return v;
    }

    public GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location currentLocation) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            if(lastKnownLocation == null);
            else if (lastKnownLocation.distanceTo(currentLocation) > 50){
                googleMap.addCircle(new CircleOptions().strokeWidth(0).fillColor(R.color.HalfTransparentBlack).center(currentLatLng).radius(40));
                lastKnownLocation = currentLocation;
            }
        }
    };

    public void fabLocate() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Voulez vous activer la localisation ?").setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            while (googleMap.getMyLocation() == null) {
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), 12));
        }
    }

    public void refreshingFinished(){
        googleMap.clear();
        happenings = ((MainActivity)getActivity()).getHappeningList();
        for(MainActivity.Happening hpg : happenings){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(hpg.getLatitude(), hpg.getLongitude())).draggable(true));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}