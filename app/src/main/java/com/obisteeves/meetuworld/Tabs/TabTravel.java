package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;

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

        return v;
    }


}

