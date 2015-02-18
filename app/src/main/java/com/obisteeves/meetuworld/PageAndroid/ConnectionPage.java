package com.obisteeves.meetuworld.PageAndroid;

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

public class ConnectionPage extends ActionBarActivity implements Observer{

    private EditText fEmail, fMdp;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_page);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Connexion");

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFAB00")));

        fEmail=(EditText) findViewById(R.id.email);
        fEmail.setText("steevex35@hotmail.com");
        fMdp = (EditText) findViewById(R.id.pwd);
        fMdp.setText("test1234");
        error = (TextView) findViewById(R.id.error);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection_page, menu);
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
    public void connection(View view){
        error.setText("");
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

        if(msg.toString().equals(NetworkRequestAdapter.NO_ERROR)){
            Utilities.enter(HomePage.class, this);
            initAdvertTypesTable(observable);
        }
        else
            error.setText(msg.toString());
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
