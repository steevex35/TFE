package com.obisteeves.meetuworld.PageAndroid;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Tabs.SlidingTabLayout;
import com.obisteeves.meetuworld.Utils.ViewPagerAdapter;

public class HomePage extends ActionBarActivity {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence titles[];
    int nbTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ini();
    }

    private void ini(){
        // Set the Settings of the tabs
        nbTabs = 3;
        titles = new CharSequence[]{"Home","U Travel","Profil"};
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFAB00"));
        getSupportActionBar().setTitle("Meet u World");
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, nbTabs);
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        // Enable the differents tabs to be evenly distributed on the screen's width.
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_apropos:
                // About option clicked.
                return true;
            case R.id.action_deco:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                             //Intent myIntent = new Intent(getApplicationContext(), ConnectionPage.class);
                                finish();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(" Vous validez votre déconnexion ?").setTitle("Avertissement").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;

            case R.id.action_settings:
                // Settings option clicked.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
