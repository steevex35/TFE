package com.obisteeves.meetuworld.Tabs;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;

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
                //page de modif du profil
               // Intent intent = new Intent(getActivity(), modifierProfil.class);
                //startActivity(intent);
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View alertDialogView = factory.inflate(R.layout.alert_dialog_new_travel, null);
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setTitle("Nouveau voyage");
                //ad.setMessage("");
                ad.setView(alertDialogView);
                ad.setButton(getActivity().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                ad.show();


            }
        });

        return v;
    }


}

