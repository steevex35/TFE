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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;
import com.obisteeves.meetuworld.Utils.Utilities;

import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;


public class modifierProfil extends ActionBarActivity implements Observer {

    Toolbar toolbar;
    EditText nom, prenom, ville, email;
    TextView error;
    User user;
    TextView restorePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil);
        try {
            user = getIntent().getExtras().getParcelable("user");
        } catch (NullPointerException e) {
        }

        iniActionBar();
        //afficheProfil();
        nom=(EditText) findViewById(R.id.hidden_edit_nom);
        prenom=(EditText) findViewById(R.id.hidden_edit_prenom);
        ville=(EditText) findViewById(R.id.hidden_edit_ville);
        email = (EditText) findViewById(R.id.hidden_edit_email);
        restorePwd = (TextView) findViewById(R.id.restorePwd);

        nom.setText(user.getmNom());
        prenom.setText((user.getmPrenom()));
        ville.setText(user.getmVille());
        email.setText(user.getmEmail());


        error = (TextView) findViewById(R.id.error);

        restorePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ConnectionPage.this, ResetPwd.class);
                // startActivity(intent);
                LayoutInflater inflater = LayoutInflater.from(modifierProfil.this);
                View dialog_layout = inflater.inflate(R.layout.alert_dialog_new_pwd, null);
                AlertDialog.Builder db = new AlertDialog.Builder(modifierProfil.this);
                db.setView(dialog_layout);
                db.setTitle("Nouveau mot de passe");

                db.setCancelable(false);
                final EditText pwdEmail = (EditText) dialog_layout.findViewById(R.id.pwdEmail);
                final EditText pwdFinal = (EditText) dialog_layout.findViewById(R.id.pwdFinal);
                final EditText pwdFinalConf = (EditText) dialog_layout.findViewById(R.id.pwdFinalConf);
                db.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mdpE = pwdEmail.getText().toString();
                        String mdpF = pwdFinal.getText().toString();
                        String mdpFc = pwdFinalConf.getText().toString();
                        System.out.println(mdpE + " " + mdpF + " " + mdpFc);
                        //envoie du mot de passe
                        envoyerMotDePasse(mdpE, mdpF, mdpFc);
                    }
                });
                db.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = db.create();
                dialog.show();


            }
        });


    }

    private void envoyerMotDePasse(String mdpEmail, String mdpFinal, String mdpFinalConf) {

        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageModifierMotDePasse);
        net.setUrl(address);

        if (Utilities.testString(mdpEmail, mdpFinal, mdpFinalConf) == true) {
            net.addParam("mdpE", mdpEmail);
            net.addParam("mdpF", mdpFinal);
            net.addParam("mdpFc", mdpFinalConf);
            net.send();
        } else
            // ((TextView)findViewById(R.id.errorDialog)).setText("Un ou plusieurs champs sont vides");
            dialogPerso("Champs vide", "info", "ok", modifierProfil.this);
    }


    /**
     * permmet d'afficher l'actionBar android
     */
    private void iniActionBar(){
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#ffffff'>Modifier votre profil</font></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        toolbar.setLogo(R.drawable.ic_logo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modifier_profil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //envoie du profil
                                nouveauProfil(nom.getText().toString(), prenom.getText().toString(),
                                        ville.getText().toString(), email.getText().toString());

                                user.setmNom(nom.getText().toString());
                                user.setmPrenom(prenom.getText().toString());
                                user.setmVille(ville.getText().toString());
                                Intent intent = new Intent(modifierProfil.this, HomePage.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Voulez-vous enregistrer ?").setTitle("Avertissement").setPositiveButton("Oui", dialogClickListener1)
                        .setNegativeButton("Non", dialogClickListener1).show();
                return true;


            case R.id.action_annule:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(" Voulez-vous annuler ?").setTitle("Avertissement").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;

            case R.id.action_settings:
                // Settings option clicked.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void afficheProfil() {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageProfil);
        net.setUrl(address);
        net.send();


    }

    private void nouveauProfil(String nom, String prenom, String ville, String email) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageModifierProfil);
        net.setUrl(address);

        if (Utilities.testStringInscription(nom, prenom, ville, email) == true) {
            net.addParam("nom", nom);
            net.addParam("prenom", prenom);
            net.addParam("ville", ville);
            net.addParam("email", email);
            net.send();
        } else
            ((TextView)findViewById(R.id.error)).setText("Un ou plusieurs champs sont vides");



    }
    public void update(Observable observable, Object data) {
        String netReq2 = String.valueOf(NetworkRequestAdapter.OKpwd);
        if (data.toString().equals(netReq2)) {
            dialogPerso("Mot de passe mise à jour", "info", "Continuer", modifierProfil.this);

        } else {
            dialogPerso(data.toString(), "info", "ok", modifierProfil.this);
        }

    }

}
