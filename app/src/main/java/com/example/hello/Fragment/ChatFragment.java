package com.example.hello.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hello.Activity.FeatureController;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Adapters.UsersAdapter;
import com.example.hello.databinding.ChatfragmentlayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatFragment extends Fragment {
    ChatfragmentlayoutBinding binding;
    ArrayList<Friends> userss = new ArrayList<>();
    String uid="";
    FirebaseDatabase database;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     binding = ChatfragmentlayoutBinding.inflate(inflater,container,false);
     uid = FeatureController.getInstance().getUid();
        UsersAdapter adapter = new UsersAdapter(getActivity(), userss);
        database = FirebaseDatabase.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.chats.setAdapter(adapter);
        binding.chats.showShimmerAdapter();
        binding.chats.hideShimmerAdapter();
             //String i = java.text.DateFormat.getDateTimeInstance().format(new Date());
        //Toast.makeText(getActivity(),i,Toast.LENGTH_SHORT).show();
        database.getReference().child("Friends").child(uid).orderByChild("lastMsgTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userss.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friends users = dataSnapshot.getValue(Friends.class);
                    userss.add(users);
                }


                FeatureController.getInstance().setMyfriends(userss);
                binding.chats.hideShimmerAdapter();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.chats.setVisibility(View.VISIBLE);
    }
}
