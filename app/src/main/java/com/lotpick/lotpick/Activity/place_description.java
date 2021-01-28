package com.lotpick.lotpick.Activity;

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
import com.lotpick.lotpick.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class place_description extends AppCompatActivity  {




    

    private TextView listTitle;
    public ImageView close;
    CheckBox fav;
    private TextView listPrice;
    private TextView listLocation;
    private TextView listRating;
    private TextView listdescription,listamenities;
    private ImageView listImage;
    private Button messagebtn , bookbtn;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    String name = user != null ? user.getDisplayName() : "User";
    String uid = Objects.requireNonNull(user).getUid();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);

        String title = getIntent().getStringExtra("Title");
        String rating = getIntent().getStringExtra("Rating");
        String price = getIntent().getStringExtra("Price");
        String location = getIntent().getStringExtra("Location");
        String desc = getIntent().getStringExtra("Desc");

        String image = getIntent().getStringExtra("Image");
        String amenities = getIntent().getStringExtra("Amenities");
        final String id = getIntent().getStringExtra("Id");







        listTitle = findViewById(R.id.Title_text);
        listPrice = findViewById(R.id.price_text   );
        listRating = findViewById(R.id.Rating_text);
        listLocation = findViewById(R.id.location_text);
        listImage = findViewById(R.id.place_image);
        listdescription = findViewById(R.id.desc_text);
        messagebtn = findViewById(R.id.messagebtn);
        listamenities = findViewById(R.id.amenities);
        bookbtn = findViewById(R.id.Bookbtn);
        close=findViewById(R.id.close);



        fav = findViewById(R.id.fav);



        final String UID = getIntent().getStringExtra("Uid");


        final DocumentReference documentReference = db.collection("Users")
        .document(name +" "+ uid).collection("favourites").document(id);

        final Map<String, Object> info = new HashMap<>();
        info.put("Title", title);
        info.put("Rating", rating);
        info.put("Price", price);
        info.put("Location", location);
        info.put("Description", desc);
        info.put("Image", image);
        info.put("Uid" , UID);



           messagebtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {



                   Intent intent = new Intent(place_description.this, MessageActivity.class);
                   intent.putExtra("userid", UID);

                       startActivity(intent);



               }

           });




        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Animation animation = AnimationUtils.loadAnimation(place_description.this,
                            R.anim.blink_anim);
                    fav.startAnimation(animation);



                    documentReference.set(info);

                }else{
                    Animation animation = AnimationUtils.loadAnimation(place_description.this,
                            R.anim.blink_anim);
                    fav.startAnimation(animation);

                    documentReference.delete();
                }

            }
        });

        CollectionReference collectionReference = db.collection("Users")
                .document(name +" "+ uid).collection("favourites");


        collectionReference.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fav.setChecked(true);
                    } else {
                        fav.setChecked(false);
                    }
                } else {
                    Log.d("PlaceDescription", "Failed with: ", task.getException());
                }
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animation animation = AnimationUtils.loadAnimation(place_description.this,R.anim.blink_anim);
                close.startAnimation(animation);

            }
        });


        listTitle.setText(title);
        listPrice.setText("Rs. "+price  );

            listRating.setText( "Not enough Ratings");





        listLocation.setText(location);
        listdescription.setText(desc);
        listamenities.setText("\n"+amenities);
        Glide.with(place_description.this).load(image).centerCrop().placeholder(R.drawable.white_background).into(listImage);


    }







}
