package com.example.lo.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class ListaSugestao extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {


    private FirebaseAuth autentificador;
    private FirebaseFirestore firebase;
    private String idUsuario;
    private ArrayList<DadosPesquisa> sugestoes;
    private ArrayAdapter<DadosPesquisa> adapter;
    private StorageReference storageReference;
    private Typeface fonte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sugestao);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        autentificador = ConfiguracaoFirebase.getAutentificador();
        firebase = ConfiguracaoFirebase.getFirebase();

        ListView lvSugestao = findViewById(R.id.lvSugestao);

        sugestoes = new ArrayList<>();
        adapter = new SugestaoAdapter(getApplicationContext(), sugestoes);
        lvSugestao.setAdapter(adapter);

        lvSugestao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String nomeUsu = sugestoes.get(i).getNomeUsuario();
                Intent intent = new Intent(getApplicationContext(),VerUsuario.class);
                intent.putExtra("nomeUsu",nomeUsu);
                startActivity(intent);

            }
        });

        fonte = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/LeagueSpartan-Bold.otf");
        TextView lblTituloAmigo = findViewById(R.id.lblTituloSugestao);
        lblTituloAmigo.setTypeface(fonte);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String s) {

        firebase.collection("Usuario").orderBy("nomeUsuario").startAt(s).endAt(s + "\uf8ff").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    sugestoes.clear();
                    if(!s.isEmpty()){
                        for(final DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                            if(document != null){
                                sugestoes.clear();
                                String codigoUsuario = CodificarBase64.codificarTexto(Objects.requireNonNull(document.get("email")).toString());

                                firebase.collection("Usuario").document(codigoUsuario).collection("Foto Perfil").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){

                                            for(DocumentSnapshot documentSnapshot : task.getResult()){

                                                DadosPesquisa dados = new DadosPesquisa();
                                                dados.setNome(document.get("nome").toString());
                                                dados.setNomeUsuario(document.get("nomeUsuario").toString());
                                                dados.setFotoPerfil(documentSnapshot.get("fotoPerfil").toString());
                                                sugestoes.add(dados);

                                            }
                                            adapter.notifyDataSetChanged();
                                        }


                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Usuario não cadastrado!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    adapter.notifyDataSetChanged();



                }
            }
        });

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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

}