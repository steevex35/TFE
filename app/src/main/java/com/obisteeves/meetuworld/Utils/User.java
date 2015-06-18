package com.obisteeves.meetuworld.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steeves.
 */
public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String mId;
    private String mNom;
    private String mPrenom;
    private String mAge;
    private String mEmail;
    private String mVille;
    private String mPays;
    private String mInscription;
    private String mNbVoyages;
    private String mNbGuides;


    public User(String id, String nom, String prenom, String age, String email, String ville, String pays, String inscription, String nbVoyages, String nbGuides) {
        super();
        this.mId = id;
        this.mNom = nom;
        this.mPrenom = prenom;
        this.mAge = age;
        this.mEmail = email;
        this.mVille = ville;
        this.mPays = pays;
        this.mInscription = inscription;
        this.mNbVoyages = nbVoyages;
        this.mNbGuides = nbGuides;
    }

    public User(Parcel in) {
        mId = in.readString();
        mNom = in.readString();
        mPrenom = in.readString();
        mAge = in.readString();
        mEmail = in.readString();
        mVille = in.readString();
        mPays = in.readString();
        mInscription = in.readString();
        mNbVoyages = in.readString();
        mNbGuides = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mNom);
        dest.writeString(mPrenom);
        dest.writeString(mAge);
        dest.writeString(mEmail);
        dest.writeString(mVille);
        dest.writeString(mPays);
        dest.writeString(mInscription);
        dest.writeString(mNbVoyages);
        dest.writeString(mNbGuides);
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmNom() {
        return mNom;
    }

    public void setmNom(String mNom) {
        this.mNom = mNom;
    }

    public String getmPrenom() {
        return mPrenom;
    }

    public void setmPrenom(String mPrenom) {
        this.mPrenom = mPrenom;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmVille() {
        return mVille;
    }

    public void setmVille(String mVille) {
        this.mVille = mVille;
    }

    public String getmPays() {
        return mPays;
    }

    public void setmPays(String mPays) {
        this.mPays = mPays;
    }

    public String getmInscription() {
        return mInscription;
    }

    public void setmInscription(String mInscription) {
        this.mInscription = mInscription;
    }

    public String getmNbVoyages() {
        return mNbVoyages;
    }

    public void setmNbVoyages(String mNbVoyages) {
        this.mNbVoyages = mNbVoyages;
    }

    public String getmNbGuides() {
        return mNbGuides;
    }

    public void setmNbGuides(String mNbGuides) {
        this.mNbGuides = mNbGuides;
    }


}
