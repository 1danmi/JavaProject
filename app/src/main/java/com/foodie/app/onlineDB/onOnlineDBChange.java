package com.foodie.app.onlineDB;

import com.foodie.app.Helper.DebugHelper;
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
import com.google.firebase.database.DatabaseReference;

/**
 * Created by David on 15/1/2017.
 */

public class onOnlineDBChange implements ChildEventListener {

    ListDBManager localDB;
    public static boolean updated =false;

    public onOnlineDBChange(ListDBManager localDB) {
        this.localDB = localDB;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        DebugHelper.Log("onChildAdded: " + dataSnapshot.getKey());
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            DebugHelper.Log("onChildAdded: " + data.getKey());
            switch (dataSnapshot.getKey()){
                case "CPUser":
                    CPUser cpu = data.getValue(CPUser.class);
                    cpu.set_ID(data.getKey());
                    localDB.addCPUser(cpu);
                    break;
                case "Business":

                    Business business = data.getValue(Business.class);
                    business.set_ID(data.getKey());
                    localDB.addBusiness(business);
                    break;
                case "Activity":
                    Activity activity = data.getValue(Activity.class);
                    activity.set_ID(data.getKey());
                    localDB.addActivity(activity);
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

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
