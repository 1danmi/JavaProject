package com.foodie.app.ProgressNotification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat.Builder;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;

/**
 * Created by David on 30/1/2017.
 */

public class ProgressNotification {

    private static int id =0;
    private NotificationManager mNotifyManager;
    private Builder mBuilder;

    private Context context;
    private NotificationCallBack callBack;

    public ProgressNotification(Context context) {
        this.context = context;
    }

    public ProgressNotification(Context context,NotificationCallBack callBack) {
        this.callBack = callBack;
        this.context = context;
    }

    public void startNotification(final NotificationCallBack callBack)
    {
        id++;
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.logo4);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        DebugHelper.Log("Notification id  " + id + " Started");
                        double incr = callBack.progress();
                        DebugHelper.Log("Notification status: " + incr + "%");
                        try {
                            // Sleep for 1 seconds
                            Thread.sleep(2000);
                        } catch (InterruptedException ignored) {

                        }
                        // Do the "lengthy" operation 20 times
                        mBuilder.setProgress(100, 0, false);
                        for (; incr <= 100; incr = callBack.progress()) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, (int) incr, false);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(id, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 1 seconds
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {

                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotifyManager.notify(id, mBuilder.build());
                    }
                }
        // Starts the thread by calling the run() method in its Runnable
        ).start();
    }

    public void setCallBack(NotificationCallBack callBack) {
        this.callBack = callBack;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
