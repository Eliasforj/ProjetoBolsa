package com.example.projetobolsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuestionariosAluno extends Fragment {

    ListView listViewQuestionarios;
    List<String> questionarios;
    ArrayAdapter<String> adapter;
    Bundle bundle;
    Aluno aluno;

    public QuestionariosAluno() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        aluno = (Aluno) bundle.getSerializable("materia");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_questionarios_aluno, container, false);

        listViewQuestionarios = view.findViewById(R.id.listViewQuestionariosRecebidos);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference professorFB = database.getReference("professores").child(aluno.getIdProf()).child("turmas")
                .child(aluno.getTurma()).child("materias").child(aluno.getMateria()).child(aluno.getBimestre()).child("questionarios");
        professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionarios = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    questionarios.add(data.getKey());

                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, questionarios);
                listViewQuestionarios.setAdapter(adapter);

                listViewQuestionarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        bundle.putString("questionario", adapter.getItem(position));
                        Intent intent = new Intent(getActivity(), ResponderPerguntas.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
