package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.hello.R;
import com.example.hello.Modal_Class.User_modalClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sign_up extends AppCompatActivity {
   FirebaseAuth auth;
   FirebaseFirestore firestore;
   Button sign_in_bt;
   EditText name_tv,email_tv,pass_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        firestore =FirebaseFirestore.getInstance();
        sign_in_bt = findViewById(R.id.signup_bt_s);
        name_tv = findViewById(R.id.name_tv_s);
        email_tv= findViewById(R.id.email_tv_s);
        pass_tv =findViewById(R.id.password_tv_s);
        sign_in_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass,name;
                email = email_tv.getText().toString();
                pass= pass_tv.getText().toString();
                name= name_tv.getText().toString();
                User_modalClass user = new User_modalClass();
                user.setEmail(email);
                user.setPassword(pass);
                user.setName(name);
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Sign_up.this,"Account Created",Toast.LENGTH_SHORT).show();
                            firestore.collection("Users")
                                    .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(Sign_up.this, Loginactivity.class));
                                }
                            });
                        }else {
                            Toast.makeText(Sign_up.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            name_tv.setText("");
                            email_tv.setText("");
                            pass_tv.setText("");
                        }
                    }
                });
            }
        });

    }
}