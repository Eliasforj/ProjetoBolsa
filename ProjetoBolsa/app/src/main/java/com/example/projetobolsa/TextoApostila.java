package com.example.projetobolsa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TextoApostila extends AppCompatActivity {

    TextView txtTexto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_texto_apostila);

        txtTexto = findViewById(R.id.txtTextoApostila);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();

        bundle = intent.getExtras();
        final Aluno aluno = (Aluno) bundle.getSerializable("apostila");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference apostilaFB = database.getReference("professores").child(aluno.getIdProf()).child("turmas").child(aluno.getTurma()).child("materias")
                .child(aluno.getMateria()).child(aluno.getBimestre()).child("apostilas");

        apostilaFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> textos = new ArrayList<>();
                List<String> apostilas = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    textos.add((String) data.getValue());
                    apostilas.add(data.getKey());
                }
                for (int i = 0; i<apostilas.size();i++)
                    if (apostilas.get(i).equals(aluno.getApostila()))
                        txtTexto.setText(textos.get(i));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
