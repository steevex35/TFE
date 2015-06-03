package com.obisteeves.meetuworld.PageAndroid;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Tabs.SlidingTabLayout;
import com.obisteeves.meetuworld.Utils.User;
import com.obisteeves.meetuworld.Utils.ViewPagerAdapter;

public class HomePage extends ActionBarActivity {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence titles[];
    int nbTabs;
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ini();
        try {
            user = getIntent().getExtras().getParcelable("user");
        } catch (NullPointerException e) {
        }

    }

    private void ini(){
        // Set the Settings of the tabs
        nbTabs = 4;
        titles = new CharSequence[]{"Home", "Ur Trips", "Profil", "Guide"};
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        getSupportActionBar().setTitle(Html.fromHtml("<center><b><font color='#ffffff'>Meet Your World</font></b></center>"));

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
            case R.id.action_filtre:
                DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                //filtre sur la Localisation

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Filtre en fonction de votre localisation ?").setTitle("Info").setPositiveButton("Oui", dialogClickListener1)
                        .setNegativeButton("Non", dialogClickListener1).show();
                return true;

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
                builder.setMessage(" Voulez-vous quitter ?").setTitle("Avertissement").setPositiveButton("Oui", dialogClickListener)
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
