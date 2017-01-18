package com.foodie.app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.backend.AppContract;
import com.foodie.app.constants.Constants;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBquery;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Business;
import com.foodie.app.ui.helpers.AnimationHelper;
import com.foodie.app.ui.view_adapters.AppBarStateChangeListener;
import com.foodie.app.ui.view_adapters.BusinessViewPagerAdapter;

import java.util.List;

public class ActivitiesActivity extends AppCompatActivity {


    private static final String TAG = "ActivitiesActivity";
    //private static CoordinatorLayout rootLayout;
    public static Business businessItem;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    //private CardView businessLogoCardView;
    private ImageView businessLogoHeader;
    private TabLayout tabLayout;
    private TextView businessNameHeader;
    private String mEditMode;
    public Boolean isPhotoChanged;
    private String businessID;
    private BusinessDetailsFragment businessDetailsFragment;
    private BusinessActivitiesFragment businessActivitiesFragment;
    //private FABProgressCircle addFAB, editFAB;
    private FloatingActionButton addButton, editButton, addActivityButton;
    private CoordinatorLayout rootLayout;
    private float fabTranslationX;
    private float fabTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
//        }

        setContentView(R.layout.activity_activities);

        initializeViews();

        initializeComponents();

        setFabs();

        setActionBar();

        inflateData();

        setAppBar();


    }

    private void initializeComponents() {
        Intent intent = getIntent();

        businessID = intent.getStringExtra(Constants.BUSINESS_ID);

        mEditMode = intent.getStringExtra(Constants.EDIT_MODE);

        businessDetailsFragment = new BusinessDetailsFragment();

        businessActivitiesFragment = new BusinessActivitiesFragment();

        final View rootView = getLayoutInflater().inflate(R.layout.fragment_business_details, null);
        businessDetailsFragment.initializeViews(rootView);


    }

    private void setFabs() {
        fabTranslationX = addButton.getTranslationX();
        fabTranslationY = addButton.getTranslationY();
        // Setup up active buttons
        if (mEditMode.equals("true")) {
            AnimationHelper.show(fabTranslationX, fabTranslationY, addButton);
            AnimationHelper.hideFab(editButton);


        } else {
            AnimationHelper.showFab(editButton);
            AnimationHelper.hideFab(addButton);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessDetailsFragment.mEditMode = true;
                mEditMode = "true";
                AnimationHelper.showFab(addButton);
                AnimationHelper.hideFab(editButton);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (businessDetailsFragment.inputCheck()) {
                    if (businessDetailsFragment.businessItem.get_ID().equals("")) {
                        //businessItem.set_ID(Business.businessID + 1);
                        //Business.businessID++;
                        CallBack<Business> callBack = new CallBack<Business>() {
                            @Override
                            public void run(DataStatus status, List<Business> data) {
                                DebugHelper.Log("Business insert callBack finish with status: " + status);
                            }
                        };
                        (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Insert, callBack)).execute(businessDetailsFragment.businessItem.toContentValues());

                        AnimationHelper.showFab(editButton);
                        AnimationHelper.hideFab(addButton);
                    } else {
                        CallBack<Business> callBack = new CallBack<Business>() {
                            @Override
                            public void run(DataStatus status, List<Business> data) {
                                DebugHelper.Log("Business insert callBack finish with status: " + status);
                            }
                        };
                        (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Update, callBack)).execute(businessItem.toContentValues());

                        AnimationHelper.showFab(editButton);
                        AnimationHelper.hideFab(addButton);
                    }
                    businessDetailsFragment.mEditMode = false;
                    mEditMode = "false";
                }
            }
        });
    }

    //Initializes the views
    private void initializeViews() {
        rootLayout = (CoordinatorLayout) findViewById(R.id.activities_activity_layout);
        addButton = (FloatingActionButton) findViewById(R.id.add_fab_button2);
        editButton = (FloatingActionButton) findViewById(R.id.edit_fab_button2);
        addActivityButton = (FloatingActionButton) findViewById(R.id.add_activity_button);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        appBarLayout = (AppBarLayout) findViewById(R.id.business_name_app_bar);
        businessLogoHeader = (ImageView) findViewById(R.id.business_header_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        businessNameHeader = (TextView) findViewById(R.id.business_header_name);
        //businessLogoCardView = (CardView) findViewById(R.id.business_header_card_view);
    }

    //Sets the appbar listener to hide the title while collapsed.
    private void setAppBar() {

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                setActivityTitle("");

//                addButton.setVisibility(View.VISIBLE);
//                editButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                setActivityTitle(businessItem.getBusinessName());

//                addButton.setVisibility(View.GONE);
//                editButton.setVisibility(View.GONE);
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {
            }
        });
    }

    //Inflates the business date from the database.
    private void inflateData() {

        if (!businessID.equals("")) {
            DBquery dBquery = new DBquery(new String[]{AppContract.Business.BUSINESS_ID},new String[]{businessID});
            (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Query, new CallBack<Business>() {
                @Override
                public void onSuccess(List<Business> data) {
                    DebugHelper.Log("inflateData: Success");
                    if(data!=null) {
                        DebugHelper.Log("inflateData: first data = " + data.get(0).get_ID());

                        businessItem = data.get(0);
                        //DebugHelper.Log("Da"));
                        setData(businessID);
                        setTabLayout();

                        businessDetailsFragment.inflateData();

                    }

                }

                @Override
                public void onFailed(DataStatus status, String error) {
                    DebugHelper.Log("inflateData: " + status + ": "+error);
                }


            })).execute(dBquery);
        } else {
            businessItem = new Business();
            setData(businessID);
            setTabLayout();
            businessDetailsFragment.inflateData();


        }


    }

    private void setData(String businessID) {
        if (businessID.equals("")) {
            businessItem = new Business();
            isPhotoChanged = false;
        } else {
            businessNameHeader.setText(businessItem.getBusinessName());
            isPhotoChanged = true;
            if (businessItem.getBusinessLogo() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(businessItem.getBusinessLogo(), 0, businessItem.getBusinessLogo().length);
                businessLogoHeader.setImageBitmap(bmp);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    businessLogoHeader.setTransitionName(Business.getURI() + businessItem.getBusinessName());
                }
                businessLogoHeader.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
    }

    //Configures the tab layout's listener.
    private void setTabLayout() {

        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                setFabTab(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setFabTab(int position) {
        Log.d(TAG, "setFabTab: starts");
        switch (position) {
            case 1:
                AnimationHelper.hideFab(editButton);
                AnimationHelper.hideFab(addButton);
                AnimationHelper.showFab(addActivityButton);
                break;
            case 0:
                AnimationHelper.hideFab(addActivityButton);
                if(mEditMode.equals("true"))
                    AnimationHelper.showFab(addButton);
                else
                    AnimationHelper.showFab(editButton);


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Sets the actionbar visibility.
    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    //Configures and adds the fragments to the view pager.
    private void setupViewPager(ViewPager viewPager) {
        BusinessViewPagerAdapter adapter = new BusinessViewPagerAdapter(getSupportFragmentManager());


        Bundle bundle = new Bundle();
        if (businessItem != null) {
            bundle.putString(Constants.BUSINESS_ID, businessItem.get_ID());
            bundle.putString(Constants.EDIT_MODE, mEditMode);
        } else {
            bundle.putInt(Constants.BUSINESS_ID, 0);
        }
        businessDetailsFragment.setArguments(bundle);
        businessActivitiesFragment.setArguments(bundle);
        adapter.addFragment(businessDetailsFragment, "Details");
        adapter.addFragment(businessActivitiesFragment, "Activities");
        businessDetailsFragment.setSnackBarView(rootLayout);

        viewPager.setAdapter(adapter);
    }

    //Sets the activity title.
    public void setActivityTitle(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    //For future use.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
