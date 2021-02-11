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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class ListaAmigos extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private FirebaseAuth autentificador;
    private FirebaseFirestore firebase;
    private String idUsuario;
    private ArrayList<DadosPesquisa> amigos;
    private ArrayAdapter<DadosPesquisa> adapter;
    private StorageReference storageReference;
    private Typeface fonte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_amigos);

        Toolbar toolbar = findViewById(R.id.toolbarListaAmigos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        autentificador = ConfiguracaoFirebase.getAutentificador();
        firebase = ConfiguracaoFirebase.getFirebase();

        ListView lvAmigos = findViewById(R.id.lvAmigos);

        amigos = new ArrayList<>();
        adapter = new AmigoAdapter(getApplicationContext(), amigos);
        lvAmigos.setAdapter(adapter);

        pegarAmigos();

        lvAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String nomeUsu = amigos.get(i).getNomeUsuario();
                Intent intent = new Intent(getApplicationContext(),VerUsuario.class);
                intent.putExtra("nomeUsu",nomeUsu);
                startActivity(intent);

            }
        });

        fonte = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/LeagueSpartan-Bold.otf");
        TextView lblTituloAmigo = findViewById(R.id.lblTituloAmigo);
        lblTituloAmigo.setTypeface(fonte);

    }

    private void pegarAmigos(){
        firebase.collection("Usuario").document(idUsuario).collection("Amigos").orderBy("nomeUsuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    amigos.clear();
                    for (final DocumentSnapshot document : task.getResult()){
                        final String codigoUsuario = Objects.requireNonNull(document.get("emailAmigo")).toString();

                        firebase.collection("Usuario").document(codigoUsuario).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){

                                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                                        DadosPesquisa dados = new DadosPesquisa();
                                        dados.setNome(document.get("nome").toString());
                                        dados.setNomeUsuario(document.get("nomeUsuario").toString());
                                        dados.setFotoPerfil(documentSnapshot.get("fotoPerfil").toString());
                                        amigos.add(dados);

                                    }
                                    adapter.notifyDataSetChanged();
                                }


                            }
                        });


                    }

                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        MenuItem itemPesquisa = menu.findItem(R.id.menu_pesquisar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(itemPesquisa);
        searchView.setQueryHint("Procurar usuário");
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        firebase.collection("Usuario").document(idUsuario).collection("Amigos").orderBy("nomeUsuario").startAt(s).endAt(s + "\uf8ff").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    amigos.clear();
                    for (final DocumentSnapshot document : task.getResult()){
                        final String codigoUsuario = Objects.requireNonNull(document.get("emailAmigo")).toString();

                        firebase.collection("Usuario").document(codigoUsuario).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){

                                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                                        DadosPesquisa dados = new DadosPesquisa();
                                        dados.setNome(document.get("nome").toString());
                                        dados.setNomeUsuario(document.get("nomeUsuario").toString());
                                        dados.setFotoPerfil(documentSnapshot.get("fotoPerfil").toString());
                                        amigos.add(dados);

                                    }
                                    adapter.notifyDataSetChanged();
                                }


                            }
                        });


                    }

                    adapter.notifyDataSetChanged();
                }

            }
        });

        return false;
    }
}
