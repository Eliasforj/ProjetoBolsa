package com.example.projetobolsa;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class BuscarTitulo {

    public static List<String> listar(String pesquisa){
        final List<String> lista = new ArrayList<>();
        final String url = "https://pt.wikipedia.org/w/api.php?action=query&list=search&format=xml&srsearch="+pesquisa;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    Document doc= Jsoup.connect(url).get();
                    Elements elements = doc.select("p");

                    for(Element element:elements){
                        String titulo=element.attr("title");
                        lista.add(titulo);

                    }

                }catch (Exception e){

                }


            }
        }).start();
        return lista;
    }
}

