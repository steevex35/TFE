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
import android.widget.EditText;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;
import com.obisteeves.meetuworld.Utils.Utilities;

import org.json.JSONException;

import java.util.Observable;
import java.util.Observer;




public class modifierProfil extends ActionBarActivity implements Observer {

    Toolbar toolbar;
    EditText nom,prenom,ville,pays;
    TextView error;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil);
        try {
            user = getIntent().getExtras().getParcelable("user");
        } catch (NullPointerException e) {
        }

        iniActionBar();
        afficheProfil();
        nom=(EditText) findViewById(R.id.hidden_edit_nom);
        prenom=(EditText) findViewById(R.id.hidden_edit_prenom);
        ville=(EditText) findViewById(R.id.hidden_edit_ville);
        pays=(EditText) findViewById(R.id.hidden_edit_pays);
        error = (TextView) findViewById(R.id.error);

    }


    /**
     * permmet d'afficher l'actionBar android
     */
    private void iniActionBar(){
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<center><b><font color='#ffffff'>Modifier votre profil</font></b></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009688")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modifier_profil, menu);
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
    private void afficheProfil() {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageProfil);
        net.setUrl(address);
        net.send();


    }
    //rajouter dialog pour confirmer l'envoie du formulaire
    public void envoyerNouveauProfil(View view){
        error.setText("");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        nouveauProfil(nom.getText().toString(),prenom.getText().toString(),
                                ville.getText().toString(),pays.getText().toString());

                        //Intent myIntent = new Intent(getApplicationContext(), ConnectionPage.class);
                        Intent intent = new Intent(modifierProfil.this, HomePage.class);
                        intent.putExtra("user", user);
                        startActivity(intent);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("êtes-vous sûre d'envoyer ces informations ?").setTitle("!! Avertissement !!")
                .setPositiveButton("Oui", dialogClickListener)
                .setNegativeButton("Non", dialogClickListener).show();


    }

    private void nouveauProfil(String nom,String prenom, String ville, String pays){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageModifierProfil);
        net.setUrl(address);

        if (Utilities.testStringInscription(nom, prenom, ville, pays) == true) {
            net.addParam("nom", nom);
            net.addParam("prenom", prenom);
            net.addParam("ville", ville);
            net.addParam("pays", pays);
            net.send();
        } else
            ((TextView)findViewById(R.id.error)).setText("Un ou plusieurs champs sont vides");



    }
    public void update(Observable observable, Object data) {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq)) {
            try {
                ((EditText) findViewById(R.id.hidden_edit_prenom)).setText(resultat.getResult().get("prenom").toString());
                ((EditText) findViewById(R.id.hidden_edit_nom)).setText(resultat.getResult().get("nom").toString());
                ((EditText) findViewById(R.id.hidden_edit_ville)).setText(resultat.getResult().get("ville").toString());
                ((EditText) findViewById(R.id.hidden_edit_pays)).setText(resultat.getResult().get("pays").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
