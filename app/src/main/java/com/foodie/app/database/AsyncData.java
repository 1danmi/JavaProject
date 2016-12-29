package com.foodie.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.Helper.HelperClass;
import com.foodie.app.backend.AppContract;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by David on 15/12/2016.
 */



public class AsyncData<T> extends AsyncTask<Object, Integer, Void>{


    private Uri uri = null;
    private Context context = null;
    private CallBack<T> callback = null;
    private DataManagerType datamanagerType = DataManagerType.Off;


    public AsyncData(Context context,Uri uri)
    {
        this.context = context;
        this.uri = uri;

    }

    public AsyncData(Context context,Uri uri,DataManagerType datamanagerType)
    {
        this.context = context;
        this.uri = uri;
        this.datamanagerType = datamanagerType;
    }

    public AsyncData(Context context,Uri uri,DataManagerType datamanagerType, CallBack<T> callback)
    {
        this.context = context;
        this.uri = uri;
        this.datamanagerType = datamanagerType;
        this.callback = callback;
    }


    @Override
    protected Void doInBackground(Object... objects) {



        DebugHelper.Log("AsyncData: doInBackground with type: "+datamanagerType.toString());

        if(uri == null) {
            runCallBack(DataStatus.InvalidArgumment,null);
            DebugHelper.Log("AsyncData: Uri is null");
        }

        if(context == null)
        {
            runCallBack(DataStatus.InvalidArgumment,null);
            DebugHelper.Log("AsyncData: Context is null" );

        }

        if(objects.length == 0) {
            runCallBack(DataStatus.InvalidArgumment,null);
            DebugHelper.Log("AsyncData: Total of Objects: " + objects.length );
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
                if(objects[0] instanceof DBquery)
                    if(objects.length>1)
                        query((DBquery[])objects);
                    else
                        query((DBquery)objects[0]);
                else {
                    DebugHelper.Log("AsyncData: object is not a DBquery type");
                    runCallBack(DataStatus.InvalidArgumment, null);
                }
                break;
            case Delete:
                if(objects[0] instanceof String)
                   remove((String)objects[0]);
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
            DebugHelper.Log("AsyncData login: Invalid username or password");

            runCallBack(DataStatus.InvalidArgumment,null);
            return;
        }

        String uriType = uri.getLastPathSegment();




        if(uriType.equals("cpuser")) {
            Cursor result = context.getContentResolver().query(uri, new String[]{AppContract.CPUser.CPUSER_EMAIL, AppContract.CPUser.CPUSER_PWD}, null, new String[]{username, psw}, null);
            List<CPUser> total = Converters.cursorToCPUserList(result);

            if (total.size() > 0) {
                DebugHelper.Log("AsyncData login: Username: " + (total.get(0).getUserFullName()));
                runCallBack(DataStatus.Success, (List<T>) total);
            } else {
                runCallBack(DataStatus.Failed, null);
            }

        }else if (uriType.equals("user"))
        {
            Cursor result = context.getContentResolver().query(uri, new String[]{AppContract.User.USER_EMAIL, AppContract.User.USER_PWD}, null, new String[]{username, psw}, null);
            List<User> total = Converters.cursorToUserList(result);

            if (total.size() > 0) {
                DebugHelper.Log("AsyncData login: Username: " + (total.get(0).getUserFullName()));
                runCallBack(DataStatus.Success, null);
            } else {
                runCallBack(DataStatus.Failed, null);
            }

        }else{
            DebugHelper.Log("AsyncData login: Invalid URI " + uri);
            runCallBack(DataStatus.InvalidArgumment, null);
        }

    }

    private void remove(String id)
    {

        if (context.getContentResolver().delete(uri,id,null ) == 1) {
            runCallBack(DataStatus.Success, null);
        } else {
            runCallBack(DataStatus.Failed, null);
        }

    }

    private void query(DBquery... query)
    {
        Cursor result;
        for (DBquery aQuery : query) {
            result = context.getContentResolver().query(uri, aQuery.getProjection(), aQuery.getSelection(), aQuery.getSelectionArgs(), aQuery.getSortOrder());

            String uriType = uri.getLastPathSegment();
            long id = -1;
            try {
                switch (uriType) {
                    case "user":
                        List<T> UserList = (List<T>) Converters.cursorToUserList(result);
                        runCallBack(DataStatus.Success, UserList);
                        break;


                    case "Business":
                        List<T> BusinessList = (List<T>) Converters.cursorToBusinessList(result);
                        runCallBack(DataStatus.Success, BusinessList);
                        break;


                    case "activity":
                        List<T> ActivityList = (List<T>) Converters.cursorToActivityList(result);
                        runCallBack(DataStatus.Success, ActivityList);
                        break;


                    case "cpuser":
                       List<T> CPUserList = (List<T>) Converters.cursorToCPUserList(result);
                       runCallBack(DataStatus.Success, CPUserList);
                        break;
                }
            } catch (Exception ex) {
                runCallBack(DataStatus.Failed, null);

            }
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

    private void runCallBack(final DataStatus status, final List<T> data)
    {
        if(callback == null) {
            DebugHelper.Log("AsyncData: callBack is null");
            return;
        }

        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler .post(new Runnable() {
            @Override
            public void run() {
                callback.run(status,data);
            }
        });

    }

}