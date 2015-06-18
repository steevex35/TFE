package com.obisteeves.meetuworld.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
                , source.getResources().getString(msg)
                , source);
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

    public static boolean testStringInscription(String data1, String data2, String data3, String data4,
                                                String data5, String data6) {
        if (data1 != null && !data1.isEmpty() && (data2 != null && !data2.isEmpty()) &&
                (data3 != null && !data3.isEmpty()) && (data4 != null && !data4.isEmpty()) &&
                (data5 != null && !data5.isEmpty()) && (data6 != null && !data6.isEmpty())) {
            return true;
        } else
            return false;
    }

    public static boolean testString(String data1, String data2, String data3) {
        if (data1 != null && !data1.isEmpty() && (data2 != null && !data2.isEmpty()) &&
                (data3 != null && !data3.isEmpty()))
            return true;
        else
            return false;
    }

    public static boolean testStringInscription(String data1, String data2, String data3, String data4) {
        if (data1 != null && !data1.isEmpty() && (data2 != null && !data2.isEmpty()) &&
                (data3 != null && !data3.isEmpty()) && (data4 != null && !data4.isEmpty())) {
            return true;
        } else
            return false;
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
