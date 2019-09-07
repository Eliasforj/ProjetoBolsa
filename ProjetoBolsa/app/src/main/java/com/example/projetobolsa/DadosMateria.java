package com.example.projetobolsa;

import java.util.List;

public class DadosMateria {
    String idProf, materia, turma;
    List<String> bimestres;

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getIdProf() {
        return idProf;
    }

    public void setIdProf(String idProf) {
        this.idProf = idProf;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public List<String> getBimestres() {
        return bimestres;
    }

    public void setBimestres(List<String> bimestres) {
        this.bimestres = bimestres;
    }

    @Override
    public String toString() {
        return materia;
    }
}
