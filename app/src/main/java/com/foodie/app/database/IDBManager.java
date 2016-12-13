package com.foodie.app.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Daniel on 12/13/2016.
 */

public interface IDBManager {

    int addCPUser(ContentValues values) throws Exception;
    int addBusiness(ContentValues values) throws Exception;
    int addActivity(ContentValues values) throws Exception;
    int addUser(ContentValues values) throws Exception;

    boolean removeCPUser(long id) throws Exception;
    boolean removeBusiness(long id) throws Exception;
    boolean removeActivity(long id) throws Exception;
    boolean removeUser(long id) throws Exception;

    Cursor getCPUser();
    Cursor getBusiness();
    Cursor getActivity();
    Cursor getUser();


    boolean updateCPUser(int id, ContentValues values) throws Exception;
    boolean updateBusiness(int id, ContentValues values) throws Exception;
    boolean updateActivity(int id, ContentValues values) throws Exception;
    boolean updateUser(int id, ContentValues values) throws Exception;

    boolean isDBUpdated();
}
