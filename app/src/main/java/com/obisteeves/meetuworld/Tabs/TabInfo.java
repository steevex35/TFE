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


public class TabInfo extends Fragment {

    infoVoyage infoVoyage;
    private TextView nomUser;
    private ImageView img;
    private String id_voyage, id_current, id_auteur, nom, pays, ville, dateA, dateD;

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
        //infoVoyage.afficheVoyage(id_voyage);


        if (!id_current.equals(id_auteur)) {
            img.setVisibility(View.VISIBLE);
            nomUser.setVisibility(View.VISIBLE);
        }

        return v;

    }



}
