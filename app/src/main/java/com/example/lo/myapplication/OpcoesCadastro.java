package com.example.lo.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpcoesCadastro extends AppCompatActivity {

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
    private  static SharedPreferences primeiraVez;
    private static SharedPreferences.Editor editor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_opcoes_cadastro);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container2);

        TabLayout dots = findViewById(R.id.dots2);
        dots.setupWithViewPager(mViewPager, true);

        mViewPager.setAdapter(mSectionsPagerAdapter);


        primeiraVez = getSharedPreferences("PRIMEIRA_VEZ", MODE_PRIVATE);




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




        public Boolean verificarSenha(String senha, String confirmarSenha){
            if (confirmarSenha.equals(senha)){
                return true;
            }else{
                Snackbar.make(getView(),"Confirme a senha corretamente",Snackbar.LENGTH_LONG).setAction("",null).show();

                return false;
            }
        }
        private Animation animacao ;
        private Typeface fonteTitulo;
        private Typeface fonteTexto;
        private TextView lblUsu;
        private TextView lblDescri;
        private TextView lblInsira;
        private TextView lblNome;
        private TextView lblNomeUsu;
        private TextView lblDataNasc;
        private TextView lblGenero;
        private TextView lblEmail;
        private TextView lblEstado;
        private TextView lblCidade;
        private TextView lblSenha;
        private TextView lblConfirmarSenha;

        private EditText txtNome;
        private EditText txtNomeUsu;
        private EditText txtDataNasc;
        private EditText txtGenero;
        private EditText txtEmail;
        private EditText txtEstado;
        private EditText txtCidade;
        private EditText txtSenha;
        private EditText txtConfirmarSenha;
        private FirebaseFirestore banco;
        private FirebaseAuth autentificador;
        private Button btnConfirmarUsuario;
        private ImageView imgUsuario;
        private Usuario usuarios;
        private RadioGroup radioGroup;
        private RadioButton rbMasc;
        private RadioButton rbFem;




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {



            View rootView = null;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_opcoes_cadastro, container, false);

                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LeagueSpartan-Bold.otf");
                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(), "fonts/aileron.heavy.otf");

                    lblUsu = rootView.findViewById(R.id.lblUsu);

                    lblDescri = rootView.findViewById(R.id.lblTextoComum);
                    lblInsira = rootView.findViewById(R.id.lblInsira);
                    lblNome = rootView.findViewById(R.id.lblNome);
                    lblNomeUsu = rootView.findViewById(R.id.lblNomeUsu);
                    lblDataNasc = rootView.findViewById(R.id.lblDataNasc);
                    lblGenero = rootView.findViewById(R.id.lblGenero);
                    lblEmail = rootView.findViewById(R.id.lblEmail);
                    lblEstado = rootView.findViewById(R.id.lblEstado);
                    lblCidade = rootView.findViewById(R.id.lblCidade);
                    lblSenha = rootView.findViewById(R.id.lblSenha);
                    lblConfirmarSenha = rootView.findViewById(R.id.lblConfirmarSenha);
                    imgUsuario = rootView.findViewById(R.id.imgUsu);



                    txtNome = rootView.findViewById(R.id.txtNome);
                    txtNomeUsu = rootView.findViewById(R.id.txtNomeUsu);
                    txtDataNasc = rootView.findViewById(R.id.txtDataNasc);
                    txtEmail = rootView.findViewById(R.id.txtEmailUsuario);
                    txtEstado = rootView.findViewById(R.id.txtEstado);
                    txtCidade = rootView.findViewById(R.id.txtCidade);
                    txtSenha = rootView.findViewById(R.id.txtSenhaUsuario);
                    txtConfirmarSenha = rootView.findViewById(R.id.txtConfirmarSenha);

                    mascaraData(txtDataNasc);
                    mascaraEstado(txtEstado);
                    radioGroup = rootView.findViewById(R.id.rgGenero);
                    rbMasc = rootView.findViewById(R.id.rbMasc);
                    rbFem = rootView.findViewById(R.id.rbFem);

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId == R.id.rbMasc){
                                rbFem.setChecked(false);
                            }
                            if(checkedId == R.id.rbFem){
                                rbMasc.setChecked(false);
                            }
                        }
                    });


                    lblUsu.setTypeface(fonteTitulo);
                    lblDescri.setTypeface(fonteTexto);
                    lblInsira.setTypeface(fonteTitulo);
                    lblNome.setTypeface(fonteTexto);
                    lblNomeUsu.setTypeface(fonteTexto);
                    lblDataNasc.setTypeface(fonteTexto);
                    lblGenero.setTypeface(fonteTexto);
                    lblEmail.setTypeface(fonteTexto);
                    lblEstado.setTypeface(fonteTexto);
                    lblCidade.setTypeface(fonteTexto);
                    lblSenha.setTypeface(fonteTexto);
                    lblConfirmarSenha.setTypeface(fonteTexto);

                    btnConfirmarUsuario = rootView.findViewById(R.id.btnConfirmarUsuario);
                    autentificador = FirebaseAuth.getInstance();

                    AutentificarUsuario();

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_ouvinte, container, false);

                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(),"fonts/aileron.heavy.otf");

                    lblUsu = rootView.findViewById(R.id.lblUsu2);
                    lblDescri = rootView.findViewById(R.id.lblTextoOuvinte);
                    lblInsira = rootView.findViewById(R.id.lblInsira2);
                    lblNome = rootView.findViewById(R.id.lblNome2);
                    lblNomeUsu = rootView.findViewById(R.id.lblNomeOuvinte);
                    lblDataNasc = rootView.findViewById(R.id.lblDataNasc2);
                    lblGenero = rootView.findViewById(R.id.lblGenero2);
                    lblEmail = rootView.findViewById(R.id.lblEmail2);
                    lblEstado = rootView.findViewById(R.id.lblEstado2);
                    lblCidade = rootView.findViewById(R.id.lblCidade2);
                    lblSenha = rootView.findViewById(R.id.lblSenha2);
                    lblConfirmarSenha = rootView.findViewById(R.id.lblConfirmarSenha2);
                    imgUsuario = rootView.findViewById(R.id.imgUsu2);

                    lblUsu.setTypeface(fonteTitulo);
                    lblDescri.setTypeface(fonteTexto);
                    lblInsira.setTypeface(fonteTitulo);
                    lblNome.setTypeface(fonteTexto);
                    lblNomeUsu.setTypeface(fonteTexto);
                    lblDataNasc.setTypeface(fonteTexto);
                    lblGenero.setTypeface(fonteTexto);
                    lblEmail.setTypeface(fonteTexto);
                    lblEstado.setTypeface(fonteTexto);
                    lblCidade.setTypeface(fonteTexto);
                    lblSenha.setTypeface(fonteTexto);
                    lblConfirmarSenha.setTypeface(fonteTexto);

                    txtNome = rootView.findViewById(R.id.txtNome2);
                    txtNomeUsu = rootView.findViewById(R.id.txtNomeOuvinte);
                    txtDataNasc = rootView.findViewById(R.id.txtDataNasc2);
               //     txtGenero = rootView.findViewById(R.id.txtGenero2);
                    txtEmail = rootView.findViewById(R.id.txtEmailUsuario2);
                    txtEstado = rootView.findViewById(R.id.txtEstado2);
                    txtCidade = rootView.findViewById(R.id.txtCidade2);
                    txtSenha = rootView.findViewById(R.id.txtSenhaUsuario2);
                    txtConfirmarSenha = rootView.findViewById(R.id.txtConfirmarSenha2);
                    btnConfirmarUsuario = rootView.findViewById(R.id.btnConfirmarUsuario2);
                    autentificador = FirebaseAuth.getInstance();

                    mascaraData(txtDataNasc);
                    mascaraEstado(txtEstado);
                    radioGroup = rootView.findViewById(R.id.rgGenero2);
                    rbMasc = rootView.findViewById(R.id.rbMasc2);
                    rbFem = rootView.findViewById(R.id.rbFem2);

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId == R.id.rbMasc2){
                                rbFem.setChecked(false);
                            }
                            if(checkedId == R.id.rbFem2){
                                rbMasc.setChecked(false);
                            }
                        }
                    });

                    AutentificarUsuario();

                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_profissional, container, false);

                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(),"fonts/aileron.heavy.otf");

                    lblUsu = rootView.findViewById(R.id.lblUsu3);
                    lblDescri = rootView.findViewById(R.id.lblTextoProfissional);
                    lblInsira = rootView.findViewById(R.id.lblInsira3);
                    lblNome = rootView.findViewById(R.id.lblNome3);
                    lblNomeUsu = rootView.findViewById(R.id.lblNomeProfissional);
                    lblDataNasc = rootView.findViewById(R.id.lblDataNasc3);
                    lblGenero = rootView.findViewById(R.id.lblGenero3);
                    lblEmail = rootView.findViewById(R.id.lblEmail3);
                    lblEstado = rootView.findViewById(R.id.lblEstado3);
                    lblCidade = rootView.findViewById(R.id.lblCidade3);
                    lblSenha = rootView.findViewById(R.id.lblSenha3);
                    lblConfirmarSenha = rootView.findViewById(R.id.lblConfirmarSenha3);

                    txtNome = rootView.findViewById(R.id.txtNome3);
                    txtNomeUsu = rootView.findViewById(R.id.txtNomeProfissional);
                    txtDataNasc = rootView.findViewById(R.id.txtDataNasc3);
                 //   txtGenero = rootView.findViewById(R.id.txtGenero3);
                    txtEmail = rootView.findViewById(R.id.txtEmailUsuario3);
                    txtEstado = rootView.findViewById(R.id.txtEstado3);
                    txtCidade = rootView.findViewById(R.id.txtCidade3);
                    txtSenha = rootView.findViewById(R.id.txtSenhaUsuario3);
                    txtConfirmarSenha = rootView.findViewById(R.id.txtConfirmarSenha3);
                    imgUsuario = rootView.findViewById(R.id.imgUsu3);

                    mascaraData(txtDataNasc);
                    mascaraEstado(txtEstado);

                    radioGroup = rootView.findViewById(R.id.rgGenero3);
                    rbMasc = rootView.findViewById(R.id.rbMasc3);
                    rbFem = rootView.findViewById(R.id.rbFem3);

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId == R.id.rbMasc3){
                                rbFem.setChecked(false);
                            }
                            if(checkedId == R.id.rbFem3){
                                rbMasc.setChecked(false);
                            }
                        }
                    });

                    lblUsu.setTypeface(fonteTitulo);
                    lblDescri.setTypeface(fonteTexto);
                    lblInsira.setTypeface(fonteTitulo);
                    lblNome.setTypeface(fonteTexto);
                    lblNomeUsu.setTypeface(fonteTexto);
                    lblDataNasc.setTypeface(fonteTexto);
                    lblGenero.setTypeface(fonteTexto);
                    lblEmail.setTypeface(fonteTexto);
                    lblEstado.setTypeface(fonteTexto);
                    lblCidade.setTypeface(fonteTexto);
                    lblSenha.setTypeface(fonteTexto);
                    lblConfirmarSenha.setTypeface(fonteTexto);

                    btnConfirmarUsuario = rootView.findViewById(R.id.btnConfirmarUsuario3);
                    autentificador = FirebaseAuth.getInstance();

                    AutentificarUsuario();

                    break;
            }

            return rootView;
        }

        public void AutentificarUsuario(){
            animacao = AnimationUtils.loadAnimation(getContext(),  R.anim.animacao);
            try {

                btnConfirmarUsuario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!txtNome.getText().toString().isEmpty() || !txtNomeUsu.getText().toString().isEmpty() || !txtDataNasc.getText().toString().isEmpty() || !txtEmail.getText().toString().isEmpty() || !txtEstado.getText().toString().isEmpty() || !txtCidade.getText().toString().isEmpty() || !txtSenha.getText().toString().isEmpty()) {
                            String genero = null;
                            if (rbMasc.isChecked()){
                                genero = "Masculino";

                            }
                            if (rbFem.isChecked()){
                                genero = "Feminino";
                            }
                            usuarios = new Usuario();
                            usuarios.setNome(txtNome.getText().toString());
                            usuarios.setNomeUsuario(txtNomeUsu.getText().toString());
                            usuarios.setDataNasc(txtDataNasc.getText().toString());
                            usuarios.setEstado(txtEstado.getText().toString());
                            usuarios.setCidade(txtCidade.getText().toString());
                            usuarios.setEmail(txtEmail.getText().toString());
                            usuarios.setGenero(genero);
                            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                                case 1:
                                    usuarios.setTipoUsuario("Comum");
                                    break;
                                case 2:
                                    usuarios.setTipoUsuario("Ouvinte");
                                    break;
                                case 3:
                                    usuarios.setTipoUsuario("Profissional");
                                    break;
                            }

                            btnConfirmarUsuario.startAnimation(animacao);
                            banco = FirebaseFirestore.getInstance();


                            if (txtEmail.getText().toString().isEmpty() || txtSenha.getText().toString().isEmpty()) {
                                Snackbar.make(getView(),  "E-mail inválido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }else if(txtSenha.getText().toString().isEmpty()){
                                Snackbar.make(getView(), "Senha inválida", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }else {
                                if (verificarSenha(txtConfirmarSenha.getText().toString(), txtSenha.getText().toString())) {

                                    autentificador.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtSenha.getText() .toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {

                                                    banco.collection("Usuario").document(CodificarBase64.codificarTexto(txtEmail.getText().toString())).set(usuarios);

                                                    Toast.makeText(getActivity(), "deu certo", Toast.LENGTH_LONG).show();

                                                    Preferencias preferencias = new Preferencias(getContext());
                                                    String identificarUsuario = CodificarBase64.codificarTexto(usuarios.getEmail());
                                                    preferencias.salvarDados(identificarUsuario);

                                                    editor = primeiraVez.edit();
                                                    editor.putInt("PRIMEIRA_VEZ_USUARIO", 1).apply();
                                                    startActivity(new Intent(getActivity().getApplicationContext(), InserirFotoPerfil.class));
                                                }
                                            });

                                        }
                                    });

                                }else{
                                    Snackbar.make(getView(), "Senha incorreta", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }

                            }

                        } else {
                            Snackbar.make(v, "Preencha todos os dados corretamente", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        }
                    }
                });

            }catch(Exception e){
                Toast.makeText(getActivity(), "erro :" + e, Toast.LENGTH_LONG).show();
            }

        }
        public void mascaraData(EditText txt){
            SimpleMaskFormatter dataFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
            MaskTextWatcher dataWatcher = new MaskTextWatcher(txt, dataFormatter);
            txt.addTextChangedListener(dataWatcher);
        }
        public void mascaraEstado(EditText txt){
            SimpleMaskFormatter estadoFormatter = new SimpleMaskFormatter("ll");
            MaskTextWatcher estadoWatcher = new MaskTextWatcher(txt, estadoFormatter);
            txt.addTextChangedListener(estadoWatcher);
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

            return 3;
        }
    }


}
