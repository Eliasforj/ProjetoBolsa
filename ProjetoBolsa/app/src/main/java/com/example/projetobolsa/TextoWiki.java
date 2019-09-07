package com.example.projetobolsa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TextoWiki extends AppCompatActivity {

    TextView txtTexto;
    String turma, periodo, titulo,materia, termo;
    Bundle bundle;
    Intent intent;
    boolean pesquisar = true;
    String url = "https://pt.wikipedia.org/w/api.php?action=query&prop=extracts&explaintext=&exsectionformat=plain&format=xml&titles=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_texto_wiki);

        txtTexto = findViewById(R.id.txtTexto);

        intent = getIntent();
        bundle = intent.getExtras();
        turma = bundle.getString("turma");
        termo = bundle.getString("termo");
        materia= bundle.getString("materia");
        periodo = bundle.getString("periodo");
        titulo = bundle.getString("titulo");
        url += titulo;

        if (bundle.getString("funcao").equals("visualizar")) {
            ExibirTexto();
            pesquisar = false;
        }
        else
            buscarTexto();
    }

    private void ExibirTexto() {
        Firebase.conectarProf();
        Firebase.professorFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child("turmas").child(turma).child("materias").child(materia).child(periodo).child("apostilas").getChildren())
                    if(data.getKey().equals(termo))
                        txtTexto.setText(data.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void buscarTexto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Document doc = Jsoup.connect(url).get();
                    final Elements elements = doc.getElementsByTag("extract");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String texto = elements.text();
                            String[] separa = texto.split(" ");

                            texto = "";

                            for (int i = 0; i < separa.length; i++)
                                if ((separa[i].equals("Ver") && separa[i + 1].equals("também")) || (separa[i].equals("Veja") && separa[i + 1].equals("também")) ||
                                        (separa[i].equals("Ligações") && separa[i + 1].equals("externas")) || (separa[i].equals("Notas")) ||
                                        (separa[i].equals("Referências")))
                                    for (int j = i; j < separa.length; j++)
                                        separa[j] = "";
                            for (int i = 0; i < separa.length; i++)
                                texto += separa[i] + " ";
                            txtTexto.setText(texto);
                        }
                    });


                } catch (Exception e) {
                    txtTexto.setText("Houve algum erro. Verifique sua conexão.");
                }


            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        if (pesquisar == true)
        inflater.inflate(R.menu.menu_texto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menu_cadastrar:
                Firebase.incluirApostila(turma,materia,titulo,periodo,txtTexto.getText().toString());
                Toast.makeText(this, "Texto Armazanado", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.menu_cancelar:
                intent = new Intent(TextoWiki.this, AdicinarApostila.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
