package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.HomePage;
import com.obisteeves.meetuworld.PageAndroid.addTravel;
import com.obisteeves.meetuworld.PageAndroid.infoVoyage;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;
import com.obisteeves.meetuworld.Utils.Voyage;
import com.obisteeves.meetuworld.Utils.listViewTabTravel_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabTravel extends Fragment implements Observer{

    User userCurrent;
    Voyage voyageUser;
    ImageButton FAB;
    String idVoyage, idAuteur;

    private ArrayList<HashMap<String, String>> listHashVoyage =  new ArrayList<HashMap<String, String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab_travel, container, false);
        HomePage homeActivity = (HomePage) getActivity();
        userCurrent = homeActivity.getUser();
        ajouterVoyage(v);
        listViewVoyage();
        return v;
    }

    /**
     * Bouton + qui permet d'ajouter des voyages
     * @param v
     * @type View
     */

    public void ajouterVoyage(View v){
        FAB= (ImageButton) v.findViewById(R.id.buttonAdd);
        FAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), addTravel.class);
                intent.putExtra("user", userCurrent);
                startActivity(intent);
            }
        });

    }

    /**
     * listViewVoyage qui permet d'afficher les voyages de l'utilisateur dans une ListView
     *
     */

    public  void listViewVoyage(){

        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.listVoyageUser);
        net.setUrl(address);
        net.send();
    }


    @Override
    public void update(Observable observable, Object data) {

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq))
        {
            try
            {
                JSONArray voyages =  resultat.getResult().getJSONArray("voyages");
                for (int i = 0; i < voyages.length(); i++)
                {
                    HashMap<String,String> listViewMap = new HashMap<String, String>();
                    JSONObject json = voyages.getJSONObject(i);
                    String pays = json.getString("pays");
                    String ville= json.getString("ville");
                    idVoyage = json.getString("id");
                    idAuteur = json.getString("id_auteur");
                    String dateA=json.getString("date_arrivee");
                    String dateD=json.getString("date_depart");
                    String jRestant=json.getString("joursRestant");
                    listViewMap.put("idVoyage", idVoyage);
                    listViewMap.put("idAuteur", idAuteur);
                    listViewMap.put("pays",pays);
                    listViewMap.put("ville",ville);
                    listViewMap.put("date_arrivee",dateA);
                    listViewMap.put("date_depart",dateD);
                    listViewMap.put("jRestant",jRestant);//mise à jour
                    listHashVoyage.add(listViewMap);

                }

                ListView voyagesListView = (ListView) getActivity().findViewById(R.id.voyageListViewTravel);
                listViewTabTravel_Adapter voyagesAdapter = new listViewTabTravel_Adapter(getActivity(), R.layout.listview_tab_tavel, listHashVoyage, getActivity());
                voyagesListView.setAdapter(voyagesAdapter);

                voyagesListView.setClickable(true);
                voyagesListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView Id = (TextView) view.findViewById(R.id.idVoyageTravel);
                                TextView idAuteur = (TextView) view.findViewById(R.id.idAuteurTravel);
                                TextView Pays = (TextView) view.findViewById(R.id.paysTravel);
                                TextView Ville= (TextView) view.findViewById(R.id.villeTravel);
                                TextView dateA=(TextView) view.findViewById(R.id.dateArriveeTravel);
                                TextView dateD=(TextView) view.findViewById(R.id.dateDepartTravel);


                                String idVoyageToSend=Id.getText().toString();
                                String idAuteurToSend = idAuteur.getText().toString();
                                String paysUserTosend=Pays.getText().toString();
                                String villeUserTosend=Ville.getText().toString();
                                String dateATosend=dateA.getText().toString();
                                String dateDTosend=dateD.getText().toString();


                                voyageUser = new Voyage(idVoyageToSend, idAuteurToSend, paysUserTosend, villeUserTosend, dateATosend, dateDTosend);
                                Intent intent = new Intent(getActivity(), infoVoyage.class);
                                intent.putExtra("id_voyage", idVoyageToSend);
                                intent.putExtra("id_auteur", idAuteurToSend);
                                intent.putExtra("userCurrent", userCurrent.getmId());
                                intent.putExtra("pays_user", paysUserTosend);
                                intent.putExtra("ville_user", villeUserTosend);
                                intent.putExtra("dateA",dateATosend);
                                intent.putExtra("dateD",dateDTosend);
                                intent.putExtra("fullUser", userCurrent);
                                intent.putExtra("parcelable", voyageUser);
                                startActivity(intent);
                            }
                        }
                );

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

    }
}

