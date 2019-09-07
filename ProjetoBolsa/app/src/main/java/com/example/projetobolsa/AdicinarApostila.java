package com.example.projetobolsa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import java.util.List;

public class AdicinarApostila extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView menuPesquisa;
    List<String> lista;
    ArrayAdapter<String> adapter;
    ListView listViewTitulos;
    Intent intent;
    Bundle bundle;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_adicinar_apostila);

        listViewTitulos = findViewById(R.id.listViewTitulos);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barra_pesquisa, menu);

        menuPesquisa = (SearchView) menu.findItem(R.id.pesquisar).getActionView();
        menuPesquisa.setQueryHint("Bucar...");
        menuPesquisa.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        lista = BuscarTitulo.listar(menuPesquisa.getQuery().toString());
        adapter = new ArrayAdapter<>(AdicinarApostila.this, android.R.layout.simple_list_item_1, lista);
        listViewTitulos.setAdapter(adapter);



        listViewTitulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = getIntent();
                bundle = new Bundle();
                bundle = intent.getExtras();
                bundle.putString("titulo",adapter.getItem(position));
                bundle.putString("funcao", "pesquisa");
                intent = new Intent(AdicinarApostila.this, TextoWiki.class);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }
        });


        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}
