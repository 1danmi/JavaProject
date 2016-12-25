package com.foodie.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.foodie.app.DebugHelper.DebugHelper;

/**
 * Created by David on 15/12/2016.
 */


//TODO query option
public class AsyncData<T> extends AsyncTask<ContentValues, Integer, Void> {


    protected Uri uri = null;
    protected Context context = null;
    protected CallBack<T> callback = null;
    protected DataManagerType datamanagerType = DataManagerType.Off;


    public AsyncData(Context context,Uri uri)
    {
        this.context = context;
        this.uri = uri;
    }


    @Override
    protected Void doInBackground(ContentValues... contentValues) {

        DebugHelper.Log("doInBackground");
        if(contentValues.length == 0 || context == null) {

            callback.DBstatus(DataStatus.InvalidArgumment,null);
            DebugHelper.Log("contentValues empty");

        }

        switch (datamanagerType) {
            case Off:
                break;
            case Insert:
                Insert(contentValues);
                break;
            case Query:
                break;
            case Delete:
                break;
            case login:
                break;
        }

        DebugHelper.Log("Finish");

        return null;
    }

    private void Insert(ContentValues... contentValues)
    {
        for (ContentValues value : contentValues ) {

            if(callback != null) {
                DebugHelper.Log("value");

                if(context.getContentResolver().insert(uri,value) != null) {
                    final Handler UIHandler = new Handler(Looper.getMainLooper());
                    UIHandler .post(new Runnable() {
                        @Override
                        public void run() {
                            callback.DBstatus(DataStatus.Success,null);
                        }
                    });
                }
                else {

                    callback.DBstatus(DataStatus.InvalidArgumment,null);
                }
            }else
                DebugHelper.Log("Callback is null");
        }
    }

    private void login(ContentValues... contentValues)
    {
        /*

        if(callback != null) {
            DebugHelper.Log("value");

            if(context.getContentResolver().query(uri,contentValues[0]) != null) {
                final Handler UIHandler = new Handler(Looper.getMainLooper());
                UIHandler .post(new Runnable() {
                    @Override
                    public void run() {
                        callback.DBstatus(DataStatus.Success,null);
                    }
                });
            }
            else {

                callback.DBstatus(DataStatus.InvalidArgumment,null);
            }
        }else
            DebugHelper.Log("Callback is null");
            */


    }


    public void setCallBack(CallBack<T> callBack)
    {
        this.callback = callBack;
    }

    public void setDatamanagerType(DataManagerType type)
    {
        datamanagerType = type;
    }


}