package com.example.lo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class Configuracoes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public SharedPreferences primeiraVez;
    public SharedPreferences.Editor editor ;
    private FirebaseAuth autentificador;
    public NavigationView navegador;
    private DrawerLayout drawer;
    private int clickBreathe ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        autentificador = ConfiguracaoFirebase.getAutentificador();
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navegador = findViewById(R.id.nav_view);
        navegador.setNavigationItemSelectedListener(this);
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
        Transicao transicao   = new Transicao();
        int id = item.getItemId();
        if (id == R.id.btnContfiguracao) {
                transicao.Trasicao(getApplicationContext(),Configuracoes.class,drawer);
            return true;
        }
        if(id == R.id.sair){
            deslogarUsuario();
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
    public void deslogarUsuario(){
        autentificador.signOut();
        Intent intent = new Intent(getApplicationContext(), Slide.class);
        startActivity(intent);
        finish();
    }
}
