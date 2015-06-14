package com.obisteeves.meetuworld.Tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.HomePage;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.ListViewTabGuide_Adapter;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class TabGuide extends Fragment implements Observer {

    TextView idPoi, idVoyage;
    private User user;
    private int[] field_R_id = {R.id.idGuide, R.id.nomPoiGuide, R.id.villeGuide, R.id.datePassage, R.id.nomAuteurVoyage, R.id.idVoyageGuide};
    private ArrayList<HashMap<String, String>> listHashGuide = new ArrayList<HashMap<String, String>>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab_guide, container, false);
        HomePage homeActivity = (HomePage) getActivity();
        user = homeActivity.getUser();
        listViewGuide();
        return v;
    }

    public void listViewGuide() {

        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.listGuideUser);
        net.setUrl(address);
        net.send();
    }

    @Override
    public void update(Observable observable, Object data) {

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq)) {
            try {
                JSONArray guides = resultat.getResult().getJSONArray("guides");
                for (int i = 0; i < guides.length(); i++) {
                    HashMap<String, String> listViewMap = new HashMap<String, String>();
                    JSONObject json = guides.getJSONObject(i);
                    String id = json.getString("id");
                    String nomPoi = json.getString("nom");
                    String ville = json.getString("ville");
                    String dateP = json.getString("date_passage");
                    String nom = json.getString("nomUser");
                    String prenom = json.getString("prenom");
                    String idVoyage = json.getString("voyages_id");

                    listViewMap.put("id", id);
                    listViewMap.put("nom", nomPoi);
                    listViewMap.put("ville", ville);
                    listViewMap.put("dateP", "Le : " + "jj-mm-aaaa");
                    listViewMap.put("nomUser", "Avec : " + nom + " " + prenom);
                    listViewMap.put("idVoyage", idVoyage);
                    listHashGuide.add(listViewMap);

                }

                ListView guidesListView = (ListView) getActivity().findViewById(R.id.guideListView);
                ListViewTabGuide_Adapter voyagesAdapter = new ListViewTabGuide_Adapter(getActivity(), R.layout.listview_tab_guide, listHashGuide, getActivity());
                guidesListView.setAdapter(voyagesAdapter);

                guidesListView.setClickable(true);
                guidesListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                idPoi = (TextView) view.findViewById(R.id.idGuide);

                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                dialog.cancel();
                                                break;
                                            case DialogInterface.BUTTON_POSITIVE:

                                                String id = idPoi.getText().toString();
                                                System.out.println(id);
                                                deleteGuide(id);
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("annulé la visite du point d'intérêt ?")
                                        .setTitle("Info")
                                        .setNegativeButton("Non", dialogClickListener)
                                        .setPositiveButton("Oui", dialogClickListener)
                                        .show();

                            }
                        }
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void deleteGuide(String id_poi) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.deleteGuide);
        net.setUrl(address);
        net.addParam("id_poi", id_poi);
        net.send();
    }

}
