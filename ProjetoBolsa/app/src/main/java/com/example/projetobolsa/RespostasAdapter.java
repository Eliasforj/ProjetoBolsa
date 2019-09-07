package com.example.projetobolsa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RespostasAdapter extends ArrayAdapter<RespostasAluno> {

    List<RespostasAluno> respostas;
    TextView txtResposta, txtNome;
    Context context;

    public RespostasAdapter(Context context, List<RespostasAluno> object){
        super(context, 0, object);

        this.context = context;
        this.respostas = object;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(respostas != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lista_respostas, parent, false);
            txtNome = convertView.findViewById(R.id.txtAluno);
            txtResposta = convertView.findViewById(R.id.txtResposta);


                String nome = respostas.get(position).getNome();
                String resposta = respostas.get(position).getResposta();

                txtResposta.setText(resposta);
                txtNome.setText(nome);
            }
        return convertView;
    }

}
