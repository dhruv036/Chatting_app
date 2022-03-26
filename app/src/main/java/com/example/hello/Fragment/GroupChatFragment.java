package com.example.hello.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hello.FeatureController;
import com.example.hello.Adapters.AllGroupAdapter;
import com.example.hello.Modal_Class.Friendinfo;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Modal_Class.Group;
import com.example.hello.R;
import com.example.hello.databinding.NewgrouplayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GroupChatFragment extends Fragment {
    NewgrouplayoutBinding binding;
    FirebaseDatabase database;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Friends> myfriends = FeatureController.getInstance().getMyfriends();
    Context context;
    Uri gicon;

    FirebaseStorage storage;
    int count = 0;
    ActivityResultLauncher<String> photoresult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewgrouplayoutBinding.inflate(inflater, container, false);
        context = getActivity().getApplicationContext();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();


        fetchgroup();
        //? FOR CREATE GROUP

        photoresult = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        gicon = result;
                        binding.image.setImageURI(result);
                    }
                }
        );
        getnames();
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, list.toArray());
        binding.addcontact.setAdapter(adapter);
        binding.addcontact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("index", String.valueOf(adapterView.getPositionForView(view)));
                Chip chip = new Chip(getActivity());
                chip.setBackgroundColor(Color.WHITE);
                chip.setText(binding.addcontact.getText().toString());
                chip.setTextColor(Color.BLACK);
                chip.setCheckedIcon(context.getDrawable(R.drawable.closee));
                chip.setCheckable(true);
                chip.setChecked(true);
                if (list.contains(binding.addcontact.getText().toString())) {
                    int pos = list.indexOf(binding.addcontact.getText().toString());
                    chip.setId(pos);
                }

                binding.childgroup.addView(chip);
//                chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        binding.childgroup.removeView(chip);
                    }
                });
                binding.addcontact.setText("");

                binding.childgroup.setSingleSelection(false);
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makegroup();
            }
        });
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoresult.launch("image/*");
            }
        });

        //? CREATE GROUP ADD

        return binding.getRoot();
    }

    public void fetchgroup() {
        ArrayList<Group> arrayList = new ArrayList<>();
        database.getReference().child("Groups").child(FeatureController.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 : snapshot.child("MyGroups").getChildren()) {
                        Group group = new Group();
                        ArrayList<Friendinfo> friendinfo = new ArrayList<>();
                        friendinfo.clear();
                        for (DataSnapshot snapshot2 : snapshot1.child("gMembers").getChildren()) {
                            friendinfo.add(snapshot2.getValue(Friendinfo.class));
                        }
                        group = snapshot1.getValue(Group.class);
                        group.setgMembers(friendinfo);
                        arrayList.add(group);
                    }
                    binding.showGroups.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.showGroups.setAdapter(new AllGroupAdapter(arrayList, getActivity()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void makegroup() {
        binding.progress.setVisibility(View.VISIBLE);
        List<Integer> ids = binding.childgroup.getCheckedChipIds();
        ArrayList<Friendinfo> arrayList = new ArrayList<>();
        for (Integer id : ids) {
            Log.e("ids ph", myfriends.get(id).getPhoneNo());
            Log.e("ids name", myfriends.get(id).getName());
            Friendinfo friendinfo = new Friendinfo();
            friendinfo.setFrdname(myfriends.get(id).getName());
            friendinfo.setFrdphn(myfriends.get(id).getPhoneNo());
            friendinfo.setFrduid(myfriends.get(id).getUid());

            arrayList.add(friendinfo);
        }
        Friendinfo myinfo = new Friendinfo();
        myinfo.setFrdname(FeatureController.getInstance().getUser().getName());
        myinfo.setFrdphn(FeatureController.getInstance().getUser().getPhoneNo());
        myinfo.setFrduid(FeatureController.getInstance().getUser().getUid());
        arrayList.add(myinfo);

        Calendar calendar = Calendar.getInstance();
        StorageReference reference = storage.getReference().child("Group_chat").child(String.valueOf(calendar.getTimeInMillis() + " "));

        reference.putFile(gicon).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(context, "Image is successfully uploaded", Toast.LENGTH_SHORT).show();
                            String gid = database.getReference().push().getKey();
                            Calendar calendar1 = Calendar.getInstance();
                            Group groupInfo = new Group("1", gid, binding.grpname.getText().toString(), binding.grpdiscpt.getText().toString(), uri.toString(), arrayList, "@Group_Created", String.valueOf(calendar1.getTimeInMillis()));
                            database.getReference().child("Groups").child(FeatureController.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        count = snapshot.child("tGroup").getValue(Integer.class);
                                        count++;
                                        database.getReference().child("Groups").child(FeatureController.getInstance().getUid()).child("tGroup").setValue(count);
                                        database.getReference().child("Groups").child(FeatureController.getInstance().getUid()).child("MyGroups").child(gid).setValue(groupInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    database.getReference("Group_Messages").child(FeatureController.getInstance().getUid()).child(gid).child("Messages").setValue("");
                                                    for (Friendinfo friendinfo : arrayList) {
                                                        Group groupInfo = new Group("0", gid, binding.grpname.getText().toString(), binding.grpdiscpt.getText().toString(), uri.toString(), arrayList, "@Group_Created", String.valueOf(calendar1.getTimeInMillis()));
                                                        database.getReference().child("Groups").child(friendinfo.getFrduid()).child("MyGroups").child(gid).setValue(groupInfo);
                                                        database.getReference("Group_Messages").child(friendinfo.getFrduid()).child(gid).child("Messages").setValue("");
                                                    }

                                                    binding.progress.setVisibility(View.GONE);
                                                    Toast.makeText(context, "Group is created", Toast.LENGTH_SHORT).show();
                                                }
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


                }
            }
        });
    }


    private void getnames() {

        if (myfriends.size() > 0) {
            for (Friends name : myfriends) {
                list.add(name.getName());
            }
        } else {
            Toast.makeText(context, "No Friend plz Add", Toast.LENGTH_SHORT).show();
        }

    }
}
