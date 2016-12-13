package com.foodie.app.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Daniel on 12/13/2016.
 */

public interface IDBManager {

    int addCPUser(ContentValues values);
    int addBusiness(ContentValues values);
    int addActivity(ContentValues values);
    int addUser(ContentValues values);

    boolean removeCPUser(ContentValues values);
    boolean removeBusiness(ContentValues values);
    boolean removeActivity(ContentValues values);
    boolean removeUser(ContentValues values);

    Cursor getCPUser(ContentValues values);
    Cursor getBusiness(ContentValues values);
    Cursor getActivity(ContentValues values);
    Cursor getUser(ContentValues values);


    boolean updateCPUser(int id, ContentValues values);
    boolean updateBusiness(int id, ContentValues values);
    boolean updateActivity(int id, ContentValues values);
    boolean updateUser(int id, ContentValues values);

    boolean isDBUpdated();
}
