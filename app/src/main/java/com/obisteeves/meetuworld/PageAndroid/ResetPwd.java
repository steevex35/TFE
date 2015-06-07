package com.obisteeves.meetuworld.PageAndroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.Utilities;

import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;

public class ResetPwd extends ActionBarActivity implements Observer {

    private Toolbar toolbar;
    private TextView info, fEmail;
    private Typeface tf;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        info = (TextView) findViewById(R.id.info);
        tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        info.setTypeface(tf, Typeface.BOLD);
        iniActionBar();
        fEmail = (EditText) findViewById(R.id.email);
    }

    private void iniActionBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#ffffff'>Mot de passe oublié </font></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        toolbar.setLogo(R.drawable.ic_logo);
    }

    public void resetPwdUser(View view) {
        email = fEmail.getText().toString();
        if (email.isEmpty()) {
            Utilities.dialogPerso("Champs Vide", "Avertissement", "retour", ResetPwd.this);
        } else {
            sendResetPwd(email);
        }
    }

    public void sendResetPwd(String email) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd) + getResources().getString(R.string.modifMotDePasse);
        net.setUrl(address);
        net.addParam("email", email);
        net.send();
    }

    @Override
    public void update(Observable observable, Object data) {
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq)) {
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
            builder.setMessage("Un Email avec un mot de passe vous a été envoyez").setTitle(" Avertissement").setPositiveButton("Continuer", dialogClickListener).show();
        } else
            dialogPerso(data.toString(), "Avertissement", "retour", ResetPwd.this);
    }
}
