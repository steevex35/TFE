package com.obisteeves.meetuworld.PageAndroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;

public class infoVoyage extends ActionBarActivity implements Observer {

    private Toolbar toolbar;
    private TextView error,nomUser,idItemPoi,nomPoi;

    private String id_voyage,nom, pays,ville,dateA,dateD,id_current,id_auteur;
    private ImageView img;
    private ImageButton boutonGuide;
    private Button boutonModif;
    private  String [] fields = {"nom","id","guide"};
    private  int[] field_R_id = {R.id.nomPoi,R.id.idPoi,R.id.guide};
    private ArrayList<HashMap<String, String>> listHashPoi =  new ArrayList<HashMap<String, String>>();


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

            Button boutonIti =(Button) findViewById(R.id.boutonMap);
            boutonIti.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //googleMaps
                    Intent intent = new Intent(infoVoyage.this, ItinerairePage.class);
                    intent.putExtra("ville", ville);
                    startActivity(intent);
                }
            });


        }else
            dialogPerso("error", "error", "error", infoVoyage.this);
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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#ffffff'>Info voyage</font></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
    }

    private void afficheVoyage(String id_voyage){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.afficheVoyages);
        net.setUrl(address);
        net.addParam("id_voyage", id_voyage);
        net.send();

    }

    @Override
    public void update(Observable observable, Object data) {

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OKlistPoi);
        String netReq2 = String.valueOf(NetworkRequestAdapter.OK);

        if(data.toString().equals(netReq2)){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //POur refesh l'activity
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(infoVoyage.this);
            builder.setMessage("Merci d'avoir choisi d'être mon guide pour ce lieu")
                    .setTitle("Info")
                    .setPositiveButton("Retour", dialogClickListener)
                    .show();

        }

        if (data.toString().equals(netReq))
        {
            try {
                JSONArray poi = resultat.getResult().getJSONArray("poi");
                for (int i = 0; i < poi.length(); i++)
                {
                    HashMap<String,String> listViewMap = new HashMap<String, String>();
                    JSONObject json = poi.getJSONObject(i);
                    String nomPoi = json.getString("nom");
                    String idPoi = json.getString("id");
                    String guide=json.getString("guide");
                    id_current=json.getString("id_current");
                    id_auteur=json.getString("id_auteur");
                    listViewMap.put("nom",nomPoi);
                    listViewMap.put("id",idPoi);
                    listViewMap.put("guide",guide);
                    listHashPoi.add(listViewMap);
                }

                ListView poiListView = (ListView) findViewById(R.id.listViewPoi);
                SimpleAdapter poiAdapter = new SimpleAdapter(infoVoyage.this, listHashPoi, R.layout.listview_poi, fields, field_R_id);
                poiListView.setAdapter(poiAdapter);

                poiListView.setClickable(true);


                if(id_current.equals(id_auteur)){
                    boutonModif.setVisibility(View.VISIBLE);
                }


                if (!id_current.equals(id_auteur)) {

                    img.setVisibility(View.VISIBLE);
                    nomUser.setVisibility(View.VISIBLE);

                    poiListView.setOnItemClickListener(

                            new AdapterView.OnItemClickListener() {



                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    idItemPoi=(TextView) view.findViewById(R.id.idPoi);
                                    nomPoi=(TextView) view.findViewById(R.id.nomPoi);
                                    String nom=nomPoi.getText().toString();
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
                                    builder.setMessage("Devenir l'accompagnateur pour "+ nom +" ? ")
                                            .setTitle("Info")
                                            .setNegativeButton("Non", dialogClickListener)
                                            .setPositiveButton("Oui", dialogClickListener)
                                            .show();

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

    private void DevenirGuide( String id_poi, String id_current,String id_auteur){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.devenirGuide);
        net.setUrl(address);
        net.addParam("id_poi", id_poi);
        net.addParam("id_current", id_current);
        net.addParam("id_auteur",id_auteur);
        net.send();
    }




}
