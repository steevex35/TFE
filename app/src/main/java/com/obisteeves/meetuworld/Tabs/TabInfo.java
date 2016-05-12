package com.obisteeves.meetuworld.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.InfoVoyage;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.DownloadImageTask;

/**
 * Fragment servant à afficher les information d'un voyage
 */

public class TabInfo extends Fragment {

    private InfoVoyage InfoVoyage;
    private TextView nomUser;
    private ImageView img;
    private String  id_current, id_auteur, nom, pays, ville, dateA, dateD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_info, container, false);
        InfoVoyage = (InfoVoyage) getActivity();
        img = (ImageView) v.findViewById(R.id.avatarCompte);
        nomUser = (TextView) v.findViewById(R.id.nomUser);
        img.setVisibility(View.GONE);
        nomUser.setVisibility(View.GONE);

        id_auteur = InfoVoyage.getId_auteur();
        id_current = InfoVoyage.getId_current();
        nom = InfoVoyage.getNom();
        pays = InfoVoyage.getPays();
        ville = InfoVoyage.getVille();
        dateA = InfoVoyage.getDateA();
        dateD = InfoVoyage.getDateD();
        ((TextView) v.findViewById(R.id.nomUser)).setText(nom);
        ((TextView) v.findViewById(R.id.paysInfo)).setText(pays);
        ((TextView) v.findViewById(R.id.villeinfo)).setText(ville);
        ((TextView) v.findViewById(R.id.dateArriveInfo)).setText(dateA);
        ((TextView) v.findViewById(R.id.dateDepartInfo)).setText(dateD);


        if (!id_current.equals(InfoVoyage.getId_auteur())) {
            img.setVisibility(View.VISIBLE);
            new DownloadImageTask(img).execute("http://www.l4h.be/TFE/android/Outils/avatars/" + id_auteur + ".jpg"); // affiche de l'avatar de l'auteur du voyage
            nomUser.setVisibility(View.VISIBLE);
        }

        return v;

    }
}
