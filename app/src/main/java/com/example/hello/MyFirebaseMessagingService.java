package com.example.hello;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import static com.google.firebase.messaging.Constants.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {




    @Override
    public void onNewToken(@NonNull String s) {
        String ss =s;
        Log.d("TAG","New Token"+ss);

    }
}
