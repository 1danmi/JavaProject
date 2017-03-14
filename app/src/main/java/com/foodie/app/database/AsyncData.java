package com.foodie.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.listsDB.ContentResolverDatabase;
import com.foodie.app.provider.MyContentProvider;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;

import java.util.List;

/**
 * Created by David on 15/12/2016.
 */

/**
 * This class wil provide the data from the database in a async method
 * @param <T>: Entities used in this project
 */

public class AsyncData<T> extends AsyncTask<Object, Integer, Void> {


    private Uri uri = null;
    private Context context = null;
    private CallBack<T> callback = null;
    private DataManagerType datamanagerType = DataManagerType.Off;

    private String contenProviderErrorMessage = "";
    private String contenProviderErrorType = "";
    List<T> BusinessList;
    public BusinessRecyclerViewAdapter businessRecyclerViewAdapter;


    /**
     * Default constructor
     *
     * @param context : The current context so the class can push data from the content provider
     * @param uri:    The entity uri
     */
    public AsyncData(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;

    }

    /**
     * Constructor
     *
     * @param context         : The current context so the class can push data from the content provider
     * @param uri             : The entity uri
     * @param datamanagerType : The type of operation (insert, query, delete)
     */
    public AsyncData(Context context, Uri uri, DataManagerType datamanagerType) {
        this.context = context;
        this.uri = uri;
        this.datamanagerType = datamanagerType;
    }

    /**
     * @param context:         The current context so the class can push data from the content provider
     * @param uri:             The entity uri
     * @param datamanagerType: The type of operation (insert, query, delete)
     * @param callback         : The function that the class will run after receive the data
     */
    public AsyncData(Context context, Uri uri, DataManagerType datamanagerType, CallBack<T> callback) {
        this.context = context;
        this.uri = uri;
        this.datamanagerType = datamanagerType;
        this.callback = callback;
    }

    /**
     * Default function for async class
     * This function will push the data from the content provider
     *
     * @param objects : The data (new data, query, id to delete)
     */
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
        /**
         * In this swicth the function will check what type of operation the user required,
         * and then check if the data (received) are correct
         */
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
                if (objects[0] instanceof String) {
                    remove((String) objects[0]);
                    DebugHelper.Log("AsyncData: doInBackground -> Delete id " + (String) objects[0]);
                }
                break;

        }

        DebugHelper.Log("AsyncData: doInBackground finish");


        return null;
    }

    /**
     * In case that the user wants to insert new data, this function will insert it by
     * using the content provider and his uri
     * After the operation the function will call the function runCallBack (To run the callback function)
     *
     * @param contentValues : the data to insert
     */
    private void Insert(ContentValues... contentValues) {
        for (ContentValues value : contentValues) {
            Uri u = context.getContentResolver().insert(uri, value);
            if (u != null) {
                ContentResolverDatabase.lastActivityID = u.toString();
                runCallBack(DataStatus.Success, null);
            } else {
                runCallBack(DataStatus.InvalidArgumment, null);
            }
        }
    }

    /**
     * In case that the user wants to remove data, this function will remove it by
     * using the content provider and his uri
     * After the operation the function will call the function runCallBack (To run the callback function)
     *
     * @param id : the id to remove
     */
    private void remove(String id) {

        if (context.getContentResolver().delete(uri, id, null) == 1) {
            runCallBack(DataStatus.Success, null);
        } else {
            runCallBack(DataStatus.Failed, null);
        }

    }

    /**
     * In case that the user wants to get data, this function will get it by
     * using the content provider and his uri
     * After the operation the function will call the function runCallBack (To run the callback function)
     *
     * @param query : An object from the class DBquery where the query is explicated
     */
    private void query(DBquery... query) {
        Cursor result;

        //creating the query to send to the Content Provider
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
                        BusinessList = (List<T>) Converters.cursorToBusinessList(result);
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

    /**
     * In case that the user wants to update data, this function will update it by getting the data and
     * using the content provider and his uri
     * After the operation the function will call the function runCallBack (To run the callback function)
     *
     * @param contentValues
     */
    private void update(ContentValues... contentValues) {
        for (ContentValues value : contentValues) {

            String id = value.getAsString("_ID");

            if (id.equals("")) {
                DebugHelper.Log("AsyncData: invalid id " + id);
                runCallBack(DataStatus.InvalidArgumment, null);
                return;
            }

            if (context.getContentResolver().update(uri, value, id, null) != 0) {
                runCallBack(DataStatus.Success, null);
            } else {
                runCallBack(DataStatus.Failed, null);
            }
        }
    }

    /**
     * Set function
     *
     * @param callBack
     */
    public void setCallBack(CallBack<T> callBack) {
        this.callback = callBack;
    }

    /**
     * Set function
     *
     * @param type
     */
    public void setDatamanagerType(DataManagerType type) {
        datamanagerType = type;
    }

    /**
     * This function will check if the callback function is set (not null) and then run it
     * Ps: The function will be executed in the main thread by using the class 'Handler'
     *
     * @param status : The status of the operation (Success, failed...)
     * @param data   : In case of data required by the user,
     */
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

    /**
     * This  function will get the error from the content provider
     *
     * @param checkProvider : True to check the provider for errors
     * @return the error
     */
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

