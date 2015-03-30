package com.obisteeves.meetuworld.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabHome extends Fragment implements Observer{

    private HashMap<String,String> listViewMap = new HashMap<String, String>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_tab_home,container,false);

        listViewVoyage();
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


    @Override
    public void update(Observable observable, Object data) {

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq))
        {
            try
            {
                JSONArray voyages =  resultat.getResult().getJSONArray("voyages");
                String[] listvoyages = new String[voyages.length()];
                for (int i = 0; i < voyages.length(); i++)
                {
                    JSONObject json = voyages.getJSONObject(i);
                    String pays = json.getString("prenom");
                    String id = json.getString("id");
                    listViewMap.put(pays,id);
                    listvoyages[i] = pays;
                }

                ListAdapter voyagesAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,listvoyages);
                ListView voyagesListView = (ListView) getActivity().findViewById(R.id.voyageListViewHome);
                voyagesListView.setAdapter(voyagesAdapter);
                voyagesListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dialogPerso("info sur le voyage","Information","retour",getActivity());
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