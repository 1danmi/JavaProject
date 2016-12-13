package com.foodie.app.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * Created by Daniel on 12/8/2016.
 */

public class Business {

    private String businessName;

    private static int _ID = 0;

    private String businessAddress;

    private String businessPhoneNo;

    private String businessEmail;

    private String businessWebsite;

    private int cpuserID;

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(Address address) throws Exception {
        this.businessAddress = address.toString();
    }

    public String getBusinessName() {
        return businessName;

    }

    public void setBusinessName(String businessName) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
        Matcher matcher =
                pattern.matcher(businessName);
        if(matcher.find())
            this.businessName = businessName;
        else
            throw new InputException("Street name must contain at least 2 characters!",FIELD.NAME );
    }

    public String getBusinessPhoneNo() {
        return businessPhoneNo;
    }

    public void setBusinessPhoneNo(String businessPhoneNo) throws Exception {
        Pattern pattern =
                Pattern.compile("^(0\\d{1,2}-?\\d{7})$$");
        Matcher matcher =
                pattern.matcher(businessPhoneNo);
        if(matcher.find())
            this.businessPhoneNo = businessPhoneNo;
        else
            throw new InputException("Double check your phone number, I think you got a mistake there",FIELD.PHONE);
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) throws Exception {
        Pattern pattern =
                Pattern.compile("^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$");
        Matcher matcher =
                pattern.matcher(businessEmail);
        if(matcher.find())
            this.businessEmail = businessEmail;
        else
            throw new InputException("Double check your email address, I think you got a mistake there", FIELD.NAME);

    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public void setBusinessWebsite(String businessWebsite) throws Exception {
        Pattern pattern =
                Pattern.compile("^((?:https\\:\\/\\/)|(?:http\\:\\/\\/)|(?:www\\.))?([a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{1,3}(?:\\??)[a-zA-Z0-9\\-\\._\\?\\,\\'\\/\\\\\\+&%\\$#\\=~]+)$");
        Matcher matcher =
                pattern.matcher(businessWebsite);
        if(matcher.find())
            this.businessWebsite = businessWebsite;
        else
            throw new InputException("Wrong URL", FIELD.URL);
    }

    public int getId() {
        return _ID;
    }

    public void setId(int Id) {
        _ID = Id;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public int getCpuserID() {
        return cpuserID;
    }

    public void setCpuserID(int cpuserID) {
        this.cpuserID = cpuserID;
    }
}
