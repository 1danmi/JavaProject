package com.foodie.app.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.foodie.app.R;

public class SettingActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private Switch switchSwitch;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_setting);
        switchSwitch = (Switch) findViewById(R.id.switch1);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        switchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchSwitch.setClickable(false);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No we don't!", Snackbar.LENGTH_SHORT);
                    snackbar.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            switchSwitch.toggle();
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {

                        }
                    });
                    snackbar.show();
                }else{
                    switchSwitch.setClickable(true);
                }
            }

        });

        setAppBar();

        setTitle("Settings");
    }

    /**
     *
     */
    private void setAppBar() {
        setSupportActionBar(toolbar);
        setActionBar();
    }

    /**
     * Sets the actionbar visibility.
     */
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

    /**
     *
     * @param item
     * @return
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
}
