package com.example.projetobolsa;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if(user == null)
                    startActivity(new Intent(getBaseContext(), Login.class));
                else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usuario = database.getReference("alunos").child(user.getUid());
                    usuario.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> list = new ArrayList<>();
                            Intent intent;
                            for (DataSnapshot data : dataSnapshot.getChildren())
                                list.add((String) data.getValue());
                            if(!(list.isEmpty()))
                                intent = new Intent(Splash.this, PrincipalAluno.class);
                            else
                                intent = new Intent(Splash.this, PrincipalProf.class);

                            startActivity(intent);

                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

        },3000);

    }
}
