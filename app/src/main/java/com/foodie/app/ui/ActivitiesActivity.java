package com.foodie.app.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
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

/**
 * The activity that shows the business details and activities.
 */
public class ActivitiesActivity extends AppCompatActivity {

    private static final String TAG = "ActivitiesActivity";
    public static Business businessItem;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private ImageView businessLogoHeader;
    private TabLayout tabLayout;
    private TextView businessNameHeader;
    private String mEditMode;
    public Boolean isPhotoChanged;
    private String businessID;
    private BusinessDetailsFragment businessDetailsFragment;
    private BusinessActivitiesFragment businessActivitiesFragment;
    private FloatingActionButton addButton, editButton, addActivityButton;
    private CoordinatorLayout rootLayout;
    private DisplayMetrics metrics;
    private boolean isBusinessExist;

    /**
     * Creates the activity where the business details and activities are presented.
     * @param savedInstanceState A saves instance of the activity from a previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activities);

        initializeViews();

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        viewPager.setTranslationY(metrics.heightPixels);

        //AccelerateDecelerateInterpolator()
        viewPager.animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(700)
                .setStartDelay(0)
                .translationYBy(-metrics.heightPixels)
                .start();

        initializeComponents();

        setFabs();

        setActionBar();

        inflateData();

        setAppBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * Initializes the views in the activity.
     */
    private void initializeComponents() {
        Intent intent = getIntent();

        businessID = intent.getStringExtra(Constants.BUSINESS_ID);

        mEditMode = intent.getStringExtra(Constants.EDIT_MODE);

        businessDetailsFragment = new BusinessDetailsFragment();

        businessActivitiesFragment = new BusinessActivitiesFragment();

        final View rootView = getLayoutInflater().inflate(R.layout.fragment_business_details, null);
        businessDetailsFragment.initializeViews(rootView);


    }

    /**
     * Handles the floating action buttons's (fabs) clicks.
     */
    private void setFabs() {
        // Setup up active buttons
        if (mEditMode.equals("true")) {
            AnimationHelper.showFab(addButton);
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
                            public void onSuccess(List<Business> data) {
                                DebugHelper.Log("Business insert callBack finish with status: Success");
//                                new ProgressNotification(getApplicationContext()).startNotification(new NotificationCallBack() {
//                                    @Override
//                                    public double progress() {
//                                        UploadStatus current = OnlineStorage.getFileStatusById(null);
//
//                                        if(current != null)
//                                            return current.getProgress();
//
//                                        return 0;
//                                    }
//                                });
                            }

                            @Override
                            public void onFailed(DataStatus status, String error) {
                                DebugHelper.Log("Business insert callBack finish with status: " + status);
                            }


                        };
                        (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Insert, callBack)).execute(businessDetailsFragment.businessItem.toContentValues());
                        Snackbar.make(rootLayout, "Business added successfully!", Snackbar.LENGTH_LONG);
                        AnimationHelper.showFab(editButton);
                        AnimationHelper.hideFab(addButton);
                    } else {
                        CallBack<Business> callBack = new CallBack<Business>() {
                            @Override
                            public void onSuccess(List<Business> data) {

                            }

                            @Override
                            public void onFailed(DataStatus status, String error) {
                                DebugHelper.Log("Business insert callBack finish with status: " + status);
                            }

                        };
                        (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Update, callBack)).execute(businessItem.toContentValues());
                        Snackbar.make(rootLayout, "Update successful!", Snackbar.LENGTH_LONG).show();
                        AnimationHelper.showFab(editButton);
                        AnimationHelper.hideFab(addButton);
                    }
                    businessDetailsFragment.mEditMode = false;
                    mEditMode = "false";
                }
            }
        });

        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityDetails.class);
                intent.putExtra(Constants.ACTIVITY_ID, "");
                intent.putExtra(Constants.EDIT_MODE, true);
                intent.putExtra(Constants.BUSINESS_NAME, businessItem.getBusinessName());
                intent.putExtra(Constants.BUSINESS_ID, businessID);
                AnimationHelper.hideFab(addActivityButton);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ActivitiesActivity.this).toBundle());
                }
            }
        });
    }

    /**
     * Initializes the views
     */
    private void initializeViews() {
        rootLayout = (CoordinatorLayout) findViewById(R.id.business_activities_coordinator_layout);
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

    /**
     * Sets the appbar.
     */
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

    /**
     * Inflates the business date from the database.
     */
    private void inflateData() {

        if (!businessID.equals("")) {
            DBquery dBquery = new DBquery(new String[]{AppContract.Business.BUSINESS_ID}, new String[]{businessID});
            (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Query, new CallBack<Business>() {
                @Override
                public void onSuccess(List<Business> data) {
                    DebugHelper.Log("inflateData: Success");
                    if (data != null) {
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
                    DebugHelper.Log("inflateData: " + status + ": " + error);
                }


            })).execute(dBquery);
        } else {
            businessItem = new Business();
            setData(businessID);
            setTabLayout();
            businessDetailsFragment.inflateData();

        }


    }

    /**
     * Sets the business details data.
     * @param businessID The business ID.
     */
    private void setData(String businessID) {
        if (businessID.equals("")) {
            //businessItem = new Business();
            isPhotoChanged = false;
            isBusinessExist = false;
        } else {
            businessNameHeader.setText(businessItem.getBusinessName());
            isPhotoChanged = true;
            isBusinessExist = true;
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

    /**
     * Configures the tab layout's listener.
     */
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

    /**
     * Sets the fab's animation on tab transition.
     * @param position The tab number.
     */
    private void setFabTab(int position) {
        Log.d(TAG, "setFabTab: starts");
        switch (position) {
            case 1:
                AnimationHelper.hideFab(editButton);
                AnimationHelper.hideFab(addButton);
                AnimationHelper.hideFab(addActivityButton);
                if (isBusinessExist && mEditMode.equals("false"))
                    AnimationHelper.showFab(addActivityButton);
                break;
            case 0:
                AnimationHelper.hideFab(addActivityButton);
                if (mEditMode.equals("true"))
                    AnimationHelper.showFab(addButton);
                else
                    AnimationHelper.showFab(editButton);
        }
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     *         proceed, true to consume it here.
     */
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

    /**
     * Sets the actionbar visibility.
     */
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

    /**
     * Configures and adds the fragments to the view pager.
     * @param viewPager The activity's viewpager.
     */
    private void setupViewPager(ViewPager viewPager) {
        BusinessViewPagerAdapter adapter = new BusinessViewPagerAdapter(getSupportFragmentManager());


        Bundle bundle = new Bundle();
        if (businessItem != null) {
            bundle.putString(Constants.BUSINESS_ID, businessItem.get_ID());
            bundle.putString(Constants.EDIT_MODE, mEditMode);
            bundle.putString(Constants.BUSINESS_NAME, businessItem.getBusinessName());
        } else {
            bundle.putInt(Constants.BUSINESS_ID, 0);
        }
        businessDetailsFragment.setArguments(bundle);
        businessActivitiesFragment.setArguments(bundle);
        adapter.addFragment(businessDetailsFragment, "Details");
        adapter.addFragment(businessActivitiesFragment, "Menu");
        businessDetailsFragment.setSnackBarView(rootLayout);

        viewPager.setAdapter(adapter);
    }

    /**
     * Sets the activity title.
     * @param title The business name (used for the title).
     */
    public void setActivityTitle(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    /**
     *  Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     * Also handles the view pager exit animation.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewPager.animate().setInterpolator(new LinearInterpolator())
                .setDuration(700)
                .setStartDelay(0)
                .translationYBy(metrics.heightPixels)
                .start();

    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (tabLayout.getSelectedTabPosition() == 1)
            AnimationHelper.showFab(addActivityButton);

    }

}
