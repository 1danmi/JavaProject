package com.foodie.app.ui.helpers;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * Created by Daniel on 1/18/2017.
 */

public class AnimationHelper {

    //global
    private static final int FAB_ANIM_DURATION = 200;

    public static void hideFab(FloatingActionButton fab) {
        // Only use scale animation if FAB is visible
        if (fab.getVisibility() == View.VISIBLE) {
            // Pivots indicate where the animation begins from
            float pivotX = fab.getPivotX() + fab.getTranslationX();
            float pivotY = fab.getPivotY() + fab.getTranslationY();

            // Animate FAB shrinking
            ScaleAnimation anim = new ScaleAnimation(1, 0, 1, 0, pivotX, pivotY);
            anim.setDuration(FAB_ANIM_DURATION);
            anim.setInterpolator(new DecelerateInterpolator());
            fab.startAnimation(anim);
        }
        fab.setVisibility(View.INVISIBLE);
    }


    public static void showFab(FloatingActionButton fab) {
        show(0, 0, fab);
    }

    public static void show(float translationX, float translationY, FloatingActionButton fab) {

        // Set FAB's translation
        setTranslation(translationX, translationY, fab);

        // Only use scale animation if FAB is hidden
        if (fab.getVisibility() != View.VISIBLE) {
            // Pivots indicate where the animation begins from
            float pivotX = fab.getPivotX() + translationX;
            float pivotY = fab.getPivotY() + translationY;

            ScaleAnimation anim;
            // If pivots are 0, that means the FAB hasn't been drawn yet so just use the
            // center of the FAB
            if (pivotX == 0 || pivotY == 0) {
                anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
            } else {
                anim = new ScaleAnimation(0, 1, 0, 1, pivotX, pivotY);
            }

            // Animate FAB expanding
            anim.setDuration(FAB_ANIM_DURATION);
            anim.setInterpolator(new DecelerateInterpolator());
            fab.startAnimation(anim);
        }
        fab.setVisibility(View.VISIBLE);
    }

    private static void setTranslation(float translationX, float translationY, FloatingActionButton fab) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            fab.animate().setInterpolator(new DecelerateInterpolator()).setDuration(FAB_ANIM_DURATION)
                    .translationX(translationX).translationY(translationY);
        }
    }
}
