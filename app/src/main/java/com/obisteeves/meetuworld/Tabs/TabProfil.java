package com.obisteeves.meetuworld.Tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.obisteeves.meetuworld.PageAndroid.HomePage;
import com.obisteeves.meetuworld.PageAndroid.ModifierProfil;
import com.obisteeves.meetuworld.R;
import com.obisteeves.meetuworld.Utils.DownloadImageTask;
import com.obisteeves.meetuworld.Utils.NetworkRequestAdapter;
import com.obisteeves.meetuworld.Utils.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * Fragment servant à afficher le profil utilisateur
 */
public class TabProfil extends Fragment implements Observer {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView avatar;
    private ImageButton boutonAlter;
    private String mCurrentPhotoPath;
    private File photoFile = null;
    private File image;
    private User userCurrent;
    private RatingBar note;
    private Bitmap imageBitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tab_profil, container, false);
        HomePage homeActivity = (HomePage) getActivity();
        note = (RatingBar) v.findViewById(R.id.profil_points);
        note.setRating(4);
        userCurrent = homeActivity.getUser();
        try {
            ((TextView) v.findViewById(R.id.profil_nomPrenom)).setText(userCurrent.getmPrenom() + " " + userCurrent.getmNom());
            ((TextView) v.findViewById(R.id.profil_villePays)).setText(userCurrent.getmPays() + ", " + userCurrent.getmVille());
            ((TextView) v.findViewById(R.id.email_profil)).setText("Email : " + userCurrent.getmEmail());
            ((TextView) v.findViewById(R.id.profil_age)).setText("Âge : " + userCurrent.getmAge() + " ans");
            ((TextView) v.findViewById(R.id.profil_membre)).setText("Membre depuis : " + userCurrent.getmInscription());
            ((TextView) v.findViewById(R.id.profil_nombreDeVoyage)).setText("Nombre de voyages : " + userCurrent.getmNbVoyages());
            ((TextView) v.findViewById(R.id.profil_nombreFoisGuide)).setText("Nombre de fois guide : " + userCurrent.getmNbGuides());


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        avatar = (ImageView) v.findViewById(R.id.avatar);
        boutonAlter = (ImageButton) v.findViewById(R.id.modifier_profil);
        boutonAlter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //page de modif du profil
                Intent intent = new Intent(getActivity(), ModifierProfil.class);
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
        new DownloadImageTask(avatar).execute("http://www.l4h.be/TFE/android/Outils/avatars/" + userCurrent.getmId() + ".jpg");

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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        return image;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] image = stream.toByteArray();
            String avatar_string = Base64.encodeToString(image, Base64.DEFAULT);
            upload_avatar(avatar_string);
            //System.out.println(avatar_string);
        }
    }

    /**
     * Function qui envoie l'image capturé en base64 au serveur PHP
     * @param avatar_str
     */
    public void upload_avatar(String avatar_str) {
        NetworkRequestAdapter net = new NetworkRequestAdapter(this.getActivity());
        net.addObserver(this);
        String address = getResources().getString(R.string.serveurAdd)
                + getResources().getString(R.string.upload_avatar);
        net.setUrl(address);
        net.addParam("avatarBase64", avatar_str);
        net.send();
    }

    /**
     *
     * @param observable
     * @param data
     */

    public void update(Observable observable, Object data) {

    }

}