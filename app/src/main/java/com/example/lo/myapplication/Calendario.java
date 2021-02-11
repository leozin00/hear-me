package com.example.lo.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class Calendario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PlaceholderFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static SharedPreferences.Editor editor;
    private FirebaseAuth autentificador;
    private FirebaseUser user;
    private NavigationView navegador;
    private DrawerLayout drawer;
    private ImageView logo;
    private static String emailCod;
    private String idUsuario;
    private String url;
    private ImageView imgFotoMenu;
    private FirebaseFirestore firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        mSectionsPagerAdapter = new PlaceholderFragment.SectionsPagerAdapter(getSupportFragmentManager());
        autentificador = ConfiguracaoFirebase.getAutentificador();
        emailCod = CodificarBase64.codificarTexto( autentificador.getCurrentUser().getEmail().toString());

        mSectionsPagerAdapter = new PlaceholderFragment.SectionsPagerAdapter(getSupportFragmentManager());
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

        imgFotoMenu = findViewById(R.id.imgFotoCalendario);
        carregarFotoPerfil();

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
            transicao.Trasicao(getApplicationContext(), VerProprioPerfil.class, drawer);
        } else if (id == R.id.rede) {

            transicao.Trasicao(getApplicationContext(), MainActivity.class, drawer);
        } else if (id == R.id.diario) {
            transicao.Trasicao(getApplicationContext(), Diario.class, drawer);

        } else if (id == R.id.breathe){
            MostrarMenuBreathe mostrar = new MostrarMenuBreathe();
            clickBreathe = mostrar.MostrarMenuBreathe(navegador,clickBreathe);

        } else if (id == R.id.calendario) {

        } else if (id == R.id.sons) {
            transicao.Trasicao(getApplicationContext(),Sons.class,drawer);
        }else if(id == R.id.acalme_se){
            transicao.Trasicao(getApplicationContext(),Acalme_se.class,drawer);
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

        private int contador;
        private GraficoDados nota;
        private Typeface fonteTitulo;
        private CalendarView cldCalendario;
        private LayoutInflater inflater;
        private RadioGroup grupoOp;
        private RadioButton rbMuitoBem;
        private RadioButton rbBem;
        private RadioButton rbRazoavel;
        private RadioButton rbRuim;
        private RadioButton rbMuitoRuim;
        private EditText txtDiaPessoa;
        private Typeface fonteTexto;
        private TextView lblComoVoce;
        private TextView lblDigaComo;
        private TextView lblMeses;
        private TextView lblCalendarioHumor;
        private  String checarData;
        private String estado ;
        private FirebaseFirestore banco;
        private long data;
        private TextView lblGraficoHumor;
        private SimpleDateFormat formaData;
        private GraphView grafico;
        private LineGraphSeries<DataPoint> linhaGrafico;
        private AlertDialog.Builder caixaHumor;
        private AlertDialog.Builder builder;
        private List<GraficoDados> dadosDoGrafico ;
        private List<Integer> intGrafico;
        private List<Integer> intDia;
        String teste;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;


            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_calendario, container, false);
                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(),"fonts/aileron.heavy.otf");
                    lblCalendarioHumor = rootView.findViewById(R.id.lblCalendario);
                    lblMeses = rootView.findViewById(R.id.lblMeses);
                    lblCalendarioHumor.setTypeface(fonteTitulo);
                    lblMeses.setTypeface(fonteTexto);
                    inflater = getActivity().getLayoutInflater();
                    cldCalendario = rootView.findViewById(R.id.cldCalendario);
                    Calendar cal = GregorianCalendar.getInstance();

                    int ano = cal.get(Calendar.YEAR);

                    data = System.currentTimeMillis();
                    Date dataInicio = new Date("01/01/"+ano);
                    Date dataFinal = new Date("12/31/"+ano);

                    cldCalendario.setFocusedMonthDateColor(Color.BLACK);// set the red color for the dates of  focused month
                    cldCalendario.setBackgroundColor(Color.WHITE);
                    cldCalendario.setMinDate(dataInicio.getTime());
                    cldCalendario.setMaxDate(dataFinal.getTime());
                    cldCalendario.setDate(data);
                    cldCalendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                            Date dataUsuBruto = new Date(month + 1 + "/" + dayOfMonth + "/" +  year) ;
                            formaData = new SimpleDateFormat("dd/MM/yyyy");
                            nota = new GraficoDados();
                            nota.setDia(dayOfMonth);
                            String dataUsu = formaData.format(dataUsuBruto);
                            final String dataCerta = formaData.format(data);
                            month += 1;
                             final String dataBanco = dayOfMonth +"-" + month + "-" + year;
                            //if(dataUsu.compareTo(dataCerta) == 0  ) {
                                            caixaHumor = new AlertDialog.Builder(getContext());
                                            LayoutInflater inflater = getLayoutInflater();
                                            final View dialogView = inflater.inflate(R.layout.caixa_calendario, null);
                                            caixaHumor.setView(dialogView)
                                                    .setPositiveButton(R.string.btnCalendario, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            txtDiaPessoa = dialogView.findViewById(R.id.txtDiaPessoa);
                                                            rbMuitoBem = dialogView.findViewById(R.id.rbMuitoBem);
                                                            rbBem = dialogView.findViewById(R.id.rbBem);
                                                            rbRazoavel = dialogView.findViewById(R.id.rbRazoavel);
                                                            rbRuim = dialogView.findViewById(R.id.rbRuim);
                                                            rbMuitoRuim = dialogView.findViewById(R.id.rbMuitoRuim);

                                                            fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LeagueSpartan-Bold.otf");
                                                            fonteTexto = Typeface.createFromAsset(getActivity().getAssets(), "fonts/aileron.heavy.otf");


                                                            // Checando RadioButton selecionado
                                                            if (rbMuitoBem.isChecked()) {
                                                                 nota.setAvaliacao("muito bem");
                                                                 nota.setAvaliacaoInt(3);
                                                            } else if (rbBem.isChecked()) {
                                                                nota.setAvaliacao("bem");
                                                                nota.setAvaliacaoInt(2);
                                                            } else if (rbRazoavel.isChecked()) {
                                                                nota.setAvaliacao("razoavel");
                                                                nota.setAvaliacaoInt(1);
                                                            } else if (rbRuim.isChecked()) {

                                                                nota.setAvaliacao("ruim");
                                                                nota.setAvaliacaoInt(-1);
                                                            } else if (rbMuitoRuim.isChecked()) {
                                                                nota.setAvaliacao("muito ruim");
                                                                nota.setAvaliacaoInt(-2);
                                                            } else {

                                                            }
                                                            if (txtDiaPessoa.getText().toString() != null) {
                                                                banco = FirebaseFirestore.getInstance();
                                                                nota.setDataCerta(dataCerta);
                                                                nota.setTextoDia(txtDiaPessoa.getText().toString());
                                                                banco.collection("Usuario").document(emailCod).collection("Nota dia").document(dataBanco).set(nota).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(getActivity(), "Dia avaliado com sucesso !", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getActivity(), "erro ao avaliar dia  " + e, Toast.LENGTH_LONG).show();
                                                                    }
                                                                });

                                                            } else {


                                                            }

                                                        }
                                                    }).setNegativeButton(R.string.btnCancelar, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            caixaHumor.create().show();

                            //} else{
                                Toast.makeText(getContext(),dataCerta + "   /  "+ dataUsu + emailCod,Toast.LENGTH_SHORT).show();

                    //-        }
                    }


                    });


                    break;
                case 2:
                    if (caixaHumor != null ) {
                            caixaHumor.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });

                    }
                    rootView = inflater.inflate(R.layout.fragment_calendario_grafico, container, false);
                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(),"fonts/aileron.heavy.otf");
                    dadosDoGrafico = new ArrayList<>();
                    intGrafico = new ArrayList<>();
                    grafico = rootView.findViewById(R.id.grafico);
                    lblGraficoHumor  = rootView.findViewById(R.id.lblGraficoHumor);
                    lblGraficoHumor.setTypeface(fonteTitulo);
                    banco = FirebaseFirestore.getInstance();
                    banco.collection("Usuario").document(emailCod).collection("Nota dia").orderBy("dia",Query.Direction.ASCENDING)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {


                                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    GraficoDados dado = document.toObject(GraficoDados.class);


                                    dadosDoGrafico.add(dado);
                                    intGrafico.add(dado.getAvaliacaoInt());

                                }
                                Toast.makeText(getContext(), "" + intGrafico.size(), Toast.LENGTH_LONG).show();
                                if(dadosDoGrafico.size() != 0) {
                                    linhaGrafico = new LineGraphSeries<DataPoint>();
                                    for (int x = 0; x < dadosDoGrafico.size(); x++) {
                                        linhaGrafico.appendData(new DataPoint(dadosDoGrafico.get(x).getDia(), dadosDoGrafico.get(x).getAvaliacaoInt()), true, dadosDoGrafico.size()
                                        );
                                    }
                                    grafico.getViewport().setScrollable(true); // enables horizontal scrolling
                                    grafico.getViewport().setScalable(true);
                                    grafico.getViewport().setXAxisBoundsManual(true);
                                    grafico.getViewport().setMinX(dadosDoGrafico.get(0).getDia() - 1);
                                    grafico.getViewport().setMaxX(dadosDoGrafico.get(dadosDoGrafico.size() - 1).getDia());


                                    // set manual Y bounds
                                    grafico.getViewport().setYAxisBoundsManual(true);
                                    grafico.getViewport().setMinY(-2);
                                    grafico.getViewport().setMaxY(3);


                                    grafico.getGridLabelRenderer().setGridColor(Color.BLACK);
                                    grafico.getGridLabelRenderer().setNumVerticalLabels(6);
                                    linhaGrafico.setColor(Color.RED);
                                    linhaGrafico.setDrawDataPoints(true);
                                    linhaGrafico.setDataPointsRadius(10);
                                    linhaGrafico.setThickness(3);
                                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(grafico);
                                    staticLabelsFormatter.setVerticalLabels(new String[] {"muito ruim", "ruim","x", "razoavel","bem","muito bem"});

                                    grafico.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                                    grafico.addSeries(linhaGrafico);

                                }else{
                                    Toast.makeText(getContext(),"voce ainda não avaliou nenhum dia !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_LONG).show();
                        }
                    });
                    Toast.makeText(getContext(), "" +intGrafico.size(), Toast.LENGTH_LONG).show();
//                    for( int x = 0;x <= dadosDoGrafico.size();x++){
  //                      intGrafico.add(dadosDoGrafico.get(x).getAvaliacaoInt());
                        //Toast.makeText(getContext(), intGrafico.get(0), Toast.LENGTH_LONG).show();
    //                }

                    //if(teste != null){

                    //}else {
                      //  Toast.makeText(getContext(),"vazio",Toast.LENGTH_LONG).show();
                    //}



                    break;


            }
            return rootView;
        }


        /**
         * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
         * one of the sections/tabs/pages.
         */
        public static class SectionsPagerAdapter extends FragmentPagerAdapter {

            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                // getItem is called to instantiate the fragment for the given page.
                // Return a PlaceholderFragment (defined as a static inner class below).
                return PlaceholderFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {

                return 2;
            }
        }
        @Override
        public void onPause(){

            super.onPause();
            if(caixaHumor != null)
                caixaHumor.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                        caixaHumor = null;
                    }
                });
        }
    }

}
