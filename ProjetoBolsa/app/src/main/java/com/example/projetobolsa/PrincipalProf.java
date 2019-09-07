package com.example.projetobolsa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalProf extends AppCompatActivity {


    List<Turma> turmas = new ArrayList<>();
    List<String> listaTurma, materias;
    List<String> listaMateria;
    List<String> listaPeriodo;
    Spinner spnTurma, spnMateria, spnPeriodo;
    EditText novaTurma, novaMateria;
    ArrayAdapter<String> adapterTurmas, adapterPeriodo, adapterMateria;
    Turma turma;
    RadioGroup radioGroup;
    String turmaSelecionada, materiaSelecionada, materia, periodoSelecionado;
    Button btnProoseguir;
    Materia materiaTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_principal_prof);

        spnTurma = findViewById(R.id.spnTurma);
        spnMateria = findViewById(R.id.spnMateria);
        spnPeriodo = findViewById(R.id.spnPeríodo);
        btnProoseguir = findViewById(R.id.btnProsseguir);
        btnProoseguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(turmaSelecionada.equals("Selecione uma turma"))) {
                    Intent intent = new Intent(PrincipalProf.this, PrincipalMateriaisProf.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("turma", turmaSelecionada);
                    bundle.putString("periodo", periodoSelecionado);
                    bundle.putString("materia", materiaSelecionada);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else
                    Toast.makeText(PrincipalProf.this, "Selecione uma turma", Toast.LENGTH_SHORT).show();

            }
        });

        gerenciarCursos();

    }

    private void gerenciarCursos() {

        Firebase.conectarProf();
        Firebase.professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaTurma = new ArrayList<>();
                turmas = new ArrayList<>();
                listaTurma.add("Selecione uma turma");
                for (DataSnapshot data : dataSnapshot.child("turmas").getChildren()) {
                    listaTurma.add(data.getValue(Turma.class).getTurma());
                    turmas.add(data.getValue(Turma.class));
                }
                listaTurma.add("Adicionar nova turma");
                adapterTurmas = new ArrayAdapter<>(PrincipalProf.this, android.R.layout.simple_spinner_dropdown_item, listaTurma);
                adapterTurmas.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spnTurma.setAdapter(adapterTurmas);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spnTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Adicionar nova turma")) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View tela = inflater.inflate(R.layout.adiciona_turma, null, false);
                    AlertDialog.Builder cadastro = new AlertDialog.Builder(PrincipalProf.this);
                    novaTurma = tela.findViewById(R.id.edtNovaTurma);
                    radioGroup  = tela.findViewById(R.id.groupBox);
                    RadioButton rdbEnsinoMedio = tela.findViewById(R.id.rdbEnsinoMedio);
                    RadioButton rdbEnsinoSuperior = tela.findViewById(R.id.rdbEnsinoSuperior);
                    rdbEnsinoMedio.setId(1);
                    rdbEnsinoSuperior.setId(2);
                    cadastro.setTitle("Cadastre um nova turma:");
                    cadastro.setView(tela);
                    cadastro.setPositiveButton("Adicionar", dialogInterface)
                            .setNegativeButton("Cancelar", dialogInterface)
                            .show();
                }else{
                    turmaSelecionada = parent.getItemAtPosition(position).toString();
                    for(int i = 0; i<turmas.size(); i++)
                        if(turmas.get(i).getTurma().equals(turmaSelecionada)) {
                            gerenciarMaterias(turmas.get(i));
                            gerenciarPeriodos(turmas.get(i));
                        }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void gerenciarMaterias(Turma turma) {

        Firebase.conectarProf();
        Firebase.professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                materias = new ArrayList<>();

                listaMateria = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.child("turmas").child(turmaSelecionada).child("materias").getChildren())
                    listaMateria.add(data.getKey());
                listaMateria.add("Adicionar nova matéria");

                adapterMateria = new ArrayAdapter<>(PrincipalProf.this, android.R.layout.simple_spinner_dropdown_item, listaMateria);
                adapterMateria.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spnMateria.setAdapter(adapterMateria);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        spnMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Adicionar nova matéria")){ ;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalProf.this);
                    novaMateria = new EditText(PrincipalProf.this);
                    builder.setTitle("Adicione uma nova matéria:");
                    novaMateria.setHint("Matéria");
                    builder.setView(novaMateria);
                    builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            materiaTurma= new Materia();
                            materiaTurma.setMateria(novaMateria.getText().toString());
                            Firebase.incluirMateria(turmaSelecionada, materiaTurma);
                            adapterMateria.notifyDataSetChanged();
                        }
                    }).setNegativeButton("Cancelar",dialogInterface);

                    builder.show();

                }else
                materiaSelecionada = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void gerenciarPeriodos(Turma turma){
        listaPeriodo = new ArrayList<>();

                if(turma.getPeriodo().equals("Bimestres")){
                    listaPeriodo.add("1º Bimestre");
                    listaPeriodo.add("2º Bimestre");
                    listaPeriodo.add("3º Bimestre");
                    listaPeriodo.add("4º Bimestre");
                }else{
                    listaPeriodo.add("1º Semestre");
                    listaPeriodo.add("2º Semestre");
                }
                adapterPeriodo = new ArrayAdapter<>(PrincipalProf.this, android.R.layout.simple_spinner_dropdown_item, listaPeriodo);
                adapterPeriodo.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spnPeriodo.setAdapter(adapterPeriodo);

                spnPeriodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        periodoSelecionado = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }




    DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            turma = new Turma();
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    int id = radioGroup.getCheckedRadioButtonId();
                    switch (id){
                        case 1:
                            turma.setTurma(novaTurma.getText().toString());
                            turma.setPeriodo("Bimestres");
                            break;
                        case 2:
                            turma.setTurma(novaTurma.getText().toString());
                            turma.setPeriodo("Semestres");
                            break;
                    }

                    Firebase.incluirTurma(turma.getTurma(), turma);
                    Toast.makeText(PrincipalProf.this, "Operação concluida", Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Toast.makeText(PrincipalProf.this, "Operação cancelada", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };
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
