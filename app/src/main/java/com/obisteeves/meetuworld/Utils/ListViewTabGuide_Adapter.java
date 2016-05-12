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
 * Class servant à personnaliser la ListView du Fragment TabGuide
 */
public class ListViewTabGuide_Adapter extends ArrayAdapter {
    private Context mContext;
    private int id;
    private ArrayList<HashMap<String, String>> items;
    private Activity activity;
    private FragmentActivity myContext;

    public ListViewTabGuide_Adapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> list, FragmentActivity c) {
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

        TextView id = (TextView) mView.findViewById(R.id.idGuide);
        TextView nomPoi = (TextView) mView.findViewById(R.id.nomPoiGuide);
        TextView ville = (TextView) mView.findViewById(R.id.villeGuide);
        TextView dateP = (TextView) mView.findViewById(R.id.datePassage);
        TextView nom = (TextView) mView.findViewById(R.id.nomAuteurVoyage);
        TextView idVoyage = (TextView) mView.findViewById(R.id.idVoyageGuide);

        Typeface typeFacePrimaire = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface typefaceSecond = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface typefaceSecond2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");


        id.setText(items.get(position).get("id"));
        nom.setText(items.get(position).get("nomUser"));
        nom.setTypeface(typefaceSecond);
        dateP.setText(items.get(position).get("dateP"));
        nomPoi.setText(items.get(position).get("nom"));
        nomPoi.setTypeface(typeFacePrimaire);
        ville.setText(items.get(position).get("ville"));
        idVoyage.setText(items.get(position).get("idVoyage"));
        return mView;

    }
}
