package com.foodie.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.Services.DataUpdated;
import com.foodie.app.constants.Constants;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.DBquery;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.ui.helpers.IntentHelper;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.RecyclerItemClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class BusinessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemClickListener.onRecyclerClickListener {

    private static final String TAG = "BusinessActivity";
    private BusinessRecyclerViewAdapter businessRecyclerViewAdapter;


    public static List<Business> businessList;
    private RecyclerView recyclerView;
    private FloatingActionButton addBusinessFAB;
    private int userId = -1;
    private CPUser user = null;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("Business")) {
                loadData();
              //// TODO: implement data update
            }
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Log.d(TAG, "onCreate: starts");

        Toolbar toolbar = setActionBarAndFAB();

        setDrawer(toolbar);
        businessList = new ArrayList<>();
        setRecyclerView();

        loadData();

        final View rootView = getLayoutInflater().inflate(R.layout.nav_header_business, null);
        TextView drawerCPUserName = (TextView) rootView.findViewById(R.id.drawerCPUserName);
        TextView numOfBusinessse = (TextView) rootView.findViewById(R.id.drawerNumOfBusinesses);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(DataUpdated.mBroadcastBusiness);

        Intent serviceIntent = new Intent(getApplicationContext(), DataUpdated.class);
        startService(serviceIntent);

        //loadData();
       // loadDemoData();

//        drawerCPUserName.setText(user.getUserFullName());
//        numOfBusinessse.setText(businessRecyclerViewAdapter.getItemCount() + " Businesses");


    }


    private void loadDemoData() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hamburger);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp = Bitmap.createScaledBitmap(bmp, 1000, 800, true);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] hamburger = stream.toByteArray();
        try {
            Activity activity = new Activity("Hamburger", "A Hamburger (or cheeseburger when served with a slice of cheese)" +
                    " is a sandwich consisting of one or more cooked patties of ground " +
                    "meat, usually beef, placed inside a sliced bread roll or bun. Hamburgers " +
                    "may be cooked in a variety of ways, including pan-frying, barbecuing, " +
                    "and flame-broiling. Hamburgers are often served with cheese, lettuce, " +
                    "tomato, bacon, onion, pickles, and condiments such as mustard, mayonnaise," +
                    " ketchup, relish, and chiles.", 23.56, 2.5, "", hamburger, "Kosher");
            //activitiesList.add(activity);

            CallBack<Activity> callBack = new CallBack<Activity>() {
                @Override
                public void onSuccess(List<Activity> data) {
                    DebugHelper.Log("Activity insert callBack finish with status: ");
                }

                @Override
                public void onFailed(DataStatus status, String error) {

                }


            };
            (new AsyncData<Activity>(getApplicationContext(), Activity.getURI(), DataManagerType.Insert, callBack)).execute(activity.toContentValues());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.business_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        businessRecyclerViewAdapter = new BusinessRecyclerViewAdapter(businessList, getApplicationContext());

        recyclerView.setAdapter(businessRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
    }

    private Toolbar setActionBarAndFAB() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTitle("");
        addBusinessFAB = (FloatingActionButton) findViewById(R.id.add_business_fab);
        addBusinessFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivitiesActivity.class);
                intent.putExtra(Constants.EDIT_MODE, "true");
                intent.putExtra(Constants.BUSINESS_ID, "");
                startActivity(intent);
            }
        });
        return toolbar;
    }

    private void setDrawer(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Do nothing to not back to login
            //  super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.business, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadData() {


        Bundle b = getIntent().getExtras();
        if (b != null) {
            userId = b.getInt("Id");
            user = new CPUser(b.getString("Fullname"));
        }


        //Create an AsyncData object and set the constructor
        AsyncData<Business> data = new AsyncData<>(getApplicationContext(), Business.getURI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Query);
        // Set the function to get status
        data.setCallBack(new CallBack<Business>() {
            @Override
            public void onSuccess(List<Business> data) {
                for(Business item : data)
                {
                    businessRecyclerViewAdapter.loadNewData(data);
                }
            }

            @Override
            public void onFailed(DataStatus status, String error) {
                Toast.makeText(getApplicationContext(), "Error: " + status , Toast.LENGTH_SHORT).show();
            }


        });
        // Execute the AsyncTask
        data.execute(new DBquery());
        businessRecyclerViewAdapter.notifyDataSetChanged();


    }


    @Override
    public void onitemClick(View v, int position) {
        try {
            Intent intent = new Intent(this, ActivitiesActivity.class);

            intent.putExtra(Constants.BUSINESS_ID, businessRecyclerViewAdapter.getBusinessesList().get(position).get_ID());
            intent.putExtra(Constants.EDIT_MODE, "false");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View image = v.findViewById(R.id.business_image_view);
                IntentHelper.startActivitiesActivity(this, image, businessRecyclerViewAdapter.getBusinessesList().get(position), "false");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
    //    DBManagerFactory.signOut();
        super.onStop();

    }
}
