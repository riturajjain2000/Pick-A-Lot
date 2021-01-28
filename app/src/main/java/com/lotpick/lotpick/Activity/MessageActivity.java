package com.lotpick.lotpick.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lotpick.lotpick.Adapters.MessageAdapter;
import com.lotpick.lotpick.Models.Chat;
import com.lotpick.lotpick.Models.User;
import com.lotpick.lotpick.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {




    CircleImageView pro_img;
    TextView username,status;

    FirebaseUser fUser;
    DatabaseReference ref;

    ImageButton btSend;
    EditText txtSend;

    DatabaseReference reference;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView reView;

    String userid;

    Intent intent;


    ValueEventListener seenListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                reference.removeEventListener(seenListener);
            }
        });


        reView = findViewById(R.id.chats_rv);
        status = findViewById(R.id.OnlineTv);
        reView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        reView.setLayoutManager(linearLayoutManager);

        pro_img = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btSend = findViewById(R.id.btn_send);
        txtSend = findViewById(R.id.text_send);

        intent = getIntent();
        userid = intent.getStringExtra("userid");

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txtSend.getText().toString();
                if (!msg.equals("")) {
                    sendMes(fUser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send blank messages", Toast.LENGTH_SHORT).show();
                }
                txtSend.setText("");
            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                status.setText(user.getStatus());
                if (user.getImageURL().equals("")) {
                    pro_img.setImageResource(R.drawable.addprofile);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).placeholder(R.drawable.addprofile)
                            .into(pro_img);
                }

                readMes(fUser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);
    }

    private void seenMessage(final String userId) {

            reference = FirebaseDatabase.getInstance().getReference("Chats");
        try {
            seenListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat;
                        chat = snapshot.getValue(Chat.class);

                        if (chat.getReceiver().equals(fUser.getUid()) && chat.getSender().equals(userId)) {

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen", true);
                            snapshot.getRef().updateChildren(hashMap);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (NullPointerException ignored){

        }

    }


    private void sendMes(String sender, final String receiver, String message) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> hashM = new HashMap<>();
            hashM.put("sender", sender);
            hashM.put("receiver", receiver);
            hashM.put("message", message);
            hashM.put("isseen", false);
            ref.child("Chats").push().setValue(hashM);

            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(fUser.getUid()).child(userid);
            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        chatRef.child("id").setValue(userid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(userid)
                    .child(fUser.getUid());
            chatRefReceiver.child("id").setValue(fUser.getUid());

            reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (NullPointerException ignored){

        }


    }


    private void readMes(final String myid, final String userid, final String imageurl) {
        mChat = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
                    reView.setAdapter(messageAdapter);
                }
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

    protected void onResume() {
        super.onResume();
        Status("online");
    }

    protected void onPause() {
        if (seenListener != null && reference != null) {
            reference.removeEventListener(seenListener);
        }
        super.onPause();
        Status("offline");
    }


}

