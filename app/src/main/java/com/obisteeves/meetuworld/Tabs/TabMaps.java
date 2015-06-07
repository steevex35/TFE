package com.obisteeves.meetuworld.Tabs;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.obisteeves.meetuworld.PageAndroid.infoVoyage;
import com.obisteeves.meetuworld.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class TabMaps extends Fragment {
    GoogleMap googleMap;
    String ville, pays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_maps, container, false);
        infoVoyage infoVoyage = (infoVoyage) getActivity();
        pays = infoVoyage.getPays();
        ville = infoVoyage.getVille();
        createMapView();
        addMarker();
        return v;
    }

    private void createMapView() {

        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapView)).getMap();
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);


                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (null == googleMap) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }


    }

    private void addMarker() {

        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> add = geoCoder.getFromLocationName(ville, 5);
            Double lat = (double) (add.get(0).getLatitude());
            Double lon = (double) (add.get(0).getLongitude());


            /** Make sure that the map has been initialised **/
            if (null != googleMap) {

                Marker afficheInfo = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title(ville)
                                .snippet("")
                                .draggable(true)

                );

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(afficheInfo.getPosition(), 15));

                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(30), 2000, null);

                afficheInfo.showInfoWindow();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
