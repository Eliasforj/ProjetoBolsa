package com.example.projetobolsa;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Firebase {

    public static FirebaseDatabase database;
    public static DatabaseReference professorFB, alunoFB;
    public static FirebaseUser usuario;

    public static void conectarProf(){
        database = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        professorFB = database.getReference("professores").child(usuario.getUid());
    }

    public static void conectarAluno(){
        database = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        alunoFB = database.getReference("alunos").child(usuario.getUid());
    }

    public static void incluirProf(){
        conectarProf();
        String id = professorFB.push().getKey();
        professorFB.child(id).setValue("srjkggr");
    }

    public static void incluirTurma(String id, Turma turma){
        conectarProf();
        professorFB.child("turmas").child(id).setValue(turma);
    }

    public static void incluirMateria(String turma, Materia materia){
        conectarProf();
        professorFB.child("turmas").child(turma).child("materias").child(materia.getMateria()).setValue(materia);
    }

    public static void incluirApostila(String turma, String materia,String id, String periodo, String texto){
        conectarProf();
        professorFB.child("turmas").child(turma).child("materias").child(String.valueOf(materia)).child(periodo).child("apostilas").child(id).setValue(texto);
    }

    public static void excluirApostila(String turma, String materia, String periodo, String termo){
        conectarProf();
        professorFB.child("turmas").child(turma).child("materias").child(materia).child(periodo).child(termo).removeValue();
    }
}
