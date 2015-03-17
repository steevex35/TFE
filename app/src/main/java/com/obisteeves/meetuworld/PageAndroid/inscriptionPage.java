package com.obisteeves.meetuworld.PageAndroid;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;

import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.Utilities;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

public class inscriptionPage extends ActionBarActivity implements Observer{

        EditText nom,prenom,email,emailConf,pwd,pwdConf;
        TextView error;
         Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_page);
        iniActionBar();

        nom=(EditText) findViewById(R.id.nom);
        nom.setText("Obiang Ndam");
        prenom=(EditText) findViewById(R.id.prenom);
        prenom.setText("steeves");
        email= (EditText) findViewById(R.id.email);
        email.setText("steeve35@hotmail.com");
        emailConf= (EditText) findViewById(R.id.emailConfirmation);
        emailConf.setText("steeve35@hotmail.com");
        pwd= (EditText) findViewById(R.id.pwd);
        pwd.setText("test1");
        pwdConf=(EditText) findViewById(R.id.pwdConfirmation);
        pwdConf.setText("test1");
        error = (TextView) findViewById(R.id.error);

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
                        envoyerDataInscription(nom.getText().toString(),prenom.getText().toString(),email.getText().toString(),
                                emailConf.getText().toString(),pwd.getText().toString(),
                                pwdConf.getText().toString());

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Validez-vous ces informations ?").setTitle("!! Confirmation !!").setPositiveButton("Oui", dialogClickListener)
                .setNegativeButton("Non", dialogClickListener).show();


    }
    //fonction de test des  valeurs des String
    public boolean testStringInscription(String data1,String data2,String data3, String data4, String data5, String data6){
        if(data1 != null && !data1.isEmpty()&&(data2!=null && !data2.isEmpty())&&
                (data3!=null && !data3.isEmpty())&&(data4!=null && !data4.isEmpty())&&
                (data5!=null && !data5.isEmpty())&&(data6!=null && !data6.isEmpty())){
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

    private void envoyerDataInscription(String nom,String prenom, String email,String emailConf,
                                    String pwd, String pwdConf){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageInscription);
        net.setUrl(address);

        //tester les valeurs à envoyer au serveur
        if(testStringInscription(nom,prenom,email,emailConf,pwd,pwdConf)==true) {
            net.addParam("nom", nom);
            net.addParam("prenom", prenom);
            net.addParam("email", email);
            net.addParam("emailConf", emailConf);
            net.addParam("pwd", pwd);
            net.addParam("pwdConf", pwdConf);

            net.send();
        } else
            ((TextView)findViewById(R.id.error)).setText("Un ou plusieurs champs sont vides");
    }

    public void update(Observable observable,final Object data){
        if(data==null) return ;
        if(data.toString().equals(NetworkRequestAdapter.NO_ERROR)){


            //Utilities.enter(ConnectionPage.class, this);
            initAdvertTypesTable(observable);
        }
        else
            error.setText(data.toString());
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


