package com.foodie.app.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBquery;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Activity;
import com.foodie.app.ui.view_adapters.ActivityRecyclerViewAdapter;

import java.io.ByteArrayOutputStream;
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
        setupRecyclerView();

//        new AsyncTask<Object, Object, Void>() {
//            protected Void doInBackground(Object... params) {
//                // Background Code
//                //loadDemoData();

                loadData();
//                return null;
//            }

//        }.execute();



        return rootView;


    }

    private void loadDemoData() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hamburger);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp = Bitmap.createScaledBitmap(bmp, 1000,800, true);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] hamburger = stream.toByteArray();
        try {
            Activity activity = new Activity("Hamburger", "A Hamburger (or cheeseburger when served with a slice of cheese)" +
                                                            " is a sandwich consisting of one or more cooked patties of ground " +
                                                            "meat, usually beef, placed inside a sliced bread roll or bun. Hamburgers " +
                                                            "may be cooked in a variety of ways, including pan-frying, barbecuing, " +
                                                            "and flame-broiling. Hamburgers are often served with cheese, lettuce, " +
                                                            "tomato, bacon, onion, pickles, and condiments such as mustard, mayonnaise," +
                                                            " ketchup, relish, and chiles.", 23.56,2.5,1,hamburger, "Kosher");
            //activitiesList.add(activity);

            CallBack<Activity> callBack = new CallBack<Activity>() {
                @Override
                public void run(DataStatus status, List<Activity> data) {
                    DebugHelper.Log("Activity insert callBack finish with status: " + status);
                }
            };
            (new AsyncData<Activity>(getContext(), Activity.getURI(), DataManagerType.Insert, callBack)).execute(activity.toContentValues());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter(activitiesList, getActivity());
        recyclerView.setAdapter(activityRecyclerViewAdapter);


    }

    public void loadData() {

               //Create an AsyncData object and set the constructor
        AsyncData<Activity> data = new AsyncData<>(getContext(), Activity.getURI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Query);
        // Set the function to get status
        data.setCallBack(new CallBack<Activity>() {
            @Override
            public void run(DataStatus status, List<Activity> data) {
                DebugHelper.Log("Query callBack finish with status: " + status);
                if(status  != DataStatus.Success) {
                    Toast.makeText(getContext(), "Error: " + status , Toast.LENGTH_SHORT).show();
                }
                DebugHelper.Log("Query callBack: items total = "+data.size());

                for(Activity item : data) {
                    activityRecyclerViewAdapter.loadNewData(data);
                    activityRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
        // Execute the AsyncTask
        data.execute(new DBquery());


    }

}

