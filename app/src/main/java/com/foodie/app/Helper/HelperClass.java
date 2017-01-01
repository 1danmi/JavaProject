package com.foodie.app.Helper;

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
}
