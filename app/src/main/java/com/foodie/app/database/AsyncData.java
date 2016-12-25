package com.foodie.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.foodie.app.DebugHelper.DebugHelper;
import com.foodie.app.backend.AppContract;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;
import com.foodie.app.ui.LoginActivity;
import com.foodie.app.ui.MainActivity;

import java.util.List;

/**
 * Created by David on 15/12/2016.
 */


//TODO query option
public class AsyncData<T> extends AsyncTask<Object, Integer, Void>{


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
    protected Void doInBackground(Object... objects) {

        DebugHelper.Log("AsyncData: doInBackground with type: "+datamanagerType.toString());
        if(objects.length == 0 || context == null) {
            runCallBack(DataStatus.InvalidArgumment,null);
            DebugHelper.Log("AsyncData: Total of Objects: " + objects.length +( (context == null)?" and context is null":"") );
        }

        switch (datamanagerType) {
            case Off:
                break;
            case Insert:
                if(objects[0] instanceof ContentValues)
                    if(objects.length>1)
                        Insert((ContentValues[])objects);
                    else
                        Insert((ContentValues)objects[0]);
                else {
                    DebugHelper.Log("AsyncData: object is not a ContentValues type");
                    runCallBack(DataStatus.InvalidArgumment, null);
                }
                break;
            case Query:
                break;
            case Delete:
                break;
            case login:
                if(objects[0] instanceof DBquery)
                    if(objects.length>1)
                        login((DBquery[])objects);
                    else
                        login((DBquery)objects[0]);
                else {
                    DebugHelper.Log("AsyncData: object is not a DBquery type");
                    runCallBack(DataStatus.InvalidArgumment, null);
                }
                break;
        }

        DebugHelper.Log("AsyncData: doInBackground finish");

        return null;
    }

    private void Insert(ContentValues... contentValues)
    {
        for (ContentValues value : contentValues ) {
            if (context.getContentResolver().insert(uri, value) != null) {
                runCallBack(DataStatus.Success, null);
            } else {
                runCallBack(DataStatus.InvalidArgumment, null);
            }
        }
    }

    private void login(DBquery... login)
    {

        String username = login[0].getLogin()[0];
        String psw = login[0].getLogin()[1];

        if(username == null || psw == null)
        {
            DebugHelper.Log("AsyncData: Invalid username or password");

            runCallBack(DataStatus.InvalidArgumment,null);
            return;
        }

        Cursor result = context.getContentResolver().query(uri,new String[]{AppContract.CPUser.CPUSER_EMAIL,AppContract.CPUser.CPUSER_PWD},null,new String[]{username,psw},null);
        List<CPUser> total = Converters.cursorToCPUserList(result);

        if(total.size()>0) {
            DebugHelper.Log("AsyncData: Username: " + (total.get(0).getUserFullName()));
            runCallBack(DataStatus.Success,null);
        }
        else {
            runCallBack(DataStatus.Failed,null);
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

    private void runCallBack(final DataStatus status, final T... data)
    {
        if(callback == null) {
            DebugHelper.Log("AsyncData: callBack is null");
            return;
        }

        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler .post(new Runnable() {
            @Override
            public void run() {
                callback.DBstatus(status,data);
            }
        });

    }

}