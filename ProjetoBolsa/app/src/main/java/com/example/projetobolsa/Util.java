package com.example.projetobolsa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {

    public static boolean verificarInternet(Context context) {
        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRede = conexao.getActiveNetworkInfo();

        if (infoRede != null && infoRede.isConnected())
            return true;
        else
            return false;
    }

    public static void opcoesErro(Context context, String resposta) {
        if (resposta.contains("least 6 characters")) {
            Toast.makeText(context, "A senha deve ter ao menos 6 caracteres.", Toast.LENGTH_LONG).show();
        } else if (resposta.contains("address is badly")) {
            Toast.makeText(context, "Endereço de email inválido.", Toast.LENGTH_LONG).show();
        } else if (resposta.contains("interrupted connection")) {
            Toast.makeText(context, "Sem conexão com o Firebase.", Toast.LENGTH_LONG).show();
        } else if (resposta.contains("There is no user")) {
            Toast.makeText(context, "Email não cadastrado.", Toast.LENGTH_LONG).show();
        } else if (resposta.contains("password is invalid")) {
            Toast.makeText(context, "Senha inválida.", Toast.LENGTH_LONG).show();
        } else if (resposta.contains("address is already")) {
            Toast.makeText(context, "Email já existe na base de dados.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
        }
    }
}
