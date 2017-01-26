package com.foodie.app.listsDB;

import android.content.Context;
import android.widget.Toast;

import com.foodie.app.backend.AppContract;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBquery;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;
import com.foodie.app.ui.view_adapters.ActivityRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 1/19/2017.
 */

public class ContentResolverDatabase {


    //lists
    public static List<User> users;
    public static List<CPUser> cpusers;
    public static List<Business> businesses;
    public static List<Activity> activities;
    public CPUser cpuserConected;
    public User userConected;
    private static BusinessRecyclerViewAdapter businessRecyclerViewAdapter;
    private static ActivityRecyclerViewAdapter activitiesRecyclerViewAdapter;

    boolean isUpdated = false;

    static {
        users = new ArrayList<>();
        cpusers = new ArrayList<>();
        businesses = new ArrayList<>();
        activities = new ArrayList<>();
    }

    public static void getBusinessesList(final Context mContext) {

        businesses.clear();
        //Create an AsyncData object and set the constructor
        AsyncData<Business> data = new AsyncData<>(mContext, Business.getURI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Query);
        // Set the function to get status
        data.setCallBack(new CallBack<Business>() {
            @Override
            public void onSuccess(List<Business> data) {
                for (Business item : data) {
                    ContentResolverDatabase.setBusinessList(data);
                }
            }

            @Override
            public void onFailed(DataStatus status, String error) {
                Toast.makeText(mContext, "Error: " + status, Toast.LENGTH_SHORT).show();
            }


        });
        // Execute the AsyncTask
        data.execute(new DBquery());
    }

    public static void getUserBusinessesList(final Context mContext, String userid) {

        businesses.clear();
        //Create an AsyncData object and set the constructor
        AsyncData<Business> data = new AsyncData<>(mContext, Business.getURI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Query);
        // Set the function to get status
        data.setCallBack(new CallBack<Business>() {
            @Override
            public void onSuccess(List<Business> data) {
                for (Business item : data) {
                    ContentResolverDatabase.setBusinessList(data);
                }
            }

            @Override
            public void onFailed(DataStatus status, String error) {
                Toast.makeText(mContext, "Error: " + status, Toast.LENGTH_SHORT).show();
            }


        });
        // Execute the AsyncTask
        data.execute(new DBquery());
    }


    public static void getActivitiesList(final Context mContext) {

        activities.clear();
        //Create an AsyncData object and set the constructor
        AsyncData<Activity> data = new AsyncData<>(mContext, Activity.getURI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Query);
        // Set the function to get status
        data.setCallBack(new CallBack<Activity>() {
            @Override
            public void onSuccess(List<Activity> data) {
                for (Activity item : data) {
                    ContentResolverDatabase.setActivityList(data,false);
                }
            }

            @Override
            public void onFailed(DataStatus status, String error) {
                Toast.makeText(mContext, "Error: " + status, Toast.LENGTH_SHORT).show();
            }


        });
        // Execute the AsyncTask
        data.execute(new DBquery());
    }

    public static void getBusinessActivitiesList(final Context mContext, String businessId, final boolean size) {

        activities.clear();
        //Create an AsyncData object and set the constructor
        AsyncData<Activity> data = new AsyncData<>(mContext, Activity.getURI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Query);
        // Set the function to get status
        data.setCallBack(new CallBack<Activity>() {
            @Override
            public void onSuccess(List<Activity> data) {
                for (Activity item : data) {
                    ContentResolverDatabase.setActivityList(data, size);
                }
            }

            @Override
            public void onFailed(DataStatus status, String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }


        });
        // Execute the AsyncTask
        data.execute(new DBquery(new String[]{AppContract.Activity.ACTIVITY_BUSINESS_ID}, new String[]{businessId}));
    }


    public static void setBusinessList(List<Business> businessList) {
        businesses = businessList;

        businessRecyclerViewAdapter.loadNewData(businesses);
    }

    public static void setActivityList(List<Activity> activityList, boolean size) {
        activities = activityList;
        if (!size)
            activitiesRecyclerViewAdapter.loadNewData(activities);
    }


    public static void setBusinessRecyclerViewAdapter(BusinessRecyclerViewAdapter adapter) {
        businessRecyclerViewAdapter = adapter;
    }

    public static void setActivityRecyclerViewAdapter(ActivityRecyclerViewAdapter adapter) {
        activitiesRecyclerViewAdapter = adapter;
    }
}
