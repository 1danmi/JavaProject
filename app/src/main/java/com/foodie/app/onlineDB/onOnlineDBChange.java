package com.foodie.app.onlineDB;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.Helper.HelperClass;
import com.foodie.app.backend.AppContract;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;
import com.foodie.app.listsDB.ListDBManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by David on 15/1/2017.
 */

public class OnOnlineDBChange implements ChildEventListener {

    ListDBManager localDB;
    public static boolean updated =false;

    public OnOnlineDBChange(ListDBManager localDB) {
        this.localDB = localDB;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        DebugHelper.Log("onChildAdded: " + dataSnapshot.getKey());
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            switch (dataSnapshot.getKey()){
                case "CPUser":
                        localDB.addCPUser(getCPUser(data));
                    break;
                case "Business":
                    localDB.addBusiness(getBusiness(data));

                    break;
                case "Activity":
                    localDB.addActivity(getActivity(data));
                    break;
                case "User":
                    User user = data.getValue(User.class);
                    user.set_ID(data.getKey());
                    localDB.addUser(user);
                    break;
            }
        }


        updated = true;
    }

    private Activity getActivity(DataSnapshot data) {
        try{
            Activity activity = new Activity();
//            activity.set_ID(data.)
            if(data.child(AppContract.Activity.ACTIVITY_NAME).getValue() != null)
                activity.setActivityName(data.child(AppContract.Activity.ACTIVITY_NAME).getValue().toString());
            if(data.child(AppContract.Activity.ACTIVITY_DESCRIPTION).getValue() != null)
                activity.setActivityDescription(data.child(AppContract.Activity.ACTIVITY_DESCRIPTION).getValue().toString());
            if(data.child(AppContract.Activity.ACTIVITY_COST).getValue() != null)
                activity.setActivityCost( Double.parseDouble(data.child(AppContract.Activity.ACTIVITY_COST).getValue().toString()));
            if(data.child(AppContract.Activity.ACTIVITY_RATING).getValue() != null)
                activity.setActivityRating(Double.parseDouble(data.child(AppContract.Activity.ACTIVITY_RATING).getValue().toString()));
            if(data.child(AppContract.Activity.ACTIVITY_BUSINESS_ID).getValue() != null)
                activity.setBusinessId(data.child(AppContract.Activity.ACTIVITY_BUSINESS_ID).getValue().toString());
            if(data.child(AppContract.Activity.ACTIVITY_FEATURE).getValue() != null)
                activity.setFeature( data.child(AppContract.Activity.ACTIVITY_FEATURE).getValue().toString());
            if (data.child(AppContract.Activity.ACTIVITY_ID).getValue() != null)
                activity.set_ID(data.child(AppContract.Activity.ACTIVITY_ID).getValue().toString());
            if(data.child(AppContract.Activity.ACTIVITY_IMAGE).getValue() != null && !data.child(AppContract.Activity.ACTIVITY_IMAGE).getValue().toString().isEmpty()){
                byte[] ba = HelperClass.fromStringToByteArray(data.child(AppContract.Activity.ACTIVITY_IMAGE).getValue().toString());
                activity.setActivityImage(ba);
            }
          return activity;
        }catch (Exception ignored)
        {

        }
        return null;
    }

    private Business getBusiness(DataSnapshot data) {
        Business business = new Business();
        business.set_ID(data.getKey());
        if(data.child(AppContract.Business.BUSINESS_NAME).getValue() != null)
            business.setBusinessName(data.child(AppContract.Business.BUSINESS_NAME).getValue().toString());
        if(data.child(AppContract.Business.BUSINESS_ADDRESS).getValue() != null)
            business.setBusinessAddress(data.child(AppContract.Business.BUSINESS_ADDRESS).getValue().toString());
        if(data.child(AppContract.Business.BUSINESS_CPUSER_ID).getValue() != null)
            business.setCpuserID(data.child(AppContract.Business.BUSINESS_CPUSER_ID).getValue().toString());
        if(data.child(AppContract.Business.BUSINESS_EMAIL).getValue() != null)
            business.setBusinessEmail(data.child(AppContract.Business.BUSINESS_EMAIL).getValue().toString());
        if(data.child(AppContract.Business.BUSINESS_PHONE_NUMBER).getValue() != null)
            business.setBusinessPhoneNo( data.child(AppContract.Business.BUSINESS_PHONE_NUMBER).getValue().toString());
        if(data.child(AppContract.Business.BUSINESS_WEBSITE).getValue() != null)
            business.setBusinessWebsite( data.child(AppContract.Business.BUSINESS_WEBSITE).getValue().toString());
        if(data.child(AppContract.Business.BUSINESS_LOGO).getValue() != null && !data.child(AppContract.Business.BUSINESS_LOGO).getValue().toString().isEmpty()) {
            byte[] b = HelperClass.fromStringToByteArray(data.child(AppContract.Business.BUSINESS_LOGO).getValue().toString());
            business.setBusinessLogo(b);
        }
        return business;
    }

    private CPUser getCPUser(DataSnapshot data)
    {
        CPUser user = new CPUser();
        user.set_ID(data.getKey());
        try {

            if(data.child(AppContract.CPUser.CPUSER_FULL_NAME).getValue() != null)
                user.setUserFullName(data.child(AppContract.CPUser.CPUSER_FULL_NAME).getValue().toString());

            if(data.child(AppContract.CPUser.CPUSER_PWD).getValue() != null)
                user.setUserFullName(data.child(AppContract.CPUser.CPUSER_PWD).getValue().toString());

            if(data.child(AppContract.CPUser.CPUSER_EMAIL).getValue() != null)
                user.setUserFullName(data.child(AppContract.CPUser.CPUSER_EMAIL).getValue().toString());


        } catch (Exception ignored) {

        }


        return user;
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        DebugHelper.Log("onChildChanged: " + dataSnapshot.getKey());
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            switch (dataSnapshot.getKey()){
                case "CPUser":
                    CPUser cpu = data.getValue(CPUser.class);
                    cpu.set_ID(data.getKey());
                    localDB.updateCPUser(cpu);
                    break;
                case "Business":
                    localDB.updateBusiness(getBusiness(data));
                    break;
                case "Activity":
                    localDB.updateActivity(getActivity(data));
                    break;
                case "User":
                    User user = data.getValue(User.class);
                    user.set_ID(data.getKey());
                    localDB.updateUser(user);
                    break;
            }
        }
        DBManagerFactory.setDBupdated(false);

        updated = true;


    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
