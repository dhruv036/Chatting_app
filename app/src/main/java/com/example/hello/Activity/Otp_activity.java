package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hello.FeatureController;
import com.example.hello.R;
import com.example.hello.databinding.ActivityOtpActivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class Otp_activity extends AppCompatActivity {
    ActivityOtpActivityBinding binding;
    String verificationId;
    FirebaseAuth auth;
    String phone;
    EditText otppp;
    private OtpTextView otpTextView;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // setContentView(R.layout.activity_otp_activity);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        phone = getIntent().getStringExtra("Phone").toString();
        Log.e("phone", phone);

        Toast.makeText(getApplicationContext(), "" + FeatureController.getInstance().getNew_user().getName(), Toast.LENGTH_SHORT).show();
        sendVerificationCodeToUser(phone);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(phone, token);
            }
        });
        otpTextView = findViewById(R.id.otp_view);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {

                check();
            }
        });


//        PhoneAuthOptions options =  PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phone)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(Otp_activity.this)
//                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                      String code = phoneAuthCredential.getSmsCode();
//                      if(code!=null)
//                      {
//                          binding.otpInput.setText(code);
//
//                      }
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                    }
//
//                    @Override
//                    public void onCodeSent(@NonNull String verifyid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//
//                        verificationId = verifyid;
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//                        binding.otpInput.requestFocus();
//                        super.onCodeSent(verifyid, forceResendingToken);
//
//                        Log.e("v",verifyid);
//                    }
//                }).build();
//        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                10,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                    token = forceResendingToken;
                    Log.e("vid", verificationId);
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        //  binding.otpInput.setText(code);
                        otppp.setText(code);
                        //     verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(Otp_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        //  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // signInWithPhoneAuthCredential(credential);
    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            //Verification completed successfully here Either
//                            // store the data or do whatever desire
//                            Toast.makeText(Otp_activity.this,"Login successfull",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Otp_activity.this,SetupProfile.class);
//                    startActivity(intent);
//                    finishAffinity();
//
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                Toast.makeText(Otp_activity.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }

    public void check() {
        String otp = binding.otpView.getOTP();
        Log.e("otp", otp);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Otp_activity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Otp_activity.this, SetupProfile.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(Otp_activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}