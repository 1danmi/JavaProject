package com.foodie.app.ui.view_adapters;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.github.jorgecastilloprz.FABProgressCircle;

import java.util.List;

/**
 * Created by Daniel on 12/26/2016.
 */

public class FABProgressCircleBehavior extends CoordinatorLayout.Behavior<FABProgressCircle> {

    //Default constructor.
    public FABProgressCircleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Configure the snackbar as dependency for the layout.
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FABProgressCircle child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }


    //Called when the snackbar appears.
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FABProgressCircle child, View dependency) {

        float translationY = getFabTranslationYForSnackbar(parent, child);
        child.setTranslationY(translationY);
        return false;
    }

    //Calculates the new Y axis position of the FABProgressCircle.
    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                FABProgressCircle fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}
