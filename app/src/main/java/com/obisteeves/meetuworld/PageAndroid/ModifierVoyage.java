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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.DatePickerFragment;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.Voyage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static com.obisteeves.meetuworld.Utils.Utilities.dialogPerso;
import static com.obisteeves.meetuworld.Utils.Utilities.getPosition;
import static com.obisteeves.meetuworld.Utils.Utilities.testString;
import static com.obisteeves.meetuworld.Utils.Utilities.valeurString;

/**
 * Activity de modification de voyage
 */
public class ModifierVoyage extends ActionBarActivity implements Observer {

    private Button buttonAdd;
    private LinearLayout container;
    private Toolbar toolbar;
    private Voyage voyageUser;
    private TextView fdateA, fdateD;
    private EditText fville, textIn;
    private String id_voyage;
    private String id_auteur;
    private String id_current;
    private String nom;
    private String pays;
    private String ville;
    private String dateA;
    private String dateD;
    private ArrayList<String> listPoiLocal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_voyage);
        textIn = (EditText) findViewById(R.id.modifTextin);
        buttonAdd = (Button) findViewById(R.id.modifAdd);
        fdateA = (TextView) findViewById(R.id.modifDateArrivee);
        fdateD = (TextView) findViewById(R.id.modifDateDepart);
        fville = (EditText) findViewById(R.id.modifVoyageVille);
        container = (LinearLayout) findViewById(R.id.containermodif);

        iniActionBar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_voyage = extras.getString("id_voyage");
            id_auteur = extras.getString("id_auteur");
            id_current = extras.getString("userCurrent");
            nom = extras.getString("nom_user");
            pays = extras.getString("pays_user");
            ville = extras.getString("ville_user");
            dateA = extras.getString("dateA");
            dateD = extras.getString("dateD");
        }

        try {
            voyageUser = getIntent().getExtras().getParcelable("parcelable");
        } catch (NullPointerException e) {
        }

        ((EditText) findViewById(R.id.modifVoyageVille)).setText(voyageUser.getVille());
        ((TextView) findViewById(R.id.modifDateArrivee)).setText(voyageUser.getDateA());
        ((TextView) findViewById(R.id.modifDateDepart)).setText(voyageUser.getDateD());
        listPoiLocal = voyageUser.getListPoi();
        ListDynamicPoi();

        for (int i = 0; i < listPoiLocal.size(); i++) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.row, null);
            final TextView textOut = (TextView) addView.findViewById(R.id.textout);
            textOut.setText(listPoiLocal.get(i));

            Button buttonRemove = (Button) addView.findViewById(R.id.remove);
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout) addView.getParent()).removeView(addView);
                    listPoiLocal.remove(getPosition(listPoiLocal, textOut.getText().toString()));
                }
            });
            container.addView(addView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modifier_voyage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_valider:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                if (((testString(fville.getText().toString()) && testString(fdateA.getText().toString())
                                        && testString(fdateD.getText().toString())) != true)) {
                                    dialogPerso("veuillez remplir tous les champs", "Avertissement", "Retour", ModifierVoyage.this);
                                } else if (listPoiLocal.isEmpty()) {
                                    dialogPerso("veuillez rentrer des POi", "Avertissement", "Retour", ModifierVoyage.this);

                                } else {
                                    voyageUser.setListPoi(listPoiLocal);
                                    updateVoyage(
                                            voyageUser.getId_voyage(),
                                            fville.getText().toString(),
                                            fdateA.getText().toString(),
                                            fdateD.getText().toString(),
                                            listPoiLocal.toString());
                                    Intent intent = new Intent(ModifierVoyage.this, InfoVoyage.class);
                                    intent.putExtra("parcelable", voyageUser);
                                    intent.putExtra("id_voyage", id_voyage);
                                    intent.putExtra("id_auteur", id_auteur);
                                    intent.putExtra("userCurrent", id_current);
                                    intent.putExtra("pays_user", pays);
                                    intent.putExtra("ville_user", fville.getText().toString());
                                    intent.putExtra("dateA", fdateA.getText().toString());
                                    intent.putExtra("dateD", fdateD.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Confirmation les modifications").setTitle("Avertissement").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;

            case R.id.action_settings:
                // Settings option clicked.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void iniActionBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#ffffff'>Modifier voyage</font></center>"));
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796B")));
        toolbar.setLogo(R.drawable.ic_logo);
    }

    /**
     * DatePicker servant � r�cup�rer la date d'arriv�e
     *
     * @param v
     */
    public void showDatePickerDialog(View v) {
        TextView dateA = ((TextView) findViewById(R.id.modifDateArrivee));
        DialogFragment newFragment = new DatePickerFragment(dateA);
        newFragment.show(getFragmentManager(), "datePicker");
        fdateA = dateA;
    }

    /**
     *DatePicker servant � r�cup�rer la date de d�part
     * @param v
     */

    public void showDatePickerDialog2(View v) {
        TextView dateD = ((TextView) findViewById(R.id.modifDateDepart));
        DialogFragment newFragment2 = new DatePickerFragment(dateD);
        newFragment2.show(getFragmentManager(), "datePicker");
        fdateD = dateD;
    }

    /**
     *Fonction qui permet de rajouter un EditText de mani�re dynamique pour rajouter des points
     * d'int�r�ts � un voyage
     */
    private void ListDynamicPoi() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if (valeurString(textIn.getText().toString(), ModifierVoyage.this) == true) {

                    boolean element = listPoiLocal.contains(textIn.getText().toString());
                    if (element == true) {
                        String titre, message, bouton;
                        message = "POi d�j� pr�sent";
                        titre = "Avertissement";
                        bouton = "Retour";
                        dialogPerso(message, titre, bouton, ModifierVoyage.this);

                    } else {
                        LayoutInflater layoutInflater =
                                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.row, null);
                        final TextView textOut = (TextView) addView.findViewById(R.id.textout);
                        textOut.setText(textIn.getText().toString());

                        Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                        buttonRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                listPoiLocal.remove(getPosition(listPoiLocal, textOut.getText().toString()));
                            }
                        });
                        container.addView(addView);
                        listPoiLocal.add(textIn.getText().toString());
                    }
                }
            }

        });

    }

    /**
     * Fonction d'envoie de la mise � jour du voyage
     * @param idVoyage
     * @param ville
     * @param dateA
     * @param dateD
     * @param listPoi
     */
    private void updateVoyage(String idVoyage, String ville, String dateA, String dateD, String listPoi) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this);
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageModifierVoyage);
        net.setUrl(address);
        net.addParam("idVoyage", idVoyage);
        net.addParam("ville", ville);
        net.addParam("dateA", dateA);
        net.addParam("dateD", dateD);
        net.addParam("listPoi", listPoi);
        net.send();
    }


    @Override
    public void update(Observable observable, Object data) {

    }
}
