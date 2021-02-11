package com.example.lo.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context contexto;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.contexto = c;
        this.mensagens = objects;

    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        View view = null;

        if(mensagens != null){

            Preferencias preferencias = new Preferencias(contexto);
            String idUsuario = preferencias.getIdentificador();

            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            Mensagem mensagem = mensagens.get(position);

            if(idUsuario.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.mensagem_direita, parent, false);
            }else{
                view = inflater.inflate(R.layout.mensagem_esquerda, parent, false);
            }


            TextView textoMensagem = view.findViewById(R.id.txtMensagemEsquerda);
            textoMensagem.setText(mensagem.getMensagem());

        }

        return view;
    }
}
