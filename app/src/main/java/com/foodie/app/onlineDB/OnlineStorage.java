package com.foodie.app.onlineDB;

import android.app.NotificationManager;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 29/1/2017.
 */



public class OnlineStorage {


    private StorageReference storageRef;
    NotificationManager mNotifyManager;
    private String id;

    private static List<UploadStatus> uploadingFiles = new ArrayList<>();

    public OnlineStorage(String userId,String entitie,String id,String imageType) {

        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://javaproject-af868.appspot.com").child("images").child(userId).child(entitie).child(id+'.'+imageType);
        this.id = id;
    }

    public void uploadFile(byte[] data)
    {
        UploadStatus temp = getFileStatusById(this.id);
        if(temp !=null) {
            uploadingFiles.remove(temp);
        }

        uploadingFiles.add(new UploadStatus(this.id,storageRef.putBytes(data)));
    }

    public void downloadFile(final DownloadFileCallBack onDownload )
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
        if(id == null || id.isEmpty())
            return uploadingFiles.get(uploadingFiles.size()-1);

        for(UploadStatus us: uploadingFiles)
        {
            if(us.getId().equals(id))
                return us;
        }
        return null;
    }


}
