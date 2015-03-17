package com.obisteeves.meetuworld.PageAndroid;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.DatePickerFragment;

public class addTravel extends ActionBarActivity  {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        iniActionBar();
    }

    private void iniActionBar(){
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
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
        TextView date=((TextView)findViewById(R.id.dateA));
        DialogFragment newFragment = new DatePickerFragment(date);
        newFragment.show(getFragmentManager(), "datePicker");
        //fdateExp=date;
    }




}
