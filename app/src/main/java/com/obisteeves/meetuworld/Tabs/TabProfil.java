package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.HomePage;
import com.obisteeves.meetuworld.PageAndroid.modifierProfil;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class TabProfil extends Fragment implements Observer {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView avatar;
    private ImageButton boutonAlter;
    private String mCurrentPhotoPath;
    private File photoFile = null;
    private File image;
    private User userCurrent;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab_profil, container, false);
        //afficheProfil(v);
        HomePage homeActivity = (HomePage) getActivity();
        userCurrent = homeActivity.getUser();
        try {
            ((TextView) v.findViewById(R.id.profil_nomPrenom)).setText(userCurrent.getmPrenom() + " " + userCurrent.getmNom());
            ((TextView) v.findViewById(R.id.profil_villePays)).setText(userCurrent.getmPays() + ", " + userCurrent.getmVille());
            ((TextView) v.findViewById(R.id.profil_Email)).setText(userCurrent.getmEmail());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        avatar = (ImageView) v.findViewById(R.id.avatar);
        boutonAlter = (ImageButton) v.findViewById(R.id.modifier_profil);
        boutonAlter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //page de modif du profil
                Intent intent = new Intent(getActivity(), modifierProfil.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", userCurrent);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePicture();

            }
        });

        ((TextView) v.findViewById(R.id.profil_Email)).setText("");

        return v;

    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.avatar) {

            getActivity().getMenuInflater().inflate(R.menu.menu_avatar, menu);

        }

    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(imageBitmap);
        }
    }


    public void afficheProfil(View v) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.pageProfil);
        net.setUrl(address);
        net.send();
    }

    /**
     *
     * @param observable
     * @param data
     */

    public void update(Observable observable, Object data) {
        NetworkRequestAdapter resultat = ((NetworkRequestAdapter) observable);
        String netReq = String.valueOf(NetworkRequestAdapter.OK);
        if (data.toString().equals(netReq)) {

            //try {
            //String nom = resultat.getResult().get("nom").toString();
            //String prenom = resultat.getResult().get("prenom").toString();
            //String ville = resultat.getResult().get("ville").toString();
            //String pays = resultat.getResult().get("pays").toString();
            //((TextView) getActivity().findViewById(R.id.profil_nomPrenom)).setText(prenom + " " + nom);
            //((TextView) getActivity().findViewById(R.id.profil_villePays)).setText(pays + ", " + ville);
            //((TextView) getActivity().findViewById(R.id.profil_Email)).setText(resultat.getResult().get("email").toString());

            //} catch (JSONException e) {
            //e.printStackTrace();
            //}
        }

    }








}