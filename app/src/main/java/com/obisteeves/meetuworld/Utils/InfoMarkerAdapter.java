package com.obisteeves.meetuworld.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.obisteeves.meetuworld.R;

/**
 * Class servant à personnaliser un marker sur Google Maps
 */
public abstract class InfoMarkerAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater=null;
    InfoMarkerAdapter(LayoutInflater inflater){
        this.inflater=inflater;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup=inflater.inflate(R.layout.perso_info_marker, null);

        TextView tv=(TextView)popup.findViewById(R.id.title);

        tv.setText(marker.getTitle());
        tv=(TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getSnippet());

        return(popup);
    }
}
