package com.foodie.app.provider;

import android.accounts.NetworkErrorException;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.IDBManager;

/**
 * Created by David on 14/13/2016.
 */


public class MyContentProvider extends ContentProvider {

    private static String lastErrorType = "";
    private static String lastErrorMessage = "";

    public static String getLastErrorType() {
        String temp = lastErrorType;
        lastErrorType = "";
        return temp;
    }

    public static String getLastErrorMessage() {
        String temp = lastErrorMessage;
        lastErrorMessage = "";
        return temp;
    }

    IDBManager manager = DBManagerFactory.getManager();
    final String TAG = "foodie";

    public MyContentProvider() {
        manager = DBManagerFactory.getManager();
        Log.d(TAG, "ContentProvided ctor ");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        String listName = uri.getLastPathSegment();
        String id = selection;
        try {
            switch (listName) {
                case "user":
                    if (manager.removeUser(id))
                        return 1;
                case "Business":
                    if (manager.removeBusiness(id))
                        return 1;
                case "Activity":
                    if (manager.removeActivity(id))
                        return 1;
                case "cpuser":
                    if (manager.removeCPUser(id))
                        return 1;
            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return DBManagerFactory.getDBtype();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (manager == null) {

            manager = DBManagerFactory.getManager();
        }
        String listName = uri.getLastPathSegment();
        String id = "";
        try {
            switch (listName) {
                case "User":
                    id = manager.addUser(values);

                case "Business":
                    id = manager.addBusiness(values);

                case "Activity":
                    id = manager.addActivity(values);

                case "Cpuser":
                    id = manager.addCPUser(values);
            }
            if(id != null && !id.isEmpty())
                return Uri.withAppendedPath(uri, id);
            return uri;

        } catch (Exception ex) {

        }
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "ContentProvided created ");
        //so we move mContext initialization here
        //mContext = getContext();
        //   dbHelper = new DatabaseHelper(mContext);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        String listName = uri.getLastPathSegment();


        try {
            switch (listName) {
                case "user":
                    return manager.getUser(selectionArgs, projection);


                case "cpuser":
                    return manager.getCPUser(selectionArgs, projection);

                case "Business":
                    return manager.getBusiness(selectionArgs, projection);


                case "Activity":
                    return manager.getActivity(selectionArgs, projection);

            }
        } catch (NetworkErrorException ex) {
            lastErrorMessage = ex.getMessage();
            lastErrorType = ex.toString();
            return null;

        } catch (Exception ex) {
            lastErrorMessage = ex.getMessage();
            lastErrorType = ex.toString();
            return null;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        Log.d(TAG, "insert " + uri.toString());
        String listName = uri.getLastPathSegment();
        String id = (selection);
        try {
            switch (listName) {
                case "user":
                    if (manager.updateUser(id, values))
                        return 1;


                case "Business":
                    if (manager.updateBusiness(id, values))
                        return 1;

                case "Activity":
                    if (manager.updateActivity(id, values))
                        return 1;

                case "cpuser":
                    if (manager.updateCPUser(id, values))
                        return 1;

            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }
}
