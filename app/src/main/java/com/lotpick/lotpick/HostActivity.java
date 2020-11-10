package com.lotpick.lotpick;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class HostActivity extends AppCompatActivity  {

    TextView addphotos , done;
    Uri filepath;
    ImageView location ,close;
    EditText ettitle,ettell,etAdress,etlistpart,etprice,
            etShared,etopenoffice,etseating,etmeetroom
            ,etameneties;

    CheckBox cbwifi,cbpantry,cbvending,cbprinter;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();


    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    String name = user != null ? user.getDisplayName() : "User";
    final String uid = Objects.requireNonNull(user).getUid();
    StorageReference reference= storageReference.child("Uploads/Hosts/" +name+" "+uid);
    DocumentReference documentReference = db.collection("Hosts").document(name +" "+ uid);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        addphotos = findViewById(R.id.Addphotos);
        location = findViewById(R.id.location_pics);
        close = findViewById(R.id.close);
        done = findViewById(R.id.done);
        cbwifi = findViewById(R.id.cbwifi);
        cbpantry = findViewById(R.id.cbpantry);
        cbprinter = findViewById(R.id.cbprinter);
        cbvending = findViewById(R.id.cbvending);
        etprice = findViewById(R.id.etprice);
        ettitle = findViewById(R.id.etTitle);
        etAdress = findViewById(R.id.etAdress);
        etlistpart = findViewById(R.id.etlistpart);
        etameneties = findViewById(R.id.etameneties);
        etmeetroom = findViewById(R.id.etmeetroom);
        etseating = findViewById(R.id.etseating);
        etShared = findViewById(R.id.etShared);
        etopenoffice = findViewById(R.id.etopenoffice);
        ettell = findViewById(R.id.ettell);






        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(HostActivity.this,R.anim.blink_anim);
                done.startAnimation(animation);
                updateProfile();
            }


        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(HostActivity.this,R.anim.blink_anim);
                close.startAnimation(animation);
                finish();

            }
        });


        addphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });



    }

    private void ChooseImage() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK

                && data!=null){

            filepath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                location.setImageBitmap(bitmap );
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


    private void updateProfile() {



        String  cwifi = "NO",cpantry="NO",cvending="NO",cprinter="NO";
        String Title = ettitle.getText().toString();
        String adress = etAdress.getText().toString();
        String listpart  = etlistpart.getText().toString();
        String amenities = etameneties.getText().toString();
        String meetrooom  = etmeetroom.getText().toString();
        String seating  = etseating.getText().toString();
        String price  = etprice.getText().toString();
        String shared  = etShared.getText().toString();
        String openoffice  = etopenoffice.getText().toString();
        String tell  = ettell.getText().toString();
        if (cbwifi.isChecked()){
            cwifi = "YES";
        }
        if (cbpantry.isChecked()){
            cpantry = "YES";
        }
        if (cbprinter.isChecked()){
            cprinter = "YES";
        }
        if (cbvending.isChecked()){
            cvending = "YES";
        }



        final Map<String, Object> info = new HashMap<>();
        info.put("Title", Title);
        info.put("Location",adress);
        info.put("Description", tell);
        info.put("Price",price);

        info.put("Listing_part",listpart);
        info.put("smthgelse", amenities);
        info.put("No_of_MeetingRooms", meetrooom);
        info.put("No_of_Seats", seating);
        info.put("Shared_Office", shared);
        info.put("openoffice", openoffice);
        info.put("wifi", cwifi);
        info.put("pantry", cpantry);
        info.put("vendingmachine", cvending);
        info.put("printer", cprinter);









        if (filepath!=null){

            final ProgressDialog progressDialog =new ProgressDialog(this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();



            reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    Toast.makeText(HostActivity.this, "Listing Added", Toast.LENGTH_SHORT).show();

                    finish();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress= (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+" %");









                }
            });


        }





        Task<Uri> urlTask = reference.getDownloadUrl();
        while (!urlTask.isSuccessful());
        Uri downloadUrl = urlTask.getResult();

        final String Downurl = String.valueOf(downloadUrl);

        info.put("Image", Downurl);
        info.put("Uid" , uid);
        info.put("UserName",name);
        documentReference.set(info, SetOptions.merge());



    }
}
