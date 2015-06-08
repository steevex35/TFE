package com.obisteeves.meetuworld.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.infoVoyage;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


public class TabInfo extends Fragment implements Observer {

    infoVoyage infoVoyage;
    private TextView nomUser, idItemPoi, nomPoi;
    private ImageView img;
    private String id_voyage, id_current, id_auteur, nom, pays, ville, dateA, dateD;
    private String[] fields = {"nom", "id", "guide"};
    private int[] field_R_id = {R.id.nomPoi, R.id.idPoi, R.id.guide};
    private ArrayList<HashMap<String, String>> listHashPoi = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> tabNomPoi = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_info, container, false);
        infoVoyage = (infoVoyage) getActivity();
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
        infoVoyage.afficheVoyage(id_voyage);


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

    }

    public ArrayList<String> getTabNomPoi() {
        return tabNomPoi;
    }

    public void setTabNomPoi(ArrayList<String> tabNomPoi) {
        this.tabNomPoi = tabNomPoi;
    }
}
