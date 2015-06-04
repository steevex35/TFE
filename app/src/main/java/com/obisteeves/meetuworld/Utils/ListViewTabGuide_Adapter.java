package com.obisteeves.meetuworld.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;

/**
 * Created by Steeves on 04-06-15.
 */
public class ListViewTabGuide_Adapter extends ArrayAdapter<String> {

    public ListViewTabGuide_Adapter(Context context, String[] guides) {
        super(context, R.layout.listview_tab_guide, guides);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater voyagesInflater = LayoutInflater.from(getContext());
        View listviewPerso = voyagesInflater.inflate(R.layout.listview_tab_guide, parent, false);

        String voyagesItem = getItem(position);
        TextView id = (TextView) listviewPerso.findViewById(R.id.idGuide);
        TextView nomPoi = (TextView) listviewPerso.findViewById(R.id.nomPoi);
        TextView ville = (TextView) listviewPerso.findViewById(R.id.villeGuide);
        TextView dateP = (TextView) listviewPerso.findViewById(R.id.datePassage);
        TextView nom = (TextView) listviewPerso.findViewById(R.id.nomAuteurVoyage);
        TextView prenom = (TextView) listviewPerso.findViewById(R.id.prenomAuteurVoyage);
        TextView idVoyage = (TextView) listviewPerso.findViewById(R.id.idVoyageGuide);


        id.setText(voyagesItem);
        nom.setText(voyagesItem);
        prenom.setText(voyagesItem);
        dateP.setText(voyagesItem);
        nomPoi.setText(voyagesItem);
        ville.setText(voyagesItem);
        idVoyage.setText(voyagesItem);
        return listviewPerso;

    }
}
