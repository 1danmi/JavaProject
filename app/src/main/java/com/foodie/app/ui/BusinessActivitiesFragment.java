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
    private RecyclerView recyclerView;
    private List<Activity> activitiesList;

    public BusinessActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_business_activities, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.business_activities_recycler_view);
        activitiesList = new ArrayList<Activity>();
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hamburger);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmp = Bitmap.createScaledBitmap(bmp, 1920,1080, true);
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] hamburger = stream.toByteArray();
//        try {
//            Activity activity = new Activity("Hamburger", "A Hamburger (or cheeseburger when served with a slice of cheese)" +
//                                                            " is a sandwich consisting of one or more cooked patties of ground " +
//                                                            "meat, usually beef, placed inside a sliced bread roll or bun. Hamburgers " +
//                                                            "may be cooked in a variety of ways, including pan-frying, barbecuing, " +
//                                                            "and flame-broiling. Hamburgers are often served with cheese, lettuce, " +
//                                                            "tomato, bacon, onion, pickles, and condiments such as mustard, mayonnaise," +
//                                                            " ketchup, relish, and chiles.", 23.56,2.5,1,hamburger, "Kosher");
//            activitiesList.add(activity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        setupRecyclerView(recyclerView, activitiesList);
        return rootView;


    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Activity> activities) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter(activities, getActivity());
        recyclerView.setAdapter(activityRecyclerViewAdapter);


    }

}

