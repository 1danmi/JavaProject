package com.foodie.app.ui;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.constants.Constants;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.listsDB.ContentResolverDatabase;
import com.foodie.app.ui.helpers.IntentHelper;
import com.foodie.app.ui.view_adapters.ActivityRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessActivitiesFragment extends Fragment implements RecyclerItemClickListener.onRecyclerClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "BusinessActivitiesFragm";

    ActivityRecyclerViewAdapter activityRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Activity> activitiesList;
    private String businessID, businessName;
    private ImageView backgroundImage;
    private View parent;
    private ImageButton btnMore;
    private SwipeRefreshLayout refreshLayout;

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
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activities_refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.business_activities_recycler_view);
        parent = getActivity().findViewById(R.id.activities_activity_layout);
        backgroundImage = (ImageView) parent.findViewById(R.id.app_bar_business_logo);

        activitiesList = new ArrayList<Activity>();
        setupRecyclerView();

        return rootView;


    }


    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        activityRecyclerViewAdapter = new ActivityRecyclerViewAdapter(ContentResolverDatabase.activities, getActivity());
        recyclerView.setAdapter(activityRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, this));


    }

    public void loadData() {

        Bundle bundle = this.getArguments();
        businessID = bundle.getString(Constants.BUSINESS_ID, "");
        businessName = bundle.getString(Constants.BUSINESS_NAME, "");
        CallBack<Void> callBack = new CallBack<Void>() {
            @Override
            public void onSuccess(List<Void> data) {
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(DataStatus status, String error) {

            }
        };
        ContentResolverDatabase.setActivityRecyclerViewAdapter(activityRecyclerViewAdapter);
        ContentResolverDatabase.setBusinessBackground(backgroundImage);
        ContentResolverDatabase.getBusinessActivitiesList(getContext(), businessID, false, callBack);

        //activityRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, final int position, MotionEvent e) {
        btnMore = (ImageButton) v.findViewById(R.id.activity_menu_button);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View image = v.findViewById(R.id.activity_image_view);
                IntentHelper.startDetailsActivity(getActivity(), image, activityRecyclerViewAdapter.getActivitiesList().get(position), businessID, businessName);
//            }
        }
    }

    private void deleteItem(int position) {
        CallBack<Business> callBack = new CallBack<Business>() {
            @Override
            public void onSuccess(List<Business> data) {
                DebugHelper.Log("Business insert callBack finish with status: Success");
                Log.d(TAG, "onSuccess: success");
            }

            @Override
            public void onFailed(DataStatus status, String error) {
                Log.d(TAG, "onFailed: failed");
            }
        };
        (new AsyncData<>(getContext(), Activity.getURI(), DataManagerType.Delete, callBack)).execute(activityRecyclerViewAdapter.getActivity(position).get_ID());
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}

