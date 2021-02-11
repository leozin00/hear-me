package com.example.lo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VerUsuario extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener{

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
    private  TextView lblVUverPerfil;
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

        bundle = getIntent().getExtras();

        //criando lista de postagens
        listPostagens = findViewById(R.id.lvTodosPostagens);
        postagens = new ArrayList<>();
        adapter = new PostagemAdapter(getApplicationContext(), postagens);
        listPostagens.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listPostagens,true);


            //adicionando views da tela
        lblNome = findViewById(R.id.lblVUNome);
        lblNomeReal = findViewById(R.id.lblVUNomeReal);
        lblNomeUsu = findViewById(R.id.lblVUNomeUsu);
        lblTipo = findViewById(R.id.lblVUTipo);
        lblTipoUsu = findViewById(R.id.lblVUTipoUsu);
        lblCidade = findViewById(R.id.lblVUCidade);
        lblCidadeUsu =findViewById(R.id.lblVUCidadeUsu);
        lblVUverPerfil = findViewById(R.id.lblVUSeuPerfil);
        lblVUverPerfil.setVisibility(View.GONE);
        lblPostagens = findViewById(R.id.lblPostagem);
        lblInformacoes = findViewById(R.id.lblVUInformacoes);

        imgPerfil = findViewById(R.id.imgVerUsuario);
        imgAdicionar = findViewById(R.id.iconeAdicionar);
        imgBloquear  = findViewById(R.id.iconeBloquear);
        fonteTitulo = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
        fonteTexto = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/aileron.heavy.otf");

        lblNome.setTypeface(fonteTitulo);
        lblTipo.setTypeface(fonteTitulo);
        lblCidade.setTypeface(fonteTitulo);
        lblNomeUsu.setTypeface(fonteTexto);
        lblNomeReal.setTypeface(fonteTexto);
        lblTipoUsu.setTypeface(fonteTexto);
        lblCidadeUsu.setTypeface(fonteTexto);
        lblPostagens.setTypeface(fonteTitulo);
        lblInformacoes.setTypeface(fonteTitulo);

        if(bundle.getString("nomeUsu")!= null)
        {

            usuarioPesquisadoString = bundle.getString("nomeUsu");
            firebase.collection("Usuario").whereEqualTo("nomeUsuario", usuarioPesquisadoString).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for(DocumentSnapshot documentReference : Objects.requireNonNull(task.getResult())){

                         usuario = documentReference.toObject(Usuario.class);

                    }

                    if(usuario != null) {
                        lblNomeUsu.setText(usuario.getNomeUsuario());
                        lblNome.setText(usuario.getNome());
                        lblTipo.setText(usuario.getNome());
                        lblCidade.setText(usuario.getCidade());
                        final String emailCod = CodificarBase64.codificarTexto(usuario.getEmail());
                        firebase.collection("Usuario").document(emailCod).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String url = null;
                                for(DocumentSnapshot document : task.getResult()){
                                    FotoPerfil foto = new FotoPerfil();
                                    foto.setFotoPerfil(document.get("fotoPerfil").toString());
                                    url = foto.getFotoPerfil();
                                    Toast.makeText(getApplicationContext(), "" + url, Toast.LENGTH_SHORT).show();

                                }
                                if(url != null) {
                                    Glide.with(getApplicationContext()).load(url).into(imgPerfil);
                                }
                            }
                        });

                        pegarImagem(emailCod);
                        firebase.collection("Usuario").document(idUsu).collection("Amigos").whereEqualTo("emailAmigo" , emailCod).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                for(QueryDocumentSnapshot documentReference : value){

                                    amigo = documentReference.get("emailAmigo").toString();

                                }
                                if (amigo != null){
                                    int id = getApplicationContext().getResources().getIdentifier("icone_adicionar2", "drawable", getApplicationContext().getPackageName());
                                    Drawable drawable= getApplicationContext().getResources().getDrawable(id) ;
                                    imgAdicionar.setImageDrawable(drawable);
                                    imgAdicionar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            firebase.collection("Usuario").document(idUsuario).collection("Amigos").document(usuario.getNomeUsuario()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(),usuario.getNomeUsuario() + " excluido da lista de amigos com sucesso! ",Toast.LENGTH_SHORT).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext()," " +  e ,Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });


                                }else{
                                    imgAdicionar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Map<String, Object> amigo = new HashMap<>();
                                            amigo.put("emailAmigo", emailCod);
                                            amigo.put("nome", usuario.getNome());
                                            amigo.put("nomeUsuario", usuario.getNomeUsuario());
                                            firebase.collection("Usuario").document(idUsuario).collection("Amigos").document(usuario.getNomeUsuario()).set(amigo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(),usuario.getNomeUsuario() + " adicionado com sucesso! ",Toast.LENGTH_SHORT).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext()," " +  e ,Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });
                                }

                            }
                        });


                    }else{
                        Toast.makeText(getApplicationContext(), "usuario não encontrado no banco", Toast.LENGTH_SHORT).show();
                    }




                }
            });

        }

        logo = findViewById(R.id.logoBarra2);
        logo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
              }
        });





    }
    private void pegarImagem( String email){
        firebase.collection("Usuario").document(email).collection("Postagens").orderBy("horaPostagem", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                startActivity(new Intent(VerUsuario.this,VerProprioPerfil.class));
            } else if (id == R.id.rede) {
                startActivity(new Intent(VerUsuario.this,MainActivity.class));

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

