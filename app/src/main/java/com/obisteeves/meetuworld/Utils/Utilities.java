package com.obisteeves.meetuworld.Utils;
import android.app.Activity;
import android.content.Intent;
/**
 * Created by Steeves on 07-02-15.
 */



public class Utilities {
    public static void enter(Class<?> activity, Activity current){
        Intent toLaunch = new Intent(current, activity); //préparation de la page suivante
        current.startActivity(toLaunch);
    }


}
