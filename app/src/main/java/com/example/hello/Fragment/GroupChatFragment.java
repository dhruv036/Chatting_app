package com.example.hello.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.hello.Activity.FeatureController;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.R;
import com.example.hello.databinding.NewgrouplayoutBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.theme.MaterialComponentsViewInflater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class GroupChatFragment extends Fragment {
    NewgrouplayoutBinding binding;
    FirebaseDatabase database;
    ArrayList<String> list = new ArrayList<>();
    Context context;
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewgrouplayoutBinding.inflate(inflater, container, false);
        context = getActivity().getApplicationContext();
        database = FirebaseDatabase.getInstance();

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
                chip.setId(count++);
                binding.childgroup.addView(chip);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
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
                List<Integer> ids = binding.childgroup.getCheckedChipIds();
                for (Integer id : ids) {
                    Log.e("error", String.valueOf(id));
                }
            }
        });
        return binding.getRoot();
    }

    private void getnames() {
        ArrayList<Friends> myfriends = FeatureController.getInstance().getMyfriends();
        if (myfriends.size() > 0) {
            for (Friends name : myfriends) {
                list.add(name.getName());
            }
        } else {
            Toast.makeText(context, "No Friend plz Add", Toast.LENGTH_SHORT).show();
        }

    }
}
