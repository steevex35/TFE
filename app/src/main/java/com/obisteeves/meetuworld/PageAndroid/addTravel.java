package com.obisteeves.meetuworld.PageAndroid;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.DatePickerFragment;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;
import static com.obisteeves.meetuworld.Utils.Utilities.getPosition;
import static com.obisteeves.meetuworld.Utils.Utilities.testString;
import static com.obisteeves.meetuworld.Utils.Utilities.valeurString;

/**
 * Activity qui permet à un utilisateur d'ajouter un voyage
 */
public class AddTravel extends ActionBarActivity implements Observer {
    private Toolbar toolbar;
    private String idSelectionPays;
    private TextView fdateA, fdateD, error;
    private EditText textIn, ville;
    private Button buttonAdd;
    private LinearLayout container;
    private Spinner spinner;
    private User userCurrent;
    private ArrayList<String> tabPoi = new ArrayList<String>();
    private  HashMap<String,String> spinnerMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        try {
            userCurrent = getIntent().getExtras().getParcelable("user"); // récupération de l'utisateur en cours par Parcelable
        } catch (NullPointerException e) {
        }
        fdateA = (TextView) findViewById(R.id.DateArrivee);
        fdateD = (TextView) findViewById(R.id.DateDepart);
        ville = (EditText) findViewById(R.id.addVoyageVille);
        textIn = (EditText) findViewById(R.id.textin);
        buttonAdd = (Button) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container);
        error = ((TextView) findViewById(R.id.error));

        iniActionBar();
        ListDynamicPoi();
        ListPays();

    }

    /**
     * initialisation de la l'ActionBar
     */
    private void iniActionBar()
    {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#ffffff'>Ajouter un voyage</font></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        toolbar.setLogo(R.drawable.ic_logo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_travel, menu);
        return true;
    }

    /**
     * Action sur les boutons de l'ActionBar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_add:
                DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                idSelectionPays = spinnerMap.get(spinner.getSelectedItem().toString());
                                if (((testString(ville.getText().toString()) && testString(fdateA.getText().toString())
                                        && testString(fdateD.getText().toString())) != true)) {
                                    dialogPerso("veuillez remplir tous les champs", "Avertissement", "Retour", AddTravel.this);
                                } else if (tabPoi.isEmpty()) {
                                    dialogPerso("veuillez rentrer des POi", "Avertissement", "Retour", AddTravel.this);

                                } else {

                                    EnvoyerVoyage(idSelectionPays,
                                            ville.getText().toString(),
                                            fdateA.getText().toString(),
                                            fdateD.getText().toString(),
                                            tabPoi.toString()
                                    );
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Voulez-vous enregistrer ce voyage ?").setTitle("Avertissement").setPositiveButton("Oui", dialogClickListener1)
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
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(" Voulez-vous annuler la saisie du voyage ?").setTitle("Avertissement").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;

            case R.id.action_settings:
                // Settings option clicked.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * DatePicker servant à récupérer la date d'arrivée
     *
     * @param v
     */

    public void showDatePickerDialog(View v)
    {
        TextView dateA = ((TextView) findViewById(R.id.DateArrivee));
        DialogFragment newFragment = new DatePickerFragment(dateA);
        newFragment.show(getFragmentManager(), "datePicker");
        fdateA=dateA;
    }

    /**
     * DatePicker servant à récupérer la date de départ
     *
     * @param v
     */

    public void showDatePickerDialog2(View v)
    {
        TextView dateD = ((TextView) findViewById(R.id.DateDepart));
        DialogFragment newFragment2 = new DatePickerFragment(dateD);
        newFragment2.show(getFragmentManager(), "datePicker");
        fdateD=dateD;
    }


    /**
     * Fonction servant à récupérer la liste des pays depuis la Base de données
     */
    public void ListPays()
    {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.listPays);
        net.setUrl(address);
        net.send();
    }

    /**
     * Fonction qui écoute les réponses du serveur pour mettre à jour l'objet Observer
     * @param observable
     * @param data
     */

    public void update(Observable observable, Object data)
    {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OKlistPays);
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
                spinner = (Spinner) findViewById(R.id.listPays);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * Fonction qui permet de rajouter un EditText de manière dynamique pour rajouter des points
     * d'intérêts à un voyage
     */
    private void ListDynamicPoi()
    {
        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                if (valeurString(textIn.getText().toString(), AddTravel.this) ==true) {

                    boolean element = tabPoi.contains(textIn.getText().toString());
                    if (element == true) {
                        String titre,message,bouton;
                        message="POi déjà présent";
                        titre="Avertissement";
                        bouton="Retour";
                        dialogPerso(message, titre, bouton, AddTravel.this);
                    } else {
                        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.row, null);
                        final TextView textOut = (TextView) addView.findViewById(R.id.textout);
                        textOut.setText(textIn.getText().toString());
                        Button buttonRemove = (Button) addView.findViewById(R.id.remove);

                        buttonRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                tabPoi.remove(getPosition(tabPoi, textOut.getText().toString()));
                            }
                        });
                        container.addView(addView);
                        tabPoi.add(textIn.getText().toString());
                    }
                }
            }

        });

    }

    /**
     * Fonction qui permet d'enregistrer un voyage dans la base de données
     * @param idPays
     * @param ville
     * @param dateA
     * @param dateD
     * @param listPoi
     */

    private void EnvoyerVoyage(String idPays, String ville, String dateA, String dateD,String listPoi)
    {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageAjouterVoyage);
        net.setUrl(address);
        net.addParam("idPays", idPays);
        net.addParam("ville",ville);
        net.addParam("dateA",dateA);
        net.addParam("dateD",dateD);
        net.addParam("listPoi",listPoi);
        net.send();

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
                        myIntent.putExtra("user", userCurrent);
                        startActivity(myIntent);
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Votre voyage est bien  enregistre").setTitle("Info").setPositiveButton("Continuer", dialogClickListener).show();
    }

}

