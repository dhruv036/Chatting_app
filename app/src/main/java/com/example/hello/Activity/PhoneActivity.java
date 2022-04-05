package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.New_user;
import com.example.hello.R;
import com.example.hello.databinding.ActivityPhoneBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class PhoneActivity extends AppCompatActivity {
    FirebaseAuth auth;

    ActivityPhoneBinding binding;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            startActivity(new Intent(PhoneActivity.this, MainDashboard.class));
        }
        getSupportActionBar().hide();

        binding.phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                    binding.phoneInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);
                } else {
                    binding.phoneInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.InputFname != null && !binding.InputFname.getText().toString().equals("")) {

                    if (binding.phoneInput.getText() != null && binding.phoneInput.getText().toString().length() == 10) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child("+91"+binding.phoneInput.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    if (binding.emailInput.getText() != null && !binding.emailInput.getText().toString().equals("")) {
                                        if (emailisvalid(binding.emailInput.getText().toString())) {

                                            if (binding.InputPassword.getText() != null && !binding.InputPassword.getText().toString().equals("")) {
                                                if (binding.InputPassword.getText() != null && !binding.InputPassword.getText().toString().equals("")) {
                                                    if (binding.InputPassword.getText().toString().equals(binding.reInputPassword.getText().toString())) {

                                                        FeatureController.getInstance().setNew_user(new New_user(binding.InputFname.getText().toString() + " " + binding.InputLname.getText().toString(), "+91" + binding.phoneInput.getText().toString(), binding.emailInput.getText().toString(), binding.InputPassword.getText().toString()));
                                                        String phone = "+91"+binding.phoneInput.getText().toString();
                                                        FeatureController.getInstance().setCurr_user_phone(phone);
                                                        Intent i = new Intent(PhoneActivity.this, Otp_activity.class);
                                                        i.putExtra("Phone", "+91" + phone);
                                                        finishAffinity();
                                                        startActivity(i);
                                                    }else {
                                                        Toast.makeText(PhoneActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(PhoneActivity.this, "plz enter your password", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(PhoneActivity.this, "Plz enter password", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(PhoneActivity.this, "Enter your correct email", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(PhoneActivity.this, "Plz enter email", Toast.LENGTH_SHORT).show();
                                    }


                                }else{
                                    Toast.makeText(PhoneActivity.this, "Number Already Registered", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }else {
                        Toast.makeText(PhoneActivity.this, "Plz enter your phoneno", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PhoneActivity.this, "Plz enter your name", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void loginn(View v) {
        startActivity(new Intent(PhoneActivity.this, LoginActivity2.class));
    }

    public static boolean emailisvalid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}