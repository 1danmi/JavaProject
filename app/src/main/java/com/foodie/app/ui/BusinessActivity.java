package com.foodie.app.ui;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.Services.DataUpdated;
import com.foodie.app.constants.Constants;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.listsDB.ContentResolverDatabase;
import com.foodie.app.provider.MyContentObserver;
import com.foodie.app.ui.helpers.IntentHelper;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.RecyclerItemClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class BusinessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemClickListener.onRecyclerClickListener, android.widget.SearchView.OnQueryTextListener {

    private static final String TAG = "BusinessActivity";
    private BusinessRecyclerViewAdapter businessRecyclerViewAdapter;
    private final boolean secondLayout = false;
    private MyContentObserver myContentObserver;
    public static List<Business> businessList;
    private RecyclerView recyclerView;
    private FloatingActionButton addBusinessFAB;
    private int userId = -1;
    private CPUser user = null;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private IntentFilter mIntentFilter;
    private ImageButton btnMore;
    private Button deleteBtn;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("Business")) {
                loadData();
                // TODO: implement data update
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



        final View rootView = getLayoutInflater().inflate(R.layout.nav_header_business, null);
        TextView drawerCPUserName = (TextView) rootView.findViewById(R.id.drawerCPUserName);
        TextView numOfBusinessse = (TextView) rootView.findViewById(R.id.drawerNumOfBusinesses);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(DataUpdated.mBroadcastBusiness);

        Intent serviceIntent = new Intent(getApplicationContext(), DataUpdated.class);
        startService(serviceIntent);


        myContentObserver = new MyContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Business.getURI(), true, myContentObserver);


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
//        recyclerView.setItemAnimator(new ScaleInBottomAnimator(new AccelerateDecelerateInterpolator()));
//        recyclerView.setItemAnimator(new ScaleInTopAnimator(new AccelerateDecelerateInterpolator()));
//        recyclerView.setItemAnimator(new ScaleInLeftAnimator(new AccelerateDecelerateInterpolator()));
        recyclerView.setItemAnimator(new SlideInUpAnimator(new AccelerateDecelerateInterpolator()));

        businessRecyclerViewAdapter = new BusinessRecyclerViewAdapter(ContentResolverDatabase.businesses, getApplicationContext());

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(BusinessActivity.this).toBundle());
                }
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
//        getMenuInflater().inflate(R.menu.business, menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
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

        if (id == R.id.bug_report_navbar) {
            // Handle the camera action
        } else if (id == R.id.language_navbar) {

        } else if (id == R.id.settings_navbar) {

        } else if (id == R.id.sign_out_navbar) {
            DBManagerFactory.signOut();
            super.onBackPressed();
        } else if (id == R.id.about_navbar) {
            DBManagerFactory.signOut();
            super.onBackPressed();
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
        ContentResolverDatabase.setBusinessRecyclerViewAdapter(businessRecyclerViewAdapter);
        ContentResolverDatabase.getBusinessesList(getApplicationContext());
        //businessRecyclerViewAdapter.notifyDataSetChanged();


        recyclerView.getItemAnimator().setAddDuration(800);
        recyclerView.getItemAnimator().setRemoveDuration(800);
        recyclerView.getItemAnimator().setMoveDuration(800);
        recyclerView.getItemAnimator().setChangeDuration(800);
    }

    @Override
    public void onitemClick(View v, final int position, MotionEvent e) {
        try {
            if (secondLayout) {
                deleteBtn = (Button) v.findViewById(R.id.deleteButton);
                if (RecyclerItemClickListener.isViewClicked(deleteBtn, e)) {
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteItem(position);
                        }
                    });
                } else {


                    Intent intent = new Intent(this, ActivitiesActivity.class);
                    intent.putExtra(Constants.BUSINESS_ID, businessRecyclerViewAdapter.getBusinessesList().get(position).get_ID());
                    intent.putExtra(Constants.EDIT_MODE, "false");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        View image = v.findViewById(R.id.business_image_view);
                        IntentHelper.startActivitiesActivity(this, image, businessRecyclerViewAdapter.getBusinessesList().get(position), "false");

                    }
                }

            } else {
                btnMore = (ImageButton) v.findViewById(R.id.businessMenuButton);

                if (RecyclerItemClickListener.isViewClicked(btnMore, e)) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), btnMore);

                    getMenuInflater().inflate(R.menu.menu_business_recycler_view, popupMenu.getMenu());

                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Log.d(TAG, "onMenuItemClick: delete pressed");
                            Log.d(TAG, "onMenuItemClick:" + businessRecyclerViewAdapter.getBusiness(position).getBusinessName());
                            switch (item.getItemId()) {
                                case R.id.deletMenuOption:
                                    deleteItem(position);
                                    return true;
                            }
                            return false;
                        }
                    });

                } else {


                    Intent intent = new Intent(this, ActivitiesActivity.class);
                    intent.putExtra(Constants.BUSINESS_ID, businessRecyclerViewAdapter.getBusinessesList().get(position).get_ID());
                    intent.putExtra(Constants.EDIT_MODE, "false");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        View image = v.findViewById(R.id.business_image_view);
                        IntentHelper.startActivitiesActivity(this, image, businessRecyclerViewAdapter.getBusinessesList().get(position), "false");
                    }
                }
            }
        } catch (Exception f) {
            f.printStackTrace();
        }


    }

    protected void deleteItem(int position) {
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
        (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Delete, callBack)).execute(businessRecyclerViewAdapter.getBusiness(position).toContentValues());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        registerReceiver(mReceiver, mIntentFilter);
        getContentResolver().registerContentObserver(Business.getURI(), true, myContentObserver);
    }

    @Override
    protected void onPause() {
        //getContentResolver().unregisterContentObserver(myContentObserver);
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DBManagerFactory.signOut();
        finish();
        super.onDestroy();
    }

    private List<Business> filterLanguage(List<Business> businesses, String query) {
        final List<Business> filteredModelList = new ArrayList<>();

        for (Business b : businesses) {
            String text = b.getBusinessName().toLowerCase();
            if (text.contains(query.trim().toLowerCase())) {
                filteredModelList.add(b);
            }
        }
        return filteredModelList;
    }


    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filterLanguage logic
        final List<Business> filteredModelList = filterLanguage(ContentResolverDatabase.businesses, query);
        businessRecyclerViewAdapter.loadNewData(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


}
