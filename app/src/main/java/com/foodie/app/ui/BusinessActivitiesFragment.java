package com.foodie.app.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodie.app.R;
import com.foodie.app.entities.Activity;
import com.foodie.app.ui.view_adapters.ActivityRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessActivitiesFragment extends Fragment {

    ActivityRecyclerViewAdapter activityRecyclerViewAdapter;
    RecyclerView recyclerView;

    public BusinessActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_business_activities, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.business_activities_recycler_view);
        List<Activity> activitiesList = new ArrayList<Activity>();
        setupRecyclerView(recyclerView, activitiesList);
        return rootView;


    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Activity> activities) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter(activities, getActivity());
        recyclerView.setAdapter(activityRecyclerViewAdapter);


    }

}

