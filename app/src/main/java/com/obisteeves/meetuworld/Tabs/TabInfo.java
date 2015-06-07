package com.obisteeves.meetuworld.Tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.infoVoyage;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


public class TabInfo extends Fragment implements Observer {

    private TextView nomUser, idItemPoi, nomPoi;
    private ImageView img;
    private String id_voyage, id_current, id_auteur, nom, pays, ville, dateA, dateD;
    private String[] fields = {"nom", "id", "guide"};
    private int[] field_R_id = {R.id.nomPoi, R.id.idPoi, R.id.guide};
    private ArrayList<HashMap<String, String>> listHashPoi = new ArrayList<HashMap<String, String>>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_info, container, false);
        infoVoyage infoVoyage = (infoVoyage) getActivity();
        img = (ImageView) v.findViewById(R.id.avatarCompte);
        nomUser = (TextView) v.findViewById(R.id.nomUser);
        img.setVisibility(View.GONE);
        nomUser.setVisibility(View.GONE);

        id_voyage = infoVoyage.getId_voyage();
        id_auteur = infoVoyage.getId_auteur();
        id_current = infoVoyage.getId_current();
        nom = infoVoyage.getNom();
        pays = infoVoyage.getPays();
        ville = infoVoyage.getVille();
        dateA = infoVoyage.getDateA();
        dateD = infoVoyage.getDateD();
        ((TextView) v.findViewById(R.id.nomUser)).setText(nom);
        ((TextView) v.findViewById(R.id.paysInfo)).setText(pays);
        ((TextView) v.findViewById(R.id.villeinfo)).setText(ville);
        ((TextView) v.findViewById(R.id.dateArriveInfo)).setText(dateA);
        ((TextView) v.findViewById(R.id.dateDepartInfo)).setText(dateD);
        afficheVoyage(id_voyage);


        if (!id_current.equals(id_auteur)) {
            img.setVisibility(View.VISIBLE);
            nomUser.setVisibility(View.VISIBLE);
        }

        return v;

    }

    private void afficheVoyage(String id_voyage) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.afficheVoyages);
        net.setUrl(address);
        net.addParam("id_voyage", id_voyage);
        net.send();

    }

    private void DevenirGuide(String id_poi, String id_current, String id_auteur) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.devenirGuide);
        net.setUrl(address);
        net.addParam("id_poi", id_poi);
        net.addParam("id_current", id_current);
        net.addParam("id_auteur", id_auteur);
        net.send();
    }


    @Override
    public void update(Observable observable, Object data) {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OKlistPoi);
        String netReq2 = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq2)) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //listHashPoi=null;
                            //afficheVoyage(id_voyage);
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage("Merci d'avoir choisi d'être mon guide pour ce lieu")
                    .setTitle("Info")
                    .setPositiveButton("Retour", dialogClickListener)
                    .show();

        }


        if (data.toString().equals(netReq)) {
            try {
                JSONArray poi = resultat.getResult().getJSONArray("poi");
                for (int i = 0; i < poi.length(); i++) {
                    HashMap<String, String> listViewMap = new HashMap<String, String>();
                    JSONObject json = poi.getJSONObject(i);
                    String nomPoi = json.getString("nom");
                    String idPoi = json.getString("id");
                    String guide = json.getString("guide");
                    id_current = json.getString("id_current");
                    id_auteur = json.getString("id_auteur");
                    listViewMap.put("nom", nomPoi);
                    listViewMap.put("id", idPoi);
                    listViewMap.put("guide", guide);
                    listHashPoi.add(listViewMap);
                }

                ListView poiListView = (ListView) getActivity().findViewById(R.id.listViewPoi);
                SimpleAdapter poiAdapter = new SimpleAdapter(getActivity(), listHashPoi, R.layout.listview_poi, fields, field_R_id);
                poiListView.setAdapter(poiAdapter);

                poiListView.setClickable(true);

                if (!id_current.equals(id_auteur)) {
                    poiListView.setOnItemClickListener(

                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    idItemPoi = (TextView) view.findViewById(R.id.idPoi);
                                    nomPoi = (TextView) view.findViewById(R.id.nomPoi);
                                    String nom = nomPoi.getText().toString();
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.cancel();
                                                    break;
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    //Yes button clicked
                                                    String id = idItemPoi.getText().toString();
                                                    DevenirGuide(id, id_current, id_auteur);
                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("Devenir l'accompagnateur pour " + nom + " ? ")
                                            .setTitle("Info")
                                            .setNegativeButton("Non", dialogClickListener)
                                            .setPositiveButton("Oui", dialogClickListener)
                                            .show();
                                }
                            }
                    );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
