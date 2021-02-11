package com.example.lo.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Comentar extends AppCompatActivity  {

    private FirebaseAuth autentificador;
    private FirebaseFirestore firebase;
    private String idUsuario;
    private ArrayList<DadosComentario>comentarios;
    private ArrayAdapter<DadosComentario> adapter;
    private StorageReference storageReference;
    private Typeface fonte;
    private ImageView imgEnviar;
    private EditText txtComentario;
    private String donoPostagem;
    private String postagem;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentar);

        Toolbar toolbar = findViewById(R.id.toolbarListComentarios);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         bundle = getIntent().getExtras();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        autentificador = ConfiguracaoFirebase.getAutentificador();
        firebase = ConfiguracaoFirebase.getFirebase();

        ListView lvComentarios = findViewById(R.id.lvComentarios);

        comentarios = new ArrayList<>();
        adapter = new ComentarAdapter(getApplicationContext(), comentarios);
        lvComentarios.setAdapter(adapter);


        lvComentarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String nomeUsu = comentarios.get(i).getNomeUsu();
                Intent intent = new Intent(getApplicationContext(),VerUsuario.class);
                intent.putExtra("nomeUsu",nomeUsu);
                startActivity(intent);

            }
        });


        fonte = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/LeagueSpartan-Bold.otf");
        TextView lblTituloAmigo = findViewById(R.id.lblTituloComentario);
        lblTituloAmigo.setTypeface(fonte);

        txtComentario = findViewById(R.id.txtMensagemDiario);
        imgEnviar = findViewById(R.id.imgEnviarDiario);

        pegarComentario();
        imgEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarComentario();
            }
        });

    }
    private void pegarComentario() {
        if (bundle.getString("donoPostagem") != null && bundle.getString("postagem") != null) {
            donoPostagem = bundle.getString("donoPostagem");
            postagem = bundle.getString("postagem");

                    firebase.collection("Usuario").document(donoPostagem).collection("Postagens").document(postagem).collection("Comentario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            comentarios.clear();
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    DadosComentario dados = new DadosComentario();
                                    dados.setNomeUsu(document.get("nomeUsu").toString());
                                    dados.setTipoUsu(document.get("tipoUsu").toString());
                                    dados.setUrl(document.get("url").toString());
                                    dados.setTxtPostagem(document.get("txtPostagem").toString());
                                    comentarios.add(dados);


                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

                }

    }
    private void enviarComentario(){
        if (bundle.getString("donoPostagem") != null && bundle.getString("postagem") != null) {
            donoPostagem = bundle.getString("donoPostagem");
            postagem = bundle.getString("postagem");
            Calendar calendar = Calendar.getInstance();
            final String horaAgr = String.valueOf( calendar.getTimeInMillis());
            final DadosComentario comentario = new DadosComentario();
            comentario.setTxtPostagem(txtComentario.getText().toString());
            txtComentario.setText("");



            String email = autentificador.getCurrentUser().getEmail().toString()        ;
            firebase.collection("Usuario").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    String nomeUsuario = null;
                    String tipoUsuario = null;
                    for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                         nomeUsuario = Objects.requireNonNull(document.get("nomeUsuario")).toString();
                         tipoUsuario = Objects.requireNonNull(document.get("tipoUsuario")).toString();

                    }
                    comentario.setTipoUsu(tipoUsuario);
                    comentario.setNomeUsu(nomeUsuario);

                    firebase.collection("Usuario").document(idUsuario).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String url = null;
                            for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                FotoPerfil foto = new FotoPerfil();
                                foto.setFotoPerfil(Objects.requireNonNull(document.get("fotoPerfil")).toString());
                                url = foto.getFotoPerfil();

                            }
                            comentario.setUrl(url);
                            firebase.collection("Usuario").document(donoPostagem).collection("Postagens").document(postagem).collection("Comentario").document(horaAgr).set(comentario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(),"sucesso ao comentar",Toast.LENGTH_SHORT).show();
                                    pegarComentario();
                                    adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"falha ao comentar",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                }
            });






        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        MenuItem itemPesquisa = menu.findItem(R.id.menu_pesquisar);



        return true;
    }

}
