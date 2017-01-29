package com.foodie.app.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.foodie.app.database.CallBack;
import com.foodie.app.database.DataStatus;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
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

    public static byte[] fromStringToByteArray(String str) {
        String[] byteValues = str.substring(1, str.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];

        for (int i=0, len=bytes.length; i<len; i++) {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }
        return bytes;
    }

    public static byte[] fromBitMapToBytearry(Bitmap bmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap fromBytearryToBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public static String fromByteArraytoString(byte[] b){
        return Arrays.toString(b);
    }
    

}
