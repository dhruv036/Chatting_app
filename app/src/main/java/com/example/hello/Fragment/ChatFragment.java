package com.example.hello.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Adapters.UsersAdapter;
import com.example.hello.databinding.ChatfragmentlayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    ChatfragmentlayoutBinding binding;
    ArrayList<Friends> userss = new ArrayList<>();
    String uid="";
    UsersAdapter adapter;
    FirebaseDatabase database;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     binding = ChatfragmentlayoutBinding.inflate(inflater,container,false);

        uid = FeatureController.getInstance().getUid();
      if(( uid== null || uid.equals("")))
      {

      }else{
          uid = FeatureController.getInstance().getUid();
      }
      Log.e("uid",uid);
     adapter = new UsersAdapter(getActivity(), userss);
        database = FirebaseDatabase.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

//        binding.chats.showShimmerAdapter();
//        binding.chats.hideShimmerAdapter();
        Log.e("Activity","Chatfragment");
             //String i = java.text.DateFormat.getDateTimeInstance().format(new Date());
        //Toast.makeText(getActivity(),i,Toast.LENGTH_SHORT).show();
        getchat();

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.chats.setVisibility(View.VISIBLE);
    }

    public void getchat()
    {
        database.getReference().child("Friends").child(uid).orderByChild("lastMsgTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userss.clear();
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Friends users = dataSnapshot.getValue(Friends.class);
                        userss.add(users);
                    }
                    FeatureController.getInstance().setMyfriends(userss);
                    adapter.notifyDataSetChanged();
                    binding.chats.setAdapter(adapter);
                }else{
                    Toast.makeText(getActivity(), "Plz Add friends", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
