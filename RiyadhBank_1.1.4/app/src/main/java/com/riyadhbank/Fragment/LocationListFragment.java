package com.riyadhbank.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riyadhbank.Adapter.LocationListAdapter;
import com.riyadhbank.Modal.LocationModal;
import com.riyadhbank.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class LocationListFragment extends Fragment {

    View view;
    RecyclerView mapListView;
    LocationListAdapter locationListAdapter;
    ArrayList<LocationModal> locationArrayList;

    public LocationListFragment(ArrayList<LocationModal> locationArrayList) {
        this.locationArrayList = locationArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location_list, container, false);

        mapListView = (RecyclerView) view.findViewById(R.id.mapListView);
        mapListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        locationListAdapter = new LocationListAdapter(getActivity(),locationArrayList);
        mapListView.setAdapter(locationListAdapter);

        return view;

    }

}
