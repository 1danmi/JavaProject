package com.foodie.app.ui.view_adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Daniel on 12/21/2016.
 */

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private static final String TAG = "BusinessRecyclerItemCli";

    public interface onRecyclerClickListener {
        void onItemClick(View v, int position, MotionEvent e);

    }

    private final onRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, final onRecyclerClickListener mListener) {
        this.mListener = mListener;
                mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onSingelTapEx(e, recyclerView, mListener);
                return true;
            }
        });

    }

    protected void onSingelTapEx(MotionEvent e, RecyclerView recyclerView, onRecyclerClickListener mListener) {
        Log.d(TAG, "onSingleTapUp: starts");
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null) {
            mListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView), e);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if (mGestureDetector != null) {
            boolean result = mGestureDetector.onTouchEvent(e);
            return result;
        } else {
            return false;
        }

    }

    public static boolean isViewClicked(View view, MotionEvent e) {
        Rect rect = new Rect();

        view.getGlobalVisibleRect(rect);

        return rect.contains((int) e.getRawX(), (int) e.getRawY());
    }
}
