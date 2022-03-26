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

import com.example.hello.Activity.FeatureController;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmRecevier extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
        show(context);
//        Intent intent2 = new Intent(context, FirebaseService.class);
//        context.startService(intent2);

    }

    public void show(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Stories").child(FeatureController.getInstance().getUser().getUid()).child("statuses").setValue(null);
        database.getReference().child("Stories").child(FeatureController.getInstance().getUser().getUid()).child("lastUpdated").setValue(101);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(),"foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Foxandroid Alarm Manager")
                .setContentText("Subscribe for android related content")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
//                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        notificationManagerCompat.notify(123,builder.build());
    }


}
