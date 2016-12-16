package com.foodie.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import android.net.Uri;

import com.foodie.app.entities.User;

/**
 * Created by David on 15/12/2016.
 */


//TODO query option
public class AsyncData<T> extends AsyncTask<ContentValues, Integer, DataStatus> {


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
    protected DataStatus doInBackground(ContentValues... contentValues) {

        if(contentValues.length == 0 || context == null)
            return DataStatus.InvalidArgumment;
        return ( context.getContentResolver().insert(uri,contentValues[0]) != null)?DataStatus.Success:DataStatus.InvalidArgumment;
    }

    @Override protected void onPostExecute(DataStatus complete) {
        super.onPostExecute(complete);

        if(this.callback != null)
        {
            callback.DBstatus(complete);
        }

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
