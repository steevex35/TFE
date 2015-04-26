package com.obisteeves.meetuworld.PageAndroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;

public class infoVoyage extends ActionBarActivity implements Observer {

    Toolbar toolbar;
    TextView error,nomUser;

    String id_voyage,nom, pays,ville,dateA,dateD,id_current,id_auteur;
    ImageView img;
    Button boutonModif;

    private HashMap<String,String> listViewPoi = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_voyage);
        error = (TextView) findViewById(R.id.error);
        iniActionBar();
        img = (ImageView)findViewById(R.id.avatarCompte);
        boutonModif=(Button) findViewById(R.id.boutonModifVoyage);
        nomUser=(TextView)findViewById(R.id.nomUser);

        img.setVisibility(View.INVISIBLE);
        nomUser.setVisibility(View.GONE);
        boutonModif.setVisibility(View.GONE);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            id_voyage = extras.getString("id_voyage");
            nom=extras.getString("nom_user");
            pays=extras.getString("pays_user");
            ville=extras.getString("ville_user");
            dateA=extras.getString("dateA");
            dateD=extras.getString("dateD");
            afficheVoyage(id_voyage);
            ((TextView) findViewById(R.id.nomUser)).setText(nom);
            ((TextView) findViewById(R.id.paysInfo)).setText(pays);
            ((TextView) findViewById(R.id.villeinfo)).setText(ville);
            ((TextView) findViewById(R.id.dateArriveInfo)).setText(dateA);
            ((TextView) findViewById(R.id.dateDepartInfo)).setText(dateD);


        }else
        dialogPerso("error","error","error",infoVoyage.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_voyage, menu);
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

    private void iniActionBar(){
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Info voyage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFAB00"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01579B")));
    }

    private void afficheVoyage(String id_voyage){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.afficheVoyages);
        net.setUrl(address);
        net.addParam("id_voyage",id_voyage);
        net.send();

    }

    @Override
    public void update(Observable observable, Object data) {

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OKlistPoi);
        if (data.toString().equals(netReq))
        {
            try {
                JSONArray voyages = resultat.getResult().getJSONArray("poi");
                String[] listvoyages = new String[voyages.length()];
                for (int i = 0; i < voyages.length(); i++) {
                    JSONObject json = voyages.getJSONObject(i);
                    String pays = json.getString("nom");
                    String id = json.getString("id");
                    id_current=json.getString("id_current");
                    id_auteur=json.getString("id_auteur");
                    listViewPoi.put(pays, id);
                    listvoyages[i] = pays;
                }

                ListAdapter voyagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listvoyages);
                ListView poiListView = (ListView) findViewById(R.id.listViewPoi);
                poiListView.setAdapter(voyagesAdapter);

                if(id_current.equals(id_auteur)){

                    boutonModif.setVisibility(View.VISIBLE);
                    poiListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.cancel();
                                                    break;
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    //Yes button clicked
                                                    break;

                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(infoVoyage.this);
                                    builder.setMessage("Modifier le Point POI")
                                            .setTitle("Info")
                                            .setNegativeButton("Non", dialogClickListener)
                                            .setPositiveButton("Oui", dialogClickListener)
                                            .show();
                                    //ouvrir un autre dialog pour modifier ou supprimer un POi
                                }
                            }
                    );

                }
                if (!id_current.equals(id_auteur)) {

                    img.setVisibility(View.VISIBLE);
                    nomUser.setVisibility(View.VISIBLE);

                    poiListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //condition pour juste ouvrir ce dialog si l'utilisateur en cours n'est pas le créateur du voyage
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.cancel();
                                                    break;
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    //Yes button clicked
                                                    break;

                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(infoVoyage.this);
                                    builder.setMessage("Devenir l'accompagnateur pour ce lieu ? ")
                                            .setTitle("Info")
                                            .setNegativeButton("Non", dialogClickListener)
                                            .setPositiveButton("Oui", dialogClickListener)
                                            .show();


                                    //ouvrir un autre dialog pour modifier ou supprimer un POi
                                }
                            }
                    );
                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

    }




}
