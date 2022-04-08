package com.example.hello.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.User;
import com.example.hello.R;
import com.example.hello.databinding.ActivityShowProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class ShowProfile extends AppCompatActivity implements View.OnClickListener {
    ActivityShowProfileBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String mynumber;
    User user;
    Animation aniup, animfade;
    ActivityResultLauncher<String> resultLauncher;
    int bool = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceseditor;
    String imgresult= null;
    int strt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityShowProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        user = new User();
        mynumber = FeatureController.getInstance().getUser().getPhoneNo();
        preferences = getSharedPreferences("Logincredentials",0);
        preferenceseditor = preferences.edit();

        getmyinfo();

        binding.edemail.setOnClickListener(this);
        binding.edphone.setOnClickListener(this);
        binding.edpass.setOnClickListener(this);
        binding.chngpic.setOnClickListener(this);
        binding.chngname.setOnClickListener(this);
        binding.logoutbt.setOnClickListener(this);


        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    StorageReference reference;
                    Calendar time = Calendar.getInstance();
                    binding.showimage.setImageURI(result);
                    reference = storage.getReference().child("Profiles").child(time.getTimeInMillis() + "");

                    reference.putFile(result).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    User user =   FeatureController.getInstance().getUser();
                                    user.setProfileImg(uri.toString());
                                    FeatureController.getInstance().setUser(user);
                                    FirebaseDatabase.getInstance().getReference("Users").child(mynumber).child("profileImg").setValue(uri.toString());
                                    binding.showimage.setImageURI(result);
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(ShowProfile.this, "No", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    public void ShowHidePass(View view){

        if(view.getId()==R.id.togglepass){

            if(binding.showpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                binding.togglepass.setImageResource(R.drawable.hide);

                //Show Password
                binding.showpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                binding.togglepass.setImageResource(R.drawable.eye);

                //Hide Password
                binding.showpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.edphone:
//                binding.shownumber.setEnabled(true);
//                break;
            case R.id.edemail:
                binding.showemail.setEnabled(true);
                break;
            case R.id.edpass:
                binding.showpassword.setEnabled(true);
                break;
            case R.id.chngpic:
                if (bool == 1) {
                    resultLauncher.launch("image/*");
                    bool = 0;
                }
                break;
            case R.id.logoutbt:
                preferenceseditor.clear().commit();
                finishAffinity();
                break;

            case R.id.chngname:
                binding.myname.setEnabled(true);
                binding.myname.setCursorVisible(true);
                binding.myname.setTextColor(getColor(R.color.white));
                binding.myname.setBackground(getDrawable(R.drawable.round_img));
                binding.frame.setVisibility(View.GONE);
                break;
        }
    }
    public void update(View v) {
        User user =   FeatureController.getInstance().getUser();

        if (binding.showpassword.isEnabled()) {
            user.setPass(binding.showpassword.getText().toString());
            Toast.makeText(this, "pass", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "no pass", Toast.LENGTH_SHORT).show();
        }
        if(binding.showemail.isEnabled()){
            user.setEmail(binding.showemail.getText().toString());
            Toast.makeText(this,"email",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"no email",Toast.LENGTH_SHORT).show();
        }
        if(binding.myname.isEnabled()){
            user.setName(binding.myname.getText().toString());
            Toast.makeText(this,"name",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"no name",Toast.LENGTH_SHORT).show();
        }
        FeatureController.getInstance().setName(binding.myname.getText().toString());
        FeatureController.getInstance().setUser(user);
        FirebaseDatabase.getInstance().getReference("Users").child(mynumber).setValue(user);
        Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show();
    }

    public void close(View v) {
        bool = 0;
        binding.frame.setVisibility(View.GONE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        database.getReference().child("Presence").child(FeatureController.getInstance().getUser().getUid()).setValue("Online");
        binding.frame.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance();
        database.getReference().child("Presence").child(FeatureController.getInstance().getUser().getUid()).setValue(String.valueOf(calendar.getTimeInMillis()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showprofile, menu);
        return true;
    }

    public void getmyinfo() {
        database.getReference().child("Users").child(mynumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    binding.showemail.setText(user.getEmail());
                    binding.shownumber.setText(user.getPhoneNo());
                    binding.showpassword.setText(user.getPass());
                    binding.myname.setText(user.getName());
                    FeatureController.getInstance().setUser(user);

                    Glide.with(getBaseContext()).load(user.getProfileImg()).into(binding.showimage);
                    FeatureController.getInstance().setBool(1);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            if (bool == 0) {
                binding.frame.setVisibility(View.VISIBLE);
                aniup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
                binding.frame.setAnimation(aniup);
                bool = 1;
            } else if (bool == 1) {
                bool = 0;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
       startActivity(new Intent(ShowProfile.this,MainDashboard.class));
        return super.onSupportNavigateUp();
    }
}