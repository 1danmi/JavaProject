package com.foodie.app.onlineDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;


import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.Helper.HelperClass;
import com.foodie.app.Services.DataUpdated;
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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by David on 9/1/2017.
 */


/**
 * This class will use google firebase as an online DB
 * In order to a faster query, the class use the local db
 * to save the data locally
 */
public class FirebaseDB implements IDBManager {

    private ListDBManager localDB;

    private DatabaseReference mDatabase;
    private DatabaseReference CPUserRef;
    private DatabaseReference BusinessRef;
    private DatabaseReference ActivityRef;
    private DatabaseReference UserRef;
    private boolean login; // check if the user did a login
    private boolean dataUpdated;
    private CPUser newUser = null;


    private FirebaseUser user = null;

    private FirebaseAuth mAuth;

    /**
     * Default constructor.
     * The constructor will check if the login was made and start the class
     */
    public FirebaseDB() {

        mAuth = FirebaseAuth.getInstance();
        login = false;
        dataUpdated = false;

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    ListDBManager.resetDB();
                    DebugHelper.Log("User authenticated: " + user.getEmail());
                    login = true;
                    DBManagerFactory.setCurrentUser(new CPUser(user.getUid(),user.getEmail(),user.getDisplayName()));
           //       ListDBManager.removeOthersUsers();
                    dataUpdated = true;
                    onCreate(user.getUid());
                    if(newUser  != null)
                        try {
                            addCPUser(newUser.toContentValues());
                        } catch (Exception ignored) {

                        }

                } else {
                    login = false;

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * After login, start the database by using the current CPUser id
     * @param id : The id of the user logged in
     */
    private void onCreate(String id) {

        // User is signed in
        localDB = new ListDBManager();
        // Write a message to the database
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child(id);
        this.mDatabase.addChildEventListener(new OnOnlineDBChange(localDB));
        this.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(ListDBManager.getBusinessListSize() ==0 &&  !dataSnapshot.child("Business").hasChildren()) {
                    DataUpdated.setNoBussiness(true);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        CPUserRef = mDatabase.child("CPUser");
        BusinessRef = mDatabase.child("Business");
        ActivityRef = mDatabase.child("Activity");
        UserRef = mDatabase.child("User");

    }

    /**
     * This Function will insert a cpuser to the online DB
     * @param values : The new data
     * @return : the entity id
     */
    @Override
    public String addCPUser(ContentValues values) throws Exception {

        if (values == null) {
            throw new NullPointerException("ContentValues is null");
        }
        CPUser user = Converters.ContentValuesToCPUser(values);
        DatabaseReference toInsert = CPUserRef.push();

        if(user.getUserFullName() != null)
            toInsert.child(AppContract.CPUser.CPUSER_FULL_NAME).setValue(user.getUserFullName());

        if(user.getUserFullName() != null)
            toInsert.child(AppContract.CPUser.CPUSER_EMAIL).setValue(user.getUserEmail());

        if(user.getUserFullName() != null)
            toInsert.child(AppContract.CPUser.CPUSER_PWD).setValue(user.getUserPwdHash());

        toInsert.child(AppContract.User.USER_ID).setValue(toInsert.getKey());

        return toInsert.getKey();
    }

    /**
     * This Function will insert a business to the online DB
     * @param values : The new data
     * @return : the entity id
     */
    @Override
    public String addBusiness(ContentValues values) throws Exception {
     
        if (values == null)
           throw new NullPointerException("ContentValues is null");

        Business business = Converters.ContentValuesToBusiness(values);
        DatabaseReference toInsert = BusinessRef.push();
        if(business.getBusinessName() != null)
            toInsert.child(AppContract.Business.BUSINESS_NAME).setValue(business.getBusinessName());
        if(business.getBusinessAddress() != null)
            toInsert.child(AppContract.Business.BUSINESS_ADDRESS).setValue(business.getBusinessAddress());

        toInsert.child(AppContract.Business.BUSINESS_CPUSER_ID).setValue(DBManagerFactory.getCurrentUser().get_ID());

        if(business.getBusinessEmail() != null)
            toInsert.child(AppContract.Business.BUSINESS_EMAIL).setValue(business.getBusinessEmail());
        if(business.getBusinessPhoneNo() != null)
            toInsert.child(AppContract.Business.BUSINESS_PHONE_NUMBER).setValue(business.getBusinessPhoneNo());
        if(business.getBusinessWebsite() != null)
            toInsert.child(AppContract.Business.BUSINESS_WEBSITE).setValue(business.getBusinessWebsite());

        if(business.getBusinessLogo() != null)
            (new OnlineStorage(DBManagerFactory.getCurrentUser().get_ID(),"Business",toInsert.getKey(),"jpg")).uploadFile(business.getBusinessLogo());

        toInsert.child(AppContract.Business.BUSINESS_ID).setValue(toInsert.getKey());

        return toInsert.getKey();
    }
    /**
     * This Function will insert a activity to the online DB
     * @param values : The new data
     * @return : the entity id
     */
    @Override
    public String addActivity(ContentValues values) throws Exception {
        if(values == null)
            throw new NullPointerException("ContentValues is null");
        Activity activity = Converters.ContentValuesToActivity(values);
        DatabaseReference toInsert = ActivityRef.push();
        toInsert.child(AppContract.Activity.ACTIVITY_NAME).setValue(activity.getActivityName());
        toInsert.child(AppContract.Activity.ACTIVITY_DESCRIPTION).setValue(activity.getActivityDescription());
        toInsert.child(AppContract.Activity.ACTIVITY_COST).setValue(activity.getActivityCost());
        toInsert.child(AppContract.Activity.ACTIVITY_RATING).setValue(activity.getActivityRating());
        toInsert.child(AppContract.Activity.ACTIVITY_BUSINESS_ID).setValue(activity.getBusinessId());
        toInsert.child(AppContract.Activity.ACTIVITY_FEATURE).setValue(activity.getFeature());
        if(activity.getActivityImage() != null)
            (new OnlineStorage(DBManagerFactory.getCurrentUser().get_ID(),"Activity",toInsert.getKey(),"jpg")).uploadFile(activity.getActivityImage());

        toInsert.child(AppContract.Activity.ACTIVITY_ID).setValue(toInsert.getKey());
        return toInsert.getKey();
    }
    /**
     * This Function will insert an user to the online DB
     * @param values : The new data
     * @return : the entity id
     */
    @Override
    public String addUser(ContentValues values) throws Exception {

     
        if (values == null) {
            throw new NullPointerException("ContentValues is null");
        }
        User users = Converters.ContentValuesToUser(values);
        UserRef.push().setValue(users);
        return "";
    }

    /**
     * This function will remove a cpuser entity by id
     * In this case its not possible to remove a cpuser
     * @param id : the cpuser id
     * @return : boolean if the operation is complete successfully
     */
    @Override
    public boolean removeCPUser(String id) throws Exception {

        return false;
    }

    /**
     * This function will remove a business entity by id
     * @param id : the entity id
     * @return : boolean if the operation is complete successfully
     */
    @Override
    public boolean removeBusiness(String id) throws Exception {
        DebugHelper.Log("removeBusiness: id = "+id);
        BusinessRef.child(id).removeValue();

        localDB.removeBusiness(id);
        List<Activity> businessActivities = Converters.cursorToActivityList(localDB.getActivity(new String[]{id},new String[]{AppContract.Activity.ACTIVITY_BUSINESS_ID}));
        for (Activity ac : businessActivities)
        {
            removeActivity(ac.get_ID());
        }
        return true;
    }
    /**
     * This function will remove a activity entity by id
     * @param id : the entity id
     * @return : boolean if the operation is complete successfully
     */
    @Override
    public boolean removeActivity(String id) throws Exception {
        ActivityRef.child(id).removeValue();
        localDB.removeActivity(id);
        return true;
    }
    /**
     * This function will remove a business entity by id
     * @param id : the entity id
     * @return : boolean if the operation is complete successfully
     */
    @Override
    public boolean removeUser(String id) throws Exception {
        //No user found
        return false;
    }

    /**
     * This function will get a cpuser from the local db
     */
    @Override
    public Cursor getCPUser(String[] args, String[] columnsArgs) throws Exception {
     

        return localDB.getCPUser(args, columnsArgs);
    }
    /**
     * This function will get a business from the local db
     */
    @Override
    public Cursor getBusiness(String[] args, String[] columnsArgs) throws Exception {
     
        return localDB.getBusiness(args, columnsArgs);
    }
    /**
     * This function will get an activity from the local db
     */
    @Override
    public Cursor getActivity(String[] args, String[] columnsArgs) throws Exception {
     
        return localDB.getActivity(args, columnsArgs);
    }
    /**
     * This function will get a user from the local db
     */
    @Override
    public Cursor getUser(String[] args, String[] columnsArgs) throws Exception {
     
        return localDB.getUser(args, columnsArgs);
    }

    /**
     * This function will update a CPUSER by id
     * @param id :  The id
     * @param values: the new data
     * @return : true in case of success
     */
    @Override
    public boolean updateCPUser(String id, ContentValues values) throws Exception {

        CPUser toUpdate = Converters.ContentValuesToCPUser(values);
        toUpdate.set_ID(DBManagerFactory.getCurrentUser().get_ID());
        CPUserRef.child(DBManagerFactory.getCurrentUser().get_ID()).updateChildren(toUpdate.toMap());

        return true;
    }
    /**
     * This function will update a bussiness by id
     * @param id :  The id
     * @param values: the new data
     * @return : true in case of success
     */
    @Override
    public boolean updateBusiness(String id, ContentValues values) throws Exception {

        Business toUpdate = Converters.ContentValuesToBusiness(values);
        toUpdate.setCpuserID(DBManagerFactory.getCurrentUser().get_ID());
        BusinessRef.child(toUpdate.get_ID()).updateChildren(toUpdate.toMap());
        if(toUpdate.getBusinessLogo() != null) {
            (new OnlineStorage(DBManagerFactory.getCurrentUser().get_ID(), "Business", toUpdate.get_ID(), "jpg")).uploadFile(toUpdate.getBusinessLogo());
            ListDBManager.UpdateBusinessPicture(toUpdate.get_ID(),toUpdate.getBusinessLogo());
        }

        return true;
    }
    /**
     * This function will update an activity by id
     * @param id :  The id
     * @param values: the new data
     * @return : true in case of success
     */
    @Override
    public boolean updateActivity(String id, ContentValues values) throws Exception {

        Activity toUpdate = Converters.ContentValuesToActivity(values);
        ActivityRef.child(toUpdate.get_ID()).updateChildren(toUpdate.toMap());
        if(toUpdate.getActivityImage() != null) {
            (new OnlineStorage(DBManagerFactory.getCurrentUser().get_ID(), "Activity", toUpdate.get_ID(), "jpg")).uploadFile(toUpdate.getActivityImage());
            ListDBManager.UpdateActivityPicture(toUpdate.get_ID(),toUpdate.getActivityImage());
        }
        return true;
    }
    /**
     * This function will update a user by id
     * @param id :  The id
     * @param values: the new data
     * @return : true in case of success
     */
    @Override
    public boolean updateUser(String id, ContentValues values) throws Exception {

        User toUpdate = Converters.ContentValuesToUser(values);
        UserRef.child(toUpdate.get_ID()).updateChildren(toUpdate.toMap());
        return true;
    }

    /**
     * This function will return if the local database is updated (not in used)
     */
    @Override
    public boolean isDBUpdated() {

        return localDB.isDBUpdated();
    }

    /**
     * This function will login a CPuser
     * @param email : The user email
     * @param password : the user password
     * @param callBack : The callback to used after the operation with the login status
     */
    public void login(final String email,final String password,final CallBack<CPUser> callBack) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {

                DebugHelper.Log("Login status:" +  task.isSuccessful());

                if (callBack != null)
                    if (!task.isSuccessful()) {
                        HelperClass.runInMain(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailed(DataStatus.Failed, task.getException().getMessage());
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

    /**
     * This function will register a bew CPuser
     * @param user : The Cpuser details
     * @param callBack : The callback to used after the operation with the registration status
     */
    public void signUp(final CPUser user, final CallBack<CPUser> callBack)
    {
        if(user.getUserEmail().isEmpty())
            callBack.onFailed(DataStatus.InvalidArgumment,"Invalid email");

        mAuth.createUserWithEmailAndPassword(user.getUserEmail(),user.getUserPwdHash()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    newUser = user;
                    callBack.onSuccess(null);
                }else
                    callBack.onFailed(DataStatus.Failed,"Invalid user o password");
            }
        });
    }

    /**
     * This function signout the user
     */
    public void signOut()
    {
        DebugHelper.Log("Signed out");
        mAuth.signOut();
    }

}

