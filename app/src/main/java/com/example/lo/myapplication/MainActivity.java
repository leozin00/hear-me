package com.example.lo.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public SharedPreferences primeiraVez;
    public SharedPreferences.Editor editor ;
    private FirebaseAuth autentificador;
    public NavigationView navegador;
    private DrawerLayout drawer;
    private int clickBreathe;
    private StorageReference storageReference;
    private ListView lvPostagem;
    private ArrayList<DadosPostagem> postagens;
    private ArrayAdapter<DadosPostagem> adapter;
    private Uri localImagemSelecionada;
    private byte[] byteArray;
    private FirebaseFirestore firebase;
    private String idUsuario;
    private String nomeUsuario;
    private String url;
    private ImageView imgFotoMenu;
    private String tipoUsuario;
    private ImageView imgPostagem;
    private AlertDialog.Builder caixaSos;
    private AlertDialog.Builder caixaPostagem;
    private String [] permissions = {"android.permission.CAMERA"};
    private View dialog;
    private EditText txtMensagemPublicacao;
    private DadosPostagem map;
    private Typeface fonte;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        primeiraVez = getSharedPreferences("PRIMEIRA_VEZ", MODE_PRIVATE);
        editor = primeiraVez.edit();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.progressoSpinner);

        fonte = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/LeagueSpartan-Bold.otf");
        TextView lblTituloAmigo = findViewById(R.id.lblTimeLine);
        lblTituloAmigo.setTypeface(fonte);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caixaSos = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialog = inflater.inflate(R.layout.caixa_sos, null);
                caixaSos.setView(dialog).setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("tel:" + 188);
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                caixaSos.create().show();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navegador = findViewById(R.id.nav_view);
        navegador.setNavigationItemSelectedListener(this);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        autentificador = ConfiguracaoFirebase.getAutentificador();
        firebase = ConfiguracaoFirebase.getFirebase();

        storageReference = FirebaseStorage.getInstance().getReference();

        pegarNomeUsuario();

        lvPostagem = findViewById(R.id.lvPostagem);



        imgFotoMenu = findViewById(R.id.imgFotoMenu);

        carregarFotoPerfil();

        postagens = new ArrayList<>();
        adapter = new PostagemAdapter(getApplicationContext(), postagens);
        lvPostagem.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        pegarPostagem();
        atualizarPostagem();


        imgPostagem = findViewById(R.id.imgPostagem);
        imgPostagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                caixaPostagem = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dialog = inflater.inflate(R.layout.caixa_postagem, null);
                caixaPostagem.setView(dialog).setPositiveButton("Publicar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        map.setTextoPostagem(txtMensagemPublicacao.getText().toString());

                        firebase.collection("Usuario").document(idUsuario).collection("Postagens").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                atualizarPostagem();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                caixaPostagem.create().show();

                ImageView imgFotoGaleriaPublicacao = dialog.findViewById(R.id.imgFotoGaleriaPublicacao);
                imgFotoGaleriaPublicacao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    }
                });

                txtMensagemPublicacao = dialog.findViewById(R.id.txtMensagemPublicacao);

            }
        });

        ImageView imgAmigos = findViewById(R.id.imgAmigos);
        imgAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transicao transicao = new Transicao();
                transicao.Trasicao(getApplicationContext(), ListaAmigos.class,drawer);
            }
        });

    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        }else if(id == R.id.sair){
            deslogarUsuario();
            editor.putInt("PRIMEIRA_VEZ_USUARIO",0).apply();

        }else if (id == R.id.menu_pesquisa){
            startActivity(new Intent(getApplicationContext(), ListaSugestao.class));
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


        } else if (id == R.id.diario) {
            transicao.Trasicao(getApplicationContext(), Diario.class, drawer);

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

    public void deslogarUsuario(){
        autentificador.signOut();
        Intent intent = new Intent(getApplicationContext(), Slide.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            localImagemSelecionada = data.getData();

            try {
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                final StorageReference pastaReferencia = storageReference.child("imagens/"+byteArray);
                UploadTask uploadTask = pastaReferencia.putFile(localImagemSelecionada);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "deu ruim meu bom", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "deu bom meu bom", Toast.LENGTH_SHORT).show();

                        pastaReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                ImageView imgFotoPublicacao = dialog.findViewById(R.id.imgFotoPublicacao);
                                Glide.with(getApplicationContext()).load(uri).into(imgFotoPublicacao);
                                Date date = new Date();
                                map = new DadosPostagem();
                                map.setNomeUsuario(nomeUsuario);
                                map.setUrl(uri.toString());
                                map.setUrlPerfil(url);
                                map.setTipoUsuario(tipoUsuario);
                                map.setHoraPostagem(date.getTime());
                                map.setEmail(idUsuario);



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        });

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void pegarPostagem(){

        postagens.clear();
        firebase.collection("Usuario").document(idUsuario).collection("Amigos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                spinner.setVisibility(View.VISIBLE);

                for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                    String amigos = Objects.requireNonNull(document.get("emailAmigo")).toString();
                    firebase.collection("Usuario").document(amigos).collection("Postagens").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                DadosPostagem dados = documentSnapshot.toObject(DadosPostagem.class);
                                postagens.add(dados);
                                ordenarPostagem();
                                adapter.notifyDataSetChanged();
                            }
                            firebase.collection("Usuario").document(idUsuario).collection("Postagens").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (DocumentSnapshot documentSnapshot2 : task.getResult()){
                                        DadosPostagem dados = documentSnapshot2.toObject(DadosPostagem.class);
                                        postagens.add(dados);
                                        ordenarPostagem();
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            });
                            spinner.setVisibility(View.GONE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            spinner.setVisibility(View.GONE);

                        }
                    });

                }


            }
        });


    }

    private void pegarNomeUsuario(){
        String email = autentificador.getCurrentUser().getEmail().toString()        ;
        firebase.collection("Usuario").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                    nomeUsuario = Objects.requireNonNull(document.get("nomeUsuario")).toString();
                    tipoUsuario = Objects.requireNonNull(document.get("tipoUsuario")).toString();

                }

            }
        });
    }

    private void atualizarPostagem(){
        firebase.collection("Usuario").document(idUsuario).collection("Postagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "deu errado", Toast.LENGTH_SHORT).show();
                    return;
                }
                postagens.clear();
                for (QueryDocumentSnapshot doc : Objects.requireNonNull(queryDocumentSnapshots)) {
                    if (doc.get("url") != null) {
                        DadosPostagem dados = doc.toObject(DadosPostagem.class);
                        postagens.add(dados);
                        ordenarPostagem();
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    private void carregarFotoPerfil(){


        firebase.collection("Usuario").document(idUsuario).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                spinner.setVisibility(View.VISIBLE);
                for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                    FotoPerfil foto = new FotoPerfil();
                    foto.setFotoPerfil(Objects.requireNonNull(document.get("fotoPerfil")).toString());
                    url = foto.getFotoPerfil();
                    spinner.setVisibility(View.GONE);
                }
                Glide.with(getApplicationContext()).load(url).into(imgFotoMenu);
            }
        });


    }

    private void ordenarPostagem(){
        Collections.sort(postagens, new Comparator<DadosPostagem>() {
            @Override
            public int compare(DadosPostagem t2, DadosPostagem t1) {
                return String.valueOf(t1.getHoraPostagem()).compareTo(String.valueOf(t2.getHoraPostagem()));
            }
        });
    }

}