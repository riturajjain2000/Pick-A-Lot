package com.lotpick.lotpick;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.lotpick.lotpick.fragment.Account;
import com.lotpick.lotpick.fragment.Explore;
import com.lotpick.lotpick.fragment.Inbox;
import com.lotpick.lotpick.fragment.Saved;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");

    BottomNavigationView bottomNavigationView;
    Fragment selectedfragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartDialog();
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Explore()).commit();



    }
    @Override
    public void onBackPressed() {

        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Exit?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finishAffinity();
                        }
                    }).show();
        }
    }

    private void showStartDialog() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user= firebaseAuth.getCurrentUser();


        String name = user != null ? user.getDisplayName() : "User";
        String email = user != null ? user.getEmail() : "user@gmail.com";
        assert user != null;
        String uid = user.getUid();
        Uri uri= user.getPhotoUrl();
        String link = uri != null ? uri.toString() : "";

        Map<String, Object> info = new HashMap<>();
        info.put("Name", name != null ? name : "User");
        info.put("Email", email != null ? email :"user@email.com");
        info.put("Uid",uid);
        info.put("Profile", link);
        info.put("Gender","");
        info.put("Date of Birth","");
        info.put("Phone no ","");
        info.put("id", uid);
        info.put("username", name != null ? name : "User");
        info.put("imageURL", link);
        info.put("status", "offline");
        String userid = user.getUid();
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        ref.setValue(info);

        db.collection("Users").document(name+" "+uid).set(info,SetOptions.merge());

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.account:
                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=getSharedPreferences("PREPS",MODE_PRIVATE).edit();
                            editor.putString("Userid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                            editor.apply();
                            selectedfragment=new Account();
                            break;
                        case R.id.saved:
                            selectedfragment= new Saved();

                            break;
                        case R.id.inbox:
                            selectedfragment= new Inbox();

                            break;
                        case R.id.explore:
                            selectedfragment= new Explore();
                            break;
                    }

                    if(selectedfragment!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedfragment).commit();
                    }

                    return true ;
                }
            };


}
