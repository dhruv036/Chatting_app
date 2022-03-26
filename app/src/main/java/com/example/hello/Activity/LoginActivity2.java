package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.User;
import com.example.hello.databinding.ActivityLogin2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity2 extends AppCompatActivity {
    ActivityLogin2Binding binding;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences =getApplicationContext().getSharedPreferences("Logincredentials",0);
        final SharedPreferences.Editor editor = preferences.edit();
        binding.submtBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.edtName.getText().toString();
                String pass = binding.edtPass.getText().toString();

                FirebaseDatabase.getInstance().getReference("Users").child(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(LoginActivity2.this, "User Exists", Toast.LENGTH_SHORT).show();
                            User user = snapshot.getValue(User.class);
                            if(user.getPass().equals(pass))
                            {
                                FeatureController.getInstance().setUid(user.getUid());
                                FeatureController.getInstance().setUser(user);
                                FeatureController.getInstance().setName(user.getName());
                                editor.putString("phone",user.getPhoneNo());
                                editor.commit();
                                Toast.makeText(LoginActivity2.this, "Correct", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity2.this,MainDashboard.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(LoginActivity2.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(LoginActivity2.this, "User not registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

}