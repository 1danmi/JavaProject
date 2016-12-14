package com.foodie.app.database;

import com.foodie.app.listsDB.ListDBManager;

/**
 * Created by Daniel on 12/14/2016.
 */

public class DBManagerFactory {

    static IDBManager manager = null;

    public static IDBManager getManager() {
        if (manager == null)
            manager = new ListDBManager();
        return manager;
    }
}
