package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.User;
import com.example.hello.Modal_Class.UserStatus;
import com.example.hello.databinding.ActivitySetupProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scottyab.aescrypt.AESCrypt;

public class SetupProfile extends AppCompatActivity {
    ActivitySetupProfileBinding binding;
    String phone;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SharedPreferences preferences;
    FirebaseStorage storage;
String pass;
    Uri selectedImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        phone = getIntent().getStringExtra("phone");
        getSupportActionBar().hide();

        preferences = getApplicationContext().getSharedPreferences("Logincredentials", 0);
        final SharedPreferences.Editor editor = preferences.edit();

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        binding.submitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                try {
                    pass = AESCrypt.encrypt("123456ASDFGHJKL;", FeatureController.getInstance().getNew_user().getPass());
                    Log.d("Encrypt", "onClick: "+pass);

                } catch (Exception e) {

                }

                if (selectedImg != null) {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imgUrl = uri.toString();
                                        String uid = auth.getUid();


                                        User user = new User(uid, FeatureController.getInstance().getNew_user().getName(), imgUrl, phone, pass, FeatureController.getInstance().getNew_user().getEmail());
                                        database.getReference()
                                                .child("Users")
                                                .child(phone)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        binding.progressBar.setVisibility(View.GONE);
                                                        database.getReference().child("Groups").child(uid).child("tGroup").setValue(0);
                                                        UserStatus status = new UserStatus( FeatureController.getInstance().getNew_user().getName(),imgUrl,101l);
                                                        database.getReference().child("Stories").child(uid).setValue(status);
                                                        FeatureController.getInstance().setUser(user);
                                                        FeatureController.getInstance().setUid(uid);
                                                        FeatureController.getInstance().setName(FeatureController.getInstance().getNew_user().getName());
                                                        FeatureController.getInstance().setUserimg(imgUrl);
                                                        editor.putString("name", user.getName());
                                                        editor.putString("phone", user.getPhoneNo());
                                                        editor.putString("userimg", imgUrl);
                                                        editor.commit();
                                                        Intent intent = new Intent(SetupProfile.this, MainDashboard.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                } else {
                    String uid = auth.getUid();
                    User user = new User(uid, FeatureController.getInstance().getNew_user().getName(), "No image", phone, pass, FeatureController.getInstance().getNew_user().getEmail());
                    database.getReference()
                            .child("Users")
                            .child(phone)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    database.getReference().child("Groups").child(uid).child("tGroup").setValue(0);
                                    FeatureController.getInstance().setUser(user);
                                    FeatureController.getInstance().setUid(uid);
                                    FeatureController.getInstance().setName(FeatureController.getInstance().getNew_user().getName());
                                    editor.putString("name", user.getName());
                                    editor.putString("phone", user.getPhoneNo());
                                    editor.putString("userimg", "");
                                    editor.commit();
                                    Intent intent = new Intent(SetupProfile.this, MainDashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                binding.imageView.setImageURI(data.getData());
                selectedImg = data.getData();
            }
        }

    }
}