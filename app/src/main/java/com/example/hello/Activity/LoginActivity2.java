package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.User;
import com.example.hello.R;
import com.example.hello.databinding.ActivityLogin2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.aescrypt.AESCrypt;

public class LoginActivity2 extends AppCompatActivity {
    ActivityLogin2Binding binding;
    SharedPreferences preferences;
    String pass;
    String passen;

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
                String username = "+91"+binding.edtName.getText().toString();

                pass = binding.edtPass.getText().toString();



                FirebaseDatabase.getInstance().getReference("Users").child(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            Toast.makeText(LoginActivity2.this, "User Exists", Toast.LENGTH_SHORT).show();
                            User user = snapshot.getValue(User.class);
                            try {
                                passen = AESCrypt.decrypt("123456ASDFGHJKL;",user.getPass());
//                             Log.d(" Decrypt", "onClick: "+msgdec);
                                Log.e(" Decrypt", "onClick: " + pass);

                            } catch (Exception e) {

                            }
                            if(passen.equals(pass))
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

        binding.forgotbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity2.this,ForgotpasswordActivity.class));
            }
        });
    }
    public void ShowHidePass(View view){

        if(view.getId()== R.id.togglepassl){

            if(binding.edtPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                binding.togglepassl.setImageResource(R.drawable.hide);

                //Show Password
                binding.edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding.togglepassl.setImageResource(R.drawable.eye);

                //Hide Password
                binding.edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}