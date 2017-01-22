package com.foodie.app.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.foodie.app.R;
import com.foodie.app.constants.Constants;
import com.foodie.app.ui.view_adapters.AppBarStateChangeListener;

public class ActivityDetails extends AppCompatActivity {
    private static final String TAG = "ActivityDetails";

    private AppBarLayout appBarLayout;
    private EditText dishNameTextView, activityDescriptionTitle;
    private boolean mEditMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        setActionBar();
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        dishNameTextView = (EditText) findViewById(R.id.dishNameTextView);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                setActivityTitle("");
                dishNameTextView.setText("Salmon");

            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                setActivityTitle("Salmon");
                dishNameTextView.setText("");
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {
            }
        });
        activityDescriptionTitle = (EditText) findViewById(R.id.activityDescriptionTitle);
        activityDescriptionTitle.setEnabled(true);
//        activityDescriptionTitle

        mEditMode = getIntent().getBooleanExtra(Constants.EDIT_MODE, false);
        if(mEditMode) {
            dishNameTextView.setEnabled(true);
        }else{
            dishNameTextView.setEnabled(false);

        }


    }

    //Sets the activity title.
    public void setActivityTitle(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    //Sets the actionbar visibility.
    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}
