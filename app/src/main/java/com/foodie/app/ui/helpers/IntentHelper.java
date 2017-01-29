package com.foodie.app.ui.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.foodie.app.R;
import com.foodie.app.constants.Constants;
import com.foodie.app.entities.Business;
import com.foodie.app.ui.ActivitiesActivity;

import static com.foodie.app.constants.Constants.EDIT_MODE;

/**
 * Created by Daniel on 1/17/2017.
 */

public class IntentHelper {

    public static void startActivitiesActivity(Activity a, View background, Business businessItem, String edit) {
        Intent intent = new Intent(a, ActivitiesActivity.class);
        intent.putExtra(Constants.BUSINESS_ID, businessItem.get_ID());
        intent.putExtra(EDIT_MODE, edit);

        ActivityOptionsCompat options;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptionsCompat.makeScaleUpAnimation(
                    background,
                    (int) background.getX(), (int) background.getY(),
                    background.getMeasuredWidth(), background.getMeasuredHeight());
        } else {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(a, Pair.create(background, a.getString(R.string.transition_collection_background)));
        }
        ActivityCompat.startActivity(a, intent, options.toBundle());
    }

}
