package com.foodie.app.entities;


import android.content.ContentValues;
import android.net.Uri;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 12/12/2016.
 */

public class CPUser implements Serializable {

    private static final long serialVersionUID = 3L;



    private int _ID = 0;

    private String userFullName;

    private String userEmail;


    private String userPwdHash;

    public String getUserPwdHash() {
        return userPwdHash;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }



    public void setUserPwd(String userPassword) throws Exception {
        if (userPassword.length() < 6)
            throw new Exception("Password must contains at least 6 characters");

        this.userPwdHash = userPassword;
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


    public void setUserPwdHash(String userPwdHash) {
        this.userPwdHash = userPwdHash;
    }

    public CPUser(){}
    public CPUser(int _ID, String userEmail, String userFullName, String userPwdHash) {
        this._ID = _ID;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.userPwdHash = userPwdHash;
    }

    public ContentValues toContentValues()
    {
        final ContentValues contentValues = new ContentValues();

        contentValues.put("_ID",this.get_ID());
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
