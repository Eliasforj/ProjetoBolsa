package com.example.projetobolsa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Respostas extends AppCompatActivity {

    ListView listViewrespostas;
    ArrayList<RespostasAluno> respostas, respostaGeral;
    ArrayAdapter<RespostasAluno> adapter;
    List<String> perguntas;
    ArrayAdapter adapterPerguntas;
    ActionBar actionBar;
    String questionario, turma, materia, periodo, perguntaSelecionada;
    boolean repete = false;
    int contador;
    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_respostas);

        alert = new AlertDialog.Builder(Respostas.this);


        listViewrespostas = findViewById(R.id.listViewRespostas);
        contador = 0;
        actionBar = getSupportActionBar();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        questionario = bundle.getString("questionario");
        turma = bundle.getString("turma");
        materia = bundle.getString("materia");
        periodo = bundle.getString("periodo");

        listarRespostas();
    }

    private void listarRespostas() {
        Firebase.conectarProf();
        Firebase.professorFB.child("turmas").child(turma).child("materias").child(materia).child(periodo)
                .child("questionarios").child(questionario).child("recebidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pergunta;
                respostaGeral = new ArrayList<>();
                perguntas = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    pergunta = data.getKey();
                    perguntas.add(data.getKey());
                    if (!(repete)) {
                        perguntaSelecionada = pergunta;
                        repete = true;
                    }

                    final String finalPergunta = pergunta;
                    Firebase.professorFB.child("turmas").child(turma).child("materias").child(materia).child(periodo)
                            .child("questionarios").child(questionario).child("recebidos").child(pergunta).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                RespostasAluno respostasAluno = new RespostasAluno();
                                respostasAluno.setNome(data.getKey());
                                respostasAluno.setPergunta(finalPergunta);
                                respostasAluno.setResposta((String) data.getValue());
                                respostaGeral.add(respostasAluno);
                            }
                            respostas = new ArrayList<>();

                            for (int i = 0; i < respostaGeral.size(); i++)
                                if (respostaGeral.get(i).getPergunta().equals(perguntaSelecionada))
                                    respostas.add(respostaGeral.get(i));
                            ArrayAdapter<RespostasAluno> adapter = new RespostasAdapter(Respostas.this, respostas);
                            actionBar.setTitle(perguntaSelecionada);
                            listViewrespostas.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_perguntas_questionario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.menu_proxima_pergunta:
                if (contador == perguntas.size() - 1) {
                    alert.setTitle("Última pergunta");
                    alert.setMessage("Você estar visualizando a última pergunta. Deseja fechar essa tela?");
                    alert.setPositiveButton("Sim", dialogFecharTela);
                    alert.setNegativeButton("Não", dialogFecharTela);
                    alert.show();
                }else {
                    contador++;
                    perguntaSelecionada = perguntas.get(contador);
                    actionBar.setTitle(perguntaSelecionada);
                    listarRespostas();
                }
                break;
            case R.id.menu_pergunta_anterior:
                if (contador == 0) {
                    alert.setTitle("Primeira pergunta");
                    alert.setMessage("Você estar visualizando a primeira pergunta. Deseja fechar essa tela?");
                    alert.setPositiveButton("Sim", dialogFecharTela);
                    alert.setNegativeButton("Não", dialogFecharTela);
                    alert.show();
                }else {
                    contador--;
                    perguntaSelecionada = perguntas.get(contador);
                    actionBar.setTitle(perguntaSelecionada);
                    listarRespostas();
                }
                break;
        }
        return true;
    }

    DialogInterface.OnClickListener dialogFecharTela = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
            }
        }
    };
}
