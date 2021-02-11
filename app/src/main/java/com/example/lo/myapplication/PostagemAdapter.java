package com.example.lo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostagemAdapter extends ArrayAdapter<DadosPostagem> {

    private Context contexto;
    private ArrayList<DadosPostagem> postagens;
    private int i = 0;
    public PostagemAdapter(@NonNull Context c, @NonNull ArrayList<DadosPostagem> objects) {
        super(c, 0, objects);
        this.contexto = c;
        this.postagens = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);



            view = inflater.inflate(R.layout.layout_postagem, parent, false);
        }

        if(postagens.size() > 0){

            ImageView imgPostagem = view.findViewById(R.id.imgPostagem);
            final TextView nomeUsuario = view.findViewById(R.id.lblNomeUsuarioPostagem);
            ImageView imgPerfilPostagem = view.findViewById(R.id.imgPerfilPostagem);
            TextView tipoUsuario = view.findViewById(R.id.lblTipoUsuarioPostagem);
            TextView textoPostagem = view.findViewById(R.id.lblTextoPublicacao);


            final ImageView imgCurtir  = view.findViewById(R.id.imgCurtir);
            ImageView imgComentar  = view.findViewById(R.id.imgComentar);
            final String nome = postagens.get(position).getNomeUsuario();
            String urlPostagem = postagens.get(position).getUrl();
            String urlPerfil = postagens.get(position).getUrlPerfil();
            String tipo = postagens.get(position).getTipoUsuario();
            final String emailUsu = postagens.get(position).getEmail();
            final long horario = postagens.get(position).getHoraPostagem();
            final String horarioString  = String.valueOf(horario);
            final FirebaseFirestore banco  = ConfiguracaoFirebase.getFirebase();
            final String email = ConfiguracaoFirebase.getAutentificador().getCurrentUser().getEmail();
            nomeUsuario.setText(nome);
            tipoUsuario.setText(tipo);
            String texto = postagens.get(position).getTextoPostagem();

            nomeUsuario.setText(nome);
            tipoUsuario.setText(tipo);
            textoPostagem.setText(texto);

            imgCurtir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (i){
                        case 0:
                            Map<String, Object> curtida = new HashMap<>();
                            curtida.put("email", email);
                            Calendar tempoAgr = Calendar.getInstance();
                            banco.collection("Usuario").document(emailUsu).collection("Postagens").document(horarioString).collection("curtidas").document(nome).set(curtida).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    TrocarIcon trocarIcon = new TrocarIcon();
                                    trocarIcon.TrocarIconPreto(imgCurtir,"icone_curtir2",contexto);
                                    Toast.makeText(getContext(), "curtido com sucesso",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "curtida fracassou ",Toast.LENGTH_SHORT).show();
                                }
                            });
                            i = 1;
                        break;
                        case 1:
                            banco.collection("Usuario").document(emailUsu).collection("Postagens").document(horarioString).collection("curtidas").document(nome).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    TrocarIcon trocarIcon = new TrocarIcon();
                                    trocarIcon.TrocarIconPreto(imgCurtir,"icone_curtir",contexto);
                                }
                            });
                            i = 0;
                        break;
                    }



                }
            });
            imgComentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(contexto,Comentar.class);
                    i.putExtra("postagem",String.valueOf( postagens.get(position).getHoraPostagem()));
                    i.putExtra("donoPostagem",emailUsu);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    contexto.startActivity(i);


                }
            });



            if (tipo.equals("Profissional")){

                int id = contexto.getResources().getIdentifier("radius_foto_perfil_profissional", "drawable", contexto.getPackageName());
                Drawable drawable = contexto.getResources().getDrawable(id);
                imgPerfilPostagem.setBackground(drawable);

            }else if(tipo.equals("Ouvinte")){

                int id = contexto.getResources().getIdentifier("radius_foto_perfil_ouvinte", "drawable", contexto.getPackageName());
                Drawable drawable = contexto.getResources().getDrawable(id);
                imgPerfilPostagem.setBackground(drawable);

            }else if(tipo.equals("Comum")){

                int id = contexto.getResources().getIdentifier("radius_foto_perfil_comum", "drawable", contexto.getPackageName());
                Drawable drawable = contexto.getResources().getDrawable(id);
                imgPerfilPostagem.setBackground(drawable);

            }

            Glide.with(contexto)
                    .load(urlPostagem)
                    .into(imgPostagem);

            Glide.with(contexto)
                    .load(urlPerfil)
                    .into(imgPerfilPostagem);

        }

        return view;
    }
}
