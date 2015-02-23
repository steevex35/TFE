package com.obisteeves.meetuworld.PageAndroid;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

        EditText nomUti,email,emailConf,pwd,pwdConf;
        TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_page);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Inscription");

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFAB00")));


        nomUti=(EditText) findViewById(R.id.userName);
       // nomUti.setText("steevex35");
        email= (EditText) findViewById(R.id.email);
       // email.setText("steevex35@hotmail.com");
        emailConf= (EditText) findViewById(R.id.emailConfirmation);
        //emailConf.setText("steevex35@hotmail.com");
        pwd= (EditText) findViewById(R.id.pwd);
       // pwd.setText("steevex35@hotmail.com");
        pwdConf=(EditText) findViewById(R.id.pwdConfirmation);
       // pwdConf.setText("steevex35@hotmail.com");
        error = (TextView) findViewById(R.id.error);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription_page, menu);


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

    public void envoyerInscription(View view){
        error.setText("");

        envoyerDataInscription(nomUti.getText().toString(),email.getText().toString(),
                emailConf.getText().toString(),pwd.getText().toString(),
                pwdConf.getText().toString());
    }
    //fonction de test de la valeur des strings
    public void testString(String data1,String data2,String data3, String data4, String sata5){

    }

    private void envoyerDataInscription(String userName, String email,String emailConf,
                                    String pwd, String pwdConf){
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageInscription);
        net.setUrl(address);

        //tester les valeurs à envoyer au serveur
        if(userName != null && !userName.isEmpty()&&(email!=null && !email.isEmpty())&&
                (emailConf!=null && !emailConf.isEmpty())&&(pwd!=null && !pwd.isEmpty())&&
                (pwdConf!=null && !pwdConf.isEmpty())) {

            net.addParam("userName", userName);
            net.addParam("email", email);
            net.addParam("emailConf", emailConf);
            net.addParam("pwd", pwd);
            net.addParam("pwdConf", pwdConf);

            net.send();
        } else
            ((TextView)findViewById(R.id.error)).setText("Un ou plusieurs sont vides");
    }

    public void update(Observable observable,final Object data){
        if(data==null) return ;

        if(data.toString().equals(NetworkRequestAdapter.NO_ERROR)){
            Utilities.enter(ConnectionPage.class, this);
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


