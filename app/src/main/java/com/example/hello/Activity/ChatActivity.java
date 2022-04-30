package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.hello.Adapters.MessageAdapter;
import com.example.hello.Constants;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Modal_Class.Messages;
import com.example.hello.R;
import com.example.hello.databinding.ActivityChatBinding;
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
import com.mesibo.api.Mesibo;
import com.mesibo.api.MesiboProfile;
import com.mesibo.calls.api.MesiboCall;
import com.scottyab.aescrypt.AESCrypt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class ChatActivity extends AppCompatActivity implements Mesibo.ConnectionListener {
    ActivityChatBinding binding;
    String message;
    String senderuid;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String Callername = "";
    ArrayList<Messages> messages;
    String receiveruid = "";
    MessageAdapter adapter;
    String username = "";
    FirebaseStorage storage;
    String currid = "";
    String phone;
    String block2;
    String token;
    String block;
    String isblock;
    String SenderRoom, ReceiverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        token = FeatureController.getInstance().getMy_mesibo_token();
        receiveruid = getIntent().getStringExtra("Uid");
        FeatureController.getInstance().setReceiveruid(receiveruid);
        username = getIntent().getStringExtra("username");
        String pimg = getIntent().getStringExtra("profileimg");
        phone = getIntent().getStringExtra("phoneno");
        isblock = getIntent().getStringExtra("isblock");
        FeatureController.getInstance().setReceiverpho(phone);
        setSupportActionBar(binding.toolbar);


        //receiver name and image
        if (username == null) {
            binding.namee.setText(phone);
        } else {
            binding.namee.setText(username);
        }

        Glide.with(this).load(pimg).into(binding.profileimg);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        currid = FeatureController.getInstance().getUid();

        database.getReference().child("Presence").child(currid).setValue("Online");
        senderuid = currid;
        SenderRoom = senderuid + receiveruid;
        ReceiverRoom = receiveruid + senderuid;

        setmiso();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // F O R   O N L I N E   S T A T U S
        database.getReference().child("Presence").child(receiveruid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (!status.isEmpty()) {
                        if (status.equals("Typing...") || status.equals("Online")) {
                            binding.statuss.setVisibility(View.VISIBLE);
//                            binding.statuss.setText(status);
                        } else {
                            binding.statuss.setText(Constants.militotimestap(status));
//                            binding.statuss.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // R E C Y C L E V I E W
        messages = new ArrayList<>();
        adapter = new MessageAdapter(ChatActivity.this, messages, SenderRoom, ReceiverRoom);
        LinearLayoutManager llm = new LinearLayoutManager(ChatActivity.this);
        llm.setStackFromEnd(true);
        binding.chatrecycle.setLayoutManager(llm);
        binding.chatrecycle.setAdapter(adapter);

        database.getReference()
                .child("Chats")
                .child(SenderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages messages1 = snapshot1.getValue(Messages.class);
                            messages1.setMessageId(snapshot1.getKey());
                            messages.add(messages1);
                        }
                        adapter.notifyDataSetChanged();
                        binding.chatrecycle.smoothScrollToPosition(binding.chatrecycle.getAdapter().getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // user A and B user a send data in both room also B send data in both room but at time of fetching they take from AB(room) for A
        // and BA(room) for B

        binding.sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = binding.msgInput.getText().toString();

                try {
                    message = AESCrypt.encrypt("123456ASDFGHJKL;", message);
//                    Log.d("Encrypt", "onClick: "+msgenc);

//                    String msgdec  = AESCrypt.decrypt("JJKK",msgenc);
//                    Log.d(" Decrypt", "onClick: "+msgdec);

                } catch (Exception e) {

                }
                database.getReference().child("Friends").child(currid).child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            block = snapshot.child("block").getValue(String.class);
                            if (!message.isEmpty()) {

                                if (block.equals("0")) {
                                    Calendar calendar = Calendar.getInstance();
                                    Messages chat = new Messages(message, senderuid, calendar.getTimeInMillis());
                                    binding.msgInput.setText("");
                                    String randomkey = database.getReference().push().getKey();

                                    HashMap<String, Object> lastmsg = new HashMap<>();
                                    lastmsg.put("lastMsg", chat.getMessage());
                                    lastmsg.put("lastMsgTime", calendar.getTimeInMillis());

                                    database.getReference().child("Chats").child(SenderRoom).updateChildren(lastmsg);
                                    database.getReference().child("Chats").child(ReceiverRoom).updateChildren(lastmsg);

                                    database.getReference().child("Friends").child(currid).child(phone).updateChildren(lastmsg);
                                    lastmsg.put("uid", senderuid);
                                    lastmsg.put("phoneNo", FeatureController.getInstance().getUser().getPhoneNo());
                                    database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).updateChildren(lastmsg);

                                    database.getReference().child("Chats")
                                            .child(SenderRoom)
                                            .child("messages")
                                            .child(randomkey)
                                            .setValue(chat)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    database.getReference().child("Chats")
                                                            .child(ReceiverRoom)
                                                            .child("messages")
                                                            .child(randomkey)
                                                            .setValue(chat)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                }
                                                            });
                                                }
                                            });
                                }
                                if (block.equals("ME") || block.equals("BOTH")) {
                                    Toast.makeText(ChatActivity.this, "You block " + username + " plz unblock", LENGTH_SHORT).show();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Unblock");
                                    builder.setMessage("Would you like to unblock " + username);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                            if (block.equals("ME")) {
                                                database.getReference().child("Friends").child(currid).child(phone).child("block").setValue("0");
                                                database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("0");
                                                Toast.makeText(ChatActivity.this, "Unblocked", LENGTH_SHORT).show();
                                            }
                                            if (block.equals("BOTH")) {
                                                database.getReference().child("Friends").child(currid).child(phone).child("block").setValue("YOU");
                                                database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("ME");
                                                Toast.makeText(ChatActivity.this, "You unblocked but receiver has blocked you", LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builder.create().show();
                                }
                                if (block.equals("YOU")) {
                                    database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {

                                                block2 = snapshot.child("block").getValue(String.class);
                                                if (block2.equals("BOTH")) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                                                    builder.setCancelable(true);
                                                    builder.setTitle("Unblock");
                                                    builder.setMessage("Would you like to unblock " + username);
                                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                            database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("ME");
                                                            Toast.makeText(ChatActivity.this, "Unblocked :)", LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    builder.create().show();

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(ChatActivity.this, "I am blocked :(", LENGTH_SHORT).show();
                                }

                            } else {
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                try {
//                    //gethash(message);
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }

            }
        });
        // S E N D  I M A G E
        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 25);
            }
        });

        //  T Y P I N I N G
        final Handler handler = new Handler();
        binding.msgInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                database.getReference().child("Presence").child(senderuid).setValue("Typing...");
                handler.removeCallbacksAndMessages(false);
                handler.postDelayed(run, 5000);

            }

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("Presence").child(senderuid).setValue("Online");
                }
            };
        });


    }

    //    private void setmiso() {
//        Mesibo api = Mesibo.getInstance();
//        api.init(getApplicationContext());
//        Mesibo.addListener(this);
//        Mesibo.setAccessToken(FeatureController.getInstance().getMy_mesibo_token());
//        Mesibo.setDatabase("ChatApp", 0);
//        Mesibo.start();
//
//        MesiboCall.getInstance().init(this);
//        MesiboCall.CallProperties cp = MesiboCall.getInstance().createCallProperties(true);
//        cp.ui.title = "First App";
//        MesiboCall.getInstance().setDefaultUiProperties(cp.ui);
//
//        Toast.makeText(getApplicationContext(),"ds",Toast.LENGTH_SHORT).show();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.voice:
                //call to  user
                if (isLoggedIn()) {
                    Toast.makeText(ChatActivity.this, "Voice Calling", LENGTH_SHORT).show();
                    Log.e("voice", " " + phone);
                    MesiboCall.getInstance().callUi(ChatActivity.this, phone, false);
                } else {
                    Toast.makeText(ChatActivity.this, "Connecting", LENGTH_SHORT).show();
                }

                break;
            case R.id.vvoice:
                // Video call to user
                if (isLoggedIn()) {
                    Toast.makeText(ChatActivity.this, "Video Calling", LENGTH_SHORT).show();
                    Log.e("video", " " + phone);
                    MesiboCall.getInstance().callUi(ChatActivity.this, phone, true);
                } else {
                    Toast.makeText(ChatActivity.this, "Connecting", LENGTH_SHORT).show();
                }
                break;
            case R.id.block:
//                database.getReference().child("")
                database.getReference().child("Friends").child(currid).child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String blockk;
                            blockk = snapshot.child("block").getValue(String.class);
                            if (blockk.equals("YOU")) {
                                database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("BOTH");
                            } else if (blockk.equals("0")) {
                                database.getReference().child("Friends").child(currid).child(phone).child("block").setValue("ME");
                                database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("YOU");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            default:
                //
        }
        return super.onOptionsItemSelected(item);
    }

//    public void gethash(String message) throws NoSuchAlgorithmException {
//        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//        byte[] bytes = message.getBytes();
//        //passing data to messga digest
//        messageDigest.digest(bytes);
//        // this give bytearray from message digest
//        byte[] bytes1 = messageDigest.digest();
//        StringBuffer hexString = new StringBuffer(); y
//
//        for (int i = 0; i < bytes1.length; i++) {
//            hexString.append(Integer.toHexString(0xFF & bytes1[i]));
//        }
//        Log.d("hashed", hexString.toString());
//
//    }


    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        String currid = FirebaseAuth.getInstance().getUid();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        String time  = sdf.format(new Date());
//        database.getReference().child("Presence").child(currid).setValue(time);
//    }


    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance();

        database.getReference().child("Presence").child(currid).setValue(String.valueOf(calendar.getTimeInMillis()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        database.getReference().child("Presence").child(currid).setValue("Online");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        database.getReference().child("Presence").child(currid).setValue("Online");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 25) {
            if (data != null) {
                if (data.getData() != null) {

                    //?
                    if (block.equals("0")) {
                        Uri selectedimg = data.getData();
                        Calendar time = Calendar.getInstance();
                        StorageReference reference = storage.getReference().child("Chats").child(time.getTimeInMillis() + "");
                        reference.putFile(selectedimg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {


                                            String urll = uri.toString();
                                            String message = binding.msgInput.getText().toString();
                                            Calendar calendar = Calendar.getInstance();
                                            Messages chat = new Messages(message, senderuid, calendar.getTimeInMillis());
                                            chat.setMessage("Photos");
                                            chat.setImage(urll);
                                            binding.msgInput.setText("");
                                            String randomkey = database.getReference().push().getKey();
                                            HashMap<String, Object> lastmsg = new HashMap<>();
                                            lastmsg.put("lastMsg", chat.getMessage());
                                            lastmsg.put("lastMsgTime", calendar.getTimeInMillis());
                                            database.getReference().child("Chats").child(SenderRoom).updateChildren(lastmsg);
                                            database.getReference().child("Chats").child(ReceiverRoom).updateChildren(lastmsg);


                                            database.getReference().child("Chats")
                                                    .child(SenderRoom)
                                                    .child("messages")
                                                    .child(randomkey)
                                                    .setValue(chat)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            database.getReference().child("Chats")
                                                                    .child(ReceiverRoom)
                                                                    .child("messages")
                                                                    .child(randomkey)
                                                                    .setValue(chat)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                        }
                                                                    });
                                                        }
                                                    });
                                            Toast.makeText(ChatActivity.this, urll, LENGTH_SHORT).show();
                                            data.setData(null);
                                        }
                                    });
                                }
                            }
                        });
                    }
                    //?
                    if (block.equals("ME") || block.equals("BOTH")) {
                        Toast.makeText(ChatActivity.this, "You block " + username + " plz unblock", LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Unblock");
                        builder.setMessage("Would you like to unblock " + username);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                if (block.equals("ME")) {
                                    database.getReference().child("Friends").child(currid).child(phone).child("block").setValue("0");
                                    database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("0");
                                    Toast.makeText(ChatActivity.this, "Unblocked", LENGTH_SHORT).show();
                                }
                                if (block.equals("BOTH")) {
                                    database.getReference().child("Friends").child(currid).child(phone).child("block").setValue("YOU");
                                    database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("ME");
                                    Toast.makeText(ChatActivity.this, "You unblocked but receiver has blocked you", LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                    if (block.equals("YOU")) {
                        database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    block2 = snapshot.child("block").getValue(String.class);
                                    if (block2.equals("BOTH")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                                        builder.setCancelable(true);
                                        builder.setTitle("Unblock");
                                        builder.setMessage("Would you like to unblock " + username);
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                database.getReference().child("Friends").child(receiveruid).child(FeatureController.getInstance().getUser().getPhoneNo()).child("block").setValue("ME");
                                                Toast.makeText(ChatActivity.this, "Unblocked :)", LENGTH_SHORT).show();
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder.create().show();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(ChatActivity.this, "I am blocked :(", LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void Mesibo_onConnectionStatus(int i) {
        binding.statuss.setVisibility(View.VISIBLE);
        binding.statuss.setText("Connection: " + i);

    }


    boolean isLoggedIn() {
        if (Mesibo.STATUS_ONLINE == Mesibo.getConnectionStatus()) return true;
        toast("Login with a valid token first");
        return false;
    }

    void toast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


    private void setmiso() {
        Mesibo api = Mesibo.getInstance();
        api.init(getApplicationContext());

        Mesibo.addListener(this);
        Mesibo.setAccessToken(token);
        Mesibo.setDatabase("mydb", 0);
        Mesibo.start();
        //Toast.makeText(getApplicationContext(), "ds", Toast.LENGTH_SHORT).show();
        /* initialize call with custom title */
        MesiboCall.getInstance().init(this);
        MesiboCall.CallProperties cp = MesiboCall.getInstance().createCallProperties(true);
        cp.ui.title = "First App";
        MesiboCall.getInstance().setDefaultUiProperties(cp.ui);
    }


}
