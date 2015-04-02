package com.obisteeves.meetuworld.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;

/**
 * Created by Steeves on 02-04-15.
 */
public class listViewPersoAdapter extends ArrayAdapter<String> {

    public listViewPersoAdapter(Context context, String[] voyages) {
        super(context, R.layout.listview_persorow,voyages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater voyagesInflater = LayoutInflater.from(getContext());
        View listviewPerso = voyagesInflater.inflate(R.layout.listview_persorow,parent,false);

        String voyagesItem = getItem(position);
        TextView nomCompte = (TextView) listviewPerso.findViewById(R.id.nomUser);
        TextView pays = (TextView) listviewPerso.findViewById(R.id.paysHome);
        TextView ville = (TextView) listviewPerso.findViewById(R.id.villeHome);
        ImageView avatarCompte= (ImageView) listviewPerso.findViewById(R.id.avatarCompte);

        Typeface typeFace= Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");


        nomCompte.setText(voyagesItem);
        nomCompte.setTypeface(typeFace);
        pays.setText(voyagesItem);
        ville.setText(voyagesItem);
        avatarCompte.setImageResource(R.drawable.ic_account_box_grey600_48dp);
        return listviewPerso;


    }
}

