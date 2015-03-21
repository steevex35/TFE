package com.obisteeves.meetuworld.PageAndroid;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.DatePickerFragment;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class addTravel extends ActionBarActivity implements Observer {
    Toolbar toolbar;
    EditText textIn;
    Button buttonAdd;
    LinearLayout container;
    Spinner spinner;

    private  HashMap<String,String> spinnerMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        iniActionBar();
        textIn = (EditText) findViewById(R.id.textin);
        buttonAdd = (Button) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container);

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                TextView textOut = (TextView) addView.findViewById(R.id.textout);
                textOut.setText(textIn.getText().toString());
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });

                container.addView(addView);
            }
        });

        listPays();
    }

    private void iniActionBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rajouter un voyage");
        toolbar.setTitleTextColor(Color.parseColor("#FFAB00"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01579B")));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_travel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        TextView dateA = ((TextView) findViewById(R.id.DateArrivee));
        DialogFragment newFragment = new DatePickerFragment(dateA);
        newFragment.show(getFragmentManager(), "datePicker");
        //fdateExp=date;
    }

    public void showDatePickerDialog2(View v) {

        TextView dateD = ((TextView) findViewById(R.id.DateDepart));
        DialogFragment newFragment2 = new DatePickerFragment(dateD);
        newFragment2.show(getFragmentManager(), "datePicker");
        //fdateExp=date;
    }

    public void listPays() {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.listPays);
        net.setUrl(address);
        net.send();

    }

    /**
     * @param observable
     * @param data
     */

    public void update(Observable observable, Object data) {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);

        String netReq = String.valueOf(NetworkRequestAdapter.OK);

        if (!data.toString().equals(netReq)) {

            try {
                JSONArray Pays =  resultat.getResult().getJSONArray("pays");
                String[] spinnerArray = new String[Pays.length()];

                //((TextView) findViewById(R.id.error)).setText(Pays.toString());
                for (int i = 0; i < Pays.length(); i++) {
                    JSONObject json = Pays.getJSONObject(i);
                    String pays = json.getString("pays");
                    String id = json.getString("id");
                    spinnerMap.put(pays,id);
                    spinnerArray[i] = pays;
                }
                spinner = (Spinner) findViewById(R.id.listPays);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }




            }
        }


}

