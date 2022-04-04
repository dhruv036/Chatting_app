package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hello.AlarmRecevier;
import com.example.hello.FeatureController;
import com.example.hello.Fragment.ChatFragment;
import com.example.hello.Fragment.GroupChatFragment;
import com.example.hello.Fragment.StatusFragment;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Modal_Class.UserStatus;
import com.example.hello.R;
import com.example.hello.ViewDialog;
import com.example.hello.VoiceCall.Api;
import com.example.hello.VoiceCall.ApiClient;
import com.example.hello.VoiceCall.Result;
import com.example.hello.databinding.ActivityMainDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mesibo.api.Mesibo;
import com.mesibo.api.MesiboProfile;
import com.mesibo.calls.api.MesiboCall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.hello.R.drawable.ic_baseline_camera_alt_24;
import static com.example.hello.R.drawable.ic_baseline_person_add_24;

public class MainDashboard extends AppCompatActivity implements MesiboCall.IncomingListener, Mesibo.ConnectionListener, Mesibo.MessageListener {
    ActivityMainDashboardBinding binding;
    private AlarmManager alarmManager;
    private Calendar calendar;
    private PendingIntent pendingIntent;
    String token = "hh3ef9cu5npwhvvo9b8rsse5n60wcn4rsrjp9b6xm54wqh16amxgdzplfe8fgoyu";
    String op = "useradd";
    Animation aniup;
    FirebaseDatabase database;
    //    ArrayList<User> users = new ArrayList<>();
    ArrayList<Friends> users = new ArrayList<>();
    ArrayList<UserStatus> status = new ArrayList<>();
    String currid = "";
    String add_name = "Bhaiya";
    int fvbttype = 0;
    Fragment fragment = null;
    String add_phone = "+918302827722";
    String pimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
//        createNotificationChannel();

//        Intent intent2 = new Intent(getApplicationContext(), FirebaseService.class);
//        startService(intent2);
        aniup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        binding.frameLayout.setAnimation(aniup);
        binding.bottomnav.setItemSelected(R.id.chatsitem, true);
//        AlarmRecevier  recevier = new AlarmRecevier();
//        recevier.createNotificationChannel(getApplicationContext());
//        recevier.show(getApplicationContext());

        if (!(FeatureController.getInstance().getUser() == null)) {
            pimg = FeatureController.getInstance().getUser().getProfileImg();
            Glide.with(this).load(pimg).placeholder(R.drawable.profile_dp).circleCrop().into(binding.mydp);
        }

        if (!FeatureController.getInstance().getName().equals("")) {
            String myname = FeatureController.getInstance().getName();
            int index = myname.indexOf(' ');
            if (!(index < 0)) {
                binding.myname.setText("Hello " + myname.substring(0, index));
            } else {
                binding.myname.setText("Hello " + myname);
            }


        }
        setuser();
        binding.mydp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this,ShowProfile.class));
            }
        });

        database = FirebaseDatabase.getInstance();
        currid = FeatureController.getInstance().getUid();
        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout2, new ChatFragment()).commit();
        database.getReference().child("Presence").child(currid).setValue("Online");


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar thirtyDaysAgo = Calendar.getInstance();
        //  thirtyDaysAgo.add(Calendar.DAY_OF_MONTH, -1);
        Date d = new Date();
        // thirtyDaysAgo
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(thirtyDaysAgo.getTimeInMillis());
        c2.add(Calendar.DAY_OF_MONTH, -1);

        Toast.makeText(this, sdf.format(c2.getTime()).toString(), Toast.LENGTH_SHORT).show();


        //   A D D Contact
        binding.addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fvbttype == 0) {
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(MainDashboard.this);
                } else {

//                    StatusFragment fragment = new StatusFragment();
//                    fragment.picdialog();
                }
//                AlertDialog.Builder builder
//                        = new AlertDialog.Builder(MainDashboard.this);
//                // set the custom layout
//                final View customLayout = getLayoutInflater().inflate(R.layout.add_user, null);
//                builder.setView(customLayout);

//                Button butto = customLayout.findViewById(R.id.sub);
//                butto.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EditText in_name,in_phone;
//                        in_name = customLayout.findViewById(R.id.name_in);
//                        in_phone = customLayout.findViewById(R.id.phone_in);
//
//                        add_name = in_name.getText().toString();
//                        add_phone = in_phone.getText().toString();
//
//                        Toast.makeText(getApplicationContext(),add_name+add_phone,Toast.LENGTH_SHORT).show();
//
//                        database.getReference("Users").child(add_phone).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) {
//                                    Toast.makeText(MainDashboard.this, "User present", Toast.LENGTH_SHORT).show();
//                                    String uid = snapshot.child("uid").getValue(String.class);
//                                    String img = snapshot.child("profileImg").getValue(String.class);
//                                    Friends f = new Friends(uid, add_name, img, add_phone, "Tap to chat", " ");
//                                    database.getReference().child("Friends").child(FirebaseAuth.getInstance().getUid()).child(add_phone).setValue(f)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(MainDashboard.this, "Friend Added", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//
//                                } else {
//                                    Toast.makeText(MainDashboard.this, "Add User", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                });
//
//                AlertDialog dialog
//                        = builder.create();
//                dialog.show();

            }
        });


        //statusAdapter = new StatusAdapter(MainDashboard.this, status);
//        UsersAdapter adapter = new UsersAdapter(MainDashboard.this, users);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        binding.chats.setAdapter(adapter);
//        binding.chats.showShimmerAdapter();
//        binding.chats.hideShimmerAdapter();  ///


//        database.getReference().child("Stories")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            status.clear();
//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                UserStatus s = new UserStatus();
//                                s.setLastupadted(snapshot1.child("lastUpdated").getValue(Long.class));
//                                s.setProfileImg(snapshot1.child("profileImg").getValue(String.class));
//                                s.setName(snapshot1.child("name").getValue(String.class));
//                                ArrayList<Status> arrayList = new ArrayList<>();
//                                for (DataSnapshot snapshot2 : snapshot1.child("statuses").getChildren()) {
//                                    Status statuss = snapshot2.getValue(Status.class);
//                                    arrayList.add(statuss);
//                                }
//                                s.setStatuses(arrayList);
//                                binding.status.hideShimmerAdapter();
//                                status.add(s);
//                            }
//                            statusAdapter.notifyDataSetChanged();
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


//        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (!FirebaseAuth.getInstance().getUid().equals(user.getUid())) {
//                        users.add(user);
//                    }
//
//                }
//                binding.chats.hideShimmerAdapter();
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        database.getReference().child("Friends").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Friends user = dataSnapshot.getValue(Friends.class);
//                    users.add(user);
//                }
//                binding.chats.hideShimmerAdapter();
//                adapter.notifyDataSetChanged();
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        binding.bottomnav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                fragment = null;
                switch (i) {
                    case R.id.chatsitem:
                        fvbttype = 0;
                        fragment = new ChatFragment();
                        binding.addPerson.setVisibility(View.VISIBLE);
                        Log.e("Activity", "callingchatfragment");
                        binding.addPerson.setImageDrawable(getResources().getDrawable(ic_baseline_person_add_24));
                        break;
                    case R.id.statusitem:
                        fvbttype = 1;
                        fragment = new StatusFragment();
                        binding.addPerson.setVisibility(View.GONE);

                        binding.addPerson.setImageDrawable(getResources().getDrawable(ic_baseline_camera_alt_24));
                        break;
                    case R.id.callitems:
                        fvbttype = 2;
                        binding.addPerson.setVisibility(View.GONE);
                        fragment = new GroupChatFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout2, fragment).commit();
            }
        });
    }



    //@Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            if (data.getData() != null) {
//                FirebaseStorage storage = FirebaseStorage.getInstance();
////                Date date = new Date();
////                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
////                String time = sdf.format(new Date());
//                Calendar calendar = Calendar.getInstance();
//                StorageReference reference = storage.getReference().child("Status")
//                        .child(calendar.getTimeInMillis() + "");
//                reference.putFile(data.getData())
//                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            UserStatus status = new UserStatus();
//                                            status.setName(FeatureController.getInstance().getName());
//                                            status.setProfileImg(FeatureController.getInstance().getUserimg());
//                                            status.setLastupadted(calendar.getTimeInMillis());
//
//                                            String imgurl = uri.toString();
//                                            Status status1 = new Status(imgurl, status.getLastupadted());
//
//                                            HashMap<String, Object> story = new HashMap<>();
//                                            story.put("name", status.getName());
//                                            story.put("profileImg", status.getProfileImg());
//                                            story.put("lastUpdated", status.getLastupadted());
//
//
//                                            database.getReference().child("Stories")
//                                                    .child(currid)
//                                                    .updateChildren(story);
//
//                                            database.getReference().child("Stories")
//                                                    .child(currid)
//                                                    .child("statuses")
//                                                    .push()
//                                                    .setValue(status1);
//                                            Toast.makeText(MainDashboard.this, "Successfull postddddddd", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        });
//            }
//        }
//    }

    public void setuser() {
        Api api = ApiClient.getClient().create(Api.class);
        Call<Result> call = api.getUser(token, op, "com.example.hello", FeatureController.getInstance().getCurr_user_phone());

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().getOp().equals("useradd")) {
                    if (response.body().getResult() == true) {
                        FeatureController.getInstance().setMy_mesibo_token(response.body().getUser().getToken());
                        Toast.makeText(MainDashboard.this, response.body().getUser().getToken().toString(), Toast.LENGTH_SHORT).show();
                        setmiso();
                    } else {
                        Toast.makeText(MainDashboard.this, "False ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainDashboard.this, "Unsuccessfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance();
        database.getReference().child("Presence").child(currid).setValue(String.valueOf(calendar.getTimeInMillis()));
    }

//    public void go() {
//        Intent intentt = new Intent(MainDashboard.this, GroupChatActivity.class);
//        startActivity(intentt);
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        database.getReference().child("Presence").child(currid).setValue("Online");
    }

    @Override
    protected void onResume() {
        super.onResume();

        database.getReference().child("Presence").child(currid).setValue("Online");
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            if (data.getData() != null) {
//                FirebaseStorage storage = FirebaseStorage.getInstance();
//                Date date = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                String time = sdf.format(new Date());
//                StorageReference reference = storage.getReference().child("Status")
//                        .child(time + "");
//                         reference.putFile(data.getData())
//                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            UserStatus status = new UserStatus();
//                                             status.setName(FeatureController.getInstance().getName());
//                                             status.setProfileImg(FeatureController.getInstance().getUserimg());
//                                             status.setUid(FirebaseAuth.getInstance().getUid());
//                                             status.setLastupadted(time);
//
//                                            String imgurl = uri.toString();
//                                            Status status1 = new Status(imgurl, status.getLastupadted());
//
//                                            HashMap<String, Object> story = new HashMap<>();
//                                            story.put("name", status.getName());
//                                            story.put("profileImg", status.getProfileImg());
//                                            story.put("lastUpdated", status.getLastupadted());
//                                            story.put("uid", status.getUid());
//
//                                            database.getReference().child("Stories")
//                                                    .child(FirebaseAuth.getInstance().getUid())
//                                                    .updateChildren(story);
//
//                                            database.getReference().child("Stories")
//                                                    .child(FirebaseAuth.getInstance().getUid())
//                                                    .child("statuses")
//                                                    .push()
//                                                    .setValue(status1);
//                                            Toast.makeText(MainDashboard.this, "Successfull", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        });
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(MainDashboard.this, "Search selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainDashboard.this, "Logout selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add:
                Toast.makeText(MainDashboard.this, "add selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setmiso() {
        Mesibo api = Mesibo.getInstance();
        api.init(getApplicationContext());
        Mesibo.addListener(this);
        Mesibo.setAccessToken(FeatureController.getInstance().getMy_mesibo_token());
        Mesibo.setDatabase("ChatApp", 0);
        Mesibo.start();
        MesiboCall.getInstance().init(this);
        MesiboCall.CallProperties cp = MesiboCall.getInstance().createCallProperties(true);
        cp.ui.title = "";
        MesiboCall.getInstance().setDefaultUiProperties(cp.ui);
        //Toast.makeText(getApplicationContext(), "ds", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean MesiboCall_OnShowUserInterface(MesiboCall.Call call, MesiboCall.CallProperties callProperties) {
        callProperties.runInBackground = true;
        //call.getCallProperties().user.setImageUrl("https://firebasestorage.googleapis.com/v0/b/hello-55d22.appspot.com/o/Profiles%2FI0xauDekdjMSf7Da1F3xx5cGWEo1?alt=media&token=85fbbed0-4b29-4d5e-b4ee-3e877b65e5cb");
        return false;
    }

    @Override
    public void MesiboCall_OnError(MesiboCall.CallProperties callProperties, int i) {

    }

    @Override
    public boolean MesiboCall_onNotify(int i, MesiboProfile mesiboProfile, boolean b) {
        return false;
    }

    @Override
    public void Mesibo_onConnectionStatus(int i) {

    }

    @Override
    public boolean Mesibo_onMessage(Mesibo.MessageParams messageParams, byte[] bytes) {

        return false;
    }

    @Override
    public void Mesibo_onMessageStatus(Mesibo.MessageParams messageParams) {

    }

    @Override
    public void Mesibo_onActivity(Mesibo.MessageParams messageParams, int i) {

    }

    @Override
    public void Mesibo_onLocation(Mesibo.MessageParams messageParams, Mesibo.Location location) {

    }

    @Override
    public void Mesibo_onFile(Mesibo.MessageParams messageParams, Mesibo.FileInfo fileInfo) {

    }

    @Override
    public MesiboCall.CallProperties MesiboCall_OnIncoming(MesiboProfile mesiboProfile, boolean b) {
        MesiboCall.CallProperties cc;
        cc = MesiboCall.getInstance().createCallProperties(b);
        String name = mesiboProfile.address.toString();

        //  mesiboProfile.setImageUrl("");
        database.getReference()
                .child("Friends")
                .child(currid)
                .child(name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Friends name = snapshot.getValue(Friends.class);
                            String Callername = name.getName();
                            mesiboProfile.setName(Callername);
                            mesiboProfile.setImageUrl(name.getProfileImg());
                        }
//                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            Messages messages1 = snapshot1.getValue(Messages.class);
//                            messages1.setMessageId(snapshot1.getKey());
//                            messages.add(messages1);
//                        }
//                        adapter.notifyDataSetChanged();
//                        binding.chatrecycle.smoothScrollToPosition(binding.chatrecycle.getAdapter().getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        toast(name);
        if (cc.video.enabled) {
            cc.video.bitrate = 3000; // Set Maximum video bitrate
            cc.video.codec = MesiboCall.MESIBOCALL_CODEC_H265;
        }
        return cc;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

    void toast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public void setalarm(String key) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar = Calendar.getInstance();
        Intent intent = new Intent(this, AlarmRecevier.class);
        intent.putExtra("key", key);
        intent.setAction(key);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 6 * 1000, pendingIntent);
        }
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY,pendingIntent);
        Log.e("Time", String.valueOf(calendar.getTimeInMillis()));

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();

    }


}
