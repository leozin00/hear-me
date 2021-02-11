package com.example.lo.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public final class ConfiguracaoFirebase {

    private static FirebaseFirestore referenciaFirebase;
    private static FirebaseAuth autentificador;

    public static FirebaseFirestore getFirebase() {
        if (referenciaFirebase == null){
            referenciaFirebase = FirebaseFirestore.getInstance();

        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getAutentificador(){
        if(autentificador == null){
            autentificador = FirebaseAuth.getInstance();
        }

        return autentificador;
    }
}
