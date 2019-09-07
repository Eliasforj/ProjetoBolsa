package com.example.projetobolsa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalAluno extends AppCompatActivity {
    Aluno turma;
    FirebaseDatabase database;
    DatabaseReference professorFb, caminho;
    ListView listViewMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_principal_aluno);


        listViewMaterias = findViewById(R.id.listViewMaterias);
        database = FirebaseDatabase.getInstance();
        final List<String> id = new ArrayList<>();
        caminho = database.getReference("professores");
        Firebase.conectarAluno();
        Firebase.alunoFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> list = new ArrayList<>();
                final Aluno aluno = new Aluno();
                turma = new Aluno();
                for(DataSnapshot data : dataSnapshot.getChildren()) {

                    list.add((String) data.getValue());

                }

                aluno.setNome(list.get(0));
                aluno.setPeriodo(list.get(1));
                aluno.setTurma(list.get(2));

                caminho.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final List<Aluno> lista = new ArrayList<>();

                        final List<String> ids = new ArrayList<>();
                        for(DataSnapshot data : dataSnapshot.getChildren())
                            ids.add(data.getKey());

                        for (int i = 0; i < ids.size();i++){
                            professorFb  = caminho.child(ids.get(i)).child("turmas").child(aluno.getTurma()).child("materias");
                            final int finalI = i;
                            professorFb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                                        Aluno aluno1 = new Aluno();
                                        aluno1.setPeriodo(list.get(1));
                                        aluno1.setTurma(list.get(2));
                                        aluno1.setNome(list.get(0));
                                        aluno1.setMateria(data.getKey());
                                        aluno1.setIdProf(ids.get(finalI));
                                        lista.add(aluno1);
                                    }
                                    final ArrayAdapter<Aluno> adapter = new ArrayAdapter<>(PrincipalAluno.this, android.R.layout.simple_list_item_1, lista);
                                    listViewMaterias.setAdapter(adapter);

                                    listViewMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            final Aluno aluno1;
                                            aluno1 = adapter.getItem(position);
                                            final Intent intent = new Intent(PrincipalAluno.this, PrincipalMateriaisAluno.class);
                                            final Bundle bundle=new Bundle();
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalAluno.this);
                                            final ListView listViewPeriodos = new ListView(PrincipalAluno.this);
                                            List<String> list1 = new ArrayList<>();
                                            Toast.makeText(PrincipalAluno.this, aluno.getPeriodo(), Toast.LENGTH_SHORT).show();
                                            if (aluno.getPeriodo().equals("Bimestres")){
                                                builder.setTitle("Selecione o bimestre");
                                                list1.add("1º Bimestre");
                                                list1.add("2º Bimestre");
                                                list1.add("3º Bimestre");
                                                list1.add("4º Bimestre");
                                            }else{
                                                builder.setTitle("Selecione o semestre");
                                                list1.add("1º Semestre");
                                                list1.add("2º Semestre");
                                            }
                                            final ArrayAdapter<String> adapter1 = new ArrayAdapter<>(PrincipalAluno.this, android.R.layout.simple_list_item_1, list1);
                                            listViewPeriodos.setAdapter(adapter1);
                                            builder.setView(listViewPeriodos);
                                            listViewPeriodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    aluno1.setBimestre(adapter1.getItem(position));
                                                    bundle.putSerializable("materia", aluno1);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    builder.setCancelable(true);
                                                }
                                            });
                                            builder.show();
                                        }


                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_sair){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getBaseContext(), Login.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
