package com.foodie.app.Helper;

import android.os.Handler;
import android.os.Looper;

import com.foodie.app.database.CallBack;
import com.foodie.app.database.DataStatus;

import java.util.List;

/**
 * Created by David on 28/12/2016.
 */

public class HelperClass {
    public static <T> T as(Class<T> t, Object o) {
        return t.isInstance(o) ? t.cast(o) : null;
    }
    public static <T> T[] listToArray()
    {
        return null;
    }
    public static void runInMain(Runnable run) {
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler.post(run);
    }
}
