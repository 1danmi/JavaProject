package com.foodie.app.database;

import com.foodie.app.listsDB.ListDBManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Daniel on 12/14/2016.
 */

public class DBManagerFactory {

    static IDBManager manager = null;

    private static String DBtype = "List";

    public static String getDBtype() {
        return DBtype;
    }

    public static IDBManager getManager() {
        if (DBtype.equals("List") && manager == null)
            manager = new ListDBManager();
        return manager;
    }


}
