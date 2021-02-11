package com.example.lo.myapplication;

import android.util.Base64;

public class CodificarBase64 {

    public static String codificarTexto(String texto){
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)" , "");
    }
    public static String decodificarTexto(String textoCodificado){
        return new String(Base64.decode(textoCodificado,Base64.DEFAULT));
    }
}
