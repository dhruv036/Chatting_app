package com.example.hello.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hello.Activity.ChatActivity;
import com.example.hello.Constants;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.R;
import com.example.hello.databinding.RowConversationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewholder> {
    Context context;
    ArrayList<Friends> arrayList;

    public UsersAdapter(Context context, ArrayList<Friends> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        String senderid = FirebaseAuth.getInstance().getUid();
        String senderrom = senderid + arrayList.get(arrayList.size() - position - 1).getUid();
        holder.binding.lastmsg.setText(arrayList.get(arrayList.size() - position - 1).getLastMsg());
       if(arrayList.size() > 0) {


           if (!(arrayList.get(arrayList.size() - position - 1).getLastMsg() == null)) {
               holder.binding.time.setText("");
           } else {
               holder.binding.time.setText(Constants.militohhmm(arrayList.get(arrayList.size() - position - 1).getLastMsgTime()));
           }


           Glide.with(context).load(arrayList.get(arrayList.size() - position - 1).getProfileImg()).into(holder.binding.userImg);
           holder.binding.userName.setText(arrayList.get(arrayList.size() - position - 1).getName());
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, ChatActivity.class);
                   intent.putExtra("Uid", arrayList.get(arrayList.size() - position - 1).getUid());
                   intent.putExtra("username", arrayList.get(arrayList.size() - position - 1).getName());
                   intent.putExtra("profileimg", arrayList.get(arrayList.size() - position - 1).getProfileImg());
                   intent.putExtra("phoneno", arrayList.get(arrayList.size() - position - 1).getPhoneNo());

                   context.startActivity(intent);
               }
           });
       }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        RowConversationBinding binding;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }
}
