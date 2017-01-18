package com.foodie.app.database;

import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;
import com.foodie.app.listsDB.ListDBManager;
import com.foodie.app.onlineDB.firebaseDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Daniel on 12/14/2016.
 */

public class DBManagerFactory {

    private static IDBManager manager = null;
    private static String DBtype = "Firebase";
    private static CPUser currentUser = null;
    public static String getDBtype() {
        return DBtype;
    }

    public static CPUser getCurrentUser(){return currentUser;}
    public static boolean setCurrentUser(CPUser value){

        if(currentUser != null)
            return false;

        currentUser =value;
        return true;
    }

    public static IDBManager getManager() {
        if (manager == null && DBtype.equals("List"))
            manager = new ListDBManager();
        if (manager == null && DBtype.equals("Firebase"))
            manager = new firebaseDB();

        return manager;
    }

    public static void login(String email,String password, CallBack<CPUser> callBack)  {

        if(currentUser != null)
            callBack.onFailed(DataStatus.AlreadyExist,"User already did login");

        if (DBtype.equals("List"))
            ListDBManager.login(email,password,callBack);
        if (DBtype.equals("Firebase"))
            ((firebaseDB)getManager()).login(email,password,callBack);



    }

    public static void signUp(CPUser user,CallBack<CPUser> callBack)
    {
        if (DBtype.equals("List"))
            ListDBManager.signUp(user,callBack);
        if (DBtype.equals("Firebase"))
            ((firebaseDB)getManager()).signUp(user,callBack);
    }

    public static void signOut()
    {

        currentUser = null;
     //   if (DBtype.equals("Firebase"))
        //      ((firebaseDB)getManager()).signUp(user,callBack);
    }

}
