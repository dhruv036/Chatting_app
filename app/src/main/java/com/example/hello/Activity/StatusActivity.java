package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hello.Modal_Class.Status;
import com.example.hello.Modal_Class.UserStatus;
import com.example.hello.Adapters.StatusAdapter;
import com.example.hello.databinding.ActivityStatusBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusActivity extends AppCompatActivity {
    ActivityStatusBinding binding;

    ArrayList<UserStatus> status = new ArrayList<>();
    StatusAdapter statusAdapter;
    UserStatus userStatus;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        userStatus = new UserStatus();
        statusAdapter = new StatusAdapter(StatusActivity.this, status);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.status.setLayoutManager(layoutManager);
        binding.status.setAdapter(statusAdapter);
        binding.status.showShimmerAdapter();


        // M Y  S T A T U S
        database.getReference("Stories").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userStatus.setName(snapshot.child("name").getValue(String.class));
                            userStatus.setProfileImg(snapshot.child("profileImg").getValue(String.class));
                            userStatus.setLastupadted(snapshot.child("lastUpdated").getValue(Long.class));
                            ArrayList<Status> status = new ArrayList<>();
                            int i = 0;
                            for (DataSnapshot snapshot1 : snapshot.child("statuses").getChildren()) {
                                Status status1 = snapshot1.getValue(Status.class);
                                status.add(status1);
                                i++;
                            }
                            userStatus.setStatuses(status);
                            binding.statName.setText(userStatus.getName());
                            binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());
                            if (userStatus.getStatuses().size() >= 1) {
                                Status laststatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);

                                Glide.with(StatusActivity.this).load(laststatus.getImgurl()).into(binding.mystatus);
                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for (Status status : userStatus.getStatuses()) {
                    myStories.add(new MyStory(status.getImgurl()));
                }
                new StoryView.Builder(getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getName().toString()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImg()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();
            }
        });

        //  F R I E N D S  S T A T U S
        database.getReference().child("Stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            status.clear();
                            if (!(snapshot.getKey().equals(FirebaseAuth.getInstance().getUid()))) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    UserStatus s = new UserStatus();
                                    s.setLastupadted(snapshot1.child("lastUpdated").getValue(Long.class));
                                    s.setProfileImg(snapshot1.child("profileImg").getValue(String.class));
                                    s.setName(snapshot1.child("name").getValue(String.class));
                                    ArrayList<Status> arrayList = new ArrayList<>();
                                    for (DataSnapshot snapshot2 : snapshot1.child("statuses").getChildren()) {
                                        Status statuss = snapshot2.getValue(Status.class);
                                        arrayList.add(statuss);
                                    }
                                    s.setStatuses(arrayList);
                                    binding.status.hideShimmerAdapter();


                                    status.add(s);
                                }
                                statusAdapter.notifyDataSetChanged();
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.tabtoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picdialog();
            }
        });


        binding.addStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picdialog();
            }
        });
    }

    public void picdialog() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getData() != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
//                Date date = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                String time = sdf.format(new Date());
                Calendar calendar  = Calendar.getInstance();
                StorageReference reference = storage.getReference().child("Status")
                        .child(calendar.getTimeInMillis() + "");
                reference.putFile(data.getData())
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            UserStatus status = new UserStatus();
                                            status.setName(FeatureController.getInstance().getName());
                                            status.setProfileImg(FeatureController.getInstance().getUserimg());
                                            status.setLastupadted(calendar.getTimeInMillis());

                                            String imgurl = uri.toString();
                                            Status status1 = new Status(imgurl, status.getLastupadted());

                                            HashMap<String, Object> story = new HashMap<>();
                                            story.put("name", status.getName());
                                            story.put("profileImg", status.getProfileImg());
                                            story.put("lastUpdated", status.getLastupadted());

                                            database.getReference().child("Stories")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .updateChildren(story);

                                            database.getReference().child("Stories")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child("statuses")
                                                    .push()
                                                    .setValue(status1);
                                            Toast.makeText(StatusActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        }
    }
}