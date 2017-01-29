package com.foodie.app.onlineDB;

/**
 * Created by David on 29/1/2017.
 */

public interface downloadFileCallBack {
    public void onDownload(byte[] img);
    public void onFailed(String error);
}
