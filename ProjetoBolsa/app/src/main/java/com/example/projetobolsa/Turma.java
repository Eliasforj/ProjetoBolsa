package com.example.projetobolsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Turma implements Serializable {
    String periodo, turma;
    List<String> materias;


    public Turma() {
    }

    public String getTurma() {
        return turma;
    }


    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return turma;
    }
}
