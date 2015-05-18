package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.modifierProfil;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONException;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabProfil extends Fragment implements Observer {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_tab_profil,container,false);

        afficheProfil(v);

        ImageButton boutonAlter = (ImageButton) v.findViewById(R.id.modifier_profil);
        boutonAlter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //page de modif du profil
                Intent intent = new Intent(getActivity(), modifierProfil.class);
                startActivity(intent);
            }
        });

        return v;

    }

    public void afficheProfil(View v) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageProfil);
        net.setUrl(address);
        net.send();
    }

    /**
     *
     * @param observable
     * @param data
     */

    public void update(Observable observable, Object data) {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq)) {

           try {
               String nom = resultat.getResult().get("nom").toString();
               String prenom = resultat.getResult().get("prenom").toString();
               String ville = resultat.getResult().get("ville").toString();
               String pays = resultat.getResult().get("pays").toString();
               ((TextView) getActivity().findViewById(R.id.profil_nomPrenom)).setText(prenom + " " + nom);
               ((TextView) getActivity().findViewById(R.id.profil_villePays)).setText(pays + ", " + ville);
               ((TextView) getActivity().findViewById(R.id.profil_Email)).setText(resultat.getResult().get("email").toString());

           } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }








}