package com.example.projetobolsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

public class Apostilas extends Fragment {

    ListView listViewApostilas;
    String turma, materia, bimestre;
    List<String> termos;
    Bundle bundle;
    ArrayAdapter<String> adapter;

    public Apostilas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        turma = bundle.getString("turma");
        bimestre = bundle.getString("periodo");
        materia = bundle.getString("materia");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_apostilas, container, false);


        FloatingActionButton fab = view.findViewById(R.id.fabApostilas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdicinarApostila.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listViewApostilas = view.findViewById(R.id.listViewApostilasRecebidas);

        Firebase.conectarProf();
        Firebase.professorFB.child("turmas").child(turma).child("materias").child(materia).child(bimestre).child("apostilas").addValueEventListener(new ValueEventListener() {;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                termos = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    termos.add(data.getKey());

                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, termos);
                listViewApostilas.setAdapter(adapter);

                listViewApostilas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        bundle.putString("termo", adapter.getItem(position));
                        bundle.putString("funcao", "visualizar");

                        Intent intent = new Intent(getActivity(), TextoWiki.class);
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
