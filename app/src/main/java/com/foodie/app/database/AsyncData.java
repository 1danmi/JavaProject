package com.foodie.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.backend.AppContract;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;
import com.foodie.app.provider.MyContentProvider;

import java.util.List;

/**
 * Created by David on 15/12/2016.
 */


public class AsyncData<T> extends AsyncTask<Object, Integer, Void> {


    private Uri uri = null;
    private Context context = null;
    private CallBack<T> callback = null;
    private DataManagerType datamanagerType = DataManagerType.Off;

    private String contenProviderErrorMessage = "";
    private String contenProviderErrorType = "";


    public AsyncData(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;

    }

    public AsyncData(Context context, Uri uri, DataManagerType datamanagerType) {
        this.context = context;
        this.uri = uri;
        this.datamanagerType = datamanagerType;
    }

    public AsyncData(Context context, Uri uri, DataManagerType datamanagerType, CallBack<T> callback) {
        this.context = context;
        this.uri = uri;
        this.datamanagerType = datamanagerType;
        this.callback = callback;
    }


    @Override
    protected Void doInBackground(Object... objects) {


        DebugHelper.Log("AsyncData: doInBackground with type: " + datamanagerType.toString());

        if (uri == null) {
            runCallBack(DataStatus.InvalidArgumment, null);
            DebugHelper.Log("AsyncData: Uri is null");
        }

        if (context == null) {
            runCallBack(DataStatus.InvalidArgumment, null);
            DebugHelper.Log("AsyncData: Context is null");

        }

        if (objects.length == 0) {
            runCallBack(DataStatus.InvalidArgumment, null);
            DebugHelper.Log("AsyncData: Total of Objects: " + objects.length);
        }

        switch (datamanagerType) {
            case Off:
                break;
            case Insert:

                if (objects[0] instanceof ContentValues)
                    if (objects.length > 1)
                        Insert((ContentValues[]) objects);
                    else
                        Insert((ContentValues) objects[0]);
                else {
                    DebugHelper.Log("AsyncData: object is not a ContentValues type");
                    runCallBack(DataStatus.InvalidArgumment, null);
                }
                break;
            case Update:
                if (objects[0] instanceof ContentValues)
                    if (objects.length > 1)
                        update((ContentValues[]) objects);
                    else
                        update((ContentValues) objects[0]);
                else {
                    DebugHelper.Log("AsyncData: object is not a ContentValues type");
                    runCallBack(DataStatus.InvalidArgumment, null);
                }
                break;
            case Query:
                if (objects[0] instanceof DBquery)
                    if (objects.length > 1)
                        query((DBquery[]) objects);
                    else
                        query((DBquery) objects[0]);
                else {
                    DebugHelper.Log("AsyncData: object is not a DBquery type");
                    runCallBack(DataStatus.InvalidArgumment, null);
                }
                break;
            case Delete:
                if (objects[0] instanceof String)
                    remove((String) objects[0]);
                break;

        }

        DebugHelper.Log("AsyncData: doInBackground finish");


        return null;
    }

    private void Insert(ContentValues... contentValues) {
        for (ContentValues value : contentValues) {
            if (context.getContentResolver().insert(uri, value) != null) {
                runCallBack(DataStatus.Success, null);
            } else {
                runCallBack(DataStatus.InvalidArgumment, null);
            }
        }
    }



    private void remove(String id) {

        if (context.getContentResolver().delete(uri, id, null) == 1) {
            runCallBack(DataStatus.Success, null);
        } else {
            runCallBack(DataStatus.Failed, null);
        }

    }

    private void query(DBquery... query) {
        Cursor result;
        for (DBquery aQuery : query) {
            result = context.getContentResolver().query(uri, aQuery.getProjection(), aQuery.getSelection(), aQuery.getSelectionArgs(), aQuery.getSortOrder());

            String uriType = uri.getLastPathSegment();
            long id = -1;
            try {
                switch (uriType) {
                    case "User":
                        List<T> UserList = (List<T>) Converters.cursorToUserList(result);
                        runCallBack(getErrorType(true), UserList);
                        break;


                    case "Business":
                        List<T> BusinessList = (List<T>) Converters.cursorToBusinessList(result);
                        runCallBack(getErrorType(true), BusinessList);
                        break;


                    case "Activity":
                        List<T> ActivityList = (List<T>) Converters.cursorToActivityList(result);
                        runCallBack(getErrorType(true), ActivityList);
                        break;


                    case "Cpuser":
                        List<T> CPUserList = (List<T>) Converters.cursorToCPUserList(result);
                        DebugHelper.Log("Query");
                        runCallBack(getErrorType(true), CPUserList);
                        break;
                }
            } catch (Exception ex) {
                runCallBack(DataStatus.Failed, null);

            }
        }


    }

    private void update(ContentValues... contentValues) {
        for (ContentValues value : contentValues) {

            String id = value.getAsString("_ID");

            if(id.equals("")) {
                DebugHelper.Log("AsyncData: invalid id " + id);
                runCallBack(DataStatus.InvalidArgumment, null);
                return;
            }

            if (context.getContentResolver().update(uri, value,id,null) != 0) {
                runCallBack(DataStatus.Success, null);
            } else {
                runCallBack(DataStatus.Failed, null);
            }
        }
    }


    public void setCallBack(CallBack<T> callBack) {
        this.callback = callBack;
    }

    public void setDatamanagerType(DataManagerType type) {
        datamanagerType = type;
    }

    private void runCallBack(final DataStatus status, final List<T> data) {

        if (callback == null) {
            DebugHelper.Log("AsyncData: callBack is null");
            return;
        }

        final Handler UIHandler = new Handler(Looper.getMainLooper());

        if (status == DataStatus.Success) {
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(data);
                }
            });
        } else {
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailed(status, contenProviderErrorMessage);
                }
            });
        }

    }


    private DataStatus getErrorType(boolean checkProvider) {
        if (checkProvider) {
            contenProviderErrorMessage = MyContentProvider.getLastErrorMessage();
            contenProviderErrorType = MyContentProvider.getLastErrorType();
        }


        if (contenProviderErrorType.contains("NetworkErrorException"))
            return DataStatus.ConectionError;

        if (contenProviderErrorType.contains("NullPointerException"))
            return DataStatus.InvalidArgumment;

        return DataStatus.Success;


    }

}