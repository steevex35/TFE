package com.obisteeves.meetuworld.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;

import org.w3c.dom.Text;


/**
 * Created by Steeves on 25-04-15.
 */
public class listViewTabTravel_Adapter extends ArrayAdapter<String> {

    public listViewTabTravel_Adapter(Context context, String[] voyages) {
        super(context, R.layout.listview_tab_tavel, voyages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater voyagesInflater = LayoutInflater.from(getContext());
        View listviewPerso = voyagesInflater.inflate(R.layout.listview_tab_tavel, parent, false);

        String voyagesItem = getItem(position);
        TextView pays = (TextView) listviewPerso.findViewById(R.id.paysTravel);
        TextView ville = (TextView) listviewPerso.findViewById(R.id.villeTravel);
        TextView dateA = (TextView) listviewPerso.findViewById(R.id.dateArriveeTravel);
        TextView dateD = (TextView) listviewPerso.findViewById(R.id.dateDepartTravel);
        TextView jRestant= (TextView) listviewPerso.findViewById(R.id.jourRestant);


        pays.setText(voyagesItem);
        ville.setText(voyagesItem);
        dateA.setText(voyagesItem);
        dateD.setText(voyagesItem);
        jRestant.setText(voyagesItem);
        return listviewPerso;

    }

}