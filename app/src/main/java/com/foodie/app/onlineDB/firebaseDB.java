package com.foodie.app.onlineDB;

import android.accounts.NetworkErrorException;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;


import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.backend.AppContract;
import com.foodie.app.database.Converters;
import com.foodie.app.database.IDBManager;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.listsDB.ListDBManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by David on 9/1/2017.
 */

public class firebaseDB implements IDBManager {

    private ListDBManager localDB;

    private DatabaseReference mDatabase;
    private DatabaseReference CPUserRef;
    private DatabaseReference BusinessRef;
    private DatabaseReference ActivityRef;
    private DatabaseReference UserRef;



    public firebaseDB() {

        localDB = new ListDBManager();
        // Write a message to the database
        this.mDatabase =  FirebaseDatabase.getInstance().getReference();
        this.mDatabase.addChildEventListener(new onOnlineDBChange(localDB));

        CPUserRef = mDatabase.child("CPUser");
        BusinessRef = mDatabase.child("Business");
        ActivityRef = mDatabase.child("Activity");
        UserRef = mDatabase.child("User");

    }



    @Override
    public String addCPUser(ContentValues values) throws Exception {
        if(values == null) {
            throw new NullPointerException("ContentValues is null");
        }
        CPUser cpuser = Converters.ContentValuesToCPUser(values);
        CPUserRef.push().setValue(cpuser);
        return "";
    }

    @Override
    public String addBusiness(ContentValues values) throws Exception {
        if(values == null)
            throw new NullPointerException("ContentValues is null");
        Business business = Converters.ContentValuesToBusiness(values);
        DatabaseReference toInsert = BusinessRef.push();
        toInsert.child(AppContract.Business.BUSINESS_NAME).setValue(business.getBusinessName());
        toInsert.child(AppContract.Business.BUSINESS_ADDRESS).setValue(business.getBusinessAddress());
        toInsert.child(AppContract.Business.BUSINESS_CPUSER_ID).setValue(business.getCpuserID());
        toInsert.child(AppContract.Business.BUSINESS_EMAIL).setValue(business.getBusinessEmail());
        toInsert.child(AppContract.Business.BUSINESS_PHONE_NUMBER).setValue(business.getBusinessPhoneNo());
        toInsert.child(AppContract.Business.BUSINESS_WEBSITE).setValue(business.getBusinessWebsite());
        String str = Arrays.toString(business.getBusinessLogo());
        toInsert.child(AppContract.Business.BUSINESS_LOGO).setValue(str);

        return "";
    }

    @Override
    public String addActivity(ContentValues values) throws Exception {
        return "";
    }

    @Override
    public String addUser(ContentValues values) throws Exception {
        return "";
    }

    @Override
    public boolean removeCPUser(String id) throws Exception {
        return false;
    }

    @Override
    public boolean removeBusiness(String id) throws Exception {
        return false;
    }

    @Override
    public boolean removeActivity(String id) throws Exception {
        return false;
    }

    @Override
    public boolean removeUser(String id) throws Exception {
        return false;
    }

    @Override
    public Cursor getCPUser(String[] args, String[] columnsArgs) throws Exception{
        if(!onOnlineDBChange.updated)
            throw new NetworkErrorException("Local database is not ready");
        return localDB.getCPUser(args,columnsArgs);
    }

    @Override
    public Cursor getBusiness(String[] args, String[] columnsArgs)throws Exception {
        return localDB.getBusiness(args,columnsArgs);
    }

    @Override
    public Cursor getActivity(String[] args, String[] columnsArgs)throws Exception {
        return localDB.getActivity(args,columnsArgs);
    }

    @Override
    public Cursor getUser(String[] args, String[] columnsArgs) throws Exception{
        return localDB.getUser(args,columnsArgs);
    }

    @Override
    public boolean updateCPUser(String id, ContentValues values) throws Exception {
        return localDB.updateCPUser(id,values);
    }

    @Override
    public boolean updateBusiness(String id, ContentValues values) throws Exception {
        return localDB.updateBusiness(id,values);
    }

    @Override
    public boolean updateActivity(String id, ContentValues values) throws Exception {
        return localDB.updateActivity(id,values);
    }

    @Override
    public boolean updateUser(String id, ContentValues values) throws Exception {
        return localDB.updateUser(id,values);
    }

    @Override
    public boolean isDBUpdated() {
        return localDB.isDBUpdated();
    }
}
