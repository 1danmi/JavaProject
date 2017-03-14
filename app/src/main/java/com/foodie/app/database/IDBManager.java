package com.foodie.app.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Daniel on 12/13/2016.
 */


/**
 * A interface used to especificate the existent operations in the DB
 */
public interface IDBManager {

    String addCPUser(ContentValues values) throws Exception;

    String addBusiness(ContentValues values) throws Exception;

    String addActivity(ContentValues values) throws Exception;

    String addUser(ContentValues values) throws Exception;

    boolean removeCPUser(String id) throws Exception;

    boolean removeBusiness(String id) throws Exception;

    boolean removeActivity(String id) throws Exception;

    boolean removeUser(String id) throws Exception;

    Cursor getCPUser(String[] args, String[] columnsArgs) throws Exception;

    Cursor getBusiness(String[] args, String[] columnsArgs) throws Exception;

    Cursor getActivity(String[] args, String[] columnsArgs) throws Exception;

    Cursor getUser(String[] args, String[] columnsArgs) throws Exception;

    boolean updateCPUser(String id, ContentValues values) throws Exception;

    boolean updateBusiness(String id, ContentValues values) throws Exception;

    boolean updateActivity(String id, ContentValues values) throws Exception;

    boolean updateUser(String id, ContentValues values) throws Exception;

    boolean isDBUpdated();

}
