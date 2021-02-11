package com.example.lo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

public class Diario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navegador;
    private int clickBreathe;
    private FirebaseAuth autentificador;
    private SharedPreferences.Editor editor;
    private String identificadorUsuario;
    private String identificadorDestinatario;
    private FirebaseFirestore firebase;
    private ImageView imgEnviarDiario;
    private EditText txtMensagemDiario;
    private String idUsuario;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private String emailContato;
    private ListView lvDiario;
    public ArrayList<String> usuarios;
    private String url;
    private ImageView imgFotoMenu;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerLayoutDiario);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navegador = findViewById(R.id.navegadorDiario);
        navegador.setNavigationItemSelectedListener(this);

        ImageView logo = findViewById(R.id.logoBarra2);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Typeface fonteTitulo = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/LeagueSpartan-Bold.otf");
        TextView lblTituloDiario = findViewById(R.id.lblTituloDiario);

        lblTituloDiario.setTypeface(fonteTitulo);

        txtMensagemDiario = findViewById(R.id.txtMensagemDiario);
        imgEnviarDiario = findViewById(R.id.imgEnviarDiario);
        lvDiario = findViewById(R.id.lvDiario);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        autentificador = ConfiguracaoFirebase.getAutentificador();
        firebase  = ConfiguracaoFirebase.getFirebase();

        emailContato = Objects.requireNonNull(autentificador.getCurrentUser()).getEmail();
        if(emailContato != null){
            identificadorUsuario = CodificarBase64.codificarTexto(emailContato);

        }

        imgFotoMenu = findViewById(R.id.imgFotoDiario);
        carregarFotoPerfil();

        usuarios = new ArrayList<>();

        firebase.collection("Usuario").whereEqualTo("tipoUsuario", "Ouvinte").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){

                        String usuario = document.get("email").toString();
                        usuarios.add(usuario);

                    }

                    int contadorOuvinte = usuarios.size();
                    int numeroAleatorio = new Random().nextInt(contadorOuvinte);

                    List<String> randomStudentList = new ArrayList<>();
                    for(int i = 1; i <= 10; i++) {
                        randomStudentList.add(usuarios.get(numeroAleatorio));
                    }

                    identificadorDestinatario = CodificarBase64.codificarTexto(randomStudentList.get(numeroAleatorio));

                    mensagens = new ArrayList<>();
                    adapter = new MensagemAdapter(getApplicationContext(), mensagens);
                    lvDiario.setAdapter(adapter);

                    carregarFirestore();

                    firebase.collection("Usuario").document(identificadorUsuario).collection("mensagem Diario").document(identificadorUsuario).collection(identificadorDestinatario).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {

                                return;
                            }

                            Mensagem pegarMensagem;

                            List<Mensagem> mensagens = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("mensagem") != null) {
                                    pegarMensagem = doc.toObject(Mensagem.class);
                                    mensagens.add(pegarMensagem);
                                }
                            }
                            carregarFirestore();

                        }
                    });

                    imgEnviarDiario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String textoMensagem = txtMensagemDiario.getText().toString();
                            if(textoMensagem.isEmpty()){
                                Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Digite algo para enviar sua mensagem", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            }else{

                                Mensagem mensagem = new Mensagem();
                                mensagem.setIdUsuario(idUsuario);
                                mensagem.setMensagem(textoMensagem);

                                salvarMensagem(identificadorUsuario, identificadorDestinatario, textoMensagem);
                                salvarMensagem(identificadorDestinatario, identificadorUsuario, textoMensagem);

                                Toast.makeText(getApplicationContext(), identificadorUsuario + " | " + identificadorDestinatario , Toast.LENGTH_SHORT).show();

                                txtMensagemDiario.setText("");

                                carregarFirestore();
                            }

                        }
                    });


                }

            }
        });


    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawerLayoutDiario);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.sair){
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
            transicao.Trasicao(getApplicationContext(), VerProprioPerfil.class, drawer);
        } else if (id == R.id.rede) {

            transicao.Trasicao(getApplicationContext(), MainActivity.class, drawer);
        } else if (id == R.id.diario) {


        } else if (id == R.id.breathe){
            MostrarMenuBreathe mostrar = new MostrarMenuBreathe();
            clickBreathe = mostrar.MostrarMenuBreathe(navegador,clickBreathe);

        } else if (id == R.id.calendario) {
            transicao.Trasicao(getApplicationContext(), Calendario.class, drawer);
        } else if (id == R.id.sons) {
            transicao.Trasicao(getApplicationContext(),Sons.class,drawer);
        }else if(id == R.id.acalme_se){
            transicao.Trasicao(getApplicationContext(),Acalme_se.class,drawer);
        }else if(id == R.id.gif){
            transicao.Trasicao(getApplicationContext(),GifRespiracao.class,drawer);
        }
        return true;
    }


    private void carregarFirestore(){

        firebase.collection("Usuario").document(identificadorUsuario).collection("mensagem Diario").document(identificadorUsuario).collection(identificadorDestinatario).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    mensagens.clear();

                    for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        Mensagem pegarMensagem = document.toObject(Mensagem.class);
                        mensagens.add(pegarMensagem);
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private boolean salvarMensagem(String identificadorUsuario,String identificadorDestinatario, String mensagem){
        try{
            Date date = new Date();
            String cod = String.valueOf(date.getTime());
            Mensagem usuario = new Mensagem();
            usuario.setIdUsuario(idUsuario);
            usuario.setMensagem(mensagem);


            firebase.collection("Usuario").document(identificadorUsuario).collection("mensagem Diario").document(identificadorUsuario).collection(identificadorDestinatario).document(cod).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            return true;
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            return false;
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
