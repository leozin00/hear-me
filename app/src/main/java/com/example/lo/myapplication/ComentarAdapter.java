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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ComentarAdapter extends ArrayAdapter<DadosComentario>{
    private Context context;
    private ArrayList<DadosComentario> comentarios;

    public ComentarAdapter(@NonNull Context c, @NonNull ArrayList<DadosComentario> objects) {
        super(c, 0, objects);
        this.context = c;
        this.comentarios = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.layout_comentar_adapter, parent, false);
        }

        if(comentarios.size() > 0){

            Toast.makeText(context,comentarios.size() + "" , Toast.LENGTH_SHORT).show();
            ImageView imgfoto = view.findViewById(R.id.imgFotoComentario);
            TextView lblNome = view.findViewById(R.id.lblNomeComentario);
            TextView lblTipoUsuario = view.findViewById(R.id.lbltipoUsuarioComentario);
            TextView lblTextoPostagem = view.findViewById(R.id.lblComentario);

            String url = comentarios.get(position).getUrl();
            String nome = comentarios.get(position).getNomeUsu();
            String txtPostagem = comentarios.get(position).getTxtPostagem();
            String TipoUsuario = comentarios.get(position).getTipoUsu();


            lblNome.setText(nome);
            lblTipoUsuario.setText(TipoUsuario);
            lblTextoPostagem.setText(txtPostagem);

            Glide.with(context).load(url).into(imgfoto);




        }

        return view;


    }
}
