package com.obisteeves.meetuworld.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Steeves on 02-04-15.
 */
public class listViewPersoAdapter extends ArrayAdapter {
    private Context mContext;
    private int id;
    private ArrayList<HashMap<String, String>> items;
    private Activity activity;
    private FragmentActivity myContext;


    public listViewPersoAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> list, FragmentActivity c) {
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


        ImageView avatarCompte = (ImageView) mView.findViewById(R.id.avatarComptePersoRow);
        TextView idVoyage = (TextView) mView.findViewById(R.id.idVoyage);
        TextView auteur = (TextView) mView.findViewById(R.id.idAuteur);
        TextView nomCompte = (TextView) mView.findViewById(R.id.nomUser);
        TextView pays = (TextView) mView.findViewById(R.id.paysHome);
        TextView ville = (TextView) mView.findViewById(R.id.villeHome);
        TextView dateA = (TextView) mView.findViewById(R.id.dateArrivee);
        TextView dateD = (TextView) mView.findViewById(R.id.dateDepart);



        Typeface typeFace= Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");

        new DownloadImageTask(avatarCompte).execute("http://www.l4h.be/TFE/android/Outils/avatars/" + items.get(position).get("avatar") + ".jpg");

        idVoyage.setText(items.get(position).get("id"));
        auteur.setText(items.get(position).get("idAuteur"));
        nomCompte.setText(items.get(position).get("nom"));
        nomCompte.setTypeface(typeFace);
        pays.setText(items.get(position).get("pays"));
        ville.setText(items.get(position).get("ville"));
        dateA.setText(items.get(position).get("date_arrivee"));
        dateD.setText(items.get(position).get("date_depart"));

        return mView;


    }
}

