








package com.example.lo.myapplication;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VerProprioPerfil extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener{

    public SharedPreferences.Editor editor ;
    private FirebaseAuth autentificador;
    public NavigationView navegador;
    private DrawerLayout drawer;
    private int clickBreathe;
    private FirebaseFirestore firebase;
    private String idUsuario;
    private StorageReference storageReference;
    private ImageView logo;
    private Bundle bundle;
    private String usuarioPesquisadoString;
    private TextView lblNome;
    private TextView lblNomeReal;
    private TextView lblNomeUsu;
    private TextView lblTipo;
    private TextView lblTipoUsu;
    private TextView lblCidade;
    private TextView lblCidadeUsu;
    private TextView lblPostagens;
    private TextView lblInformacoes;
    private Usuario usuarioPesquisado;
    private Typeface fonteTitulo;
    private Typeface fonteTexto;
    private  ImageView imgPerfil;
    private ImageView imgAdicionar;
    private ImageView imgBloquear;
    private String idUsu;
    private Usuario usuario;
    private String amigo;
    private ArrayList<DadosPostagem> postagens;
    private ArrayAdapter<DadosPostagem> adapter;
    private ListView listPostagens;
    private  TextView lblVUverPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayoutVerUsuario);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navegador = findViewById(R.id.NavViewVerAmigo);
        navegador.setNavigationItemSelectedListener(this);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        storageReference = FirebaseStorage.getInstance().getReference();
        autentificador = ConfiguracaoFirebase.getAutentificador();
        firebase = ConfiguracaoFirebase.getFirebase();
        idUsu = autentificador.getCurrentUser().getEmail();



        //adicionando views da tela
        listPostagens = findViewById(R.id.lvTodosPostagens);
        ViewCompat.setNestedScrollingEnabled(listPostagens,true);

        lblNome = findViewById(R.id.lblVUNome);
        lblNomeReal = findViewById(R.id.lblVUNomeReal);
        lblNomeUsu = findViewById(R.id.lblVUNomeUsu);
        lblTipo = findViewById(R.id.lblVUTipo);
        lblTipoUsu = findViewById(R.id.lblVUTipoUsu);
        lblCidade = findViewById(R.id.lblVUCidade);
        lblCidadeUsu = findViewById(R.id.lblVUCidadeUsu);
        lblPostagens   = findViewById(R.id.lblPostagem);
        lblInformacoes = findViewById(R.id.lblVUInformacoes);

        imgPerfil = findViewById(R.id.imgVerUsuario);
        imgAdicionar = findViewById(R.id.iconeAdicionar);
        imgBloquear  = findViewById(R.id.iconeBloquear);
        fonteTitulo = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
        fonteTexto = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/aileron.heavy.otf");

        lblVUverPerfil  = findViewById(R.id.lblVUSeuPerfil);

        lblNome.setTypeface(fonteTitulo);
        lblTipo.setTypeface(fonteTitulo);
        lblCidade.setTypeface(fonteTitulo);
        lblNomeUsu.setTypeface(fonteTexto);
        lblNomeReal.setTypeface(fonteTexto);
        lblTipoUsu.setTypeface(fonteTexto);
        lblCidadeUsu.setTypeface(fonteTexto);
        lblPostagens.setTypeface(fonteTitulo);
        lblInformacoes.setTypeface(fonteTitulo);
        lblVUverPerfil.setTypeface(fonteTitulo);
        imgAdicionar.setVisibility(View.GONE);
        imgBloquear.setVisibility(View.GONE);




        logo = findViewById(R.id.logoBarra2);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        pegarImagem();
        postagens = new ArrayList<>();
        adapter = new PropriaPostagemAdapter(getApplicationContext(), postagens);
        listPostagens.setAdapter(adapter);


        firebase.collection("Usuario").document(CodificarBase64.codificarTexto(autentificador.getCurrentUser().getEmail())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               DocumentSnapshot doc = task.getResult();
               lblNome.setText(doc.get("nome").toString());
               lblCidade.setText(doc.get("cidade").toString());
               lblTipo.setText(doc.get("tipoUsuario").toString());
               }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        firebase.collection("Usuario").document(idUsuario).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String url = null;
                for(DocumentSnapshot document : task.getResult()){
                    FotoPerfil foto = new FotoPerfil();
                    foto.setFotoPerfil(document.get("fotoPerfil").toString());
                    url = foto.getFotoPerfil();

                }
                if(url != null) {
                    Glide.with(getApplicationContext()).load(url).into(imgPerfil);
                }
            }
        });




    }

    private void pegarImagem(){
        firebase.collection("Usuario").document(idUsuario).collection("Postagens").orderBy("horaPostagem", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                postagens.clear();
                for(DocumentSnapshot document : task.getResult()){
                    DadosPostagem dados = document.toObject(DadosPostagem.class);
                    postagens.add(dados);
                }

                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawerLayoutVerUsuario);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem itemPesquisa = menu.findItem(R.id.menu_pesquisa);
        /*
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(itemPesquisa);
        searchView.setQueryHint("Procurar usuario");
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));
        searchView.setIconifiedByDefault(false);
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.sair){
            DeslogarUsuario deslogarUsuario = new DeslogarUsuario();
            deslogarUsuario.deslogarUsuario(autentificador,getApplicationContext());
            editor.putInt("PRIMEIRA_VEZ_USUARIO",0).apply();

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Transicao transicao = new Transicao();
        int id = item.getItemId();

        if (id == R.id.perfil) {

        } else if (id == R.id.rede) {


        } else if (id == R.id.diario) {

            transicao.Trasicao(getApplicationContext(),Diario.class,drawer);
        } else if (id == R.id.breathe) {
            MostrarMenuBreathe mostrar = new MostrarMenuBreathe();
            clickBreathe = mostrar.MostrarMenuBreathe(navegador,clickBreathe);

        } else if (id == R.id.calendario) {
            transicao.Trasicao(getApplicationContext(),Calendario.class,drawer);
        } else if (id == R.id.sons) {
            transicao.Trasicao(getApplicationContext(),Sons.class,drawer);
        }else if(id == R.id.acalme_se){
            transicao.Trasicao(getApplicationContext(),Acalme_se.class,drawer);
        }else if(id == R.id.gif){
            transicao.Trasicao(getApplicationContext(),GifRespiracao.class,drawer);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }



}

























