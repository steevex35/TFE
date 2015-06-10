package com.obisteeves.meetuworld.PageAndroid;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Tabs.SlidingTabLayout;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.ViewPagerAdapterInfoTravel;
import com.obisteeves.meetuworld.Utils.Voyage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


public class infoVoyage extends ActionBarActivity implements Observer {

    public Voyage voyageUser;
    ListView poiListView;
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
    private ArrayList<String> tabNomPoi = new ArrayList<String>();
    private TextView nomUser, idItemPoi, nomPoi;
    private String[] fields = {"nom", "id", "guide"};
    private int[] field_R_id = {R.id.nomPoi, R.id.idPoi, R.id.guide};
    private ArrayList<HashMap<String, String>> listHashPoi = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_voyage);

        try {
            voyageUser = getIntent().getExtras().getParcelable("parcelable");
            System.out.println(voyageUser.getListPoi().toString() + "classe inVoyage");
        } catch (NullPointerException e) {
        }

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
        afficheVoyage(id_voyage);
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
                intent.putExtra("parcelable", voyageUser);
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


    public void afficheVoyage(String id_voyage) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.afficheVoyages);
        net.setUrl(address);
        net.addParam("id_voyage", id_voyage);
        net.send();

    }

    private void DevenirGuide(String id_poi, String id_current, String id_auteur) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.devenirGuide);
        net.setUrl(address);
        net.addParam("id_poi", id_poi);
        net.addParam("id_current", id_current);
        net.addParam("id_auteur", id_auteur);
        net.send();
    }


    @Override
    public void update(Observable observable, Object data) {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OKlistPoi);
        String netReq2 = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq2)) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            listHashPoi.clear();
                            poiListView.setAdapter(null);
                            afficheVoyage(id_voyage);
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Merci d'avoir choisi d'être mon guide pour ce lieu")
                    .setTitle("Info")
                    .setPositiveButton("Retour", dialogClickListener)
                    .show();

        }


        if (data.toString().equals(netReq)) {
            try {
                JSONArray poi = resultat.getResult().getJSONArray("poi");
                for (int i = 0; i < poi.length(); i++) {
                    HashMap<String, String> listViewMap = new HashMap<String, String>();
                    JSONObject json = poi.getJSONObject(i);
                    String nomPoi = json.getString("nom");
                    String idPoi = json.getString("id");
                    String guide = json.getString("guide");
                    id_current = json.getString("id_current");
                    id_auteur = json.getString("id_auteur");
                    tabNomPoi.add(nomPoi);
                    //System.out.println(tabNomPoi.size()+"classe INfo");
                    //System.out.println(tabNomPoi.toString());
                    listViewMap.put("nom", nomPoi);
                    listViewMap.put("id", idPoi);
                    listViewMap.put("guide", guide);
                    listHashPoi.add(listViewMap);

                }
                //voyageUser.setListPoi(tabNomPoi);
                //System.out.println(voyageUser.getListPoi().toString());

                poiListView = (ListView) findViewById(R.id.listViewPoi);
                SimpleAdapter poiAdapter = new SimpleAdapter(this, listHashPoi, R.layout.listview_poi, fields, field_R_id);
                poiListView.setAdapter(poiAdapter);

                poiListView.setClickable(true);


                if (!id_current.equals(id_auteur)) {
                    poiListView.setOnItemClickListener(

                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    idItemPoi = (TextView) view.findViewById(R.id.idPoi);
                                    nomPoi = (TextView) view.findViewById(R.id.nomPoi);
                                    String nom = nomPoi.getText().toString();
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.cancel();
                                                    break;
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    //Yes button clicked
                                                    String id = idItemPoi.getText().toString();
                                                    DevenirGuide(id, id_current, id_auteur);
                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(infoVoyage.this);
                                    builder.setMessage("Devenir l'accompagnateur pour " + nom + " ? ")
                                            .setTitle("Info")
                                            .setNegativeButton("Non", dialogClickListener)
                                            .setPositiveButton("Oui", dialogClickListener)
                                            .show();
                                }
                            }
                    );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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

    public ArrayList<String> getTabNomPoi() {
        return tabNomPoi;
    }

    public void setTabNomPoi(ArrayList<String> tabNomPoi) {
        this.tabNomPoi = tabNomPoi;
    }
}
