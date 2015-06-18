package com.obisteeves.meetuworld.PageAndroid;


import android.app.AlertDialog;
import android.app.DialogFragment;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.DatePickerFragment;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;

/**
 * Activity d'inscription
 */
public class inscriptionPage extends ActionBarActivity implements Observer {

         Toolbar toolbar;
    private EditText nom, prenom, email, pwd, ville;
    private TextView error, fdateDob;
    private Spinner spinner;
    private String idSelectionPays;
    private HashMap<String,String> spinnerMap = new java.util.HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_page);
        iniActionBar();
        fdateDob= (TextView)findViewById(R.id.Dob);
        nom=(EditText) findViewById(R.id.nom);
        nom.setText("Obiang Ndam");
        prenom=(EditText) findViewById(R.id.prenom);
        prenom.setText("steeves");
        ville=(EditText)findViewById(R.id.villeInscription);
        ville.setText("Louvain-La-Neuve");
        email= (EditText) findViewById(R.id.email);
        email.setText("steeve35@hotmail.com");
        pwd= (EditText) findViewById(R.id.pwd);
        pwd.setText("test1");
        error = (TextView) findViewById(R.id.error);
        ListPays();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription_page, menu);
        return true;
    }

    private void iniActionBar(){
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Inscription</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        toolbar.setLogo(R.drawable.ic_logo);
    }

    public void ListPays()
    {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.listPays);
        net.setUrl(address);
        net.send();
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

    public void envoyerInscription(View view){
        error.setText("");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        idSelectionPays = spinnerMap.get(spinner.getSelectedItem().toString());
                       envoyerDataInscription(  nom.getText().toString(),
                                                prenom.getText().toString(),
                                                email.getText().toString(),
                                                pwd.getText().toString(),
                                                ville.getText().toString(),
                                                fdateDob.getText().toString(),
                                                idSelectionPays);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.cancel();

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Validez-vous ces informations ?").setTitle("Confirmation").setPositiveButton("Oui", dialogClickListener)
                .setNegativeButton("Non", dialogClickListener).show();
    }


    public void showDatePickerDialog(View v)
    {
        TextView date = ((TextView) findViewById(R.id.Dob));
        DialogFragment newFragment = new DatePickerFragment(date);
        newFragment.show(getFragmentManager(), "datePicker");
        fdateDob=date;
    }

    /**
     *
     * @param nom
     * @param prenom
     * @param email
     * @param pwd
     * @param ville
     * @param dateOb
     * @param pays
     * @return
     */
    private boolean envoyerDataInscription(String nom, String prenom, String email,
                                           String pwd, String ville, String dateOb, String pays) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageInscription);
        net.setUrl(address);

        if (Utilities.testStringInscription(nom, prenom, email, pwd, ville, dateOb) == true) {
            net.addParam("nom", nom);
            net.addParam("prenom", prenom);
            net.addParam("email", email);
            net.addParam("pays",pays);
            net.addParam("ville",ville);
            net.addParam("dateOb",dateOb);
            net.addParam("pwd", pwd);
            net.send();
            return true;
        } else
            dialogPerso("Un ou plusieurs champs sont vides", "Avertissement", "Retour", inscriptionPage.this);
        return false;
    }

    public void update(Observable observable,final Object data){

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OKlistPays);
        String netReqInscription = String.valueOf(NetworkRequestAdapter.OKinscription);

        if (data.toString().equals(netReq))
        {
            try
            {
                JSONArray Pays =  resultat.getResult().getJSONArray("pays");
                String[] spinnerArray = new String[Pays.length()];
                for (int i = 0; i < Pays.length(); i++)
                {
                    JSONObject json = Pays.getJSONObject(i);
                    String pays = json.getString("pays");
                    String id = json.getString("id");
                    spinnerMap.put(pays,id);
                    spinnerArray[i] = pays;
                }

                spinner = (Spinner) findViewById(R.id.listPaysInscription);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            initAdvertTypesTable(observable);

        }else if(data.toString().equals(netReqInscription)) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent myIntent = new Intent(getApplicationContext(), ConnectionPage.class);
                            startActivity(myIntent);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Inscription Confirmée").setTitle(" Avertissement").setPositiveButton("Continuer", dialogClickListener).show();
            initAdvertTypesTable(observable);
        }else
            dialogPerso(data.toString(), "Avertissement", "Retour", inscriptionPage.this);
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


