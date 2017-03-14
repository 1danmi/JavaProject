package com.foodie.app.database;

import java.util.List;

/**
 * Created by David on 15/12/2016.
 */

/**
 * This interface will be use to save function to a callback
 * @param <T>
 */
public interface CallBack<T> {

    /**
     * The function that gets the data in case of success
     * @param data : The data requiered by the user
     */
    public void onSuccess(final List<T> data);

    /**
     * The function that gets in case of a error
     * @param status : The error type
     * @param error : The error message
     */
    public void onFailed(final DataStatus status, final String error);

}