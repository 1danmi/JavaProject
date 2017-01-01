package com.foodie.app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.backend.AppContract;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBquery;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Business;
import com.foodie.app.ui.view_adapters.AppBarStateChangeListener;
import com.foodie.app.ui.view_adapters.BusinessViewPagerAdapter;

import java.util.List;

public class ActivitiesActivity extends AppCompatActivity {

    private static final String BUSINESS_ID = "businessId";
    private static final String TAG = "ActivitiesActivity";
    //private static CoordinatorLayout rootLayout;
    public static Business businessItem;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    //private CardView businessLogoCardView;
    private ImageView businessLogoHeader;
    private TextView businessNameHeader;
    private static final String EDIT_MODE = "mEditKey";
    private String editMode;
    public Boolean isPhotoChanged;
    private int businessID;
    private BusinessDetailsFragment businessDetailsFragment;
    private BusinessActivitiesFragment businessActivitiesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activities);

        initializeComponents();

        initializeViews();

        setActionBar();

        inflateData();

        setAppBar();

    }

    private void initializeComponents() {
        Intent intent = getIntent();

        businessID = intent.getIntExtra(BUSINESS_ID, 0);

        editMode = intent.getStringExtra(EDIT_MODE);

        businessDetailsFragment = new BusinessDetailsFragment();

        businessActivitiesFragment = new BusinessActivitiesFragment();
    }

    //Initializes the views
    private void initializeViews() {
        //rootLayout = (CoordinatorLayout) findViewById(R.id.activities_activity_layout);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        appBarLayout = (AppBarLayout) findViewById(R.id.business_name_app_bar);
        businessLogoHeader = (ImageView) findViewById(R.id.business_header_image);
        businessNameHeader = (TextView) findViewById(R.id.business_header_name);
        //businessLogoCardView = (CardView) findViewById(R.id.business_header_card_view);
    }

    //Sets the appbar listener to hide the title while collapsed.
    private void setAppBar() {

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                setActivityTitle("");
            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                setActivityTitle(businessItem.getBusinessName());
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {
            }
        });
    }

    //Inflates the business date from the database.
    private void inflateData() {

        if (businessID != 0) {
            DBquery dBquery = new DBquery(new String[]{AppContract.Business.BUSINESS_ID},new String[]{Integer.toString(businessID)});
            (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Query, new CallBack<Business>() {
                @Override
                public void run(DataStatus status, List<Business> data) {
                    if(data!=null) {
                        businessItem = data.get(0);
                        //DebugHelper.Log("Da"));
                        setData(businessID);
                        setTabLayout();
                        businessDetailsFragment.inflateData();

                    }
                }
            })).execute(dBquery);
        }else{
            businessItem = new Business();
            setTabLayout();
            businessDetailsFragment.inflateData();


        }


    }

    private void setData(int businessID) {
        if (businessID == 0) {
            businessItem = new Business();
            isPhotoChanged = false;
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(businessItem.getBusinessLogo(), 0, businessItem.getBusinessLogo().length);
            businessLogoHeader.setImageBitmap(bmp);
            businessNameHeader.setText(businessItem.getBusinessName());
            businessLogoHeader.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    //Configures the tab layout's listener.
    private void setTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //Sets the actionbar visibility.
    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
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
            bundle.putInt(BUSINESS_ID, businessItem.get_ID());
            bundle.putString(EDIT_MODE, editMode);
        } else {
            bundle.putInt(BUSINESS_ID, 0);
        }
        businessDetailsFragment.setArguments(bundle);
        businessActivitiesFragment.setArguments(bundle);
        adapter.addFragment(businessDetailsFragment, "Details");
        adapter.addFragment(businessActivitiesFragment, "Activities");

        viewPager.setAdapter(adapter);
    }

    //Set the activity title.
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
