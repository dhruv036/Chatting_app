package com.example.hello;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmRecevier extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String key = intent.getStringExtra("key");
        show(context,key);
    }

    public void show(Context context,String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Stories").child("bRfcORJZIkR713Qhg56NjsrHGps1").child("statuses").child(key).setValue(null);
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
