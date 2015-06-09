package com.obisteeves.meetuworld.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Steeves on 09-06-15.
 */
public class Voyage implements Parcelable {
    public static final Creator<Voyage> CREATOR = new Creator<Voyage>() {
        @Override
        public Voyage createFromParcel(Parcel source) {
            return new Voyage(source);
        }

        @Override
        public Voyage[] newArray(int size) {
            return new Voyage[size];
        }
    };
    private String id_voyage;
    private String id_auteur;
    private String nom;
    private String pays;
    private String ville;
    private String dateA;
    private String dateD;
    private ArrayList<String> listPoi = new ArrayList<String>();

    public Voyage(String id_voyage, String id_auteur, String nom, String pays, String ville, String dateA, String dateD, ArrayList<String> listPoi) {
        this.id_voyage = id_voyage;
        this.id_auteur = id_auteur;
        this.nom = nom;
        this.pays = pays;
        this.ville = ville;
        this.dateA = dateA;
        this.dateD = dateD;
        this.listPoi = listPoi;
    }

    public Voyage(String id_voyage, String id_auteur, String pays, String ville, String dateA, String dateD) {
        this.id_voyage = id_voyage;
        this.id_auteur = id_auteur;
        this.nom = nom;
        this.pays = pays;
        this.ville = ville;
        this.dateA = dateA;
        this.dateD = dateD;
        this.listPoi = listPoi;
    }

    public Voyage(Parcel source) {
        id_voyage = source.readString();
        id_auteur = source.readString();
        nom = source.readString();
        pays = source.readString();
        ville = source.readString();
        dateA = source.readString();
        dateD = source.readString();
        listPoi = source.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_voyage);
        dest.writeString(id_auteur);
        dest.writeString(nom);
        dest.writeString(pays);
        dest.writeString(ville);
        dest.writeString(dateA);
        dest.writeString(dateD);
        dest.writeList(listPoi);
    }

    public String getId_voyage() {
        return id_voyage;
    }

    public void setId_voyage(String id_voyage) {
        this.id_voyage = id_voyage;
    }

    public String getId_auteur() {
        return id_auteur;
    }

    public void setId_auteur(String id_auteur) {
        this.id_auteur = id_auteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDateA() {
        return dateA;
    }

    public void setDateA(String dateA) {
        this.dateA = dateA;
    }

    public String getDateD() {
        return dateD;
    }

    public void setDateD(String dateD) {
        this.dateD = dateD;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public ArrayList<String> getListPoi() {
        return listPoi;
    }

    public void setListPoi(ArrayList<String> listPoi) {
        this.listPoi = listPoi;
    }
}
