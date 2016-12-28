package com.foodie.app.entities;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 11/28/2016.
 */

public class User implements Serializable {

    private static final long serialVersionUID = 4L;

    private int _ID;


    private String userFullName;

    private String userEmail;

    private String userPwdHash;

    private String userPhoneNumber;

    private String userAddress;

    private byte[] userImage;

    public User() {
    }

    public User(String userFullName, String userEmail, String userPhoneNumber, String password, String address) throws Exception {

        _ID++;
        setUserFullName(userFullName);
        setUserEmail(userEmail);
        setUserPhoneNumber(userPhoneNumber);
        setUserPwd(password);
        setUserAddress(address);
    }

    public User(int ID,String userFullName, String userEmail, String userPhoneNumber, String password, String address) throws Exception {

        set_ID(ID);
        setUserFullName(userFullName);
        setUserEmail(userEmail);
        setUserPhoneNumber(userPhoneNumber);
        setUserPwd(password);
        setUserAddress(address);
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int Id) {
        _ID = Id;
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

    public String getUserPwdHash() {
        return userPwdHash;
    }

    public void setUserPwdHash(String userPwdHash) {
        this.userPwdHash = userPwdHash;
    }

    public void setUserPwd(String userPassword) throws Exception {

        this.userPwdHash = userPassword;
    }


    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) throws Exception {
        Pattern pattern =
                Pattern.compile("^(0\\d{1,2}-?\\d{7})$$");
        Matcher matcher =
                pattern.matcher(userPhoneNumber);
        if (matcher.find())
            this.userPhoneNumber = userPhoneNumber;
        else
            throw new Exception("Double check your phone number, I think you got a mistake there");
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

}
