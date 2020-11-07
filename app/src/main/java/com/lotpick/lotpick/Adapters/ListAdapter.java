package com.lotpick.lotpick.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lotpick.lotpick.Models.Modelclass;
import com.lotpick.lotpick.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private String tvTitle;
    String id ;
    String Listing_part,No_of_MeetingRooms,No_of_Seats
            ,Shared_Office,openoffice,wifi,pantry,vendingmachine,printer,smthngelse;
    private String tvPrice;
    private String tvLocation;
    private String tvRating;
    private String tvImage;
    private String tvDesc,tvUid,tvUserName;





    private List<Modelclass> placeListModels;
    private OnListItemClicked onListItemClicked;

    public ListAdapter( OnListItemClicked OnListItemClicked) {
        this.onListItemClicked = OnListItemClicked;
    }


    public void setPlaceListModels(List<Modelclass> placeListModels) {
        this.placeListModels = placeListModels;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

         String location = placeListModels.get(position).getLocation();
         String rating = placeListModels.get(position).getRating();
         String price = placeListModels.get(position).getPrice();

         String imageuri = placeListModels.get(position).getImage();





        String listTitle = placeListModels.get(position).getTitle();
        if (listTitle.length() > 20) {
            listTitle = listTitle.substring(0, 20);
        }

        holder.listTitle.setText(listTitle + "...");

        holder.listLocation.setText(location);


            holder.listRating.setText("0.0/5.0");


        holder.listPrice.setText("Rs.  " + price);

        Glide.with(holder.itemView.getContext()).load(imageuri).centerCrop().placeholder(R.drawable.white_background    ).into(holder.listImage);




    }

    @Override
    public int getItemCount() {
        if (placeListModels == null) {
            return 0;
        } else {
            return placeListModels.size();
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView listTitle;
        private TextView listPrice;
        private TextView listLocation;
        private TextView listRating;
        private ImageView listImage;


        ListViewHolder(@NonNull View itemView ) {
            super(itemView);
            listTitle = itemView.findViewById(R.id.list_title);
            listPrice = itemView.findViewById(R.id.list_price);
            listRating = itemView.findViewById(R.id.list_rating);
            listLocation = itemView.findViewById(R.id.list_location);
            listImage = itemView.findViewById(R.id.list_image);
            listImage = itemView.findViewById(R.id.list_image);




            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(),R.anim.blink_anim);
            itemView.startAnimation(animation);
            int pos = getAdapterPosition();
            tvTitle = placeListModels.get(pos).getTitle();
            tvLocation = placeListModels.get(pos).getLocation();
            tvRating = placeListModels.get(pos).getRating();
            tvPrice = placeListModels.get(pos).getPrice();
            tvDesc = placeListModels.get(pos).getDescription();
            tvImage = placeListModels.get(pos).getImage();
            tvUid = placeListModels.get(pos).getUid();
            tvUserName = placeListModels.get(pos).getUserName();
            id = placeListModels.get(pos).getId();

            Listing_part = placeListModels.get(pos).getListing_part();
            No_of_MeetingRooms= placeListModels.get(pos).getNo_of_MeetingRooms();
            No_of_Seats= placeListModels.get(pos).getNo_of_Seats();
            Shared_Office= placeListModels.get(pos).getShared_Office();
            openoffice= placeListModels.get(pos).getOpenoffice();
            wifi= placeListModels.get(pos).getWifi();
            pantry= placeListModels.get(pos).getPantry();
            vendingmachine= placeListModels.get(pos).getVendingmachine();
            printer= placeListModels.get(pos).getPrinter();
            smthngelse =placeListModels.get(pos).getSmthgelse();

            String amenities = "Listing part of office: "+Listing_part+
                    "\nNo. of Meeting rooms: "+No_of_MeetingRooms+
                    "\nNo. of Seats: "+No_of_Seats+
                    "\nShared Office : "+Shared_Office+
                    "\nOpen Office: "+openoffice+
                    "\nWi-Fi: "+wifi+
                    "\nPantry: "+pantry+
                    "\nVending machines: "+vendingmachine+
                    "\nPrinters: "+printer+
                    "\nOther details: "+ smthngelse;



            onListItemClicked.onItemClicked(tvTitle,tvRating,tvPrice,tvLocation,tvDesc,tvImage,id ,pos, tvUid,tvUserName , amenities);

        }







    }



    public interface OnListItemClicked {
        void onItemClicked(  String title ,String rating ,String price ,String location ,String desc ,String image,String Id , int pos ,String Uid , String userName ,String amenities);
    }




}