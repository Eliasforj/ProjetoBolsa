package com.example.projetobolsa;

import android.support.annotation.NonNull;

import java.util.List;

public class RespostasAluno {
    String nome, resposta, pergunta;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    @Override
    public String toString() {
        return pergunta + "\n" + nome + "\n" + resposta;
    }
}
