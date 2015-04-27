package com.obisteeves.meetuworld.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;

/**
 * Created by Steeves on 27-04-15.
 */
public class listViewPersoPoi extends ArrayAdapter<String> {

    public listViewPersoPoi(Context context, String[] voyages) {
        super(context, R.layout.listview_poi, voyages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater voyagesInflater = LayoutInflater.from(getContext());
        View listviewPerso = voyagesInflater.inflate(R.layout.listview_poi, parent, false);

        String voyagesItem = getItem(position);
        TextView nom = (TextView) listviewPerso.findViewById(R.id.nomPoi);
        TextView id = (TextView) listviewPerso.findViewById(R.id.idPoi);

        nom.setText(voyagesItem);
        id.setText(voyagesItem);
        return listviewPerso;

    }
}
