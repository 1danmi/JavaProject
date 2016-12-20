package com.foodie.app.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.foodie.app.R;
import com.foodie.app.ui.view_adapters.BusinessViewPagerAdapter;

public class ActivitiesActivity extends AppCompatActivity {


    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("BusinessName");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(R.color.gray100, R.color.black);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.dark_gray));
        tabLayout.setSelectedTabIndicatorHeight(2);




    }
    private void setupViewPager(ViewPager viewPager){
        BusinessViewPagerAdapter adapter = new BusinessViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BusinessActivitiesFragment(), "Activities");

        viewPager.setAdapter(adapter);
    }




}
