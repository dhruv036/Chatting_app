package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hello.Adapters.GroupMessageAdapter;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Friendinfo;
import com.example.hello.Modal_Class.Messages;
import com.example.hello.R;
import com.example.hello.databinding.ActivityGroupChatBinding;
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

import java.util.ArrayList;
import java.util.Calendar;


public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;
    String gid;
    String senderuid;
    FirebaseDatabase database;
    ArrayList<Friendinfo> friendinfos;
    FirebaseStorage storage;
    GroupMessageAdapter adapter;
    String gname ="";
    String gimg ="";
    ArrayList<Messages> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gname =  getIntent().getStringExtra("g_name");
        setSupportActionBar(binding.toolbar);
        // setSupportActionBar(binding.toolbar);
        gid = getIntent().getStringExtra("G_id");
        gimg = getIntent().getStringExtra("g_img");
        Glide.with(this).load(gimg).placeholder(R.drawable.placeholderr).into(binding.profileimg);
        binding.namee.setText(gname);


        FeatureController.getInstance().setG_id(gid);
        friendinfos = FeatureController.getInstance().getGroupFrdList();
        Toast.makeText(this, "" + gid, Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        senderuid = FeatureController.getInstance().getUid(); // my uid

        messages =  new ArrayList<>();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupChatActivity.this,GroupInfoActivity.class);
                i.putExtra("g_id",gid);
                startActivity(i);
            }
        });

        adapter = new GroupMessageAdapter(this,messages);
        binding.chatrecycle.setLayoutManager(new LinearLayoutManager(this));
        binding.chatrecycle.setAdapter(adapter);

        database.getReference("Group_Messages").child(senderuid).child(gid).child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {    messages.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Messages messages1 = snapshot1.getValue(Messages.class);
                                messages1.setMessageId(snapshot1.getKey());
                               messages.add(messages1);
                            }
                            adapter.notifyDataSetChanged();
                            binding.chatrecycle.smoothScrollToPosition( binding.chatrecycle.getAdapter().getItemCount());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.msgInput.getText().toString();
                Calendar calendar = Calendar.getInstance();
                Messages chat = new Messages(message, senderuid, calendar.getTimeInMillis());
                chat.setPhoneno(FeatureController.getInstance().getUser().getPhoneNo());
                binding.msgInput.setText("");
                String key = database.getReference().push().getKey();
                friendinfos  = FeatureController.getInstance().getGroupFrdList();
                for (Friendinfo friendinfo : friendinfos) {

                    FirebaseDatabase.getInstance().getReference()
                            .child("Groups")
                            .child(friendinfo.getFrduid())
                            .child("MyGroups")
                            .child(gid)
                            .child("gLastmsg")
                            .setValue(message);
                    FirebaseDatabase.getInstance().getReference()
                            .child("Groups")
                            .child(friendinfo.getFrduid())
                            .child("MyGroups")
                            .child(gid)
                            .child("glstMsgTime")
                            .setValue(String.valueOf(chat.getTimestamp()));

                    database.getReference("Group_Messages").child(friendinfo.getFrduid()).child(gid).child("Messages").child(key).setValue(chat);
                }
            }
        });

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getData() != null) {
                Uri selectedimg = data.getData();
                //     Uri compressedImageFile = Compressor.INSTANCE.compress(this,selectedimg);
                Calendar time = Calendar.getInstance();
                StorageReference reference = storage.getReference().child("Chats").child(time.getTimeInMillis() + "");
                reference.putFile(selectedimg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urll = uri.toString();
                                ;
                                Calendar calendar = Calendar.getInstance();
                                Messages chat = new Messages("Photos", senderuid, calendar.getTimeInMillis());
                                chat.setImage(urll);
                                chat.setPhoneno(FeatureController.getInstance().getUser().getPhoneNo());
                                binding.msgInput.setText("");
                                String key = database.getReference().push().getKey();
                                friendinfos  = FeatureController.getInstance().getGroupFrdList();
                                for (Friendinfo friendinfo : friendinfos) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Groups")
                                            .child(friendinfo.getFrduid())
                                            .child("MyGroups")
                                            .child(gid)
                                            .child("gLastmsg")
                                            .setValue("Photos");

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Groups")
                                            .child(friendinfo.getFrduid())
                                            .child("MyGroups")
                                            .child(gid)
                                            .child("glstMsgTime")
                                            .setValue(String.valueOf(chat.getTimestamp()));
                                    database.getReference("Group_Messages").child(friendinfo.getFrduid()).child(gid).child("Messages").child(key).setValue(chat);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Calendar calendar = Calendar.getInstance();
//        String currid = FirebaseAuth.getInstance().getUid();
//        database.getReference().child("Presence").child(currid).setValue(String.valueOf(calendar.getTimeInMillis()));
    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        String currid = FirebaseAuth.getInstance().getUid();
//        database.getReference().child("Presence").child(currid).setValue("Offline");
//    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference().child("Presence").child(FeatureController.getInstance().getUid()).setValue("Online");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}