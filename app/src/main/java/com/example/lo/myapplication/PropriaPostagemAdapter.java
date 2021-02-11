package com.example.lo.myapplication;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PropriaPostagemAdapter extends ArrayAdapter<DadosPostagem>{


        private Context contexto;
        private ArrayList<DadosPostagem> postagens;

        public PropriaPostagemAdapter(@NonNull Context c, @NonNull ArrayList<DadosPostagem> objects) {
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
                view = inflater.inflate(R.layout.layout_propria_postagem, parent, false);
            }
            Toast.makeText(contexto,""+ postagens.size(),Toast.LENGTH_SHORT).show();
            if(postagens.size() > 0){

                ImageView imgPostagem = view.findViewById(R.id.imgPropriaPostagem);
                TextView nomeUsuario = view.findViewById(R.id.lblNomeUsuarioPropriaPostagem);
                ImageView imgPerfilPostagem = view.findViewById(R.id.imgPerfilPropriaPostagem);
                TextView lblTempoPostagem = view.findViewById(R.id.lblTempoPostagem);
                String nome = postagens.get(position).getNomeUsuario();
                String urlPostagem = postagens.get(position).getUrl();
                String urlPerfil = postagens.get(position).getUrlPerfil();
                String tipo = postagens.get(position).getTipoUsuario();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String dataFormatada = dateFormat.format(postagens.get(position).getHoraPostagem());
                lblTempoPostagem.setText(dataFormatada);
                nomeUsuario.setText(nome);

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
