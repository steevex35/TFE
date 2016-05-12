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
import com.obisteeves.meetuworld.PageAndroid.InfoVoyage;
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

/**
 * Fragment qui permet d'afficher une carte GoogleMaps
 */

public class TabMaps extends Fragment implements Observer {
    GoogleMap googleMap;
    InfoVoyage InfoVoyage;
    private ArrayList<String> tabNomPoi = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_maps, container, false);
        InfoVoyage = (InfoVoyage) getActivity();
        createMapView();
        afficheVoyage(InfoVoyage.getId_voyage());

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


                if (null == googleMap) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Erreur lors de la création de la carte", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    /**
     * fonction qui permet de mettre plusieurs Marker sur la map
     * crée une latitude et une longitude depuis le nom du POI
     */

    private void addMarker() {

        googleMap.clear();
        Double[] latitude = new Double[InfoVoyage.voyageUser.getListPoi().size()];
        Double[] longitude = new Double[InfoVoyage.voyageUser.getListPoi().size()];
        String[] addrs = new String[InfoVoyage.voyageUser.getListPoi().size()];
        addrs = InfoVoyage.voyageUser.getListPoi().toArray(addrs);

        //System.out.println(InfoVoyage.voyageUser.getListPoi().size());
        List<Address> addressList;
        if (addrs != null && addrs.length > 0) {
            for (int i = 0; i < addrs.length; i++) {
                try {
                    Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                    addressList = geoCoder.getFromLocationName(addrs[i], 1);
                    if (addressList == null || addressList.isEmpty() || addressList.equals("")) {
                        addressList = geoCoder.getFromLocationName(InfoVoyage.voyageUser.getListPoi().get(i), 1);
                    }
                    latitude[i] = addressList.get(0).getLatitude();
                    longitude[i] = addressList.get(0).getLongitude();
                    //System.out.println("latitude = " + latitude[i] + " longitude = " + longitude[i]);
                    googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude[i], longitude[i]))
                                    .title(InfoVoyage.voyageUser.getListPoi().get(i))
                                    .snippet(InfoVoyage.voyageUser.getListPoi().get(i))
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
                    tabNomPoi.add(nomPoi);
                }
                InfoVoyage.voyageUser.setListPoi(tabNomPoi);
                //System.out.println(InfoVoyage.voyageUser.getListPoi().toString());
                addMarker();

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
