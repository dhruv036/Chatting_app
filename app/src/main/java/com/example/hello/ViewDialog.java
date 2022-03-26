package com.example.hello;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hello.Modal_Class.Friends;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ViewDialog {
    String add_name = "", add_phone = "";
    FirebaseDatabase database;
    String curuid = "";

    public void showDialog(Activity activity) {
        curuid = FeatureController.instance.getUid();

        database = FirebaseDatabase.getInstance();
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custome_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FloatingActionButton mDialogNo = dialog.findViewById(R.id.no);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NO", "Clicked");
                //hghfg


                dialog.dismiss();
            }
        });

        FloatingActionButton mDialogOk = dialog.findViewById(R.id.ok);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Ok", "Clicked");
                EditText in_name, in_phone;
                in_name = dialog.findViewById(R.id.name);
                in_phone = dialog.findViewById(R.id.phone);

                add_name = in_name.getText().toString();
                add_phone = in_phone.getText().toString();

                Toast.makeText(activity.getApplicationContext(), add_name + add_phone, Toast.LENGTH_SHORT).show();

                database.getReference("Users").child(add_phone).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(activity.getApplicationContext(), "User present", Toast.LENGTH_SHORT).show();
                            String uid = snapshot.child("uid").getValue(String.class);
                            String img = snapshot.child("profileImg").getValue(String.class);
                            Calendar calendar = Calendar.getInstance();
                            Friends f = new Friends(uid, add_name, img, add_phone, "Tap to chat", calendar.getTimeInMillis());
                            database.getReference().child("Friends").child(curuid).child(add_phone).setValue(f)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(activity.getApplicationContext(), "Friend Added", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(activity.getApplicationContext(), "Add User", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dialog.cancel();
            }
        });

        dialog.show();
    }
}
