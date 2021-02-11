package com.example.lo.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AmigoAdapter extends ArrayAdapter<DadosPesquisa> {

    private Context context;
    private ArrayList<DadosPesquisa> amigos;

    public AmigoAdapter(@NonNull Context c, @NonNull ArrayList<DadosPesquisa> objects) {
        super(c, 0, objects);
        this.context = c;
        this.amigos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.layout_amigo, parent, false);
        }

        if(amigos.size() > 0){


            ImageView imgfoto = view.findViewById(R.id.imgFotoAmigo);
            TextView lblNome = view.findViewById(R.id.lblNomeAmigo);
            TextView lblNomeUsuario = view.findViewById(R.id.lblNomeUsuarioAmigo);

            String url = amigos.get(position).getFotoPerfil();
            String nome = amigos.get(position).getNome();
            String nomeUsuario = amigos.get(position).getNomeUsuario();

            lblNome.setText(nome);
            lblNomeUsuario.setText(nomeUsuario);

            Glide.with(context).load(url).into(imgfoto);




        }

        return view;


    }
}
