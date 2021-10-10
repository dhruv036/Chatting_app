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
import com.example.hello.Activity.Sign_up;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginactivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText email_tv,pass_tv;
    Button login_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        auth  =FirebaseAuth.getInstance();
        email_tv= findViewById(R.id.email_tv);
        pass_tv =findViewById(R.id.password_tv);
        login_bt =findViewById(R.id.login_bt);
        findViewById(R.id.signup_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Loginactivity.this, Sign_up.class));
            }
        });
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass;
                email = email_tv.getText().toString();
                pass = pass_tv.getText().toString();
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Loginactivity.this,"Login successfull",Toast.LENGTH_SHORT).show();
                         //   startActivity(new Intent(Loginactivity.this,DashboardActivity.class));
                        }else {
                            Toast.makeText(Loginactivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}