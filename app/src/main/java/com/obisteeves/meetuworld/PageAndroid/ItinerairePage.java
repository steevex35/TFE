package com.obisteeves.meetuworld.PageAndroid;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.obisteeves.meetuworld.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Activity de test pour la google Maps
 */
public class ItinerairePage extends ActionBarActivity {

    private Toolbar toolbar;
    private GoogleMap googleMap;
    private String ville;

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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#ffffff'>Itinéraire</font></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        toolbar.setLogo(R.drawable.ic_logo);
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
        String Poi1 = "Tour Eiffel";
        String Poi2 = "Pigalle";
        String Poi3 = "Place de clichy";
        try {
            List<Address> add = geoCoder.getFromLocationName(Poi3, 5);
            Double lat = (double) (add.get(0).getLatitude());
            Double lon = (double) (add.get(0).getLongitude());

            List<Address> add2 = geoCoder.getFromLocationName(Poi1, 5);
            Double lat1 = (double) (add2.get(0).getLatitude());
            Double lon1 = (double) (add2.get(0).getLongitude());

            List<Address> add3 = geoCoder.getFromLocationName(Poi2, 5);
            Double lat2 = (double) (add3.get(0).getLatitude());
            Double lon2 = (double) (add3.get(0).getLongitude());

            /** Make sure that the map has been initialised **/
            if(null != googleMap) {
                Marker afficheInfo1 = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat2, lon2))
                                .title(Poi2)
                                .snippet("")
                                .draggable(true)

                );
                Marker afficheInfo = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title(Poi3)
                                .snippet("")
                                .draggable(true)

                );
                Marker affichePOi = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat1, lon1))
                                .snippet("Celien Nanson")
                                .title(Poi1)
                                .draggable(true)


                );
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(afficheInfo.getPosition(), 15));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(affichePOi.getPosition(), 15));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(afficheInfo1.getPosition(), 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(30), 2000, null);


                afficheInfo.showInfoWindow();
                afficheInfo1.showInfoWindow();
                affichePOi.showInfoWindow();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        }
    }





