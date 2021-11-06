package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hello.Modal_Class.User;
import com.example.hello.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Animation aniup, animfade;
    Button button;
    ImageView image;
    FirebaseDatabase database;
    FrameLayout frameLayout;
    SharedPreferences preferences;
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        frameLayout = findViewById(R.id.frame);
        button = findViewById(R.id.splashbt);
        image = findViewById(R.id.imageView3);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
       database = FirebaseDatabase.getInstance();

        preferences = getApplicationContext().getSharedPreferences("Logincredentials", 0);
        if(preferences.getString("phone","").length() > 0)
        {
            phone = preferences.getString("phone","");
//            FirebaseMessaging.getInstance().getToken()
//                    .addOnSuccessListener(new OnSuccessListener<String>() {
//                        @Override
//                        public void onSuccess(String s) {
//
//                            database.getReference("Users").child(phone)
//                                    .updateChildren()
//                            Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
//                        }
//                    });
            Thread thread = new Thread();
            thread.start();
            Toast.makeText(MainActivity.this, ""+preferences.getString("phone",""), Toast.LENGTH_SHORT).show();

        }


//        FeatureController.getInstance().setCurr_user_phone(preferences.getString("phone", ""));
//        FeatureController.getInstance().setName(preferences.getString("name", ""));
//        FeatureController.getInstance().setUserimg(preferences.getString("userimg", ""));

//        FirebaseDatabase.getInstance().getReference().child("Users").push().setValue("ffs").addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString("phone","").length() > 0)
                {
                    Intent intent = new Intent(MainActivity.this,MainDashboard.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent i = new Intent(MainActivity.this, PhoneActivity.class);
                    startActivity(i);
                }
            }
        });
        aniup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
        frameLayout.setAnimation(aniup);
        animfade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        image.setAnimation(animfade);
    }
    class Thread extends java.lang.Thread{

        @Override
        public void run() {
            database.getReference("Users").child(phone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        User user = snapshot.getValue(User.class);
                        FeatureController.getInstance().setUser(user);
                        FeatureController.getInstance().setCurr_user_phone(user.getPhoneNo());
                        FeatureController.getInstance().setUid(user.getUid());
                        FeatureController.getInstance().setName(user.getName());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}