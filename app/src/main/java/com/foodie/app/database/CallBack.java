package com.foodie.app.database;

import java.util.List;

/**
 * Created by David on 15/12/2016.
 */

public interface CallBack<T> {
    public void onSuccess(final List<T> data);

    public void onFailed(final DataStatus status, final String error);

}