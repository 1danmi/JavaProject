package com.foodie.app.database;

/**
 * Created by David on 15/12/2016.
 */

public interface CallBack<T>{
    public void DBstatus(DataStatus status,T... data);

}