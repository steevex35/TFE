package com.obisteeves.meetuworld.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obisteeves.meetuworld.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TabNewTravel extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_tab_new_travel,container,false);
        return v;
    }
}