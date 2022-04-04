package com.example.hello;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hello.Modal_Class.Status;
import com.example.hello.Modal_Class.UserStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlarmRecevier extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String key = intent.getStringExtra("key");
        String uid = intent.getStringExtra("uid");
        show(context,key,uid);
    }

    public void show(Context context,String key,String uid) {
        UserStatus userStatus = new UserStatus();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Stories").child(uid).child("statuses").child(key).setValue(null);
        database.getReference("Stories").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<Status> status = new ArrayList<>();
                    for (DataSnapshot snapshot1 : snapshot.child("statuses").getChildren()) {
                        Status status1 = snapshot1.getValue(Status.class);
                        status.add(status1);
                    }
                    userStatus.setStatuses(status);
                    if(userStatus.getStatuses().size()>0)
                    {
                        database.getReference().child("Stories")
                                .child(uid)
                                .child("lastUpdated")
                                .setValue(userStatus.getStatuses().get(userStatus.getStatuses().size()-1).getTimestamp());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference().child("Stories").child("bRfcORJZIkR713Qhg56NjsrHGps1").child("lastUpdated").setValue(101);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(key)
                .setContentText("Subscribe for android related content")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
//                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
    }


}
