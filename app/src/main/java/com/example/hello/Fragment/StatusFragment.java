package com.example.hello.Fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.example.hello.Activity.MainDashboard;
import com.example.hello.AlarmRecevier;
import com.example.hello.Constants;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Modal_Class.Status;
import com.example.hello.Modal_Class.UserStatus;
import com.example.hello.Adapters.StatusAdapter;
import com.example.hello.databinding.StatusfragmentlayoutBinding;
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

public class StatusFragment extends Fragment {
    StatusfragmentlayoutBinding binding;
    private AlarmManager alarmManager;
    private Calendar calendar;
    private PendingIntent pendingIntent;
    ArrayList<UserStatus> status = new ArrayList<>();
    StatusAdapter statusAdapter;
    UserStatus userStatus;
    Context contextt;
    FirebaseDatabase database;
    String currid = "";


    @Override
    public void onStart() {
        super.onStart();
        contextt = binding.getRoot().getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StatusfragmentlayoutBinding.inflate(inflater, container, false);
        contextt = binding.getRoot().getContext();
        currid = FeatureController.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        userStatus = new UserStatus();

//show();
        statusAdapter = new StatusAdapter(getActivity(), status);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.status.setLayoutManager(layoutManager);
        binding.status.setAdapter(statusAdapter);
        String myname = FeatureController.getInstance().getName();
        binding.statName.setText(myname);
        // M Y  S T A T U S
        database.getReference("Stories").child(currid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            userStatus.setName(snapshot.child("name").getValue(String.class));
                            userStatus.setProfileImg(snapshot.child("profileImg").getValue(String.class));
                            FeatureController.getInstance().setUserimg(snapshot.child("profileImg").getValue(String.class));

                            userStatus.setLastupadted(snapshot.child("lastUpdated").getValue(Long.class));
                            ArrayList<Status> status = new ArrayList<>();
                            for (DataSnapshot snapshot1 : snapshot.child("statuses").getChildren()) {
                                Status status1 = snapshot1.getValue(Status.class);
                                status.add(status1);
                            }
                            userStatus.setStatuses(status);
                            binding.circularStatusView.setPortionsCount(status.size());
                            if (userStatus.getStatuses().size() > 1) {
                                Status laststatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);
                                if (userStatus.getLastupadted() == 101) {
                                    binding.timeupdated.setText(" ");
                                } else {
                                    binding.timeupdated.setText(Constants.militotime(userStatus.getLastupadted()));
                                }
                                if (getActivity() != null) {
                                    Glide.with(getActivity()).load(laststatus.getImgurl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.mystatus);
                                } else {
//                                    Glide.with(getActivity()).load(laststatus.getImgurl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.mystatus);
                                }

                            } else if (userStatus.getStatuses().size() == 1) {
                                Status laststatus = userStatus.getStatuses().get(0);
                                binding.timeupdated.setText(Constants.militotime(userStatus.getLastupadted()));
                                if (contextt != null) {
                                    Glide.with(contextt).load(laststatus.getImgurl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.mystatus);
                                } else {

                                }

                            } else {
                                if (contextt != null) {
                                    Glide.with(contextt).load(userStatus.getProfileImg()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.mystatus);
                                } else {

                                }

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
                if (userStatus.getStatuses() != null && userStatus.getStatuses().size() > 0) {
                    ArrayList<MyStory> myStories = new ArrayList<>();

                    for (Status status : userStatus.getStatuses()) {

                        myStories.add(new MyStory(status.getImgurl()));
                        // to add comment in status
                        //  myStories.add(new MyStory(status.getImgurl(),null,"Add Comment here"));

                    }
                    new StoryView.Builder((getActivity()).getSupportFragmentManager())
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
                } else {
                    Toast.makeText(contextt, "Add Status", Toast.LENGTH_SHORT).show();
                }
            }
        });

        statues();

        //  F R I E N D S  S T A T U S


//        binding.tabtoadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                picdialog();
//            }
//        });
//
//        binding.addStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//     ktnoihgg
//
//
//     picdialog();
//            }
//        });
        binding.addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picdialog();
            }
        });
        binding.tabtoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picdialog();
            }
        });
        return binding.getRoot();


    }
//    public void show()
//    {
//        AlarmRecevier  recevier = new AlarmRecevier();
////        recevier.createNotificationChannel(context);
//        recevier.show(contextt);
//        Log.e("calling","calling");
//    }


    public void picdialog() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 1) {
            if (data.getData() != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Calendar calendar = Calendar.getInstance();
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

                                            Toast.makeText(getActivity(), "Dhruv", Toast.LENGTH_SHORT).show();

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
                                                    .child(currid)
                                                    .updateChildren(story);
                                            String key = database.getReference().push().getKey();
                                            database.getReference().child("Stories")
                                                    .child(currid)
                                                    .child("statuses")
                                                    .child(key)
                                                    .setValue(status1);
                                            setalarm(key);
                                            Toast.makeText(getActivity(), "Successfull posted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(contextt);
        contextt = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contextt = null;
    }

    public void statues() {
        ArrayList<Friends> myfriends = FeatureController.getInstance().getMyfriends();
        int size = myfriends.size();
        status.clear();
        for (int i = 0; i < size; i++) {
            int finalI = i;
            database.getReference().child("Stories")
                    .child(myfriends.get(i).getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                UserStatus s = new UserStatus();

                                s.setLastupadted(snapshot.child("lastUpdated").getValue(Long.class));
                                s.setProfileImg(snapshot.child("profileImg").getValue(String.class));
                                s.setName(myfriends.get(finalI).getName());
                                s.setUid(snapshot.child("uid").getValue(String.class));
                                ArrayList<Status> arrayList = new ArrayList<>();
                                for (DataSnapshot snapshot2 : snapshot.child("statuses").getChildren()) {
                                    Status statuss = snapshot2.getValue(Status.class);
                                    arrayList.add(statuss);
                                }
                                s.setStatuses(arrayList);
                                status.add(s);
                                statusAdapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }

    public void setalarm(String key) {
        alarmManager = (AlarmManager) contextt.getSystemService(Context.ALARM_SERVICE);
        calendar = Calendar.getInstance();
        Intent intent = new Intent(contextt, AlarmRecevier.class);
        intent.putExtra("key", key);
        intent.setAction(key);
        pendingIntent = PendingIntent.getBroadcast(contextt, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 86340000, pendingIntent);
        }
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY,pendingIntent);
        Log.e("Time", String.valueOf(calendar.getTimeInMillis()));

        Toast.makeText(contextt, "Alarm set Successfully", Toast.LENGTH_SHORT).show();

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = contextt.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }
}
