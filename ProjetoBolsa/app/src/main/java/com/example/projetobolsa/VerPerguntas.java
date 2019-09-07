package com.example.projetobolsa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerPerguntas extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    String materia, turma, periodo, questionario;
    List<String> perguntas, questoes;
    ArrayAdapter<String> adapter;
    ListView listViewPerguntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ver_perguntas);

        listViewPerguntas = findViewById(R.id.listViewPerguntas);

        intent = getIntent();
        bundle = intent.getExtras();

        materia = bundle.getString("materia");
        periodo = bundle.getString("periodo");
        turma = bundle.getString("turma");
        questionario = bundle.getString("questionario");

        Firebase.conectarProf();
        Firebase.professorFB.child("turmas").child(turma).child("materias").child(materia).child(periodo)
                .child("questionarios").child(questionario).child("perguntas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                perguntas = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    perguntas.add((String) data.getValue());
                    Toast.makeText(VerPerguntas.this, data.getKey(), Toast.LENGTH_SHORT).show();
                    }

                questoes = new ArrayList<>();
                for (int i = 0; i < perguntas.size(); i++)
                    questoes.add((i+1) + ") " + perguntas.get(i));
                adapter = new ArrayAdapter<>(VerPerguntas.this, android.R.layout.simple_list_item_1, questoes);
                listViewPerguntas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
