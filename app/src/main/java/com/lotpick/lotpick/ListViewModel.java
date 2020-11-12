package com.lotpick.lotpick;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lotpick.lotpick.Models.Modelclass;

import java.util.List;

public class ListViewModel extends ViewModel implements FirebaseRepository.OnFirestoreTaskComplete {

    private MutableLiveData<List<Modelclass>> placeListModelData = new MutableLiveData<>();

    public LiveData<List<Modelclass>> getPlacesListModelData() {
        return placeListModelData;
    }

    public ListViewModel() {
        FirebaseRepository firebaseRepository = new FirebaseRepository(this);
        firebaseRepository.getPlaceData();
         }

    @Override
    public void placesListDataAdded(List<Modelclass> placeListModelsList) {
        placeListModelData.setValue(placeListModelsList);
    }

    @Override
    public void onError(Exception e) {

    }
}
