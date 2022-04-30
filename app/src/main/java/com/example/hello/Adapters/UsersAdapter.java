package com.example.hello.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hello.Activity.ChatActivity;
import com.example.hello.Constants;
import com.example.hello.Fragment.ChatFragment;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.R;
import com.example.hello.databinding.RowConversationBinding;
import com.scottyab.aescrypt.AESCrypt;

import java.util.ArrayList;

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
        Log.e("Activity","Chatadapter");

        try {
            String msgdec = AESCrypt.decrypt("123456ASDFGHJKL;", arrayList.get(arrayList.size() - position - 1).getLastMsg());
//                    Log.d(" Decrypt", "onClick: "+msgdec);
            Log.d(" Decrypt", "onClick: " + msgdec);
            holder.binding.lastmsg.setText(msgdec);
        } catch (Exception e) {

        }

       if(arrayList.size() > 0) {
           if ((arrayList.get(arrayList.size() - position - 1).getLastMsg() == null)) {
               ChatFragment fragment =new ChatFragment();
               fragment.getchat();
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
                   intent.putExtra("isblock",arrayList.get(arrayList.size() - position - 1).getBlock());
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
