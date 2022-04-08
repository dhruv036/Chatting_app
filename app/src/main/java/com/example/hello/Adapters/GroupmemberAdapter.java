package com.example.hello.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Friendinfo;
import com.example.hello.R;
import com.example.hello.databinding.DeleteDialogBinding;
import com.example.hello.databinding.GroupmemberchildBinding;
import com.example.hello.databinding.GroupmemberupdateBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupmemberAdapter extends RecyclerView.Adapter<GroupmemberAdapter.MyviewHolder> {
    ArrayList<Friendinfo> gMembers;
    Context context;
    ArrayList<Friendinfo> gMembersnew = new ArrayList<>();

    public GroupmemberAdapter(ArrayList<Friendinfo> gMembers, Context context) {
        this.gMembers = gMembers;
        this.context = context;

        for (Friendinfo friendinfo : gMembers) {
            if (friendinfo != null) {
                gMembersnew.add(friendinfo);
            }
        }

    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupmemberchild, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
          int size =gMembersnew.size();

        if (gMembersnew.get(size-position-1) != null) {
            
            if(gMembersnew.get(size-position-1).getFrduid().equals(FeatureController.getInstance().getAdminuid()))
            {
                holder.binding.admin.setVisibility(View.VISIBLE);
            }else{
                holder.binding.admin.setVisibility(View.GONE);
            }
         
            holder.binding.userName.setText(gMembersnew.get(size-position-1).getFrdname());
            holder.binding.mylay.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String uid = gMembersnew.get(size-position-1).getFrduid();
                    if (FeatureController.getInstance().getIsadmin().equals("1")) {
                        View view2 = LayoutInflater.from(context).inflate(R.layout.groupmemberupdate, null);
                        GroupmemberupdateBinding binding2 = GroupmemberupdateBinding.bind(view2);
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("Delete Message")
                                .setView(binding2.getRoot())
                                .create();

                        binding2.makegadmin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (Friendinfo info :gMembersnew)
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Groups").child(info.getFrduid()).child("MyGroups").child(FeatureController.getInstance().getG_id()).child("adminuid").setValue(uid);
                                }
                                FirebaseDatabase.getInstance().getReference().child("Groups").child(uid).child("MyGroups").child(FeatureController.getInstance().getG_id()).child("gIsadmin").setValue("1");
                                FirebaseDatabase.getInstance().getReference().child("Groups").child(FeatureController.getInstance().getUid()).child("MyGroups").child(FeatureController.getInstance().getG_id()).child("gIsadmin").setValue("0");
                                dialog.dismiss();
                            }
                        });

                        binding2.remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseDatabase.getInstance().getReference().child("Groups").child(FeatureController.getInstance().getUid()).child("MyGroups").child(FeatureController.getInstance().getG_id()).child("gMembers").orderByChild("frduid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                Friendinfo friendinfo = snapshot1.getValue(Friendinfo.class);
                                                if (friendinfo.getFrduid().equals(uid)) {
                                                    for (Friendinfo info :gMembersnew)
                                                    {
                                                        //? set in featurecontroller also
                                                        FirebaseDatabase.getInstance().getReference().child("Groups").child(info.getFrduid()).child("MyGroups").child(FeatureController.getInstance().getG_id()).child("gMembers").child(snapshot1.getKey()).getRef().removeValue();
                                                    }
                                                    dialog.dismiss();
                                                }
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                        dialog.show();
                    }

                    return false;
                }
            });
            if (context != null) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(gMembersnew.get(size-position-1).getFrdphn()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String url = snapshot.child("profileImg").getValue(String.class);
                            Glide.with(context).load(url).placeholder(context.getDrawable(R.drawable.pplace)).into(holder.binding.userImg);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {

            }
        } else {

        }

    }

    @Override
    public int getItemCount() {
        return gMembersnew.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        GroupmemberchildBinding binding;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            binding = GroupmemberchildBinding.bind(itemView);
        }
    }
}
