package com.foodie.app.provider;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.foodie.app.database.CallBack;
import com.foodie.app.database.DataStatus;
import com.foodie.app.listsDB.ContentResolverDatabase;

import java.util.List;

/**
 * Created by Daniel on 1/19/2017.
 */

public class MyContentObserver extends ContentObserver {
    private static final String TAG = "MyContentObserver";

    private Context mContext;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public MyContentObserver(Handler handler, Context context) {
        super(handler);
        setmContext(context);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        CallBack<Void> callBack = new CallBack<Void>() {
            @Override
            public void onSuccess(List<Void> data) {
                Log.d(TAG, "onSuccess: success");
            }

            @Override
            public void onFailed(DataStatus status, String error) {

            }
        };
        ContentResolverDatabase.getBusinessesList(mContext, callBack);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
