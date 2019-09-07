package com.example.projetobolsa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResponderPerguntas extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    Aluno aluno;
    String questionario;
    TextView txtPergunta;
    EditText edtResposta;
    List<String> perguntas, respostas;
    ActionBar actionBar;
    Menu menu;
    int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_responder_perguntas);

        actionBar = getSupportActionBar();
        contador = 1;

        txtPergunta = findViewById(R.id.txtResposta);
        edtResposta = findViewById(R.id.edtResposta);

        intent = getIntent();
        bundle = intent.getExtras();
        aluno = (Aluno) bundle.getSerializable("materia");
        questionario = bundle.getString("questionario");
        listarPerguntas();

    }

    private void listarPerguntas() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference professorFB = database.getReference("professores").child(aluno.getIdProf()).child("turmas")
                .child(aluno.getTurma()).child("materias").child(aluno.getMateria()).child(aluno.getBimestre()).child("questionarios").child(questionario).child("perguntas");
        professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntas = new ArrayList<>();
                respostas = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    perguntas.add((String) data.getValue());
                }

                actionBar.setTitle("Questão " + contador + " de " + perguntas.size());
                txtPergunta.setText(perguntas.get(0));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_apostila_aluno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_anterior:
                contador--;
                actionBar.setTitle("Questão " + contador + " de " + perguntas.size());
                txtPergunta.setText(perguntas.get(contador - 1));
                edtResposta.setText(respostas.get(contador - 1));

                break;
            case R.id.menu_proximo:
                respostas.add(edtResposta.getText().toString());
                if (contador == perguntas.size()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderPerguntas.this);
                    alertDialog.setMessage("Você respondeu todas as perguntas. \nDeseja enviar suas respostas?");
                    alertDialog.setPositiveButton("Sim", dialogClickListener)
                            .setNegativeButton("Não", dialogClickListener).show();
                } else {
                    contador++;
                    actionBar.setTitle("Questão " + contador + " de " + perguntas.size());
                    txtPergunta.setText(perguntas.get(contador - 1));
                    edtResposta.setText("");
                }
                break;
        }
        return true;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    for (int i = 0; i < perguntas.size(); i++) {
                        DatabaseReference professorFB = database.getReference("professores").child(aluno.getIdProf()).child("turmas")
                                .child(aluno.getTurma()).child("materias").child(aluno.getMateria()).child(aluno.getBimestre())
                                .child("questionarios").child(questionario).child("recebidos").child(perguntas.get(i)).child(aluno.getNome());
                        professorFB.setValue(respostas.get(i));
                    }
                    Toast.makeText(ResponderPerguntas.this, "Respostas Enviadas", Toast.LENGTH_SHORT).show();
                    finish();

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Toast.makeText(ResponderPerguntas.this, "Envio cancelado", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
