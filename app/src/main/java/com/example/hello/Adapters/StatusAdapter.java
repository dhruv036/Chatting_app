package com.example.hello.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hello.Activity.MainDashboard;
import com.example.hello.Constants;
import com.example.hello.Modal_Class.Status;
import com.example.hello.Modal_Class.UserStatus;
import com.example.hello.R;
import com.example.hello.databinding.StatusItemBinding;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Myviewholder> {
    Context context;
    ArrayList<UserStatus> arrayList;

    public StatusAdapter(Context context, ArrayList<UserStatus> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_item, parent, false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
//        if (arrayList.size() > 1) {
//            if (arrayList.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) {
//                arrayList.remove(position);
//            }
//        }
        UserStatus userStatus = arrayList.get(position);
        Status laststatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);
        holder.binding.statName.setText(userStatus.getName());
        Glide.with(context).load(laststatus.getImgurl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.binding.circularImg);
        holder.binding.timeupdated.setText(Constants.militotime(userStatus.getStatuses().get(userStatus.getStatuses().size()-1).getTimestamp()));
        holder.binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());

        holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for (Status status : userStatus.getStatuses()) {
                    myStories.add(new MyStory(status.getImgurl()));
                }
                new StoryView.Builder(((MainDashboard) context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(6000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getName().toString()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImg()) // Default is Hidden
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        StatusItemBinding binding;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            binding = StatusItemBinding.bind(itemView);
        }
    }

}
