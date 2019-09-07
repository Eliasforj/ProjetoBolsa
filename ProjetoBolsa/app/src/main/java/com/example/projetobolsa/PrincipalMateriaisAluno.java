package com.example.projetobolsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

public class PrincipalMateriaisAluno extends AppCompatActivity {
    Fragment apostilas = new ApostilasAluno();
    Fragment questionarios = new QuestionariosAluno();
    FragmentManager fm = getSupportFragmentManager();
    Fragment ativo = apostilas;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_apostilas_recebidos:
                    fm.beginTransaction().hide(ativo).show(apostilas).commit();
                    ativo = apostilas;
                    return true;
                case R.id.navigation_questionarios_recebidos:
                    fm.beginTransaction().hide(ativo).show(questionarios).commit();
                    ativo = questionarios;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_principal_materiais_aluno);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        apostilas.setArguments(bundle);
        questionarios.setArguments(bundle);

        fm.beginTransaction().add(R.id.container_aluno, questionarios).hide(questionarios).commit();
        fm.beginTransaction().add(R.id.container_aluno, apostilas).commit();
    }

}
