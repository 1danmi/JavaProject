package com.foodie.app.entities;


import android.content.ContentValues;
import android.net.Uri;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by Daniel on 12/12/2016.
 */

public class CPUser {

    private int _ID = -1;

    private String userFullName;

    private String userEmail;


    private String userPwdHash;


    public CPUser()
    {

    }


    public CPUser(int _ID,String userFullName,String userEmail,String userPwdHash )
    {
        this._ID = _ID;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        try {
            setUserPwd(userPwdHash);
        } catch (Exception e) {
            userPwdHash = null;
        }
    }


    public CPUser(String userFullName,String userEmail,String userPwdHash )
    {
        this._ID = _ID;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        try {
            setUserPwd(userPwdHash);
        } catch (Exception e) {
            userPwdHash = null;
        }
    }


    public String getUserPwdHash() {
        return userPwdHash;
    }

    public int getUserId() {
        return _ID;
    }

    public void setUserPwd(String userPassword) throws Exception {
        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(userPassword.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        this.userPwdHash = hexString.toString();
    }


    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15})(\\s([a-zA-Z]{2,15}))+)$");
        Matcher matcher =
                pattern.matcher(userFullName);
        if (matcher.find())
            this.userFullName = userFullName;
        else
            throw new Exception("Name must contains only letters and must consists of at least 2 words (Private and Last name)");
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) throws Exception {
        Pattern pattern =
                Pattern.compile("^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$");
        Matcher matcher =
                pattern.matcher(userEmail);
        if (matcher.find())
            this.userEmail = userEmail;
        else
            throw new Exception("Double check your email address, I think you got a mistake there");
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public void setUserPwdHash(String userPwdHash) {
        this.userPwdHash = userPwdHash;
    }

    public ContentValues toContentValues()
    {
        final ContentValues contentValues = new ContentValues();

        contentValues.put("_ID",this.getUserId());
        contentValues.put("userFullName",this.getUserFullName());
        contentValues.put("userEmail",this.getUserEmail());
        contentValues.put("userPwdHash",this.getUserPwdHash());

        return  contentValues;
    }

    public static Uri getCPUser_URI()
    {
        return Uri.parse("content://com.foodie.app/cpuser");
    }
}
