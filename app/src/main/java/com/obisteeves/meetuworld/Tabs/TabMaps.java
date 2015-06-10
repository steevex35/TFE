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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.obisteeves.meetuworld.PageAndroid.infoVoyage;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;


public class TabMaps extends Fragment implements Observer {
    GoogleMap googleMap;
    String ville, pays;
    infoVoyage infoVoyage;
    private ArrayList<String> tabNomPoi = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_maps, container, false);
        infoVoyage = (infoVoyage) getActivity();

        //tabNomPoi=infoVoyage.voyageUser.getListPoi();
        //System.out.println(infoVoyage.voyageUser.getListPoi());
        createMapView();
        afficheVoyage(infoVoyage.getId_voyage());
        //addMarker();
        return v;
    }

    public void afficheVoyage(String id_voyage) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.afficheVoyages);
        net.setUrl(address);
        net.addParam("id_voyage", id_voyage);
        net.send();

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

        googleMap.clear();
        Double[] latitude = new Double[infoVoyage.voyageUser.getListPoi().size()];
        Double[] longitude = new Double[infoVoyage.voyageUser.getListPoi().size()];
        String[] addrs = new String[infoVoyage.voyageUser.getListPoi().size()];
        addrs = infoVoyage.voyageUser.getListPoi().toArray(addrs);

        System.out.println(infoVoyage.voyageUser.getListPoi().size());
        List<Address> addressList;
        if (addrs != null && addrs.length > 0) {
            for (int i = 0; i < addrs.length; i++) {
                try {
                    Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                    addressList = geoCoder.getFromLocationName(addrs[i], 1);
                    if (addressList == null || addressList.isEmpty() || addressList.equals("")) {
                        addressList = geoCoder.getFromLocationName(infoVoyage.voyageUser.getListPoi().get(i) + " " + infoVoyage.voyageUser.getVille(), 1);
                    }
                    latitude[i] = addressList.get(0).getLatitude();
                    longitude[i] = addressList.get(0).getLongitude();
                    System.out.println("latitude = " + latitude[i] + " longitude = " + longitude[i]);
                    googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude[i], longitude[i]))
                                    .title(infoVoyage.voyageUser.getListPoi().get(i))
                                    .snippet(infoVoyage.voyageUser.getListPoi().get(i))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .alpha(0.7f)
                    );
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[i], longitude[i]), 15.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void update(Observable observable, Object data) {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OKlistPoi);

        if (data.toString().equals(netReq)) {
            try {
                JSONArray poi = resultat.getResult().getJSONArray("poi");
                for (int i = 0; i < poi.length(); i++) {
                    HashMap<String, String> listViewMap = new HashMap<String, String>();
                    JSONObject json = poi.getJSONObject(i);
                    String nomPoi = json.getString("nom");
                    String idPoi = json.getString("id");
                    String guide = json.getString("guide");
                    //id_current = json.getString("id_current");
                    //id_auteur = json.getString("id_auteur");
                    tabNomPoi.add(nomPoi);

                }
                infoVoyage.voyageUser.setListPoi(tabNomPoi);
                System.out.println(infoVoyage.voyageUser.getListPoi().toString());
                addMarker();

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
