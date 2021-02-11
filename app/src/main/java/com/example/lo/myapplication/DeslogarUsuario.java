package com.example.lo.myapplication;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;


public class DeslogarUsuario {

    public void deslogarUsuario(FirebaseAuth autentificador, Context context){
        autentificador.signOut();
        Intent intent = new Intent(context, Slide.class);
        context.startActivity(intent);

    }
}
