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

    public static String getHashPws(String psw)
    {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(psw.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
