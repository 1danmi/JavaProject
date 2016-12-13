package com.foodie.app.entities;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 11/28/2016.
 */

public class User {

    private static int _ID = 0;

    private String userPhoneNumber;

    private byte[] userPwdHash;

    private String userAddress;

    private String userFullName; //We are using full name instead of private and last name separately in order to use Google Sign-In in the future.

    private String userEmail;

    //private Image userImage; //For future use.

    public User(String userFullName, String userEmail, String userPhoneNumber, String password,Address address) throws Exception {

        _ID++;
        setUserFullName(userFullName);
        setUserEmail(userEmail);
        setUserPhoneNumber(userPhoneNumber);
        setUserPwdHash(password);
        setUserAddress(address);
    }


    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))+)$");
        Matcher matcher =
                pattern.matcher(userFullName);
        if(matcher.find())
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
        if(matcher.find())
            this.userEmail = userEmail;
        else
            throw new InputException("Double check your email address, I think you got a mistake there",FIELD.EMAIL);
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) throws Exception {
        Pattern pattern =
                Pattern.compile("^(0\\d{1,2}-?\\d{7})$$");
        Matcher matcher =
                pattern.matcher(userPhoneNumber);
        if(matcher.find())
            this.userPhoneNumber = userPhoneNumber;
        else
            throw new InputException("Double check your phone number, I think you got a mistake there",FIELD.PHONE);

    }

    public boolean checkUserPwd(String inputPassword) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(inputPassword.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        return digest == this.userPwdHash;
    }

    public void setUserPwdHash(String userPassword) throws Exception {
        if(userPassword.length()<6)
            throw new InputException("Password must contains at least 6 characters", FIELD.PWD);
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(userPassword.getBytes("UTF-8")); // Change this to "UTF-16" if needed.
        this.userPwdHash = md.digest();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Address address) throws Exception {
        this.userAddress = address.toString();
    }

    public int getUserId() {
        return _ID;
    }
//    public void setUserId(int _ID) {
//        this._ID = _ID;
//    }

}
