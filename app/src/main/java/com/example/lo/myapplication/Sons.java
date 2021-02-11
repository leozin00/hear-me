package com.example.lo.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;


public class Sons extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private ImageView imgMar;
    private ImageView imgVentania;
    private ImageView imgTempestade;
    private ImageView imgRelax;
    private ImageView imgAgua;
    private ImageView imgFloresta;
    private TextView lblMar;
    private TextView lblVentania;
    private TextView lblTempestade;
    private TextView lblRelax;
    private TextView lblAgua;
    private TextView lblFloresta;
    private TextView lblSons;
    private SharedPreferences naoMostrar;
    private SharedPreferences.Editor editor;
    private FirebaseAuth autentificador;
    private ImageView logo;
    private MediaPlayer musica;
    private int contador;
    private int x = 1;
    private int n = 0;
    private int clickBreathe;
    private Typeface fonteTitulo;
    private Typeface fonteTexto;
    public NavigationView navegador;
    private DrawerLayout drawer;
    private TrocarIcon trocar;
    private ImageView imgAntiga;
    private String Antiga;
    private String idUsuario;
    private String url;
    private ImageView imgFotoMenu;
    private FirebaseFirestore firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sons);

        autentificador.getInstance();


        //criação toolbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase();

        navegador = findViewById(R.id.nav_view2);
        navegador.setNavigationItemSelectedListener(this);

        logo = findViewById(R.id.logoBarra2);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        imgFotoMenu = findViewById(R.id.imgFotoSons);
        carregarFotoPerfil();


        naoMostrar = getSharedPreferences("NAO_MOSTRAR", MODE_PRIVATE);
        editor = naoMostrar.edit();
        trocar = new TrocarIcon();

        lblSons = findViewById(R.id.lblSons);
        lblAgua = findViewById(R.id.lblAgua);
        lblFloresta =  findViewById(R.id.lblFloresta);
        lblMar =  findViewById(R.id.lblMar);
        lblRelax = findViewById(R.id.lblRelax);
        lblTempestade = findViewById(R.id.lblTempestade);
        lblVentania = findViewById(R.id.lblVentania);
        imgFloresta =  findViewById(R.id.imgFloresta);
        imgAgua =  findViewById(R.id.imgAgua);
        imgMar = findViewById(R.id.imgMar);
        imgTempestade =  findViewById(R.id.imgTempestade);
        imgVentania =  findViewById(R.id.imgVentania);
        imgRelax =  findViewById(R.id.imgRelax);

        fonteTitulo = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
        fonteTexto = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/aileron.heavy.otf");

        //colocando fonte
        lblAgua.setTypeface(fonteTexto);
        lblFloresta.setTypeface(fonteTexto);
        lblMar.setTypeface(fonteTexto);
        lblRelax.setTypeface(fonteTexto);
        lblTempestade.setTypeface(fonteTexto);
        lblVentania.setTypeface(fonteTexto);
        lblSons.setTypeface(fonteTitulo);

        imgFloresta.setOnClickListener(this);
        imgAgua.setOnClickListener(this);
        imgMar.setOnClickListener(this);
        imgTempestade.setOnClickListener(this);
        imgVentania.setOnClickListener(this);
        imgRelax.setOnClickListener(this);

    }

    //click som
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgAgua:
                x = 1;
                if (n != x) {
                    trocar.TrocarIconBranco(imgAgua,"icon_gotas",getApplicationContext(),imgAntiga,Antiga);
                    imgAntiga  = imgAgua;
                    Antiga = "icon_gotas";
                    n = 1;
                    stop();
                    musica = MediaPlayer.create(this, R.raw.chuva);
                    tempo();
                    play();


                }else{
                    stop();
                    trocar.TrocarIconPreto(
                    imgAgua,"icon_gotas",getApplicationContext());
                    n = 0;
                    imgAntiga = null;
                }

                break;
            case R.id.imgMar:
                x = 2;
                if (n != x) {
                    n = 2;
                    trocar.TrocarIconBranco(imgMar,"icon_mar",getApplicationContext(),imgAntiga,Antiga);
                    imgAntiga = imgMar;
                    Antiga = "icon_mar";
                    stop();
                    musica = MediaPlayer.create(this, R.raw.mar);
                    play();
                    tempo();


                }else{
                   stop();
                    n = 0;
                    trocar.TrocarIconPreto(imgMar,"icon_mar",getApplicationContext());
                    imgAntiga = null;
                }

                break;
            case R.id.imgTempestade:
                x = 3;
                if (n != x) {
                    trocar.TrocarIconBranco(imgTempestade,"icon_tempestade",getApplicationContext(),imgAntiga,Antiga);
                    imgAntiga  = imgTempestade;
                    Antiga = "icon_tempestade";
                    n = 3;
                    tempo();
                    stop();
                    musica = MediaPlayer.create(this, R.raw.tempestade);
                    play();

                }else{
                    stop();
                    n = 0;
                    trocar.TrocarIconPreto(imgTempestade,"icon_tempestade",getApplicationContext());
                    imgAntiga = null;
                }

                break;
            case R.id.imgVentania:
                x = 4;
                if (n != x) {
                    trocar.TrocarIconBranco(imgVentania,"icon_ventania",getApplicationContext(),imgAntiga,Antiga);
                    imgAntiga = imgVentania;
                    Antiga = "icon_ventania";
                    n = 4;
                    stop();
                    musica = MediaPlayer.create(this, R.raw.ventania);
                    play();
                    tempo();
                }else{
                    stop();
                    n = 0;
                    trocar.TrocarIconPreto(imgVentania,"icon_ventania",getApplicationContext());
                    imgAntiga = null;
                }

                break;
            case R.id.imgFloresta:
                x = 5;
                if (n != x) {
                    trocar.TrocarIconBranco(imgFloresta,"icon_floresta",getApplicationContext(),imgAntiga,Antiga);
                    imgAntiga  = imgFloresta;
                    Antiga = "icon_floresta";
                    n = 5;
                    tempo();
                    stop();
                    musica = MediaPlayer.create(this, R.raw.floresta);
                    play();
                }else{
                    stop();
                    n = 0;
                    trocar.TrocarIconPreto(imgFloresta,"icon_floresta",getApplicationContext());
                    imgAntiga = null;
                }

                break;
            case R.id.imgRelax:
                x = 6;
                if (n != x) {
                    trocar.TrocarIconBranco(imgRelax,"icon_relax",getApplicationContext(),imgAntiga,Antiga);
                    imgAntiga  = imgRelax;
                    Antiga = "icon_relax";
                    n = 6;
                    tempo();
                    stop();
                    musica = MediaPlayer.create(this, R.raw.relax);
                    play();
                }else{
                     stop();
                    n = 0;
                    trocar.TrocarIconPreto(imgRelax,"icon_relax",getApplicationContext());
                    imgAntiga = null;
                }

                break;
        }

    }



    //parar som
    public void stop(){
        try{
            if(musica != null) {
                if (musica.isPlaying()) {
                    musica.stop();
                    musica.release();


                }
            }
        }catch (Exception e){

        }

    }

    //play som
    public void play(){
        musica.setLooping(true);
        if (!musica.isPlaying()){
            musica.start();





        }else{
            musica.pause();

        }
    }

    //temporizador som
    public void tempo() {
        if(naoMostrar.getInt("NAO_MOSTRAR_NOVAMENTE" , 0) == 0){

            final CharSequence[] array = new CharSequence[]{"5 minutos", "10 minutos", "30 minutos", "1 hora", "Deixar tocando", "Não mostrar novamente"};
            final AlertDialog.Builder caixaSons = new AlertDialog.Builder(this);
            caixaSons.setTitle("Tempo").setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    contador = i;

                }
            }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    switch (contador) {
                        case 0:
                            new CountDownTimer(30000, 30000) {

                                public void onTick(long millisUntilFinished) {
                                }
                                public void onFinish() {
                                    //stop();
                                }
                            }.start();

                            break;
                        case 1:
                            new CountDownTimer(600000, 600000) {

                                public void onTick(long millisUntilFinished) {
                                }
                                public void onFinish() {
                                    //stop();
                                }
                            }.start();

                            break;
                        case 2:
                            new CountDownTimer(1800000, 1800000) {

                                public void onTick(long millisUntilFinished) {
                                }
                                public void onFinish() {
                                    //stop();
                                }
                            }.start();

                            break;
                        case 3:
                            new CountDownTimer(3600000, 3600000) {

                                public void onTick(long millisUntilFinished) {
                                }
                                public void onFinish() {
                                    //stop();
                                }
                            }.start();

                            break;
                        case 4:

                            break;
                        case 5:

                            editor.putInt("NAO_MOSTRAR_NOVAMENTE", 1);
                            editor.apply();

                            break;
                    }

                }
            });
            caixaSons.create().show();

        }else{

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.sair ){
            editor.putInt("PRIMEIRA_VEZ_USUARIO",0).apply();
            autentificador.signOut();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Transicao transicao = new Transicao();
        int id = item.getItemId();

        if (id == R.id.perfil) {
            transicao.Trasicao(getApplicationContext(), VerProprioPerfil.class,drawer);
        } else if (id == R.id.rede) {

            transicao.Trasicao(getApplicationContext(), MainActivity.class,drawer);
        } else if (id == R.id.diario) {
            transicao.Trasicao(getApplicationContext(),Diario.class,drawer);

        } else if (id == R.id.breathe) {
            MostrarMenuBreathe mostrar = new MostrarMenuBreathe();
            clickBreathe = mostrar.MostrarMenuBreathe(navegador,clickBreathe);

        } else if (id == R.id.calendario) {
            transicao.Trasicao(getApplicationContext(),Calendario.class,drawer);
        } else if (id == R.id.sons) {

        }else if(id == R.id.acalme_se){
            transicao.Trasicao(getApplicationContext(),Acalme_se.class,drawer);

        }else if(id == R.id.gif){

            transicao.Trasicao(getApplicationContext(),GifRespiracao.class,drawer);
        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(naoMostrar.contains("NAO_MOSTRAR_NOVAMENTE")){
            editor.putInt("NAO_MOSTRAR_NOVAMENTE" , 0).apply();

        }

    }

    private void carregarFotoPerfil(){

        firebase.collection("Usuario").document(idUsuario).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                    FotoPerfil foto = new FotoPerfil();
                    foto.setFotoPerfil(Objects.requireNonNull(document.get("fotoPerfil")).toString());
                    url = foto.getFotoPerfil();

                }
                Glide.with(getApplicationContext()).load(url).into(imgFotoMenu);
            }
        });

    }
}