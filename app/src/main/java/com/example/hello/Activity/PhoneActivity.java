package com.example.hello.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.hello.Modal_Class.New_user;
import com.example.hello.R;
import com.example.hello.databinding.ActivityPhoneBinding;
import com.google.firebase.auth.FirebaseAuth;

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
        if(auth.getCurrentUser() != null)
        {

            startActivity(new Intent(PhoneActivity.this,MainDashboard.class));
        }
        getSupportActionBar().hide();

        binding.phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 10)
                {
                    binding.phoneInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.checkmark,0);
                }else{
                    binding.phoneInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, 0,0);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       binding.submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FeatureController.getInstance().setNew_user(new New_user(binding.InputFname.getText().toString()+" "+binding.InputLname.getText().toString(),"+91"+binding.phoneInput.getText().toString(),binding.emailInput.getText().toString(),binding.InputPassword.getText().toString()));
               String phone = binding.phoneInput.getText().toString();
               FeatureController.getInstance().setCurr_user_phone(phone);
             Intent i  = new Intent(PhoneActivity.this,Otp_activity.class);
             i.putExtra("Phone","+91"+phone);
             finishAffinity();
             startActivity(i);
           }
       });


    }
       public void loginn(View v)
       {
           startActivity(new Intent(PhoneActivity.this,LoginActivity2.class));
       }
}