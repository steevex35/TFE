package com.obisteeves.meetuworld.PageAndroid;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.obisteeves.meetuworld.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ItinerairePage extends ActionBarActivity {

    Toolbar toolbar;
    GoogleMap googleMap;
    String ville;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itineraire_page);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ville = extras.getString("ville");

        }



        createMapView();
        addMarker();

    }

    private void iniActionBar(){
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Itinéraire");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFAB00"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01579B")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_itineraire_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createMapView(){
        iniActionBar();
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();
               googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);




                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }


    }

    private void addMarker(){

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String adresse = ville;
        try {
            List<Address> add= geoCoder.getFromLocationName(adresse,5);
            Double lat = (double) (add.get(0).getLatitude());
            Double lon = (double) (add.get(0).getLongitude());

            /** Make sure that the map has been initialised **/
            if(null != googleMap) {
                Marker afficheInfo = googleMap.addMarker(new MarkerOptions()

                                .position(new LatLng(lat, lon))
                                .title("POi")
                                .snippet("Pour le fun bro")
                                .draggable(true)


                );
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(afficheInfo.getPosition(), 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


                afficheInfo.showInfoWindow();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        }
    }





