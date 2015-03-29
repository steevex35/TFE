package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.obisteeves.meetuworld.PageAndroid.addTravel;
import com.obisteeves.meetuworld.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabTravel extends Fragment {

    ImageButton FAB;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab_travel, container, false);

        FAB= (ImageButton) v.findViewById(R.id.buttonAdd);
        FAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               Intent intent = new Intent(getActivity(),addTravel.class);
                startActivity(intent);
            }
        });

        String[] voyages={"Gabon","Cameroun","Guinée-équatorial","Espagne"};
        ListAdapter  voyagesAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,voyages);
        ListView voyagesListView = (ListView) v.findViewById(R.id.voyageListView);
        voyagesListView.setAdapter(voyagesAdapter);


        return v;
    }


}

