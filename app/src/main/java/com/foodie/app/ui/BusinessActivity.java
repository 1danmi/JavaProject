package com.foodie.app.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ProgressBar;
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
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.listsDB.ContentResolverDatabase;
import com.foodie.app.provider.MyContentObserver;
import com.foodie.app.ui.helpers.IntentHelper;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class BusinessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemClickListener.onRecyclerClickListener, android.widget.SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

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
    private ProgressBar progressBar;
    private TextView noBusinessesText;
    private SwipeRefreshLayout refreshLayout;
    private TextView drawerCPUserName;
    private TextView drawerUserEmail;
    private BroadcastReceiver mReceiver;
    private Activity thisActivity = this;
    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Log.d(TAG, "onCreate: starts");

        Toolbar toolbar = setActionBarAndFAB();
        progressBar = (ProgressBar) findViewById(R.id.business_progress_bar);
        noBusinessesText = (TextView) findViewById(R.id.noBusinessesTextView);
        noBusinessesText.setVisibility(View.GONE);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_business);
        refreshLayout.setOnRefreshListener(this);
        setDrawer(toolbar);
        businessList = new ArrayList<>();
        setRecyclerView();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        drawerCPUserName = (TextView) header.findViewById(R.id.drawerUserNameTextView);
        drawerUserEmail = (TextView) header.findViewById(R.id.drawerEmailTextView);



        refreshLayout.setRefreshing(true);


        myContentObserver = new MyContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Business.getURI(), true, myContentObserver);

/*****************************SERVICE*********************************************/
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(DataUpdated.mBroadcastBusiness);
        mIntentFilter.addAction(DataUpdated.mBroadcastNoBussiness);
        mIntentFilter.addAction(DataUpdated.mBroadcastNoBussiness);
        mIntentFilter.addAction(DataUpdated.mBroadcastCpusers);




        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(DataUpdated.mBroadcastBusiness)) {

                    noBusinessesText.setVisibility(View.GONE);
                    onRefresh();

                    // TODO: implement data update
                }
                if (intent.getAction().equals(DataUpdated.mBroadcastCpusers)) {
                    DebugHelper.Log("Business activity: cpu updated");
                    setUserName();
                    onRefresh();
                    //Todo: implement user details here


                }
                if (intent.getAction().equals(DataUpdated.mBroadcastNoBussiness)) {
                    noBusiness();

                    DebugHelper.Log("Business activity: no business");

                }

            }
        };

        registerReceiver(mReceiver, mIntentFilter);

        serviceIntent = new Intent(getApplicationContext(), DataUpdated.class);
        startService(serviceIntent);
        /****************************************END SERVICE****************************************/


    }

    private void setUserName() {

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
        Log.d(TAG, "run: run");
        String email = DBManagerFactory.getCurrentUser().getUserEmail();
        String name = DBManagerFactory.getCurrentUser().getUserFullName();

        drawerCPUserName.setText(name);
        drawerUserEmail.setText(email);
//            }
//        });

    }

    private void setRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.business_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new ScaleInBottomAnimator(new AccelerateDecelerateInterpolator()));
//        recyclerView.setItemAnimator(new ScaleInTopAnimator(new AccelerateDecelerateInterpolator()));
//        recyclerView.setItemAnimator(new ScaleInLeftAnimator(new AccelerateDecelerateInterpolator()));
        recyclerView.setItemAnimator(new SlideInUpAnimator(new AccelerateDecelerateInterpolator()));

        businessRecyclerViewAdapter = new BusinessRecyclerViewAdapter(ContentResolverDatabase.businesses, getApplicationContext(), progressBar, noBusinessesText, this);

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
            String emailAddress = "abenathardavid@gmail.com; danielmiller20@gmail.com;";
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } else if (id == R.id.privacy_navbar) {
            String url = "https://sites.google.com/view/foodie-app/home";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.primary)).setShowTitle(true);
            builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setCloseButtonIcon(
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));

        } else if (id == R.id.settings_navbar) {
            Intent intent = new Intent(this, SettingActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
        } else if (id == R.id.sign_out_navbar) {
            DBManagerFactory.signOut();
            if(serviceIntent!=null)
                stopService(serviceIntent);
            ContentResolverDatabase.businesses.clear();
            ContentResolverDatabase.loadingCounter = 0;
            super.onBackPressed();
        } else if (id == R.id.about_navbar) {
            Intent intent = new Intent(this, AboutActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }

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
        CallBack<Void> callback = new CallBack<Void>() {
            @Override
            public void onSuccess(List<Void> data) {
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(DataStatus status, String error) {

            }
        };
        ContentResolverDatabase.setBusinessRecyclerViewAdapter(businessRecyclerViewAdapter);
        ContentResolverDatabase.getBusinessesList(getApplicationContext(), callback);
        //businessRecyclerViewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(View v, final int position, MotionEvent e) {
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
                        View image = v.findViewById(R.id.gif_loading_business);
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
                        View image = v.findViewById(R.id.gif_loading_business);
                        IntentHelper.startActivitiesActivity(this, image, businessRecyclerViewAdapter.getBusinessesList().get(position), "false");
                    }
                }
            }
        } catch (Exception f) {
            f.printStackTrace();
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
        (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Delete, callBack)).execute(businessRecyclerViewAdapter.getBusiness(position).get_ID());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadData();
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
        ContentResolverDatabase.businesses.clear();
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


    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: starts");
        loadData();
    }

    private void noBusiness() {
        refreshLayout.setRefreshing(false);
        noBusinessesText.setVisibility(View.VISIBLE);
    }
}
