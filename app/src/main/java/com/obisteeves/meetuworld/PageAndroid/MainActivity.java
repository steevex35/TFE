package com.obisteeves.meetuworld.PageAndroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.Utilities;

/**
 * Activity permettant d'accéder à la connexion et à l'inscription
 */
public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void connectionPage(View view){
        Utilities.enter(ConnectionPage.class,this);
    }

    public void inscription(View view) {
        Utilities.enter(inscriptionPage.class, this);
    }
}
