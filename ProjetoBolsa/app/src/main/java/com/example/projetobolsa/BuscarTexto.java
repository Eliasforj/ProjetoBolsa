package com.example.projetobolsa;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BuscarTexto {
    public static String url = "https://pt.wikipedia.org/w/api.php?action=query&prop=extracts&explaintext=&exsectionformat=plain&format=xml&titles=";
    public static String buscar(String titulo) {
        final String[] textoWiki = new String[1];
        url += titulo;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Document doc = Jsoup.connect(url).get();
                    final Elements elements = doc.getElementsByTag("extract");



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
                            textoWiki[0] = texto;

                } catch (Exception e) {

                }


            }
        }).start();
        return textoWiki[0];
    }
}
