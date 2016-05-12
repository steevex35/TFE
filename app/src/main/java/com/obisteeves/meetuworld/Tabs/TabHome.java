package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.HomePage;
import com.obisteeves.meetuworld.PageAndroid.InfoVoyage;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;
import com.obisteeves.meetuworld.Utils.Voyage;
import com.obisteeves.meetuworld.Utils.ListViewPersoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Fragment qui regroupe tous les voyages contenu en base de données et qui sont encore valide
 *
 */
public class TabHome extends Fragment implements Observer{

    User userCurrent;
    Voyage voyageUser;
    ImageView avatarCompte;

    String idVoyage, idAuteur;
    private ArrayList<HashMap<String, String>> listHashVoyage = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> listPoi = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_tab_home,container,false);
        HomePage homeActivity = (HomePage) getActivity();
        userCurrent = homeActivity.getUser();
        avatarCompte = (ImageView) v.findViewById(R.id.avatarComptePersoRow);

        listViewVoyage();
        afficheVoyage(idVoyage);

        return v;
    }

    public  void listViewVoyage(){

        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.listVoyages);
        net.setUrl(address);
        net.send();
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


    @Override
    public void update(Observable observable, final Object data) {

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        String netReq2 = String.valueOf(NetworkRequestAdapter.OKlistPoi);

        if (data.toString().equals(netReq))
        {
            try
            {
                JSONArray voyages =  resultat.getResult().getJSONArray("voyages");

                for (int i = 0; i < voyages.length(); i++)
                {
                    HashMap<String,String> listViewMap = new HashMap<String, String>();
                    JSONObject json = voyages.getJSONObject(i);
                    String nom = json.getString("nom");
                    String prenom= json.getString("prenom");
                    String pays=json.getString("pays");
                    String ville=json.getString("ville");
                    String dateA=json.getString("date_arrivee");
                    String dateD=json.getString("date_depart");
                    idVoyage = json.getString("id");
                    idAuteur = json.getString("id_auteur");

                    listViewMap.put("idVoyage", idVoyage);
                    listViewMap.put("idAuteur", idAuteur);
                    listViewMap.put("avatar", idAuteur);
                    listViewMap.put("nom",nom+" "+prenom);
                    listViewMap.put("pays",pays);
                    listViewMap.put("ville",ville);
                    listViewMap.put("date_arrivee",dateA);
                    listViewMap.put("date_depart", dateD);
                    listHashVoyage.add(listViewMap);

                }

                final ListView voyagesListView = (ListView) getActivity().findViewById(R.id.voyageListViewHome);
                ListViewPersoAdapter voyagesAdapter = new ListViewPersoAdapter(getActivity(), R.layout.listview_persorow, listHashVoyage, getActivity());
                voyagesListView.setAdapter(voyagesAdapter);

                voyagesListView.setClickable(true);

                voyagesListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView Id = (TextView) view.findViewById(R.id.idVoyage);

                                TextView idAuteur = (TextView) view.findViewById(R.id.idAuteur);
                                TextView Nom = (TextView) view.findViewById(R.id.nomUser);
                                TextView Pays = (TextView) view.findViewById(R.id.paysHome);
                                TextView Ville= (TextView) view.findViewById(R.id.villeHome);
                                TextView dateA=(TextView) view.findViewById(R.id.dateArrivee);
                                TextView dateD=(TextView) view.findViewById(R.id.dateDepart);


                                String idVoyageToSend=Id.getText().toString();
                                String idAuteurToSend = idAuteur.getText().toString();
                                String nomUserToSend=Nom.getText().toString();
                                String paysUserTosend=Pays.getText().toString();
                                String villeUserTosend=Ville.getText().toString();
                                String dateATosend=dateA.getText().toString();
                                String dateDTosend=dateD.getText().toString();


                                voyageUser = new Voyage(idVoyageToSend, idAuteurToSend, nomUserToSend, paysUserTosend, villeUserTosend, dateATosend, dateDTosend, listPoi);
                                Intent intent = new Intent(getActivity(), InfoVoyage.class);
                                intent.putExtra("userCurrent", userCurrent.getmId());
                                intent.putExtra("id_voyage", idVoyageToSend);
                                intent.putExtra("id_auteur", idAuteurToSend);
                                intent.putExtra("nom_user", nomUserToSend);
                                intent.putExtra("pays_user", paysUserTosend);
                                intent.putExtra("ville_user", villeUserTosend);
                                intent.putExtra("dateA",dateATosend);
                                intent.putExtra("dateD",dateDTosend);
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
        if (data.toString().equals(netReq2)) {
            try {
                JSONArray poi = resultat.getResult().getJSONArray("poi");
                for (int i = 0; i < poi.length(); i++) {
                    HashMap<String, String> listViewMap = new HashMap<String, String>();
                    JSONObject json = poi.getJSONObject(i);
                    String nomPoi = json.getString("nom");
                    String idPoi = json.getString("id");
                    String guide = json.getString("guide");
                    listPoi.add(nomPoi);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}