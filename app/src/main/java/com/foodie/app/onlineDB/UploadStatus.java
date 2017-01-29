package com.foodie.app.onlineDB;

import android.support.annotation.NonNull;

import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

/**
 * Created by David on 29/1/2017.
 */

public class UploadStatus {

    private UploadTask uploadTask;
    private String id;
    private double progress = 0;

    public UploadStatus(String id,@NonNull UploadTask uploadTask) {
        this.id = id;
        this.uploadTask = uploadTask;

        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        });
    }

    public String getId() {
        return id;
    }

    public double getProgress() {
        return progress;
    }
}
