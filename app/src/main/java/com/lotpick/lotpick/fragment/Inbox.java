package com.lotpick.lotpick.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lotpick.lotpick.Adapters.UserAdapter;
import com.lotpick.lotpick.Models.User;
import com.lotpick.lotpick.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Inbox extends Fragment {
    private UserAdapter userAdapter;
    private RecyclerView reView;
    private List<User> mUser;
    private List<User> mUser2;

    DatabaseReference reference;
    FirebaseUser fUser;
    EditText search_txt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inbox, container, false);
        search_txt = view.findViewById(R.id.search_users);
        search_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        reView = view.findViewById(R.id.Rview);
        reView.setHasFixedSize(true);
        reView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser = new ArrayList<>();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);


                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        mUser.add(user);
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUser, false);
                reView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void searchUser(String toString) {
        mUser2 = new ArrayList<>();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(toString).
                        endAt(toString + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!search_txt.getText().toString().equals("")) {
                    mUser2.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        assert user != null;
                        if (!user.getId().equals(fUser.getUid())) {
                            mUser2.add(user);
                        }
                    }
                    userAdapter = new UserAdapter(getContext(), mUser2, false);
                }else{
                    userAdapter = new UserAdapter(getContext(), mUser, false);
                }
                reView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void Status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    public void onResume() {
        super.onResume();
        Status("online");
    }

    public void onPause() {
        super.onPause();
        Status("offline");
    }
}
