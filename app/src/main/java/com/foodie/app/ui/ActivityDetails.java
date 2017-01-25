package com.foodie.app.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityDetails extends AppCompatActivity {
    private static final String TAG = "ActivityDetails";

    private boolean debug = true;

    //Dish values
    private com.foodie.app.entities.Activity activityItem;
    private String activityID, businessID, businessName;
    private double mPrice;
    private boolean mEditMode, isPhotoChanged, isPriceChanged;


    //Views:
    private CoordinatorLayout rootLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ImageView dishImage;
    private FloatingActionButton addEditActivityBtn;
    private EditText dishNameEditText, featureEditText, dishDescription;
    private RatingBar ratingBar;
    private TextView dishBusinessName, ratingBarText, mNumOfVotes;
    private Button priceBtn;
    private RecyclerView suggestionRecyclerView;

    Bitmap doneBitmap, editBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initializeViews();

        mEditMode = getIntent().getBooleanExtra(Constants.EDIT_MODE, false);
        activityID = getIntent().getStringExtra(Constants.ACTIVITY_ID);
        businessID = getIntent().getStringExtra(Constants.BUSINESS_ID);
        businessName = getIntent().getStringExtra(Constants.BUSINESS_NAME);

        setEditMode(mEditMode);

        setFabs();

        setAppBar();


        if (!activityID.equals("")) {
            DBquery dBquery = new DBquery(new String[]{AppContract.Activity.ACTIVITY_ID}, new String[]{activityID});
            (new AsyncData<>(getApplicationContext(), com.foodie.app.entities.Activity.getURI(), DataManagerType.Query, new CallBack<com.foodie.app.entities.Activity>() {
                @Override
                public void onSuccess(List<com.foodie.app.entities.Activity> data) {
                    DebugHelper.Log("inflateData: Success");
                    if (data != null) {
                        DebugHelper.Log("inflateData: first data = " + data.get(0).get_ID());
                        activityItem = data.get(0);
                        setData(activityID);
                    }

                }

                @Override
                public void onFailed(DataStatus status, String error) {
                    DebugHelper.Log("inflateData: " + status + ": " + error);
                }


            })).execute(dBquery);
        } else {
            setData(activityID);
        }


        dishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditMode) {
                    Log.d(TAG, "pickImage: starts");
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, Constants.PICK_ACTIVITY_PHOTO);
                }
            }
        });

        priceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPrice();
            }
        });
    }

    private void setPrice() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityDetails.this, R.style.MyAlertDialogStyle);
        alert.setTitle("Enter dish price");
        alert.setPositiveButton("OK", null);
        alert.setNegativeButton("Cancel", null);
        // Create EditText box to input repeat number
        final EditText input = new EditText(ActivityDetails.this);

        input.setHint("Dish price");
        input.setInputType(InputType.TYPE_CLASS_NUMBER |  InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setTextColor(getResources().getColor(R.color.white));

        alert.setView(input, 60, 0, 60, 0);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Pattern pattern =
                                Pattern.compile("^([1-9]{1}\\d{0,3}(|\\.\\d{2}))$");
                        String price = input.getText().toString().trim();
                        Matcher matcher =
                                pattern.matcher(price);
                        if (matcher.find()) {
                            mPrice = Double.parseDouble(price);
                            priceBtn.setText(price + "$");
                        } else {
                            Snackbar snackbar = Snackbar.make(rootLayout, "Price has to be a number!", Snackbar.LENGTH_LONG).setAction("Try again",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            setPrice();
                                        }
                                    });
                            snackbar.setActionTextColor(getResources().getColor(R.color.accent));
                            snackbar.show();

                        }
                    }
                }

        );
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing
                    }
                }

        );
        alert.show();
    }

    private void setAppBar() {
        setSupportActionBar(toolbar);
        setActionBar();
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
//                setActivityTitle("");
            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
//                setActivityTitle(dishNameEditText.getText().toString());
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {
            }
        });
    }

    private void setFabs() {

        if (mEditMode) {
            AnimationHelper.hideFab(addEditActivityBtn);
            addEditActivityBtn.setImageBitmap(doneBitmap);
            AnimationHelper.showFab(addEditActivityBtn);

        } else {
            AnimationHelper.hideFab(addEditActivityBtn);
            addEditActivityBtn.setImageBitmap(editBitmap);
            AnimationHelper.showFab(addEditActivityBtn);
        }

        addEditActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditMode) {
                    if (inputCheck()) {
                        AnimationHelper.hideFab(addEditActivityBtn);
                        addEditActivityBtn.setImageBitmap(editBitmap);
                        AnimationHelper.showFab(addEditActivityBtn);
                        setEditMode(false);
                        setTitle(dishNameEditText.getText().toString().trim());
                        if(addActivity()) {
                            Snackbar.make(rootLayout, "Success", Snackbar.LENGTH_LONG).show();
                        }else{
                            Snackbar.make(rootLayout, "Failed", Snackbar.LENGTH_LONG).show();
                        }
                    }
                } else {
                    AnimationHelper.hideFab(addEditActivityBtn);
                    addEditActivityBtn.setImageBitmap(doneBitmap);
                    AnimationHelper.showFab(addEditActivityBtn);
                    setEditMode(true);
                }
            }
        });
    }

    private boolean addActivity() {
        try {
            activityItem.setActivityName(dishNameEditText.getText().toString().trim());
            activityItem.setActivityDescription(dishDescription.getText().toString().trim());
            activityItem.setActivityCost(mPrice);
            activityItem.setActivityRating(3.5);
            activityItem.setBusinessId(businessID);
            activityItem.setFeature(featureEditText.getText().toString().trim());

            CallBack<com.foodie.app.entities.Activity> callBack = new CallBack<com.foodie.app.entities.Activity>() {
                @Override
                public void onSuccess(List<com.foodie.app.entities.Activity> data) {
                    DebugHelper.Log("Business insert callBack finish with status: Success");
                }

                @Override
                public void onFailed(DataStatus status, String error) {

                }


            };
            (new AsyncData<>(getApplicationContext(), com.foodie.app.entities.Activity.getURI(), DataManagerType.Insert, callBack)).execute(activityItem.toContentValues());
        } catch (Exception e) {
            Snackbar.make(rootLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
        return true;
    }

    private boolean inputCheck() {
        Snackbar snackbar = Snackbar.make(rootLayout, "You have to choose a photo!", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));

        if (debug)
            return true;
        if (!isPhotoChanged) {
//            snackbar = Snackbar.make(rootLayout, "You have to choose a photo!", Snackbar.LENGTH_LONG);
//            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
            return false;
        } else if (dishNameEditText.getText().toString().trim().length() == 0) {
//            snackbar = Snackbar.make(rootLayout, "You have to fill a title!", Snackbar.LENGTH_LONG);
//            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.setText("You have to fill a title!");
            snackbar.show();
            return false;
        } else if (featureEditText.getText().toString().trim().length() == 0) {
//            snackbar = Snackbar.make(rootLayout, "You have to fill a feature!", Snackbar.LENGTH_LONG);
//            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.setText("You have to fill a feature!");
            snackbar.show();
            return false;
        } else if (dishDescription.getText().toString().trim().length() == 0) {
//            snackbar = Snackbar.make(rootLayout, "You have to fill a description!", Snackbar.LENGTH_LONG);
//            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.setText("You have to fill a description!");
            snackbar.show();
            return false;
        } else if (!isPriceChanged || priceBtn.getText().toString().trim().length() == 0 || priceBtn.getText().toString().trim().equals("PRICE")) {
            snackbar.setText("You have to set a price!");
            snackbar.show();
        }
        return true;
    }

    private void initializeViews() {
        //Initialize views:
        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_details_root_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dishImage = (ImageView) findViewById(R.id.dish_image);
        addEditActivityBtn = (FloatingActionButton) findViewById(R.id.add_edit_activity_btn);

        dishNameEditText = (EditText) findViewById(R.id.dish_name);
        ratingBar = (RatingBar) findViewById(R.id.activity_rating_bar);
        ratingBarText = (TextView) findViewById(R.id.dish_rating_bar_text);
        dishBusinessName = (TextView) findViewById(R.id.dish_business_name);
        featureEditText = (EditText) findViewById(R.id.dish_feature);
        priceBtn = (Button) findViewById(R.id.price_button);
        dishDescription = (EditText) findViewById(R.id.dish_description_text_view);
        suggestionRecyclerView = (RecyclerView) findViewById(R.id.suggestion_recycler_view);

        doneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white);
        editBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mode_edit_white_48dp);
    }


    private void setEditMode(boolean b) {
        mEditMode = b;
        if (b) {
            dishNameEditText.setEnabled(true);
            featureEditText.setEnabled(true);
            dishDescription.setEnabled(true);

        } else {
            dishNameEditText.setEnabled(false);
            featureEditText.setEnabled(false);
            dishDescription.setEnabled(false);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_ACTIVITY_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Snackbar.make(rootLayout, "No picture was selected", Snackbar.LENGTH_LONG).show();
                return;
            }
            try {

                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                bmp = Bitmap.createScaledBitmap(bmp, 400, 225, true);
                dishImage.setImageBitmap(bmp);
                dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] logo = stream.toByteArray();
                activityItem.setActivityImage(logo);
                isPhotoChanged = true;
                isPriceChanged = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
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

    private void setData(String activityID) {
        if (activityID.equals("")) {
            setTitle("");
            dishBusinessName.setText(businessName);
            activityItem = new com.foodie.app.entities.Activity();
            isPhotoChanged = false;
            isPriceChanged = false;
        } else {
            setTitle(activityItem.getActivityName());
            dishNameEditText.setText(activityItem.getActivityName());
            featureEditText.setText(activityItem.getActivityDescription());
            dishDescription.setText(activityItem.getActivityDescription());
            ratingBar.setRating((float) activityItem.getActivityRating());
            ratingBarText.setText(Double.toString(activityItem.getActivityRating()));
            dishBusinessName.setText(businessName);
            priceBtn.setText(Double.toString(activityItem.getActivityCost()));
            isPhotoChanged = true;
            isPriceChanged = true;
            if (activityItem.getActivityImage() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(activityItem.getActivityImage(), 0, activityItem.getActivityImage().length);
                dishImage.setImageBitmap(bmp);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dishImage.setTransitionName(Business.getURI() + activityItem.getActivityName());
                }
            }
        }
    }
}
