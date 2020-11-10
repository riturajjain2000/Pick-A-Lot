package com.lotpick.lotpick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Mylist extends AppCompatActivity  {






    private TextView listTitle;
    public ImageView close;

    private TextView listPrice;
    private TextView listLocation;
    private TextView listRating;
    private TextView listdescription,listamenities;
    private ImageView listImage;
    private Button editlist ;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    String name = user != null ? user.getDisplayName() : "User";
    String uid = Objects.requireNonNull(user).getUid();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);









        listTitle = findViewById(R.id.Title_text);
        listPrice = findViewById(R.id.price_text   );
        listRating = findViewById(R.id.Rating_text);
        listLocation = findViewById(R.id.location_text);
        listImage = findViewById(R.id.place_image);
        listdescription = findViewById(R.id.desc_text);
        editlist = findViewById(R.id.editlistbtn);
        listamenities = findViewById(R.id.amenities);
        close=findViewById(R.id.close);










        final DocumentReference documentReference = db.collection("Users")
                .document(name +" "+ uid);







        editlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mylist.this, HostActivity.class);
                startActivity(intent);

            }

        });












        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animation animation = AnimationUtils.loadAnimation(Mylist.this,R.anim.blink_anim);
                close.startAnimation(animation);

            }
        });


        listTitle.setText("title");
        listPrice.setText("Rs. "+"00000"  );
        listRating.setText( "Not enough Ratings");
        listLocation.setText("location");
        listdescription.setText("Description");
        listamenities.setText("\n"+"amenities");
        Glide.with(Mylist.this).load(R.drawable.addprofile).centerCrop().placeholder(R.drawable.white_background).into(listImage);


    }







}