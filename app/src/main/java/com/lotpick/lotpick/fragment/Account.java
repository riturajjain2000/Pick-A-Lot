package com.lotpick.lotpick.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lotpick.lotpick.Contact;
import com.lotpick.lotpick.HostActivity;
import com.lotpick.lotpick.Login;
import com.lotpick.lotpick.Mylist;
import com.lotpick.lotpick.PersonalInfo;
import com.lotpick.lotpick.R;
import com.lotpick.lotpick.TermsActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Account extends Fragment {
    private TextView username;
    private CircleImageView profilepic;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TextView tv = view.findViewById(R.id.logout);


        firebaseAuth =FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        username = view.findViewById(R.id.profileName);
        Button persInfo = view.findViewById(R.id.persInfo);
        Button terms=view.findViewById(R.id.terms);
        Button hostbtn = view.findViewById(R.id.hostbtn);
        Button seelist = view.findViewById(R.id.seelist);
        Button contactbutton = view.findViewById(R.id.contact_button);
        profilepic=view.findViewById(R.id.profile_image);


        hostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HostActivity.class));

            }
        });
        seelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Mylist.class));

            }
        });
        contactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Contact.class));

            }
        });

        persInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PersonalInfo.class));
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TermsActivity.class));
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), Login.class));

            }
        });
        String name = user != null ? user.getDisplayName() : "User";
        String uid = Objects.requireNonNull(user).getUid();
        DocumentReference documentReference = db.collection("Users").document(name + " " + uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String Name=documentSnapshot.getString("Name");
                    username.setText("Hi, "+Name);

                    String tImage=documentSnapshot.getString("Profile");
                    if(!Objects.equals(tImage, "")){
                        Picasso.with(getContext()).load(tImage).into(profilepic);
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity",e.toString());

            }
        });
        return view;
    }


}
