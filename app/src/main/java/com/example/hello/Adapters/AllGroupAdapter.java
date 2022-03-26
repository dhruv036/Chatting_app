package com.example.hello.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.hello.Activity.GroupChatActivity;
import com.example.hello.Constants;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Group;
import com.example.hello.R;
import com.example.hello.databinding.RowConversationBinding;

import java.util.ArrayList;

public class AllGroupAdapter extends RecyclerView.Adapter<AllGroupAdapter.MyviewHolder> {
    ArrayList<Group> arrayList = new ArrayList<>();
    Context context;

    public AllGroupAdapter(ArrayList<Group> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.binding.userName.setText(arrayList.get(arrayList.size() - position - 1).getgName());
        holder.binding.lastmsg.setText(arrayList.get(arrayList.size() - position - 1).getgLastmsg());
        Glide.with(context).load(arrayList.get(arrayList.size() - position - 1).getgIcon()).placeholder(R.drawable.placeholderr).into(holder.binding.userImg);
        holder.binding.time.setText(Constants.militohhmm(Long.valueOf(arrayList.get(arrayList.size() - position - 1).getGlstMsgTime())));
        holder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("G_id",arrayList.get(arrayList.size()-position-1).getGid());
                FeatureController.getInstance().setGroupFrdList(arrayList.get(arrayList.size()-position-1).getgMembers());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyviewHolder extends ViewHolder {
        RowConversationBinding binding;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }
}
