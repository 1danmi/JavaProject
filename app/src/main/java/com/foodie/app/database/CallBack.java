package com.foodie.app.database;

import java.util.List;

/**
 * Created by David on 15/12/2016.
 */

public interface CallBack<T>{
    public void run(DataStatus status, List<T> data);

}