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
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.entities.Business;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;
import com.foodie.app.ui.view_adapters.RecyclerItemClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BusinessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemClickListener.onRecyclerClickListener {

    private static final String TAG = "BusinessActivity";
    BusinessRecyclerViewAdapter businessRecyclerViewAdapter;
    private static final String BUSINESS_DETAILS = "businessDetails";
    private static final String BUSINESS_ID = "businessId";
    private static final String KEY_MODE = "mEditKey";
    public static List<Business> businessList;
    private RecyclerView recyclerView;
    private FloatingActionButton addBusinessFAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setExitTransition(new Explode());
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Log.d(TAG, "onCreate: starts");

        Toolbar toolbar = setActionBarAndFAB();

        setDrawer(toolbar);

        businessList = loadDemoData();

        setRecyclerView();



    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.business_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            //Do nothingh to not back to login
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

    public List<Business> loadDemoData() {

        businessList = new ArrayList<>();
        Business demo;


        try {

            String name1 = "Burgeranch ";


            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.burgeranch);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 1000, 800, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo1 = stream.toByteArray();

            demo = new Business(name1, "Derech Agudat Sport Beitar 1, Jerusalem, 9695235", "0543051733", "Burgeranch@burgeranch.co.il", "burgeranch.co.il", 1, logo1);
            businessList.add(demo);

            String name2 = "McDonald's ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mcdonalds_logo);
            stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 1000, 800, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo2 = stream.toByteArray();

            demo = new Business(name2, "Sderot Yitshak Rabin 10, Jerusalem, 1234558", "0543051733", "McDonald@mcdonald.com", "mcdonald.com", 2, logo2);
            businessList.add(demo);

            String name3 = "Duda Lapizza ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.duda_lapizza_logo);
            stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 1000, 800, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo3 = stream.toByteArray();

            demo = new Business(name3, "Sderot Hatsvi 5, Jerusalem, 6546185", "0543051733", "duda@lapizza.com", "duda-lapizza.com", 3, logo3);
            businessList.add(demo);


            String name4 = "Pizza Hut ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pizza_hut_logo);
            stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 1000, 800, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo4 = stream.toByteArray();

            demo = new Business(name4, "Nayot 9, Jerusalem, 6546185", "0543051733", "pizza@pizza-hut.com", "pizza-hut.com", 4, logo4);
            businessList.add(demo);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return businessList;
    }



    @Override
    public void onitemClick(View v, int position) {
        try {
            Intent intent = new Intent(this, ActivitiesActivity.class);

            intent.putExtra(BUSINESS_ID, businessList.get(position).get_ID());
            intent.putExtra(KEY_MODE, "false");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
            else{
                startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //Snackbar.make(v,"Item at position " + position + " had been clicked", Snackbar.LENGTH_LONG).show();
    }



}
