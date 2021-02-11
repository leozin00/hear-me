package com.example.lo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

class SugestaoAdapter extends android.widget.ArrayAdapter<DadosPesquisa> {

    private Context context;
    private ArrayList<DadosPesquisa> sugestoes;

    public SugestaoAdapter(@NonNull Context c, @NonNull ArrayList<DadosPesquisa> objects) {
        super(c, 0, objects);
        this.context = c;
        this.sugestoes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.layout_sugestao, parent, false);
        }

        if(sugestoes.size() > 0){

            LinearLayout linear = view.findViewById(R.id.linearSugestao);
            ImageView imgfoto = view.findViewById(R.id.imgFotoSugestao);
            TextView lblNome = view.findViewById(R.id.lblNomeSugestao);
            TextView lblNomeUsuario = view.findViewById(R.id.lblNomeUsuarioSugestao);

            String url = sugestoes.get(position).getFotoPerfil();
            final String nome = sugestoes.get(position).getNome();
            final String nomeUsuario = sugestoes.get(position).getNomeUsuario();

            lblNome.setText(nome);
            lblNomeUsuario.setText(nomeUsuario);

            Glide.with(context).load(url).into(imgfoto);




        }

        return view;


    }
}
