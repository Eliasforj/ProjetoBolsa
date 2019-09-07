package com.example.projetobolsa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApostilaAluno extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    String turma, tipo, periodo, materia, idProf;
    ListView listViewApostilas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apostila_aluno);

        intent = getIntent();
        bundle = new Bundle();
        bundle = intent.getExtras();
        final Aluno aluno = (Aluno) bundle.getSerializable("materia");
        listViewApostilas = findViewById(R.id.listViewApostilasMateria);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference professorFB = database.getReference("professores").child(aluno.getIdProf()).child("turmas").child(aluno.getTurma()).child("materias").child(aluno.getMateria());
        professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.child(aluno.getBimestre()).getChildren())
                    list.add(data.getKey());
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(ApostilaAluno.this, android.R.layout.simple_list_item_1, list);
                listViewApostilas.setAdapter(adapter);

                listViewApostilas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        aluno.setApostila(adapter.getItem(position));
                        intent = new Intent(ApostilaAluno.this, TextoApostila.class);
                        bundle = new Bundle();
                        bundle.putSerializable("apostila", aluno);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
