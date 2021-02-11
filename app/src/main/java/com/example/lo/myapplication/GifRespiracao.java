package com.example.lo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class GifRespiracao extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ImageView logo;
    private SharedPreferences.Editor editor;
    private FirebaseAuth autentificador;
    private DrawerLayout drawer;
    private int clickBreathe;
    private NavigationView navegador;
    private String idUsuario;
    private String url;
    private ImageView imgFotoMenu;
    private FirebaseFirestore firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_respiracao);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container4);

        TabLayout dots = findViewById(R.id.dots4);
        dots.setupWithViewPager(mViewPager, true);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navegador = findViewById(R.id.nav_view4);
        navegador.setNavigationItemSelectedListener(this);

        logo = findViewById(R.id.logoBarra2);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Preferencias preferencias = new Preferencias(getApplicationContext());
        idUsuario = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase();

        imgFotoMenu = findViewById(R.id.imgFotoGif);
        carregarFotoPerfil();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout4);
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
            transicao.Trasicao(getApplicationContext(),Sons.class,drawer);


        }else if(id == R.id.acalme_se){
            transicao.Trasicao(getApplicationContext(),Acalme_se.class,drawer);

        }else if(id == R.id.gif){

        }
    return true;
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        private Typeface fonteTexto;
        private Typeface fonteTitulo;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_gif_respiracao, container, false);
                    TextView lblTecnica = rootView.findViewById(R.id.lblTecnica);
                    TextView lblTextoTecnica = rootView.findViewById(R.id.lblTextoTecnica);

                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(), "fonts/aileron.heavy.otf");
                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LeagueSpartan-Bold.otf");

                    lblTecnica.setTypeface(fonteTitulo);
                    lblTextoTecnica.setTypeface(fonteTexto);

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_gif_respiracao_gif, container, false);

                    TextView textoSuperior = rootView.findViewById(R.id.lblTextoSuperiorGif);
                    TextView textoInferior = rootView.findViewById(R.id.lblTextoInferiorGif);

                    ImageView gif = (ImageView) rootView.findViewById(R.id.gifRespiracao);
                    Glide.with(this).load(R.drawable.gif_respiracao).into(gif);

                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(), "fonts/aileron.heavy.otf");

                    textoSuperior.setTypeface(fonteTexto);
                    textoInferior.setTypeface(fonteTexto);

                    break;
            }

            return rootView;

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}