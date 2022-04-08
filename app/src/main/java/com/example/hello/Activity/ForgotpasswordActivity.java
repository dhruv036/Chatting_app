package com.example.hello.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.hello.R;
import com.example.hello.databinding.ActivityForgotpasswordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import in.aabhasjindal.otptextview.OtpTextView;

public class ForgotpasswordActivity extends AppCompatActivity {
    ActivityForgotpasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.submtBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotpasswordActivity.this, Otp_activity.class);
                String phone = binding.edtName.getText().toString();
                i.putExtra("Phone", "+91" + phone);
                i.putExtra("activity","forgot");
                startActivity(i);
            }
        });

    }
}