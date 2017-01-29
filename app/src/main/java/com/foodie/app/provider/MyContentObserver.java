package com.foodie.app.provider;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.foodie.app.listsDB.ContentResolverDatabase;

/**
 * Created by Daniel on 1/19/2017.
 */

public class MyContentObserver extends ContentObserver {


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
        ContentResolverDatabase.getBusinessesList(mContext);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
