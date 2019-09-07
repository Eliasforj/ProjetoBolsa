package com.example.projetobolsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdicionarQuestionario extends AppCompatActivity implements View.OnClickListener {

    Bundle bundle;
    Intent intent;
    String turma, materia, periodo;
    TextView txtQuestao;
    EditText edtTema,edtQuestao;
    Button btnProx, btnAnt, btnPublic;
    List<String> perguntas;
    int numQuestao = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_adicionar_questionario);

        edtQuestao = findViewById(R.id.edtQuestao);
        edtTema = findViewById(R.id.edtTema);

        txtQuestao = findViewById(R.id.txtQuestao);
        txtQuestao.setText("Questão " + numQuestao);

        btnAnt = findViewById(R.id.btnAnterior);
        btnProx = findViewById(R.id.btnProximo);
        btnPublic = findViewById(R.id.btnPublicar);

        perguntas = new ArrayList<>();

        intent = getIntent();
        bundle = intent.getExtras();
        turma = bundle.getString("turma");
        materia = bundle.getString("materia");
        periodo = bundle.getString("periodo");

        btnPublic.setOnClickListener(this);
        btnProx.setOnClickListener(this);
        btnAnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAnterior:
                numQuestao--;
                if (!(edtQuestao.getText().toString().isEmpty()) && numQuestao + 1 > perguntas.size()) {
                    perguntas.add(edtQuestao.getText().toString());
                    edtQuestao.setText(perguntas.get(numQuestao - 1));
                } else if(edtQuestao.getText().toString().isEmpty() || edtQuestao.getText().toString().equals(perguntas.get(numQuestao)))
                    edtQuestao.setText(perguntas.get(numQuestao - 1));
                else if (!(edtQuestao.getText().toString().equals(perguntas.get(numQuestao)))) {
                    perguntas.set(numQuestao, edtQuestao.getText().toString());
                    edtQuestao.setText(perguntas.get(numQuestao - 1));
                }

                txtQuestao.setText("Questão " + numQuestao);
                break;
            case R.id.btnProximo:
                if(perguntas.size() >= numQuestao){
                   if(!(perguntas.get(numQuestao - 1).equals(edtQuestao.getText().toString()))) {
                       perguntas.set(numQuestao - 1, edtQuestao.getText().toString());
                       if (perguntas.size() < numQuestao + 1)
                           edtQuestao.setText("");
                       else
                           edtQuestao.setText(perguntas.get(numQuestao));
                   }else if (perguntas.size() < numQuestao + 1)
                       edtQuestao.setText("");
                   else
                       edtQuestao.setText(perguntas.get(numQuestao));
                } else {
                    perguntas.add(edtQuestao.getText().toString());
                    edtQuestao.setText("");
                }

                numQuestao++;
                txtQuestao.setText("Questão " + numQuestao);

                break;
            case R.id.btnPublicar:
                if(!(edtQuestao.getText().toString().isEmpty()))
                    perguntas.add(edtQuestao.getText().toString());
                Firebase.conectarProf();
                Firebase.professorFB.child("turmas").child(turma).child("materias").child(materia).child(periodo)
                        .child("questionarios").child(edtTema.getText().toString()).child("perguntas").setValue(perguntas);

                Toast.makeText(this, "Questionário publicado", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
