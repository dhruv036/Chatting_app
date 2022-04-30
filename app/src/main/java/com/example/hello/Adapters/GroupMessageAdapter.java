package com.example.hello.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.hello.Activity.ViewImageActivity;
import com.example.hello.Constants;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Friendinfo;
import com.example.hello.Modal_Class.Messages;
import com.example.hello.Modal_Class.User;
import com.example.hello.R;
import com.example.hello.databinding.DeleteDialogBinding;
import com.example.hello.databinding.GroupchatreceiverBinding;
import com.example.hello.databinding.ReceiverDeleteDialogBinding;
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
import com.scottyab.aescrypt.AESCrypt;

import java.util.ArrayList;

public class GroupMessageAdapter extends RecyclerView.Adapter {

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
        if (viewType == ITEM_SENT) {
            View v = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.groupchatreceiver, parent, false);
            return new ReceiverViewHolder(v);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = arrayList.get(position);
        if (FeatureController.getInstance().getUid().equals(messages.getSenderTd())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArrayList<Friendinfo> friendList = FeatureController.getInstance().getGroupFrdList();
        String g_id = FeatureController.getInstance().getG_id();
        Messages m = arrayList.get(position);
        int reactions[] = {
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
            if (holder.getClass() == SenderViewHolder.class) {
                if (pos <= reactions.length && pos != -1) {
                    SenderViewHolder viewHolder = (SenderViewHolder) holder;
                    viewHolder.binding.imageView2.setImageResource(reactions[pos]);
                    viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
                } else {

                }

            } else {
                if (pos <= reactions.length && pos != -1) {
                    ReceiverViewHolder viewHolder2 = (ReceiverViewHolder) holder;
                    viewHolder2.binding.imageView2.setImageResource(reactions[pos]);
                    viewHolder2.binding.imageView2.setVisibility(View.VISIBLE);
                } else {

                }

            }
            m.setFeeling(pos);

            for (Friendinfo friendinfo : friendList) {
                FirebaseDatabase.getInstance().getReference("Group_Messages").child(friendinfo.getFrduid()).child(g_id).child("Messages").child(m.getMessageId()).setValue(m);
            }



            return true; // true is closing popup, false is requesting a new selection
        });

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;

            viewHolder.binding.show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(m.getMessage().equals("Photos"))
                    {
                        Intent i= new Intent(context, ViewImageActivity.class);
                        i.putExtra("url",m.getImage());
                        context.startActivity(i);
                    }
                }
            });

            if (m.getMessage().equals("Photos")) {
                viewHolder.binding.show.setVisibility(View.VISIBLE);
                viewHolder.binding.senderImg.setVisibility(View.VISIBLE);
                viewHolder.binding.sender.setVisibility(View.GONE);

                Glide.with(context).load(m.getImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                ((GroupMessageAdapter.SenderViewHolder) holder).binding.status.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.place).apply(RequestOptions.centerInsideTransform()).into(viewHolder.binding.senderImg);

            } else {
                viewHolder.binding.show.setVisibility(View.GONE);
                viewHolder.binding.senderImg.setVisibility(View.GONE);
            }
            if (position == 0) {
                viewHolder.binding.date.setVisibility(View.VISIBLE);
                viewHolder.binding.date.setText(Constants.givedate(String.valueOf(arrayList.get(position).getTimestamp())));
            }else{
                if(Constants.checktwodate(String.valueOf(arrayList.get(position-1).getTimestamp()), String.valueOf(arrayList.get(position).getTimestamp())).equals("same"))
                {
                    viewHolder.binding.date.setVisibility(View.GONE);
                    viewHolder.binding.date.setText("");
                }else {
                    viewHolder.binding.date.setText(Constants.givedate(String.valueOf(arrayList.get(position).getTimestamp())));
                    viewHolder.binding.date.setVisibility(View.VISIBLE);
                }
            }

            try {
                String msgdec = AESCrypt.decrypt("123456ASDFGHJKL;", m.getMessage());
//                    Log.d(" Decrypt", "onClick: "+msgdec);
                Log.d(" Decrypt", "onClick: " + msgdec);
                viewHolder.binding.sender.setText(msgdec);
            } catch (Exception e) {

            }


            if (m.getFeeling() >= 0) {
                viewHolder.binding.imageView2.setImageResource(reactions[m.getFeeling()]);
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.imageView2.setVisibility(View.GONE);
            }

//            viewHolder.binding.sender.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });
//            viewHolder.binding.senderImg.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });

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

                            for (Friendinfo friendinfo : friendList) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Group_Messages")
                                        .child(friendinfo.getFrduid())
                                        .child(g_id)
                                        .child("Messages")
                                        .child(m.getMessageId()).setValue(m);

                                if (position == (arrayList.size() - 1)) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Groups")
                                            .child(friendinfo.getFrduid())
                                            .child("MyGroups")
                                            .child(g_id)
                                            .child("gLastmsg")
                                            .setValue("This message is removed.");
                                }
                            }
                            dialog.dismiss();
                        }
                    });
                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            m.setMessage("This message is removed.");
                            m.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Group_Messages")
                                    .child(FeatureController.getInstance().getUid())
                                    .child(g_id)
                                    .child("Messages")
                                    .child(m.getMessageId()).removeValue();

                            if (position == (arrayList.size() - 1)) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Groups")
                                        .child(FeatureController.getInstance().getUid())
                                        .child("MyGroups")
                                        .child(g_id)
                                        .child("gLastmsg")
                                        .setValue(arrayList.get(arrayList.size() - 1).getMessage());
                            }
                            dialog.dismiss();
                        }
                    });

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


        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;

            viewHolder.binding.show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(m.getMessage().equals("Photos"))
                    {
                        Intent i= new Intent(context, ViewImageActivity.class);
                        i.putExtra("url",m.getImage());
                        context.startActivity(i);
                    }
                }
            });
            if (m.getMessage().equals("Photos")) {
                viewHolder.binding.show.setVisibility(View.VISIBLE);
                viewHolder.binding.receiverImg.setVisibility(View.VISIBLE);
                viewHolder.binding.receiver.setVisibility(View.GONE);
                Glide.with(context).load(m.getImage()).placeholder(R.drawable.placeholderr).into(viewHolder.binding.receiverImg);
                FirebaseDatabase.getInstance().getReference().child("Friends").child(FeatureController.getInstance().getUid()).child(m.getPhoneno())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User user = snapshot.getValue(User.class);
                                    viewHolder.binding.receivername.setText(user.getName());
                                    viewHolder.binding.receivername.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            } else {
                viewHolder.binding.show.setVisibility(View.GONE);
                viewHolder.binding.receiverImg.setVisibility(View.GONE);

                if (position == 0) {
                    viewHolder.binding.date.setVisibility(View.VISIBLE);
                    viewHolder.binding.date.setText(Constants.givedate(String.valueOf(arrayList.get(position).getTimestamp())));
                }

                FirebaseDatabase.getInstance().getReference().child("Friends").child(FeatureController.getInstance().getUid()).child(m.getPhoneno())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User user = snapshot.getValue(User.class);
                                    viewHolder.binding.receivername.setText(user.getName());
                                    viewHolder.binding.receivername.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }

            try {
                String msgdec = AESCrypt.decrypt("123456ASDFGHJKL;",arrayList.get(position).getMessage());
//                    Log.d(" Decrypt", "onClick: "+msgdec);
                Log.d(" Decrypt", "onClick: " + msgdec);
                viewHolder.binding.receiver.setText(msgdec);
            } catch (Exception e) {

            }


            if (position == 0) {
                viewHolder.binding.date.setVisibility(View.VISIBLE);
                viewHolder.binding.date.setText(Constants.givedate(String.valueOf(arrayList.get(position).getTimestamp())));
            }else{
                if(Constants.checktwodate(String.valueOf(arrayList.get(position-1).getTimestamp()), String.valueOf(arrayList.get(position).getTimestamp())).equals("same"))
                {
                    viewHolder.binding.date.setVisibility(View.GONE);
                    viewHolder.binding.date.setText("");
                }else {
                    viewHolder.binding.date.setText(Constants.givedate(String.valueOf(arrayList.get(position).getTimestamp())));
                    viewHolder.binding.date.setVisibility(View.VISIBLE);
                }
            }
            if (m.getFeeling() >= 0) {
                viewHolder.binding.imageView2.setImageResource(reactions[m.getFeeling()]);
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.imageView2.setVisibility(View.GONE);
            }

//            viewHolder.binding.receiver.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });
//            viewHolder.binding.receiverImg.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });


            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.receiver_delete_dialog, null);
                    ReceiverDeleteDialogBinding binding = ReceiverDeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            m.setMessage("This message is removed.");
                            m.setFeeling(-1);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Group_Messages")
                                    .child(FeatureController.getInstance().getUid())
                                    .child(g_id)
                                    .child("Messages")
                                    .child(m.getMessageId()).removeValue();

                            if (position == (arrayList.size() - 1)) {

                                FirebaseDatabase.getInstance().getReference()
                                        .child("Groups")
                                        .child(FeatureController.getInstance().getUid())
                                        .child("MyGroups")
                                        .child(g_id)
                                        .child("gLastmsg")
                                        .setValue(arrayList.get(arrayList.size() - 1).getMessage());
                            }
                            dialog.dismiss();
                        }
                    });

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
        GroupchatreceiverBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = GroupchatreceiverBinding.bind(itemView);
        }
    }
}
