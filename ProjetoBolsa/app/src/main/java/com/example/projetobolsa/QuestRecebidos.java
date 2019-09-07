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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestRecebidos extends Fragment {

    ListView listViewQuestionarios;
    List<String> questionarios;
    ArrayAdapter<String> adapter;
    String turma, materia, periodo;
    Intent intent;
    Bundle bundle;

    public QuestRecebidos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        turma = bundle.getString("turma");
        periodo = bundle.getString("periodo");
        materia = bundle.getString("materia");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_quest_recebidos, container, false);

        listViewQuestionarios = view.findViewById(R.id.listViewQuestionariosRespondidos);
        listarQuestionarios();

        return view;
    }

    private void listarQuestionarios() {
        Firebase.conectarProf();
        Firebase.professorFB.child("turmas").child(turma).child("materias").child(materia).child(periodo)
                .child("questionarios").addValueEventListener(new ValueEventListener() {
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
                        bundle.putString("turma", turma);
                        bundle.putString("materia", materia);
                        bundle.putString("periodo", periodo);
                        bundle.putString("questionario", adapter.getItem(position));
                        intent = new Intent(getActivity(), Respostas.class);
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
