package com.example.projetobolsa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLogin, edtSenha;
    private Button btnentrar;
    private TextView txtAbrecadastro, txtEsqueceuSenha;
    private Usuario usuario;
    Intent intent;
    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        edtLogin = findViewById(R.id.edtLogin);
        edtSenha = findViewById(R.id.edtSenha);
        btnentrar = findViewById(R.id.btnEntrar);
        txtAbrecadastro = findViewById(R.id.txtAbreCadastro);
        txtEsqueceuSenha = findViewById(R.id.txtEsqueceSenha);

        btnentrar.setOnClickListener(this);
        txtEsqueceuSenha.setOnClickListener(this);
        txtAbrecadastro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEntrar:
                loginEmail();

                break;

            case R.id.txtEsqueceSenha:
                recuperarSenha();
                break;

            case R.id.txtAbreCadastro:
                selecionaCadastro();
                break;


        }
    }

    @SuppressLint("ResourceType")
    private void selecionaCadastro() {
        AlertDialog.Builder cadastro = new AlertDialog.Builder(Login.this);
        final RadioGroup radioGroup = new RadioGroup(Login.this);
        RadioButton rdbExecucao = new RadioButton(Login.this);
        RadioButton rdbConcluida = new RadioButton(Login.this);
        rdbConcluida.setText("Professor");
        rdbExecucao.setText("Aluno");
        rdbConcluida.setId(1);
        rdbExecucao.setId(2);
        radioGroup.addView(rdbExecucao);
        radioGroup.addView(rdbConcluida);
        cadastro.setTitle("Se cadastrar como:");
        cadastro.setView(radioGroup);
        cadastro.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id){
                    case 1:
                        intent = new Intent(Login.this, CadastroProf.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(Login.this, CadastroAluno.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        cadastro.show();
    }



    private void recuperarSenha() {
        String email = edtLogin.getText().toString().trim();
        if(email.isEmpty()){
            Toast.makeText(this, "Preencha o email para recuperar a senha", Toast.LENGTH_SHORT).show();
        }else
            enviarEmail(email);
    }

    private void enviarEmail(String email) {
        FirebaseAuth autenticacao = FirebaseAuth.getInstance();
        autenticacao.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Login.this, "Enviamos uma mensagem para o seu email com um " +
                                "link para redefinir a senha", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String erro = e.toString();
                Util.opcoesErro(getBaseContext(), erro);
            }
        });
    }

    private void loginEmail() {
        String email = edtLogin.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(getBaseContext(), "Preencha os campos obrigatórios",
                    Toast.LENGTH_LONG).show();
        } else {
            if (Util.verificarInternet(this)) {
                ConnectivityManager conexao =
                        (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                usuario = new Usuario();
                usuario.setEmail(edtLogin.getText().toString().trim());
                usuario.setSenha(edtSenha.getText().toString().trim());
                validarLogin();
            } else {
                Toast.makeText(getBaseContext(), "Erro - Sem conexão com a internet",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private void validarLogin() {
        final FirebaseAuth autenticacao = FirebaseAuth.getInstance();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference professorFB = database.getReference("professores").child(usuario.getUid());
                            professorFB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String l = null;
                                    for(DataSnapshot data : dataSnapshot.getChildren())
                                        l = data.getKey();

                                    if(l==null) {
                                        intent = new Intent(Login.this, PrincipalAluno.class);
                                        startActivity(intent);
                                    }else {
                                        intent = new Intent(Login.this, PrincipalProf.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {


                                }
                            });

                        } else {
                            String resposta = task.getException().toString();
                            Util.opcoesErro(getBaseContext(), resposta);
                        }
                    }
                });
    }
}
