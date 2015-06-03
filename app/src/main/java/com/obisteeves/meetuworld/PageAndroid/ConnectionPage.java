package com.obisteeves.meetuworld.PageAndroid;


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

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;
import com.obisteeves.meetuworld.Utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;

public class ConnectionPage extends ActionBarActivity implements Observer{

    private Toolbar toolbar;
    private EditText fEmail, fMdp;
    private String id, nom, prenom, email, ville, pays;
    private User userCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_page);
        iniActionBar();

        fEmail=(EditText) findViewById(R.id.email);
        fEmail.setText("steeve35@hotmail.com");
        fMdp = (EditText) findViewById(R.id.pwd);
        fMdp.setText("test1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection_page, menu);
        return true;

    }

    /**
     * permmet d'afficher l'actionBar android
     */
    private void iniActionBar(){
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<center><b><font color='#ffffff'>Connexion</font></b></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009688")));
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
    public void connection(View view){
        connect(fEmail.getText().toString(), fMdp.getText().toString());
        //Utilities.enter(HomePage.class, this);
    }

    private void connect (String email, String mdp){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);


        String address= getResources().getString(R.string.serveurAdd)+ getResources().getString(R.string.connexionAdd);
        net.setUrl(address);
        net.addParam("email",email);
        net.addParam("mdp",mdp);

        net.send();

    }

    public void update(Observable observable,final Object msg) {
        if(msg==null) return ;
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        if(msg.toString().equals(NetworkRequestAdapter.NO_ERROR)){

            try {
                id = resultat.getResult().get("id").toString();
                nom = resultat.getResult().get("nom").toString();
                prenom = resultat.getResult().get("prenom").toString();
                email = resultat.getResult().get("email").toString();
                ville = resultat.getResult().get("ville").toString();
                pays = resultat.getResult().get("pays").toString();

                userCurrent = new User(id, nom, prenom, email, ville, pays);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Utilities.enter(HomePage.class, this);
            Intent intent = new Intent(ConnectionPage.this, HomePage.class);
            intent.putExtra("user", userCurrent);
            startActivity(intent);
            initAdvertTypesTable(observable);

        }
        else
            dialogPerso(msg.toString(),"Avertissement","retour",ConnectionPage.this);

    }

    private void initAdvertTypesTable(final Observable observable){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Utilities.fillAdvertTable((JSONObject) ((NetworkRequestAdapter)observable).getResult().get("types"));
                    Utilities.fillErrorsTable((JSONObject) ((NetworkRequestAdapter)observable).getResult().get("errors"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }
}
