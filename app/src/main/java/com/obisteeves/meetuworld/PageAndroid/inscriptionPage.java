package com.obisteeves.meetuworld.PageAndroid;



import android.app.AlertDialog;
import android.app.DialogFragment;
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

public class inscriptionPage extends ActionBarActivity implements Observer{

        EditText nom,prenom,email,emailConf,pwd,pwdConf,ville;
        TextView error,fdateDob;
        Spinner spinner;
        String idSelectionPays;

         Toolbar toolbar;

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
        emailConf= (EditText) findViewById(R.id.emailConfirmation);
        emailConf.setText("steeve35@hotmail.com");
        pwd= (EditText) findViewById(R.id.pwd);
        pwd.setText("test1");
        pwdConf=(EditText) findViewById(R.id.pwdConfirmation);
        pwdConf.setText("test1");
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
        getSupportActionBar().setTitle("Inscription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFAB00")));
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
                                                emailConf.getText().toString(),
                                                pwd.getText().toString(),
                                                pwdConf.getText().toString(),
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

    /**
     *
     * @param data1
     * @param data2
     * @param data3
     * @param data4
     * @param data5
     * @param data6
     * @param data7
     * @param data8
     * @return
     */
    public boolean testStringInscription(String data1,String data2,String data3, String data4,
                                         String data5, String data6, String data7, String data8){
        if(data1 != null && !data1.isEmpty()&&(data2!=null && !data2.isEmpty())&&
                (data3!=null && !data3.isEmpty())&&(data4!=null && !data4.isEmpty())&&
                (data5!=null && !data5.isEmpty())&&(data6!=null && !data6.isEmpty())&&
                (data7!=null && !data7.isEmpty())&&(data8!=null && !data8.isEmpty())){
            return true;
        }else
            return false;
    }
    public static boolean testStringInscription(String data1,String data2,String data3, String data4){
        if(data1 != null && !data1.isEmpty()&&(data2!=null && !data2.isEmpty())&&
                (data3!=null && !data3.isEmpty())&&(data4!=null && !data4.isEmpty())){
            return true;
        }else
            return false;
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
     * @param emailConf
     * @param pwd
     * @param pwdConf
     * @param ville
     * @param dateOb
     * @param pays
     * @return
     */
    private boolean  envoyerDataInscription(String nom,String prenom, String email,String emailConf,
                                    String pwd, String pwdConf, String ville, String dateOb,String pays){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageInscription);
        net.setUrl(address);

        if(testStringInscription(nom,prenom,email,emailConf,pwd,pwdConf,ville,dateOb)==true) {
            net.addParam("nom", nom);
            net.addParam("prenom", prenom);
            net.addParam("email", email);
            net.addParam("pays",pays);
            net.addParam("ville",ville);
            net.addParam("dateOb",dateOb);
            net.addParam("emailConf", emailConf);
            net.addParam("pwd", pwd);
            net.addParam("pwdConf", pwdConf);

            net.send();
            return true;
        } else
            dialogPerso("Un ou plusieurs champs sont vides","Avertissement","Retour",inscriptionPage.this);
        return false;
    }

    public void update(Observable observable,final Object data){

        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
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
            dialogPerso(data.toString(),"Avertissement","Retour",inscriptionPage.this);
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


