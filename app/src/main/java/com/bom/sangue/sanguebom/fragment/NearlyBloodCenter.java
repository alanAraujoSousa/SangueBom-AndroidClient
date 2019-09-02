package com.bom.sangue.sanguebom.fragment;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bom.sangue.sanguebom.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by alan on 29/11/15.
 */
public class NearlyBloodCenter extends Fragment {

    View rootView;
    GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map, container, false);
        setUp();
        return rootView;
    }

    private void setUp() {
        if (mMap == null) {

            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_frag))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            LatLng position = new LatLng(-8.162124, -34.916616);
            mMap.addMarker(new MarkerOptions().position(position)).setTitle("Você está aqui!");

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

            if (mMap != null) {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location arg0) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    }
                });

            }
        }
    }
}
