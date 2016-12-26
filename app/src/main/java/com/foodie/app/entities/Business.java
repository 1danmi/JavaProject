package com.foodie.app.entities;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Daniel on 12/8/2016.
 */

public class Business implements Serializable {

    private static final long serialVersionUID = 2L;


    private static int businessID=0;

    private int _ID;

    private String businessName;

    private String businessAddress;

    private String businessPhoneNo;

    private String businessEmail;

    private String businessWebsite;

    private int cpuserID;

    private byte[] businessLogo;

    public Business(){}

    public Business(String businessName, String businessAddress, String businessPhoneNo, String businessEmail, String businessWebsite, int cpuserID, byte[] businessLogo) throws Exception{
        set_ID(businessID+1);
        businessID++;
        setBusinessName(businessName);
        setBusinessAddress(businessAddress);
        setBusinessPhoneNo(businessPhoneNo);
        setBusinessEmail(businessEmail);
        setBusinessWebsite(businessWebsite);
        setCpuserID(cpuserID);
        setBusinessLogo(businessLogo);
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int Id) {
        _ID = Id;
    }

    public String getBusinessName() {
        return businessName;

    }

    public void setBusinessName(String businessName) throws Exception {
//        Pattern pattern =
//                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
//        Matcher matcher =
//                pattern.matcher(businessName);
//        if (matcher.find())
        this.businessName = businessName;
//        else
//            throw new InputException("Name can contains only letters", FIELD.NAME);
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }


    public String getBusinessPhoneNo() {
        return businessPhoneNo;
    }

    public void setBusinessPhoneNo(String businessPhoneNo) throws Exception {
        Pattern pattern =
                Pattern.compile("^(0\\d{1,2}-?\\d{7})$$");
        Matcher matcher =
                pattern.matcher(businessPhoneNo);
        if (matcher.find())
            this.businessPhoneNo = businessPhoneNo;
        else
            throw new Exception("Double check your phone number, I think you got a mistake there");
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) throws Exception {
        Pattern pattern =
                Pattern.compile("^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$");
        Matcher matcher =
                pattern.matcher(businessEmail);
        if (matcher.find())
            this.businessEmail = businessEmail;
        else
            throw new Exception("Double check your email address, I think you got a mistake there");

    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public void setBusinessWebsite(String businessWebsite) throws Exception {
        Pattern pattern =
                Pattern.compile("^((?:https\\:\\/\\/)|(?:http\\:\\/\\/)|(?:www\\.))?([a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{1,3}(?:\\??)[a-zA-Z0-9\\-\\._\\?\\,\\'\\/\\\\\\+&%\\$#\\=~]+)$");
        Matcher matcher =
                pattern.matcher(businessWebsite);
        if (matcher.find())
            this.businessWebsite = businessWebsite;
        else
            throw new Exception("Wrong URL");    }

    public int getCpuserID() {
        return cpuserID;
    }

    public void setCpuserID(int cpuserID) {
        this.cpuserID = cpuserID;
    }

    public byte[] getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(byte[] businessLogo) {
        this.businessLogo = businessLogo;
    }
}
