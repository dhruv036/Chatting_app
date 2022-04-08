package com.example.hello.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hello.Adapters.GroupmemberAdapter;
import com.example.hello.FeatureController;
import com.example.hello.Modal_Class.Friendinfo;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Modal_Class.Group;
import com.example.hello.R;
import com.example.hello.databinding.ActivityGroupInfoBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupInfoActivity extends AppCompatActivity {
    ActivityGroupInfoBinding binding;
    String gid;
    ArrayList<String> list = new ArrayList<>();
    Group groupp = new Group();
    ArrayList<Friends> myfriends = FeatureController.getInstance().getMyfriends();
    GroupmemberAdapter groupmemberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (!getIntent().getStringExtra("g_id").equals("") || getIntent().getStringExtra("g_id") != null) {
            gid = getIntent().getStringExtra("g_id");
            FeatureController.getInstance().setG_id(gid);

            binding.showmember.setLayoutManager(new LinearLayoutManager(this));
            getlist();
        }


        binding.addmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.lay.setVisibility(View.VISIBLE);
            }
        });

        getnames();
        ArrayAdapter adapter = new ArrayAdapter(GroupInfoActivity.this, android.R.layout.simple_list_item_1, list.toArray());
        binding.addcontact.setAdapter(adapter);
        binding.addcontact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("index", String.valueOf(adapterView.getPositionForView(view)));
                Chip chip = new Chip(GroupInfoActivity.this);
                chip.setBackgroundColor(getColor(R.color.colorGradientStartlighter));
                chip.setText(binding.addcontact.getText().toString());
                chip.setTextColor(Color.WHITE);

                chip.setCheckedIcon(getDrawable(R.drawable.closee));
                chip.setCheckable(true);
                chip.setChecked(true);
                if (list.contains(binding.addcontact.getText().toString())) {
                    int pos = list.indexOf(binding.addcontact.getText().toString());
                    chip.setId(pos);
                }
                binding.addd.setVisibility(View.VISIBLE);

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

        binding.addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Group updated = FeatureController.getInstance().getGroup();
                //? OLD MEMBER
                ArrayList<Friendinfo> gMembersnew = new ArrayList<>();
                gMembersnew.addAll(FeatureController.getInstance().getGroupFrdList());
                Log.e("oldcount", String.valueOf(gMembersnew.size()));

                //? SELECTED PEOPLE
                ArrayList<Friendinfo> friendinfos = new ArrayList<>();
                List<Integer> ids = binding.childgroup.getCheckedChipIds();
                for (Integer id : ids) {
                    Log.e("ids ph", myfriends.get(id).getPhoneNo());
                    Log.e("ids name", myfriends.get(id).getName());
                    Friendinfo friendinfo = new Friendinfo();
                    friendinfo.setFrdname(myfriends.get(id).getName());
                    friendinfo.setFrdphn(myfriends.get(id).getPhoneNo());
                    friendinfo.setFrduid(myfriends.get(id).getUid());

                    friendinfos.add(friendinfo);
                }
                friendinfos.addAll(gMembersnew);
                Log.e("afteradd", String.valueOf(friendinfos.size()));

                ArrayList<Friendinfo> noRepeat = new ArrayList<Friendinfo>();
                for (Friendinfo event : friendinfos) {
                    boolean isFound = false;
                    // check if the event name exists in noRepeat
                    for (Friendinfo e : noRepeat) {
                        if (e.getFrdphn().equals(event.getFrdphn()) || (e.equals(event))) {
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound)
                    {
                        noRepeat.add(event);
                        Log.e("name",event.getFrdname());
                    //    FirebaseDatabase.getInstance().getReference("Group_Messages").child(event.getFrduid()).child(gid).child("Messages").setValue("");
                    }

                }
                updated.setgMembers(noRepeat);
                FeatureController.getInstance().setGroupFrdList(noRepeat);
                Log.e("unique count", String.valueOf(noRepeat.size()));
                for (Friendinfo infoo : noRepeat)  //! all users
                {
                    if (infoo.getFrduid().equals(FeatureController.getInstance().getUid())) { //? acceptadmin
                        updated.setgIsadmin("1");
                        FirebaseDatabase.getInstance().getReference().child("Groups").child(infoo.getFrduid()).child("MyGroups").child(gid).setValue(updated);
                    } else {  //? for admin
                        updated.setgIsadmin("0");
                        FirebaseDatabase.getInstance().getReference().child("Groups").child(infoo.getFrduid()).child("MyGroups").child(gid).setValue(updated);
                    }
                }
                binding.childgroup.removeAllViews();
                binding.lay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit)
        {
            binding.gname.setEnabled(true);
            binding.gname.setBackground(getDrawable(R.drawable.round_img));
            binding.gdis.setEnabled(true);
            binding.gdis.setBackground(getDrawable(R.drawable.round_img));

        }

        return super.onOptionsItemSelected(item);
    }

    private void getlist() {
        FirebaseDatabase.getInstance().getReference().child("Groups").child(FeatureController.getInstance().getUid()).child("MyGroups").child(gid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Group group = snapshot.getValue(Group.class);
                    FeatureController.getInstance().setGroup(group);
                    Glide.with(getApplicationContext()).load(group.getgIcon()).placeholder(getDrawable(R.drawable.placeholderr)).into(binding.showgicon);
                    binding.gname.setText(group.getgName());
                    FeatureController.getInstance().setIsadmin(group.getgIsadmin());
                    FeatureController.getInstance().setAdminuid(group.getAdminuid());
                    binding.gdis.setText(group.getgDiscpt());
                    ArrayList<Friendinfo> gMembers = group.getgMembers();
                    ArrayList<Friendinfo> gMembersnew = new ArrayList<>();
                    for (Friendinfo friendinfo : gMembers) {
                        if (friendinfo != null) {
                            gMembersnew.add(friendinfo);
                        }
                    }


                    int size = gMembersnew.size();
                    binding.pcount.setText(size + " participants");
                    FeatureController.getInstance().setGroupFrdList(gMembersnew);
                    groupmemberAdapter = new GroupmemberAdapter(group.getgMembers(), GroupInfoActivity.this);
                    binding.showmember.setAdapter(groupmemberAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getnames() {

        if (myfriends.size() > 0) {
            for (Friends name : myfriends) {
                list.add(name.getName());
            }
        } else {
            Toast.makeText(GroupInfoActivity.this, "No Friend plz Add", Toast.LENGTH_SHORT).show();
        }

    }
}