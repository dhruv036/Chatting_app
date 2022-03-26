package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.hello.Adapters.GroupMessageAdapter;
import com.example.hello.Modal_Class.Messages;
import com.example.hello.databinding.ActivityGroupChatBinding;
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



public class GroupChatActivity extends AppCompatActivity {
     ActivityGroupChatBinding binding;

    String senderuid;
    GroupMessageAdapter adapter;
    FirebaseAuth auth;
    ArrayList<Messages> messages;
    FirebaseDatabase database;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       getSupportActionBar().setTitle("Group Chat");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // setSupportActionBar(binding.toolbar);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        senderuid = auth.getUid(); // my uid
        messages =  new ArrayList<>();
        adapter = new GroupMessageAdapter(this,messages);
        binding.chatrecycle.setLayoutManager(new LinearLayoutManager(this));
        binding.chatrecycle.setAdapter(adapter);

        database.getReference().child("Public")
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
               // Date date = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                String time = sdf.format(new Date());
                Calendar calendar = Calendar.getInstance();
                Messages chat = new Messages(message, senderuid, calendar.getTimeInMillis());
                binding.msgInput.setText("");

                database.getReference().child("Public")
                        .push()
                        .setValue(chat);
            }
        });

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,25);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
        {
            if(data.getData() != null)
            {
                Uri selectedimg = data.getData();
            //     Uri compressedImageFile = Compressor.INSTANCE.compress(this,selectedimg);
                Calendar time = Calendar.getInstance();
                StorageReference reference = storage.getReference().child("Chats").child(time.getTimeInMillis()+"");
                reference.putFile(selectedimg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urll = uri.toString();
                                String message = binding.msgInput.getText().toString();
                              //  Date date = new Date();
//                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:");
//                                String time = sdf.format(new Date());
                                Calendar calendar = Calendar.getInstance();
                                Messages chat = new Messages(message, senderuid,calendar.getTimeInMillis());
                                chat.setMessage("Photos");
                                chat.setImage(urll);
                                binding.msgInput.setText("");
                                database.getReference().child("Public").push().setValue(chat);
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
        Calendar calendar = Calendar.getInstance();
        String currid = FirebaseAuth.getInstance().getUid();
        database.getReference().child("Presence").child(currid).setValue(String.valueOf(calendar.getTimeInMillis()));
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
        String currid = FirebaseAuth.getInstance().getUid();
        database.getReference().child("Presence").child(currid).setValue("Online");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}