package com.foodie.app.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.listsDB.ListDBManager;

/**
 * Created by David on 19/1/2017.
 */

public class DataUpdated extends Service {


    private int cpuserTotal = 0;
    private int businessTotal = 0;
    private int userTotal = 0;
    private int activitiesTotal = 0;

    public static final String mBroadcastCpusers = "Cpusers";
    public static final String mBroadcastUsers = "Users";
    public static final String mBroadcastActivities = "Activities";
    public static final String mBroadcastBusiness = "Business";

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            public void run() {

                DebugHelper.Log("DataUpdated: Services start");
                while (true) {
                    try {

                        if (!DBManagerFactory.getDBupdated()) {
                            sendMessage("Cpusers");
                            sendMessage("Users");
                            sendMessage("Activities");
                            sendMessage("Business");

                        }

                        if (cpuserTotal != ListDBManager.getCpusersListSize()) {
                            cpuserTotal = ListDBManager.getCpusersListSize();
                            sendMessage("Cpusers");

                        }

                        if (userTotal != ListDBManager.getUsersListSize()) {
                            userTotal = ListDBManager.getUsersListSize();
                            sendMessage("Users");
                        }

                        if (activitiesTotal != ListDBManager.getActivitiesListSize()) {
                            activitiesTotal = ListDBManager.getActivitiesListSize();
                            sendMessage("Activities");
                        }

                        if (businessTotal != ListDBManager.getBusinessListSize()) {
                            businessTotal = ListDBManager.getBusinessListSize();
                            sendMessage("Business");
                        }


                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }

                }

            }
        }).start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendMessage(String from) {
        DebugHelper.Log("DataUpdated: Something happen in " + from);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(from);
        broadcastIntent.putExtra("Data", "Broadcast Data");
        sendBroadcast(broadcastIntent);
    }

}
