package com.example.hello;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.hello.Modal_Class.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class EditGroupinfo {
    Activity activity1;
    Group group1;
    FirebaseDatabase database;

    public void update(Activity activity, Group group) {
        this.activity1 = activity;
        this.group1 = group;

    }

    public void set() {
        final Dialog dialog = new Dialog(activity1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_ginfo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FloatingActionButton mDialogNo = dialog.findViewById(R.id.no);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NO", "Clicked");
                dialog.dismiss();
            }
        });

        FloatingActionButton mDialogOk = dialog.findViewById(R.id.ok);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Ok", "Clicked");
                EditText in_name, in_phone;
                in_name = dialog.findViewById(R.id.name);
                in_phone = dialog.findViewById(R.id.phone);


            }
        });
    }
}
