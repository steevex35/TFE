package com.obisteeves.meetuworld.PageAndroid;


import android.content.Intent;
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
import com.obisteeves.meetuworld.Utils.ViewPagerAdapterInfoTravel;


public class infoVoyage extends ActionBarActivity {

    private Toolbar toolbar;
    private String id_voyage;
    private String nom;
    private String pays;
    private String ville;
    private String dateA;
    private String dateD;
    private String id_current;
    private String id_auteur;
    private MenuItem itemModif;


    private ViewPager pager;
    private ViewPagerAdapterInfoTravel adapter;
    private SlidingTabLayout tabs;
    private CharSequence titles[];
    private int nbTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_voyage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_voyage = extras.getString("id_voyage");
            id_auteur = extras.getString("id_auteur");
            id_current = extras.getString("userCurrent");
            nom = extras.getString("nom_user");
            pays = extras.getString("pays_user");
            ville = extras.getString("ville_user");
            dateA = extras.getString("dateA");
            dateD = extras.getString("dateD");
        }

        iniActionBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_voyage, menu);
        itemModif = menu.findItem(R.id.action_edit);
        itemModif.setVisible(false);
        if (id_auteur.equals(id_current)) {
            itemModif.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(infoVoyage.this, ModifierVoyage.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void iniActionBar(){
        nbTabs = 2;
        titles = new CharSequence[]{"Information", "Carte"};
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#ffffff'>Info voyage</font></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        toolbar.setLogo(R.drawable.ic_logo);

        adapter = new ViewPagerAdapterInfoTravel(getSupportFragmentManager(), titles, nbTabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
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


    public String getId_voyage() {
        return id_voyage;
    }

    public void setId_voyage(String id_voyage) {
        this.id_voyage = id_voyage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDateA() {
        return dateA;
    }

    public void setDateA(String dateA) {
        this.dateA = dateA;
    }

    public String getDateD() {
        return dateD;
    }

    public void setDateD(String dateD) {
        this.dateD = dateD;
    }

    public String getId_current() {
        return id_current;
    }

    public void setId_current(String id_current) {
        this.id_current = id_current;
    }

    public String getId_auteur() {
        return id_auteur;
    }

    public void setId_auteur(String id_auteur) {
        this.id_auteur = id_auteur;
    }




}
