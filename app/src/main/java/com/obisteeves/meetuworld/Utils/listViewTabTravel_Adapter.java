package com.obisteeves.meetuworld.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Steeves on 25-04-15.
 */
public class listViewTabTravel_Adapter extends ArrayAdapter {
    private Context mContext;
    private int id;
    private ArrayList<HashMap<String, String>> items;
    private Activity activity;
    private FragmentActivity myContext;

    public listViewTabTravel_Adapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> list, FragmentActivity c) {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
        myContext = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View mView = convertView;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface typefaceSecond = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface typefaceSecond2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");


        TextView idVoyage = (TextView) mView.findViewById(R.id.idVoyageTravel);
        TextView idAuteur = (TextView) mView.findViewById(R.id.idAuteurTravel);
        TextView pays = (TextView) mView.findViewById(R.id.paysTravel);
        TextView ville = (TextView) mView.findViewById(R.id.villeTravel);
        TextView dateA = (TextView) mView.findViewById(R.id.dateArriveeTravel);
        TextView dateD = (TextView) mView.findViewById(R.id.dateDepartTravel);
        TextView jRestant = (TextView) mView.findViewById(R.id.jourRestant);


        idVoyage.setText(items.get(position).get("idVoyage"));
        idAuteur.setText(items.get(position).get("idAuteur"));
        pays.setText(items.get(position).get("pays"));
        pays.setTypeface(typeFace);
        ville.setText(items.get(position).get("ville"));
        ville.setTypeface(typefaceSecond);
        dateA.setText(items.get(position).get("date_arrivee"));
        dateD.setText(items.get(position).get("date_depart"));
        jRestant.setText(items.get(position).get("jRestant"));
        jRestant.setTypeface(typefaceSecond2);

        return mView;

    }

}