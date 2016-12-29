package com.foodie.app.ui;

import android.app.ActivityOptions;
import android.content.Intent;
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
import com.foodie.app.backend.AppContract;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBquery;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.RecyclerItemClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

public class BusinessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemClickListener.onRecyclerClickListener {

    private static final String TAG = "BusinessActivity";
    private BusinessRecyclerViewAdapter businessRecyclerViewAdapter;
    private static final String BUSINESS_ID = "businessId";
    private static final String EDIT_MODE = "mEditKey";
    public static List<Business> businessList;
    private RecyclerView recyclerView;
    private FloatingActionButton addBusinessFAB;
    private int userId = -1;
    private CPUser  user = null;


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

        TextView title = (TextView) findViewById(R.id.BusinessName);



        title.setText(user.getUserFullName());


    }

    private void setRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.business_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new SlideInDownAnimator());

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
                intent.putExtra(BUSINESS_ID, 0);
                intent.putExtra(EDIT_MODE, "true");
                startActivity(intent);
            }
        });
        return toolbar;
    }

    private void setDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

    @SuppressWarnings("StatementWithEmptyBody")
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
        if(b != null){
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
            public void run(DataStatus status, List<Business> data) {
                DebugHelper.Log("Query callBack finish with status: " + status);
                if(status  != DataStatus.Success)
                {
                    Toast.makeText(getApplicationContext(), "Error: " + status , Toast.LENGTH_SHORT).show();

                }
                DebugHelper.Log("Query callBack: items total = "+data.size());

                for(Business item : data)
                {
                    businessRecyclerViewAdapter.addItem(item);
                }
            }
        });
        // Execute the AsyncTask
        data.execute(new DBquery());


    }


    @Override
    public void onitemClick(View v, int position) {
        try {
            Intent intent = new Intent(this, ActivitiesActivity.class);

            intent.putExtra(BUSINESS_ID, businessList.get(position).get_ID());
            intent.putExtra(EDIT_MODE, "false");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Snackbar.make(v,"Item at position " + position + " had been clicked", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        businessRecyclerViewAdapter.notifyDataSetChanged();
    }


}
