package com.example.projetobolsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

public class PrincipalMateriaisProf extends AppCompatActivity {

    Fragment apostilas = new Apostilas();
    Fragment questionarios = new Questionarios();
    Fragment questRecebidos = new QuestRecebidos();
    FragmentManager fm = getSupportFragmentManager();
    Fragment ativo = apostilas;
    Intent intent;
    Bundle bundle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_apostilas:
                    fm.beginTransaction().hide(ativo).show(apostilas).commit();
                    ativo = apostilas;
                    return true;
                case R.id.navigation_questionarios:
                    fm.beginTransaction().hide(ativo).show(questionarios).commit();
                    ativo = questionarios;
                    return true;
                case R.id.navigation_respostas:
                    fm.beginTransaction().hide(ativo).show(questRecebidos).commit();
                    ativo = questRecebidos;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_principal_materiais_prof);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        intent = getIntent();
        bundle = intent.getExtras();
        apostilas.setArguments(bundle);
        questionarios.setArguments(bundle);
        questRecebidos.setArguments(bundle);

        fm.beginTransaction().add(R.id.container_materiais, questionarios).hide(questionarios).commit();
        fm.beginTransaction().add(R.id.container_materiais, questRecebidos).hide(questRecebidos).commit();
        fm.beginTransaction().add(R.id.container_materiais, apostilas).commit();

    }

}
