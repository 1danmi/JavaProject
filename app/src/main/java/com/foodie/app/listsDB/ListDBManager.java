package com.foodie.app.listsDB;

import android.content.ContentValues;
import android.database.Cursor;

import com.foodie.app.database.IDBManager;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;

import java.util.List;

/**
 * Created by Daniel on 12/13/2016.
 */

public class ListDBManager implements IDBManager {
    List<User>  users;
    List<CPUser> cpusers;
    List<Business> businesses;
    List<Activity> activities;


    @Override
    public int addCPUser(ContentValues values) {
        return 0;
    }

    @Override
    public int addBusiness(ContentValues values) {
        return 0;
    }

    @Override
    public int addActivity(ContentValues values) {
        return 0;
    }

    @Override
    public int addUser(ContentValues values) {
        return 0;
    }

    @Override
    public boolean removeCPUser(ContentValues values) {
        return false;
    }

    @Override
    public boolean removeBusiness(ContentValues values) {
        return false;
    }

    @Override
    public boolean removeActivity(ContentValues values) {
        return false;
    }

    @Override
    public boolean removeUser(ContentValues values) {
        return false;
    }

    @Override
    public Cursor getCPUser(ContentValues values) {
        return null;
    }

    @Override
    public Cursor getBusiness(ContentValues values) {
        return null;
    }

    @Override
    public Cursor getActivity(ContentValues values) {
        return null;
    }

    @Override
    public Cursor getUser(ContentValues values) {
        return null;
    }

    @Override
    public boolean updateCPUser(int id, ContentValues values) {
        return false;
    }

    @Override
    public boolean updateBusiness(int id, ContentValues values) {
        return false;
    }

    @Override
    public boolean updateActivity(int id, ContentValues values) {
        return false;
    }

    @Override
    public boolean updateUser(int id, ContentValues values) {
        return false;
    }

    @Override
    public boolean isDBUpdated() {
        return false;
    }
}
