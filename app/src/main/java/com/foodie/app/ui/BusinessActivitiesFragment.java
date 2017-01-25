package com.foodie.app.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodie.app.R;
import com.foodie.app.constants.Constants;
import com.foodie.app.entities.Activity;
import com.foodie.app.listsDB.ContentResolverDatabase;
import com.foodie.app.ui.view_adapters.ActivityRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessActivitiesFragment extends Fragment {

    ActivityRecyclerViewAdapter activityRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Activity> activitiesList;
    private String businessID;

    public BusinessActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_business_activities, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.business_activities_recycler_view);
        activitiesList = new ArrayList<Activity>();
        setupRecyclerView();





        return rootView;


    }


    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter( ContentResolverDatabase.activities, getActivity());
        recyclerView.setAdapter(activityRecyclerViewAdapter);


    }

    public void loadData() {

        Bundle bundle = this.getArguments();
        businessID = bundle.getString(Constants.BUSINESS_ID, "");
        ContentResolverDatabase.setActivityRecyclerViewAdapter(activityRecyclerViewAdapter);
        ContentResolverDatabase.getBusinessActivitiesList(getContext(), businessID);
        activityRecyclerViewAdapter.notifyDataSetChanged();
    }

}

