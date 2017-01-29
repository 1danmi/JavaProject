package com.foodie.app.ui;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.foodie.app.R;
import com.foodie.app.constants.Constants;
import com.foodie.app.entities.Activity;
import com.foodie.app.listsDB.ContentResolverDatabase;
import com.foodie.app.ui.view_adapters.ActivityRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessActivitiesFragment extends Fragment implements RecyclerItemClickListener.onRecyclerClickListener{

    ActivityRecyclerViewAdapter activityRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Activity> activitiesList;
    private String businessID, businessName;
    private ImageView backgroundImage;
    private View parent;

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
        parent = getActivity().findViewById(R.id.activities_activity_layout);
        backgroundImage = (ImageView) parent.findViewById(R.id.app_bar_business_logo);

        activitiesList = new ArrayList<Activity>();
        setupRecyclerView();

        return rootView;


    }


    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter( ContentResolverDatabase.activities, getActivity());
        recyclerView.setAdapter(activityRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, this));


    }

    public void loadData() {

        Bundle bundle = this.getArguments();
        businessID = bundle.getString(Constants.BUSINESS_ID, "");
        businessName = bundle.getString(Constants.BUSINESS_NAME,"");
        ContentResolverDatabase.setActivityRecyclerViewAdapter(activityRecyclerViewAdapter);
        ContentResolverDatabase.setBusinessBackground(backgroundImage);
        ContentResolverDatabase.getBusinessActivitiesList(getContext(), businessID, false);

        //activityRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onitemClick(View v, int position, MotionEvent e) {

        Intent intent = new Intent(v.getContext(), ActivityDetails.class);
        intent.putExtra(Constants.ACTIVITY_ID,activityRecyclerViewAdapter.getActivitiesList().get(position).get_ID());
        intent.putExtra(Constants.EDIT_MODE, false);
        intent.putExtra(Constants.BUSINESS_NAME, businessName);
        intent.putExtra(Constants.BUSINESS_ID, businessID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }





//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            View image = v.findViewById(R.id.business_image_view);
//            IntentHelper.startActivitiesActivity(this, image, businessRecyclerViewAdapter.getBusinessesList().get(position), "false");
//        }
    }

}

