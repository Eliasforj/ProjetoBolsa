package com.example.projetobolsa;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastroAluno extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference professorFB;
    FirebaseUser user;
    Spinner spńTurmas;
    ArrayAdapter<Turma> adapter;
    EditText edtEmail, edtSenha, edtNome;
    Button btnGravar;
    String  id;
    RadioButton rdbMedio, rdbSuperior;
    Turma turma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cadastro_aluno);

        final List<Turma> list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference caminho = database.getReference("professores");
        professorFB = caminho;
        spńTurmas = findViewById(R.id.spnTurmas);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmailAluno);
        edtSenha = findViewById(R.id.edtSenhaAluno);
        btnGravar = findViewById(R.id.btnGravarAluno);
        rdbMedio = findViewById(R.id.rdbEnsinoMedio);
        rdbSuperior = findViewById(R.id.rdbEnsinoSuperior);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarUsuario(edtEmail.getText().toString(), edtSenha.getText().toString());
            }
        });

        professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    id = data.getKey();

                    professorFB = caminho.child(id);
                    professorFB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.child("turmas").getChildren()){
                                list.add(data.getValue(Turma.class));
                            }
                            List<Turma> turmas = new ArrayList<>();
                            turmas.add(list.get(0));

                            for (int i = 1; i<list.size(); i++){
                                boolean repete = false;
                                String verifica = list.get(i).getTurma();
                                for(int j = 0; j<turmas.size(); j++)
                                    if(verifica.equals(turmas.get(j).getTurma()))
                                        repete = true;

                                if (!(repete))
                                    turmas.add(list.get(i));
                            }

                            adapter=new ArrayAdapter<>(CadastroAluno.this, android.R.layout.simple_spinner_dropdown_item,turmas);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spńTurmas.setAdapter(adapter);
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

        spńTurmas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                turma = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void criarUsuario(String email, String senha) {
        final FirebaseAuth autenticacao = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        autenticacao.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference professorFB = database.getReference("alunos").child(autenticacao.getUid());
                            Aluno aluno = new Aluno();

                            aluno.setNome(edtNome.getText().toString());
                            aluno.setTurma(turma.getTurma());
                            aluno.setPeriodo(turma.getPeriodo());
                            professorFB.setValue(aluno);
                            finish();
                        }
                        else {
                            Toast.makeText(CadastroAluno.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
