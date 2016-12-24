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
import com.foodie.app.entities.Business;
import com.foodie.app.ui.view_adapters.AppBarStateChangeListener;
import com.foodie.app.ui.view_adapters.BusinessViewPagerAdapter;

public class ActivitiesActivity extends AppCompatActivity {

    private static final String TAG = "ActivitiesActivity";
    private static Business businessItem;
    private static final String BUSINESS_DETAILS = "businessDetails";
    private static final String BUSINESS_ID = "businessId";
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private ImageView businessLogoHeader;
    private TextView businessNameHeader;
    private TextView businessActivitiesHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activities);

        initializeViews();

        setActionBar();

        setTabLayout();


        inflateData();


        setAppBar();


    }

    private void initializeViews() {
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        appBarLayout = (AppBarLayout) findViewById(R.id.business_name_app_bar);
        businessLogoHeader = (ImageView) findViewById(R.id.business_header_image);
        businessNameHeader = (TextView) findViewById(R.id.business_header_name);
        businessActivitiesHeader = (TextView) findViewById(R.id.business_header_activity);
    }

    private void setAppBar() {

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                //Log.e("AppBar","->Expanded");
                setActivityTitle("");
                //mFavoriteButton.show();
            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                //Log.e("AppBar","->Collapsed");
                setActivityTitle(businessItem.getBusinessName());
                //mFavoriteButton.hide();
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {
                //Log.e("AppBar","->Idle");
                //setActivityTitle("");
            }
        });
    }

    private void inflateData() {
        Intent intent = getIntent();
        int businesdID = 0;
        businesdID = (int) intent.getSerializableExtra(BUSINESS_ID);
        if (businesdID != 0) {
            for (Business business : BusinessActivity.businessList) {
                if (business.get_ID() == businesdID) {
                    businessItem = business;
                }
            }
        }
        if (businessItem == null) {
            //Toast.makeText(this, "Business does not exist", Toast.LENGTH_SHORT).show();
            businessItem = new Business();
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(businessItem.getBusinessLogo(), 0, businessItem.getBusinessLogo().length);
            businessLogoHeader.setImageBitmap(bmp);
            businessNameHeader.setText(businessItem.getBusinessName());
        }
    }

    private void setTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        setTitle("BusinessName");
    }

    private void setupViewPager(ViewPager viewPager) {
        BusinessViewPagerAdapter adapter = new BusinessViewPagerAdapter(getSupportFragmentManager());

        //TODO: create bundles.
        adapter.addFragment(new BusinessDetailsFragment(), "Details");
        adapter.addFragment(new BusinessActivitiesFragment(), "Activities");


        viewPager.setAdapter(adapter);
    }

    public void setActivityTitle(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

}
