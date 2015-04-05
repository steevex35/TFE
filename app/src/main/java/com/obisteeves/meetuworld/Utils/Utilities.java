package com.obisteeves.meetuworld.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import com.obisteeves.meetuworld.PageAndroid.ConnectionPage;
import com.obisteeves.meetuworld.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by Steeves on 07-02-15.
 */



public class Utilities {

    private static final HashMap<String, Integer> ADVERT_TYPES = new HashMap<String, Integer>();
    private static final SparseArray<String> ERRORS_MSG = new SparseArray<String>();


    public static void enter(Class<?> activity, Activity current){
        Intent toLaunch = new Intent(current, activity); //préparation de la page suivante
        current.startActivity(toLaunch);
    }


    public static StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public static HashMap<String, Integer> getAdvertTypes() {
        return ADVERT_TYPES;
    }

    public static String[] getAdvertTypesString(){
        String[] result = new String[ADVERT_TYPES.size()];
        int i = 0;
        for(String s : ADVERT_TYPES.keySet()){
            result[i] = s;
            i++;
        }
        return result;
    }

    public static void fillAdvertTable(JSONObject object) {
        try {
            Iterator<?> ite = object.keys();
            while(ite.hasNext()){
                String i = ite.next().toString();
                ADVERT_TYPES.put(object.getString(i), Integer.parseInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void fillErrorsTable(JSONObject object) {
        try {
            Iterator<?> ite = object.keys();
            while(ite.hasNext()){
                String i = ite.next().toString();
                ERRORS_MSG.put(Integer.parseInt(i), object.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static SparseArray<String> getErrorMsgs(){
        return ERRORS_MSG;
    }

    /**
     * Affiche un dialog avec message de validation
     * @param title ressource vers le titre
     * @param msg ressource vers le msg
     */
    public static void agreed(String title, String msg, Activity source){
        new AlertDialog.Builder(source)
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();

                    }
                })
                .show();
    }

    public static void agreed(int title, int msg, Activity source){
        agreed(source.getResources().getString(title)
                ,source.getResources().getString(msg)
                ,source);
    }

    /**
     * Affiche un un dialog avec les erreurs contenue dans l'adapter
     * @param net un {@linkplain NetworkRequestAdapter} ayant reçu une réponse
     */
    public static void showErrorMessages(NetworkRequestAdapter net, Activity source){
        try {
            if(net.getResult().get("return") instanceof JSONArray){
                JSONArray o = (JSONArray)net.getResult().get("return");

                StringBuilder str = new StringBuilder();
                for(int i = 0; i<o.length(); i++)
                    str.append(Utilities.getErrorMsgs().get(o.getInt(i)) + '\n');
                Utilities.agreed(source.getResources().getString(R.string.failed)
                        , str.toString()
                        , source);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean valeurString(String data,Activity classe) {
        if (data != null && !data.isEmpty())
            return true;
        else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            dialog.cancel();
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(classe);
            builder.setMessage("Champs vide").setTitle("Avertissement")
                    .setPositiveButton("Retour", dialogClickListener).show();
            return false;
        }


    }
    public static boolean testString(String data) {
        if (data != null && !data.isEmpty())
            return true;
        else {

            return false;
        }


    }



    public static int getPosition(ArrayList tab,String element){
     return tab.indexOf(element);
 }


    public static void dialogPerso(String message , String titre ,String nomBoutton,Activity classe) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            dialog.cancel();
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(classe);
            builder.setMessage(message).setTitle(titre)
                    .setPositiveButton(nomBoutton, dialogClickListener).show();



    }





}
