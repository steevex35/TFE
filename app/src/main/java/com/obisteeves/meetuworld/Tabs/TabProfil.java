package com.obisteeves.meetuworld.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONException;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabProfil extends Fragment {







    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_tab_profil,container,false);

        changeView(v);

        return v;

    }

    public void afficheProfil(final View v){

        NetworkRequestAdapter net = new NetworkRequestAdapter(getActivity());
        net.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
                if (data.toString().equals("" + NetworkRequestAdapter.OK)) {

                    try {
                        ((TextView) v.findViewById(R.id.profil_Prenom)).setText(resultat.getResult().get("prenom").toString());
                        ((EditText) v.findViewById(R.id.hidden_edit_prenom)).setText(resultat.getResult().get("prenom").toString());
                        ((TextView) v.findViewById(R.id.profil_nom)).setText(resultat.getResult().get("nom").toString());
                        ((EditText) v.findViewById(R.id.hidden_edit_nom)).setText(resultat.getResult().get("nom").toString());
                        ((TextView) v.findViewById(R.id.profil_Email)).setText(resultat.getResult().get("email").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {

                        ((TextView) v.findViewById(R.id.Profil_Ville)).setText(resultat.getResult().get("ville").toString());
                        ((EditText) v.findViewById(R.id.hidden_edit_ville)).setText(resultat.getResult().get("ville").toString());

                        ((TextView) v.findViewById(R.id.profil_pays)).setText(resultat.getResult().get("pays").toString());
                        ((EditText) v.findViewById(R.id.hidden_edit_pays)).setText(resultat.getResult().get("pays").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageProfil);
        net.setUrl(address);
        net.send();


    }






    private void changeView(View v){
        Button boutonAlter =(Button) v.findViewById(R.id.modifier_profil);
        Button boutonSend = (Button) v.findViewById(R.id.envoyer_modifier_profil);

        final EditText fnom = (EditText) v.findViewById(R.id.hidden_edit_nom);
        final EditText fprenom = (EditText) v.findViewById(R.id.hidden_edit_prenom);
        final EditText fville = (EditText) v.findViewById(R.id.hidden_edit_ville);
        final EditText fpays = (EditText) v.findViewById(R.id.hidden_edit_pays);

        final ViewSwitcher switcher1 = (ViewSwitcher) v.findViewById(R.id.my_switcher1);
        final ViewSwitcher switcher2 = (ViewSwitcher) v.findViewById(R.id.my_switcher2);
        final ViewSwitcher switcher3 = (ViewSwitcher) v.findViewById(R.id.my_switcher3);
        final ViewSwitcher switcher4 = (ViewSwitcher) v.findViewById(R.id.my_switcher4);
        final ViewSwitcher switcher5 = (ViewSwitcher) v.findViewById(R.id.my_switcher5);

        boutonAlter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switcher1.showNext();
                switcher2.showNext();
                switcher3.showNext();
                switcher4.showNext();
                switcher5.showNext();
            }
        });


    }
}