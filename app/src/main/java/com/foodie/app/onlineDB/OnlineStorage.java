package com.foodie.app.onlineDB;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

/**
 * Created by David on 29/1/2017.
 */



public class OnlineStorage {


    private StorageReference storageRef;
    NotificationManager mNotifyManager;
    private String id;

    private static List<UploadStatus> uploadingFiles;

    public OnlineStorage(String userId,String entitie,String id,String imageType) {

        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://javaproject-af868.appspot.com").child("images").child(userId).child(entitie).child(id+'.'+imageType);
        this.id = id;
    }

    public void uploadFile(byte[] data)
    {
        uploadingFiles.add(new UploadStatus(this.id,storageRef.putBytes(data)));
    }

    public void downloadFile(final downloadFileCallBack onDownload )
    {

        final long TWO_MEGABYTE = 2*1024 * 1024;
        storageRef.getBytes(TWO_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                onDownload.onDownload(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                onDownload.onFailed(exception.getMessage());
            }
        });
    }

    public static UploadStatus getFileStatusById(String id)
    {
        for(UploadStatus us: uploadingFiles)
        {
            if(us.getId().equals(id))
                return us;
        }
        return null;
    }


}
