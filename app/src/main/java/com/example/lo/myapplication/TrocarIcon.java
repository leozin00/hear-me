package com.example.lo.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class TrocarIcon {

    private ImageView img ;
    private String idDrawable;

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public String getIdDrawable() {
        return idDrawable;
    }

    public void setIdDrawable(String idDrawable) {
        this.idDrawable = idDrawable;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;




    public TrocarIcon() {
    }


    public void TrocarIconBranco(ImageView img, String idDrawable, Context context, ImageView imgAntiga, String Antiga){

        int id = context.getResources().getIdentifier(idDrawable + "2", "drawable", context.getPackageName());

        Drawable drawable= context.getResources().getDrawable(id) ;
        img.setImageDrawable(drawable);
        if(imgAntiga != null) {

            int x = context.getResources().getIdentifier(Antiga, "drawable", context.getPackageName());
            Drawable drawablee = context.getResources().getDrawable(x);
            imgAntiga.setImageDrawable(drawablee);

        }
    }
    public void TrocarIconPreto(ImageView img, String idDrawable, Context context){

        int id = context.getResources().getIdentifier(idDrawable , "drawable", context.getPackageName());

        Drawable drawable= context.getResources().getDrawable(id) ;
        img.setImageDrawable(drawable);
    }
    public void TrocarIconAntigo(){

    }
}
