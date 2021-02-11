package com.example.lo.myapplication;

import android.support.design.widget.NavigationView;

public class MostrarMenuBreathe  {


    public int MostrarMenuBreathe( NavigationView navegador, int clickBreathe ) {

        if(clickBreathe == 0){
            clickBreathe = 1;
            navegador.getMenu().setGroupVisible(R.id.grupoBreathe, true);

        }else{
            navegador.getMenu().setGroupVisible(R.id.grupoBreathe, false);
            clickBreathe = 0;
        }
        return clickBreathe;

    }




}
