package com.obisteeves.meetuworld.PageAndroid;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.Utilities;


public class MainActivity extends Activity {

    private EditText fEmail, fMdp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);




        setContentView(R.layout.activity_main);


        //fEmail=(EditText) findViewById(R.id.email);
        //fEmail.setText("steevex35@hotmail.com");
        //fMdp = (EditText) findViewById(R.id.pwd);
       // fMdp.setText("test");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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

    public void connectionPage(View view){
        Utilities.enter(ConnectionPage.class,this);
    }

    public void inscription(View view){Utilities.enter(inscriptionPage.class,this);}
}
