package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.New_user;
import com.example.hello.R;
import com.example.hello.databinding.ActivityPasswordResetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class PasswordResetActivity extends AppCompatActivity {
ActivityPasswordResetBinding binding;
String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phone = getIntent().getStringExtra("phone");
        binding.submtBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtPass.getText() != null && !binding.edtPass.getText().toString().equals("")) {
                    if (binding.edtPass2.getText() != null && !binding.edtPass2.getText().toString().equals("")) {
                        if (binding.edtPass.getText().toString().equals(binding.edtPass2.getText().toString())) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("pass").setValue(binding.edtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(PasswordResetActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(PasswordResetActivity.this, LoginActivity2.class);
                                        finishAffinity();
                                        startActivity(i);
                                    }

                                }
                            });


                        }else {
                            Toast.makeText(PasswordResetActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(PasswordResetActivity.this, "plz enter your password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PasswordResetActivity.this, "Plz enter password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ShowHidePass1(View view){

        if(view.getId()== R.id.togglepass1){

            if(binding.edtPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                binding.togglepass1.setImageResource(R.drawable.hide);

                //Show Password
                binding.edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding.togglepass1.setImageResource(R.drawable.eye);

                //Hide Password
                binding.edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
    public void ShowHidePass2(View view){

        if(view.getId()== R.id.togglepass2){

            if(binding.edtPass2.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                binding.togglepass2.setImageResource(R.drawable.hide);

                //Show Password
                binding.edtPass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding.togglepass2.setImageResource(R.drawable.eye);

                //Hide Password
                binding.edtPass2.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}