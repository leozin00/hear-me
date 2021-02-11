package com.example.lo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

public class Slide extends AppCompatActivity {

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
    private  static SharedPreferences.Editor editor;
    private static SharedPreferences primeiraVez;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = ConfiguracaoFirebase.getAutentificador();
        if(auth.getCurrentUser()    != null){
            startActivity(new Intent(Slide.this,MainActivity.class));
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slide);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);

        TabLayout dots = findViewById(R.id.dots);
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
        private TextView lblEmail;
        private TextView lblLogin;
        private TextView lblSenha;
        private TextView lblBemvindo;
        private TextView lblSobre;
        private TextView lblDescri;
        private Button btnOK;
        private TextView lblCadastrar;
        private FirebaseAuth autentificador;
        public EditText emailUsuario;
        public EditText senhaUsuario;
        private Typeface fonteTitulo;
        private Typeface fonteTexto;
        private Animation animacao ;

        private FirebaseFirestore firebase;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_slide, container, false);
                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/LeagueSpartan-Bold.otf");


                    lblBemvindo = rootView.findViewById(R.id.lblBemvindo);
                    lblBemvindo.setTypeface(fonteTitulo);
                break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_segundo, container, false);
                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/LeagueSpartan-Bold.otf");
                    fonteTexto = Typeface.createFromAsset(getActivity().getAssets(),"fonts/aileron.heavy.otf");
                    lblSobre = rootView.findViewById(R.id.lblSobre);
                    lblDescri = rootView.findViewById(R.id.lblDescricao);

                    lblSobre.setTypeface(fonteTitulo);
                    lblDescri.setTypeface(fonteTexto);

                break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_terceiro, container, false);

                    firebase = ConfiguracaoFirebase.getFirebase();

                    fonteTitulo = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/LeagueSpartan-Bold.otf");

                    btnOK = rootView.findViewById(R.id.btnOK);
                    lblEmail =  rootView.findViewById(R.id.lblEmail);
                    lblLogin = rootView.findViewById(R.id.lblLogin);
                    lblSenha = rootView.findViewById(R.id.lblSenha);
                    lblCadastrar = rootView.findViewById(R.id.lblCadastrar);
                    emailUsuario = rootView.findViewById(R.id.txtEmailUsuario);
                    senhaUsuario = rootView.findViewById(R.id.txtSenha);
                    animacao = AnimationUtils.loadAnimation(getContext(),  R.anim.animacao);

                    lblLogin.setTypeface(fonteTitulo);
                    lblEmail.setTypeface(fonteTitulo);
                    lblSenha.setTypeface(fonteTitulo);
                    lblCadastrar.setTypeface(fonteTitulo);
                    btnOK.setTypeface(fonteTitulo);

                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            btnOK.startAnimation(animacao);
                            if(!emailUsuario.getText().toString().isEmpty() && !senhaUsuario.getText().toString().isEmpty()){
                                autentificador = FirebaseAuth.getInstance();
                                autentificador.signInWithEmailAndPassword(emailUsuario.getText().toString(), senhaUsuario.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()){
                                            try {
                                                throw  task.getException();
                                            }catch (FirebaseAuthInvalidUserException e ){

                                                Snackbar.make(view, "Usuário não cadatrado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            }catch (FirebaseAuthWeakPasswordException e) {
                                                Snackbar.make(view, "Senha Incorreta", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            }catch (FirebaseTooManyRequestsException e){
                                                Snackbar.make(view, "Muitas tentativas falhas, tente novamente em 5 segundos", Snackbar.LENGTH_LONG).setAction("Action", null).show();


                                            }catch (FirebaseAuthInvalidCredentialsException e){
                                                Snackbar.make(view, "E-mail inválido", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                                            }catch (FirebaseNetworkException e ){
                                                Snackbar.make(view, "Verfique sua conexão com a internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                                            }catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getActivity().getApplicationContext(), "Não" + task.getException(), Toast.LENGTH_LONG).show();
                                            }
                                        }else{

                                            Preferencias preferencias = new Preferencias(getContext());
                                            String identificarUsuario = CodificarBase64.codificarTexto(emailUsuario.getText().toString());
                                            preferencias.salvarDados(identificarUsuario);

                                            editor = primeiraVez.edit();
                                            editor.putInt("PRIMEIRA_VEZ_USUARIO", 1).apply();
                                            Snackbar.make(view, "Logado com sucesso !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                        }
                                    }
                                });
                            }else{
                                Snackbar.make(view, "Preencha os dados corretamente", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                //Toast.makeText(getContext(),"dasdasdas",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    lblCadastrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lblCadastrar.startAnimation(animacao);
                            startActivity(new Intent(getActivity().getApplicationContext(), OpcoesCadastro.class ));
                        }
                    });

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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


}
