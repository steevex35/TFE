package com.obisteeves.meetuworld.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;

import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import static com.obisteeves.meetuworld.Utils.Utilities.inputStreamToString;;


/**
 * Created by Steeves on 15-02-15.
 */
public class NetworkRequestAdapter extends Observable{

    /**
     * Suite des parametres a envoyer en post
     */
    private List<BasicNameValuePair> nameValuePairs;
    /**
     * Url en cours de requête
     */
    private String url;


    public static final String NO_ERROR = "0";

    public static final int NEW = 0;
    public static final int WAITING = 1;
    public static final int FINISH = 2;

    public static final int OK  = 23;
    public static final int OKinscription = 24;
    public static final int OKlistPays = 25;


    /**
     * Erreur, réponse correcte impossible
     */
    public final static int ERROR = 9;

    /**
     * Etat de l'adapter {@linkplain #NEW} | {@linkplain #WAITING} | {@linkplain #FINISH}}
     */
    private int state;

    /**
     * Présence d'une barre de chargement ou pas
     */
    private boolean progress = true;

    /**
     * code d'erreur renvoyé par le serveur
     */
    private String error ="";

    /**
     * Source demandant les services de l'adapter
     */
    private Activity client;
    /**
     * Retour de la requête
     */
    private JSONObject result;

    /**
     * Classe servant pour la gestion des cookies
     */
    private static CookieStore cookieStore = new BasicCookieStore();
    /**
     * Utile pour la gestion des coockies
     */
    private static HttpContext localContext = new BasicHttpContext();

    /**
     * Constructor
     * @param c source
     */
    public NetworkRequestAdapter(Activity c) {
        nameValuePairs = new ArrayList<BasicNameValuePair>();
        state = NEW;
        client = c;

    }

    /**
     * Constructor
     * @param c source
     * @param url addresse a contacter
     */
    public NetworkRequestAdapter(Activity c, String url) {
        this(c);

        this.url = url;

    }

    /**
     * Constructor
     * @param c source
     * @param url adresse a contacter
     * @param param Liste des paramettres
     */
    public NetworkRequestAdapter(Activity c, String url, Collection<BasicNameValuePair> param){
        this(c, url);

        nameValuePairs = new ArrayList<BasicNameValuePair>();
    }

    /**
     * Set l'adresse ou envoyer la requête
     * @param url l'adresse à contacter
     */
    public void setUrl(String url){
        if(state == NEW)
            this.url = url;
    }

    /**
     * Ajoute un paramettre a envoyer en post
     * @param name nom du param
     * @param value valeur a envoyer en post
     * @return true si l'ajout a fonctionné
     */
    public boolean addParam(String name, String value){
        return addParam(new BasicNameValuePair(name, value));
    }

    /**
     * Ajoute un paramettre à envoyer en post
     * @param pair
     * @return
     */
    public boolean addParam(BasicNameValuePair pair){
        if(state == NEW){
            nameValuePairs.add(pair);
            return true;
        }else
            return false;
    }

    /**
     * Affiche ou cache la barre de progression
     * @param visib
     */
    public void setProgressVisible(boolean visib){
        progress = visib;
    }


    /**
     * Renvoie une liste de paires corespondant aux parametres de la requête
     * @return les parametres
     */
    public List<BasicNameValuePair> getPairs(){
        return nameValuePairs;

    }

    /**
     * Envoie la requête a condition qu'elle possède au moins une url
     * et qu'une autre requête ne soit pas en attente
     */
    public void send(){
        if( (url != null) && (state == NEW || state == FINISH)){
            state = WAITING; //changement d'état
            BckgrndTask task = new BckgrndTask();
            task.execute();
        }
    }


    /**
     * Résultat de la requête
     */
    public JSONObject getResult() {
        return result;
    }

    /**
     * Code d'erreur
     */
    public String getError() {
        return error;
    }

    /**
     * Retourne l'état de l'adapter.
     * New : Nouvellement initialisé
     * Waiting : En attente d'une réponse
     * Finish : Résultat obtenu
     */
    public int getState(){
        return state;
    }

    /**
     * Reset tout l'adapteur.
     */
    public void reset(){
        nameValuePairs = new ArrayList<BasicNameValuePair>();
        state = NEW;
        url = "";
        error = "";
        result = null;
    }

    /**
     * Effectue l'envoi en post
     * @throws IOException si probleme de connexion
     */
    private InputStream post() throws IOException{
        // Create a new HttpClient and Post Header
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        // Bind custom cookie store to the local context
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Execute HTTP Post Request
        return httpclient.execute(httppost
                , localContext
        ).getEntity().getContent();


    }

    /**
     * Classe interne permettant de délocaliser l'exécution de la requête sur un autre thread
     * @author Peri
     *
     */
    private class BckgrndTask  extends AsyncTask<Void, Void, Void>{

        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(progress)
                client.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog = ProgressDialog.show(client, "Chargement", "Patientez", false, true);
                    }
                });

        }

        @Override
        protected Void doInBackground(Void... params) {
            String jsonResult = "";
            try {
                jsonResult = inputStreamToString(post()).toString();
                if(jsonResult.length() != 0){
                    result = new JSONObject(jsonResult);
                    error = result.getString("return");
                }
                else{
                    result = new JSONObject();
                    error = "no result";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                error = "Server Error";
            } catch (IOException e) {
                e.printStackTrace();
                error = "Connection error";
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                state = FINISH;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void json) {
            super.onPostExecute(json);
            setChanged();
            if(pDialog != null)
                client.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        pDialog.dismiss();
                        notifyObservers(error);
                    }
                });

            clearChanged();

        }

    }




}
