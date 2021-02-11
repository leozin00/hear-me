package com.example.lo.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

public class Transicao {
    public void Trasicao(Context origem, Class destino, DrawerLayout drawer){
        Intent intent = new Intent(origem,destino);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK   );
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(origem, R.anim.trasicao_tela, R.anim.mover_centro);
        ActivityCompat.startActivity(origem, intent, activityOptionsCompat.toBundle());
        drawer.closeDrawer(GravityCompat.START);
    }
}
