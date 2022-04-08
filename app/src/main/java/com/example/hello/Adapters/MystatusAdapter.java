package com.example.hello.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hello.Activity.MainDashboard;
import com.example.hello.Constants;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Status;
import com.example.hello.R;
import com.example.hello.databinding.MystatuschildBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class MystatusAdapter extends RecyclerView.Adapter<MystatusAdapter.MyviewHolder> {

    ArrayList<Status> arrayList;
    Context context;

    public MystatusAdapter(ArrayList<Status> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mystatuschild, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        if (arrayList.size() > 0) {
            int size = arrayList.size();
            if (context != null) {
                Glide.with(context).load(arrayList.get(size - position - 1).getImgurl()).into(holder.binding.circularImg);
            }
            holder.binding.timeupdated.setText(Constants.militotime(arrayList.get(size - position - 1).getTimestamp()));
            holder.binding.deletebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("Stories").child(FeatureController.getInstance().getUid()).child("statuses").orderByChild("timestamp").equalTo(arrayList.get(size - position - 1).getTimestamp()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    FirebaseDatabase.getInstance().getReference().child("Stories").child(FeatureController.getInstance().getUid()).child("statuses").child(snapshot1.getKey()).removeValue();
                                }
                                FirebaseDatabase.getInstance().getReference().child("Stories").child(FeatureController.getInstance().getUid()).child("statuses").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                        if (snapshot.exists()) {
                                            ArrayList<Status> arrayList2 =new ArrayList<>();
                                            for (DataSnapshot snapshot2 : snapshot3.getChildren()) {
                                                Status status = new Status();
                                                status = snapshot2.getValue(Status.class);
                                                arrayList2.add(status);
                                            }
                                            if((arrayList2.size()) > 0)
                                            {
                                                Long lsttime = arrayList2.get(arrayList2.size()-1).getTimestamp();
                                                holder.binding.timeupdated.setText(Constants.militotime(lsttime));
                                                FirebaseDatabase.getInstance().getReference().child("Stories").child(FeatureController.getInstance().getUid()).child("lastUpdated").setValue(lsttime);
                                            }else {
                                                holder.binding.timeupdated.setText("");
                                                FirebaseDatabase.getInstance().getReference().child("Stories").child(FeatureController.getInstance().getUid()).child("lastUpdated").setValue(101);
                                            }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<MyStory> myStories = new ArrayList<>();
                    myStories.add(new MyStory(arrayList.get(size - position - 1).getImgurl()));
                    new StoryView.Builder(((MainDashboard) context).getSupportFragmentManager())
                            .setStoriesList(myStories) // Required
                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                            .setTitleText("") // Default is Hidden
                            .setSubtitleText("") // Default is Hidden
                            .setTitleLogoUrl(FeatureController.getInstance().getUser().getProfileImg()) // Default is Hidden
                            .setStoryClickListeners(new StoryClickListeners() {
                                @Override
                                public void onDescriptionClickListener(int position) {
                                    //your action
                                }

                                @Override
                                public void onTitleIconClickListener(int position) {
                                    //your action
                                }
                            }) // Optional Listeners
                            .build() // Must be called before calling show method
                            .show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        MystatuschildBinding binding;

        public MyviewHolder(@NonNull View itemView) {

            super(itemView);
            binding = MystatuschildBinding.bind(itemView);
        }
    }
}
