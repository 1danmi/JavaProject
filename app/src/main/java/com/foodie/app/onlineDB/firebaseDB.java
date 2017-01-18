package com.foodie.app.onlineDB;

import android.accounts.NetworkErrorException;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;


import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.Helper.HelperClass;
import com.foodie.app.backend.AppContract;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.Converters;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.DataStatus;
import com.foodie.app.database.IDBManager;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;
import com.foodie.app.listsDB.ListDBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.Executor;

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
    private boolean login; // check if the user did a login
    private boolean dataUpdated;


    private FirebaseUser user = null;

    private FirebaseAuth mAuth;


    public firebaseDB() {

        mAuth = FirebaseAuth.getInstance();
        login = false;
        dataUpdated = false;

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    OnLogIn();
                    login = true;
                    DBManagerFactory.setCurrentUser(new CPUser(user.getUid(),user.getEmail(),user.getDisplayName()));
                    ListDBManager.removeOthersUsers();
                    dataUpdated = true;

                } else {
                    login = false;

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
        mAuth.removeAuthStateListener(mAuthListener);
    }


    private void OnLogIn() {

        login = true;
        // User is signed in
        localDB = new ListDBManager();
        // Write a message to the database
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mDatabase.addChildEventListener(new onOnlineDBChange(localDB));


        CPUserRef = mDatabase.child("CPUser");
        BusinessRef = mDatabase.child("Business");
        ActivityRef = mDatabase.child("Activity");
        UserRef = mDatabase.child("User");
    }

    @Override
    public String addCPUser(ContentValues values) throws Exception {
        if (!login)
            throw new InvalidPropertiesFormatException("No user login");

        if (values == null) {
            throw new NullPointerException("ContentValues is null");
        }
        CPUser cpuser = Converters.ContentValuesToCPUser(values);
        CPUserRef.push().setValue(cpuser);
        return "";
    }

    @Override
    public String addBusiness(ContentValues values) throws Exception {
        checkForLogin();
        if (values == null)
            throw new NullPointerException("ContentValues is null");
        Business business = Converters.ContentValuesToBusiness(values);
        DatabaseReference toInsert = BusinessRef.push();
        toInsert.child(AppContract.Business.BUSINESS_NAME).setValue(business.getBusinessName());
        toInsert.child(AppContract.Business.BUSINESS_ADDRESS).setValue(business.getBusinessAddress());
        toInsert.child(AppContract.Business.BUSINESS_CPUSER_ID).setValue(DBManagerFactory.getCurrentUser().get_ID());
        toInsert.child(AppContract.Business.BUSINESS_EMAIL).setValue(business.getBusinessEmail());
        toInsert.child(AppContract.Business.BUSINESS_PHONE_NUMBER).setValue(business.getBusinessPhoneNo());
        toInsert.child(AppContract.Business.BUSINESS_WEBSITE).setValue(business.getBusinessWebsite());
        //  toInsert.child(AppContract.Business.BUSINESS_LOGO).setValue(str);

        return "";
    }

    @Override
    public String addActivity(ContentValues values) throws Exception {
        if(values == null)
            throw new NullPointerException("ContentValues is null");
        Activity activity = Converters.ContentValuesToActivity(values);
        DatabaseReference toInsert = ActivityRef.push();
        toInsert.child(AppContract.Activity.ACTIVITY_NAME).setValue(activity.getActivityRating());
        toInsert.child(AppContract.Activity.ACTIVITY_DESCRIPTION).setValue(activity.getActivityDescription());
        toInsert.child(AppContract.Activity.ACTIVITY_COST).setValue(activity.getActivityCost());
        toInsert.child(AppContract.Activity.ACTIVITY_RATING).setValue(activity.getActivityRating());
        toInsert.child(AppContract.Activity.ACTIVITY_BUSINESS_ID).setValue(activity.getBusinessId());
//        toInsert.child(AppContract.Activity.ACTIVITY_IMAGE).setValue(activity.getActivityImages());
        toInsert.child(AppContract.Activity.ACTIVITY_FEATURE).setValue(activity.getFeature());
        return "";
    }

    @Override
    public String addUser(ContentValues values) throws Exception {

        checkForLogin();
        if (values == null) {
            throw new NullPointerException("ContentValues is null");
        }
        User users = Converters.ContentValuesToUser(values);
        UserRef.push().setValue(users);
        return "";
    }

    @Override
    public boolean removeCPUser(String id) throws Exception {
        checkForLogin();

        return false;
    }

    @Override
    public boolean removeBusiness(String id) throws Exception {
        checkForLogin();
        return false;
    }

    @Override
    public boolean removeActivity(String id) throws Exception {
        checkForLogin();
        return false;
    }

    @Override
    public boolean removeUser(String id) throws Exception {
        checkForLogin();
        return false;
    }

    @Override
    public Cursor getCPUser(String[] args, String[] columnsArgs) throws Exception {
        checkForLogin();

        return localDB.getCPUser(args, columnsArgs);
    }

    @Override
    public Cursor getBusiness(String[] args, String[] columnsArgs) throws Exception {
        checkForLogin();
        return localDB.getBusiness(args, columnsArgs);
    }

    @Override
    public Cursor getActivity(String[] args, String[] columnsArgs) throws Exception {
        checkForLogin();
        return localDB.getActivity(args, columnsArgs);
    }

    @Override
    public Cursor getUser(String[] args, String[] columnsArgs) throws Exception {
        checkForLogin();
        return localDB.getUser(args, columnsArgs);
    }

    @Override
    public boolean updateCPUser(String id, ContentValues values) throws Exception {
        checkForLogin();
        return localDB.updateCPUser(id, values);
    }

    @Override
    public boolean updateBusiness(String id, ContentValues values) throws Exception {
        checkForLogin();
        return localDB.updateBusiness(id, values);
    }

    @Override
    public boolean updateActivity(String id, ContentValues values) throws Exception {
        checkForLogin();
        return localDB.updateActivity(id, values);
    }

    @Override
    public boolean updateUser(String id, ContentValues values) throws Exception {
        checkForLogin();
        return localDB.updateUser(id, values);
    }

    @Override
    public boolean isDBUpdated() {

        return localDB.isDBUpdated();
    }


    public void login(final String email,final String password,final CallBack<CPUser> callBack) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (callBack != null)
                    if (!task.isSuccessful()) {

                        HelperClass.runInMain(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailed(DataStatus.Failed, "Invalid username or password");
                            }
                        });
                    } else {

                        HelperClass.runInMain(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(null);
                            }
                        });
                    }

            }
        });
    }

    public void signUp(CPUser user, final CallBack<CPUser> callBack)
    {
        if(user.getUserEmail().isEmpty())
            callBack.onFailed(DataStatus.InvalidArgumment,"Invalid email");

        mAuth.createUserWithEmailAndPassword(user.getUserEmail(),user.getUserPwdHash()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    callBack.onSuccess(null);
                }else
                    callBack.onFailed(DataStatus.Failed,"Invalid user o password");

                // ...
            }
        });
    }

    private void checkForLogin() throws Exception {
        if(!login)
            throw new Exception("Login requiered");

       while (!dataUpdated){
           try {
               Thread.sleep(1000);
           } catch (InterruptedException ignored) {

           }
       }
    }

}

