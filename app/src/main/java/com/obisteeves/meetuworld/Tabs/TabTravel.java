package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.obisteeves.meetuworld.PageAndroid.addTravel;
import com.obisteeves.meetuworld.R;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabTravel extends Fragment {

    ImageButton FAB;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab_travel, container, false);
        ajouterVoyage(v);
        listViewVoyage(v);
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

                Intent intent = new Intent(getActivity(),addTravel.class);
                startActivity(intent);
            }
        });

    }

    /**
     * listViewVoyage qui permet d'afficher les voyages de l'utilisateur dans une ListView
     * @param v
     * @type View
     */

    public  void listViewVoyage(View v){

        String[] voyages={"Gabon","Cameroun","Guinée-équatorial","Espagne"};

        ListAdapter  voyagesAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,voyages);
        ListView voyagesListView = (ListView) v.findViewById(R.id.voyageListView);
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


}

