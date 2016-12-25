package com.foodie.app.entities;


import android.content.ContentValues;
import android.net.Uri;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 12/12/2016.
 */

public class CPUser {

    private int _ID = -1;

    private String userFullName;

    private String userEmail;


    private byte[] userPwdHash;

    public byte[] getUserPwdHash() {
        return userPwdHash;
    }

    public int getUserId() {
        return _ID;
    }

    public boolean checkUserPwd(String inputPassword) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(inputPassword.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        return digest == this.userPwdHash;
    }

    public void setUserPwd(String userPassword) throws Exception {
        if (userPassword.length() < 6)
            throw new InputException("Password must contains at least 6 characters", FIELD.PWD);
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(userPassword.getBytes("UTF-8")); // Change this to "UTF-16" if needed.
        this.userPwdHash = md.digest();
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
            throw new InputException("Name must contains only letters and must consists of at least 2 words (Private and Last name)", FIELD.NAME);
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
            throw new InputException("Double check your email address, I think you got a mistake there", FIELD.EMAIL);
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public void setUserPwdHash(byte[] userPwdHash) {
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
