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


public class ApostilasAluno extends Fragment {

    Bundle bundle;
    Aluno aluno;
    ListView listViewApostilas;
    Intent intent;

    public ApostilasAluno() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        aluno = (Aluno) bundle.getSerializable("materia");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_apostilas_aluno, container, false);

        listViewApostilas = view.findViewById(R.id.listViewApostilasRecebidas);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference professorFB = database.getReference("professores").child(aluno.getIdProf()).child("turmas")
                .child(aluno.getTurma()).child("materias").child(aluno.getMateria()).child(aluno.getBimestre()).child("apostilas");
        professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    list.add(data.getKey());
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
                listViewApostilas.setAdapter(adapter);

                listViewApostilas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        aluno.setApostila(adapter.getItem(position));
                        intent = new Intent(getActivity(), TextoApostila.class);
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
        return view;
    }


}
