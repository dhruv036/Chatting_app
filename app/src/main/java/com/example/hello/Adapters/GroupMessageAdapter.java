package com.example.hello.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hello.Modal_Class.Messages;
import com.example.hello.Modal_Class.User;
import com.example.hello.R;
import com.example.hello.databinding.DeleteDialogBinding;
import com.example.hello.databinding.ReceiverLayoutBinding;
import com.example.hello.databinding.SenderLayoutBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupMessageAdapter extends RecyclerView.Adapter{

  Context context;
  ArrayList<Messages> arrayList;
  final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public GroupMessageAdapter(Context context, ArrayList<Messages> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT)
        {
                View v = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
                return new SenderViewHolder(v);
        }
        else
        {
            View v = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new ReceiverViewHolder(v);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = arrayList.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(messages.getSenderTd()))
        {
            return ITEM_SENT;
        }else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages m = arrayList.get(position);
        int reactions[] ={
                R.drawable.like,
                R.drawable.heart,
                R.drawable.laughing,
                R.drawable.surprised,
                R.drawable.sad,
                R.drawable.angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(holder.getClass() == SenderViewHolder.class)
            {
                if(pos <=reactions.length && pos != -1)
                {
                    SenderViewHolder viewHolder = (SenderViewHolder) holder;
                    viewHolder.binding.imageView2.setImageResource(reactions[pos]);
                    viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
                }else {

                }

            }else
            {
                if( pos <=reactions.length && pos != -1)
                {
                    ReceiverViewHolder viewHolder2 = (ReceiverViewHolder) holder;
                    viewHolder2.binding.imageView2.setImageResource(reactions[pos]);
                    viewHolder2.binding.imageView2.setVisibility(View.VISIBLE);
                }
                else
                {

                }

            }
            m.setFeeling(pos);

            FirebaseDatabase.getInstance().getReference()
                    .child("Public")
                    .child(m.getMessageId()).setValue(m);

            return true; // true is closing popup, false is requesting a new selection
        });

        if(holder.getClass() == SenderViewHolder.class)
        {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;

            if(m.getMessage().equals("Photos"))
            {
                viewHolder.binding.senderImg.setVisibility(View.VISIBLE);
                viewHolder.binding.sender.setVisibility(View.GONE);
                Glide.with(context).load(m.getImage()).placeholder(R.drawable.placeholderr).into(viewHolder.binding.senderImg);
            }else {
                viewHolder.binding.senderImg.setVisibility(View.GONE);
            }

//            FirebaseDatabase.getInstance().getReference().child("Users").child(m.getSenderTd())
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists())
//                            {
//                                User user = snapshot.getValue(User.class);
//                              viewHolder.binding.sendername.setText(user.getName());
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

            viewHolder.binding.sender.setText(m.getMessage());

            if(m.getFeeling() >=0)
            {
                viewHolder.binding.imageView2.setImageResource(reactions[m.getFeeling()]);
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
            }else {
                viewHolder.binding.imageView2.setVisibility(View.GONE);
            }

            viewHolder.binding.sender.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
            viewHolder.binding.senderImg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();


                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            m.setMessage("This message is removed.");
                            m.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Public")
                                    .child(m.getMessageId()).setValue(m);


//                            if(position == (arrayList.size()-1))
//                            {
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Public")
//                                        .child(m.getMessageId()).setValue(m);
//
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Chats")
//                                        .child(receiverRoom)
//                                        .child("lastMsg")
//                                        .setValue("This message is removed.");
//                            }


                            //     arrayList.remove(position);
                            dialog.dismiss();
                        }
                    });

//                    binding.delete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            m.setMessage("This message is removed.");
//                            m.setFeeling(-1);
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("Chats")
//                                    .child(senderRoom)
//                                    .child("messages")
//                                    .child(m.getMessageId()).setValue(m);
//                            //  arrayList.remove(position);
//
//                            if(position == (arrayList.size()-1))
//                            {
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Chats")
//                                        .child(senderRoom)
//                                        .child("lastMsg")
//                                        .setValue("This message is removed.");
//                            }
//                            dialog.dismiss();
//                        }
//                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });


        }else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            if(m.getMessage().equals("Photos"))
            {
                viewHolder.binding.receiverImg.setVisibility(View.VISIBLE);
                viewHolder.binding.receiver.setVisibility(View.GONE);
                Glide.with(context).load(m.getImage()).placeholder(R.drawable.placeholderr).into(viewHolder.binding.receiverImg);
            }
            FirebaseDatabase.getInstance().getReference().child("Users").child(m.getSenderTd())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                User user = snapshot.getValue(User.class);
                                viewHolder.binding.receivername.setText(user.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            viewHolder.binding.receiver.setText(arrayList.get(position).getMessage());

            if(m.getFeeling() >=0)
            {
                viewHolder.binding.imageView2.setImageResource(reactions[m.getFeeling()]);
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
            }else {
                viewHolder.binding.imageView2.setVisibility(View.GONE);
            }

            viewHolder.binding.receiver.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
            viewHolder.binding.receiverImg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        SenderLayoutBinding binding;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SenderLayoutBinding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverLayoutBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceiverLayoutBinding.bind(itemView);
        }
    }
}
