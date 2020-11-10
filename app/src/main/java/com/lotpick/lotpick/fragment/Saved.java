package com.lotpick.lotpick.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lotpick.lotpick.FavListViewModel;
import com.lotpick.lotpick.Adapters.ListAdapter;
import com.lotpick.lotpick.Models.Modelclass;
import com.lotpick.lotpick.R;
import com.lotpick.lotpick.place_description;

import java.util.List;


public class Saved extends Fragment implements ListAdapter.OnListItemClicked {

    private RecyclerView listView;


    private ListAdapter adapter;

    private Animation fadeInAnim;


    public Saved() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);

        
        }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);



            listView = view.findViewById(R.id.Fav_Firestore_list);
            adapter = new ListAdapter(this);
            listView.setLayoutManager(new LinearLayoutManager(getContext()));
            listView.setHasFixedSize(true);
            listView.setAdapter(adapter);


            fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            FavListViewModel placeListViewModel = new ViewModelProvider(requireActivity()).get(FavListViewModel.class);
            placeListViewModel.getPlacesListModelData().observe(getViewLifecycleOwner(), new Observer<List<Modelclass>>() {
                @Override
                public void onChanged(List<Modelclass> placeListModels) {


                    listView.startAnimation(fadeInAnim);
                    adapter.setPlaceListModels(placeListModels);
                    adapter.notifyDataSetChanged();

                }
            });

        }


    @Override
    public void onItemClicked( String title ,String rating ,String price ,String location ,String desc ,String image,String id ,int pos,String Uid ,String userName,String amenities) {

        Intent intent = new Intent(getActivity(), place_description.class);
        intent.putExtra("Title",title);
        intent.putExtra("Rating",rating);
        intent.putExtra("Price",price);
        intent.putExtra("Location",location);
        intent.putExtra("Desc",desc);
        intent.putExtra("Image",image);
        intent.putExtra("Id",id);
        intent.putExtra("Pos",pos);
        intent.putExtra("Uid",Uid);
        intent.putExtra("username",userName);
        intent.putExtra("Amenities",amenities);

        startActivity(intent);
        }
    }