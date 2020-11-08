package com.lotpick.lotpick.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lotpick.lotpick.Adapters.ListAdapter;
import com.lotpick.lotpick.ListViewModel;
import com.lotpick.lotpick.Models.Modelclass;
import com.lotpick.lotpick.R;
import com.lotpick.lotpick.place_description;

import java.util.List;


public class Explore extends Fragment implements ListAdapter.OnListItemClicked {

    private RecyclerView listView;
    private EditText searchview;
    private ListAdapter adapter;
    private Animation fadeInAnim;
    private CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("Hosts");

    public Explore() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);
        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);


        searchview = view.findViewById(R.id.search_field);

        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView = view.findViewById(R.id.Firestore_list);
        adapter = new ListAdapter(this);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);

        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);




    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListViewModel placeListViewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        placeListViewModel.getPlacesListModelData().observe(getViewLifecycleOwner(), new Observer<List<Modelclass>>() {
            @Override
            public void onChanged(List<Modelclass> placeListModels) {


                adapter.setPlaceListModels(placeListModels);
                adapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onItemClicked( String title ,String rating ,String price ,String location ,String desc ,String image,String id ,int pos ,String Uid,String userName,String amenities) {

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
