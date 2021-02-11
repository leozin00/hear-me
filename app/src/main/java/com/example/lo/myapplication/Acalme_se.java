package com.example.lo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuItemView;
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

public class Acalme_se extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private NavigationView navegador;
    private DrawerLayout drawer;
    private String idUsuario;
    private String url;
    private ImageView imgFotoMenu;
    private FirebaseFirestore firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acalme_se);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container3);

         TabLayout dots = findViewById(R.id.dots3);
        dots.setupWithViewPager(mViewPager, true);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navegador = findViewById(R.id.nav_view3);
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

        imgFotoMenu = findViewById(R.id.imgFotoAcalmese);
        carregarFotoPerfil();

    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout3);
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
    private int clickBreathe;
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

        }else if(id == R.id.gif){
            transicao.Trasicao(getApplicationContext(),GifRespiracao.class,drawer);
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

        private Typeface titulo;
        private Typeface descricao;
        private TextView lblTituloAcalmese;
        private TextView lblDescricaoAcalmese;
        private TextView lblTituloAcalmeseA;
        private TextView lblDescricaoAcalmeseA;
        private TextView lblTituloAcalmeseA2;
        private TextView lblDescricaoAcalmeseA2;
        private TextView lblTituloAcalmeseC;
        private TextView lblDescricaoAcalmeseC;
        private TextView lblTituloAcalmeseE;
        private TextView lblDescricaoAcalmeseE;
        private TextView lblTituloAcalmeseE2;
        private TextView lblDescricaoAcalmeseE2;
        private TextView lblTituloAcalmeseL;
        private TextView lblDescricaoAcalmeseL;
        private TextView lblTituloAcalmeseM;
        private TextView lblDescricaoAcalmeseM;
        private TextView lblTituloAcalmeseS;
        private TextView lblDescricaoAcalmeseS;
        private ImageView logo;
        private NavigationMenuItemView listener;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)  {
            View rootView = null;

            titulo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Adumu.ttf");
            descricao = Typeface.createFromAsset(getActivity().getAssets(), "fonts/aileron.heavy.otf");

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se, container, false);

                    lblDescricaoAcalmese = rootView.findViewById(R.id.lblDescricaoAcalmese);
                    lblDescricaoAcalmese.setTypeface(descricao);

                    lblTituloAcalmese = rootView.findViewById(R.id.lblTituloAcalmese);
                    lblTituloAcalmese.setTypeface(titulo);

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_a, container, false);

                    lblDescricaoAcalmeseA = rootView.findViewById(R.id.lblDescricaoAcalmeseA);
                    lblDescricaoAcalmeseA.setTypeface(descricao);

                    lblTituloAcalmeseA = rootView.findViewById(R.id.lblTituloAcalmeseA);
                    lblTituloAcalmeseA.setTypeface(titulo);

                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_c, container, false);

                    lblDescricaoAcalmeseC = rootView.findViewById(R.id.lblDescricaoAcalmeseC);
                    lblDescricaoAcalmeseC.setTypeface(descricao);

                    lblTituloAcalmeseC = rootView.findViewById(R.id.lblTituloAcalmeseC);
                    lblTituloAcalmeseC.setTypeface(titulo);

                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_a2, container, false);

                    lblDescricaoAcalmeseA2 = rootView.findViewById(R.id.lblDescricaoAcalmeseA2);
                    lblDescricaoAcalmeseA2.setTypeface(descricao);

                    lblTituloAcalmeseA2 = rootView.findViewById(R.id.lblTituloAcalmeseA2);
                    lblTituloAcalmeseA2.setTypeface(titulo);

                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_l, container, false);

                    lblDescricaoAcalmeseL = rootView.findViewById(R.id.lblDescricaoAcalmeseL);
                    lblDescricaoAcalmeseL.setTypeface(descricao);

                    lblTituloAcalmeseL = rootView.findViewById(R.id.lblTituloAcalmeseL);
                    lblTituloAcalmeseL.setTypeface(titulo);

                    break;
                case 6:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_m, container, false);

                    lblDescricaoAcalmeseM = rootView.findViewById(R.id.lblDescricaoAcalmeseM);
                    lblDescricaoAcalmeseM.setTypeface(descricao);

                    lblTituloAcalmeseM = rootView.findViewById(R.id.lblTituloAcalmeseM);
                    lblTituloAcalmeseM.setTypeface(titulo);

                    break;
                case 7:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_e, container, false);

                    lblDescricaoAcalmeseE = rootView.findViewById(R.id.lblDescricaoAcalmeseE);
                    lblDescricaoAcalmeseE.setTypeface(descricao);

                    lblTituloAcalmeseE = rootView.findViewById(R.id.lblTituloAcalmeseE);
                    lblTituloAcalmeseE.setTypeface(titulo);

                    break;
                case 8:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_s, container, false);

                    lblDescricaoAcalmeseS = rootView.findViewById(R.id.lblDescricaoAcalmeseS);
                    lblDescricaoAcalmeseS.setTypeface(descricao);

                    lblTituloAcalmeseS = rootView.findViewById(R.id.lblTituloAcalmeseS);
                    lblTituloAcalmeseS.setTypeface(titulo);

                    break;
                case 9:
                    rootView = inflater.inflate(R.layout.fragment_acalme_se_e2, container, false);

                    lblDescricaoAcalmeseE2 = rootView.findViewById(R.id.lblDescricaoAcalmeseE2);
                    lblDescricaoAcalmeseE2.setTypeface(descricao);

                    lblTituloAcalmeseE2 = rootView.findViewById(R.id.lblTituloAcalmeseE2);
                    lblTituloAcalmeseE2.setTypeface(titulo);

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

            return 9;
        }
    }

}