package com.foodie.app.entities;

import android.content.ContentValues;
import android.net.Uri;

import com.foodie.app.Helper.HelperClass;
import com.foodie.app.backend.AppContract;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Business implements Serializable {

    private static final long serialVersionUID = 2L;

    public static int businessID = 0;

    private String _ID;

    private String businessName;

    private String businessAddress;

    private String businessPhoneNo;

    private String businessEmail;

    private String businessWebsite;

    private String cpuserID;

    private byte[] businessLogo;

    //Empty constructor.
    public Business() {

        _ID = "";
        businessName = "";
        businessAddress = "";
        businessPhoneNo = "";
        businessEmail = "";
        businessWebsite = "";
        cpuserID = "";
        businessLogo = null;
    }

    //Full constructor
    public Business(String businessName, String businessAddress, String businessPhoneNo, String businessEmail, String businessWebsite, String cpuserID, byte[] businessLogo) throws Exception {

        setBusinessName(businessName);
        setBusinessAddress(businessAddress);
        setBusinessPhoneNo(businessPhoneNo);
        setBusinessEmail(businessEmail);
        setBusinessWebsite(businessWebsite);
        setCpuserID(cpuserID);
        setBusinessLogo(businessLogo);
    }

    public Business(String id, String businessName, String businessAddress, String businessPhoneNo, String businessEmail, String businessWebsite, String cpuserID, byte[] businessLogo) throws Exception {
        set_ID(id);
        setBusinessName(businessName);
        setBusinessAddress(businessAddress);
        setBusinessPhoneNo(businessPhoneNo);
        setBusinessEmail(businessEmail);
        setBusinessWebsite(businessWebsite);
        setCpuserID(cpuserID);
        setBusinessLogo(businessLogo);
    }

    public Business(String businessName, String businessAddress, String businessPhoneNo, String businessEmail, String businessWebsite) throws Exception {

        setBusinessName(businessName);
        setBusinessAddress(businessAddress);
        setBusinessPhoneNo(businessPhoneNo);
        setBusinessEmail(businessEmail);
        setBusinessWebsite(businessWebsite);

    }

    public Business(String id, String businessName, String businessAddress, String businessPhoneNo, String businessEmail, String businessWebsite) throws Exception {
        set_ID(id);
        setBusinessName(businessName);
        setBusinessAddress(businessAddress);
        setBusinessPhoneNo(businessPhoneNo);
        setBusinessEmail(businessEmail);
        setBusinessWebsite(businessWebsite);
    }


    public String get_ID() {
        return _ID;
    }

    public void set_ID(String Id) {
        _ID = Id;
    }


    public String getBusinessName() {
        return businessName;

    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public void setBusinessPhoneNo(String businessPhoneNo) {
        this.businessPhoneNo = businessPhoneNo;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public void setBusinessWebsite(String businessWebsite) {
        this.businessWebsite = businessWebsite;
    }

    public String getCpuserID() {
        return cpuserID;
    }

    public void setCpuserID(String cpuserID) {
        this.cpuserID = cpuserID;
    }

    public byte[] getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(byte[] businessLogo) {
        this.businessLogo = businessLogo;
    }


    public ContentValues toContentValues() {
        //    public Business(int id,String businessName, String businessAddress, String businessPhoneNo, String businessEmail, String businessWebsite, int cpuserID, byte[] businessLogo) throws Exception {


        final ContentValues contentValues = new ContentValues();

        contentValues.put(AppContract.Business.BUSINESS_ID, this.get_ID());
        contentValues.put(AppContract.Business.BUSINESS_NAME, this.getBusinessName());
        contentValues.put(AppContract.Business.BUSINESS_ADDRESS, this.getBusinessAddress());
        contentValues.put(AppContract.Business.BUSINESS_PHONE_NUMBER, this.getBusinessPhoneNo());
        contentValues.put(AppContract.Business.BUSINESS_EMAIL, this.getBusinessEmail());
        contentValues.put(AppContract.Business.BUSINESS_WEBSITE, this.getBusinessWebsite());
        contentValues.put(AppContract.Business.BUSINESS_CPUSER_ID, this.getCpuserID());
        contentValues.put(AppContract.Business.BUSINESS_LOGO, this.getBusinessLogo());


        return contentValues;
    }

    public static Uri getURI() {
        return Uri.parse("content://com.foodie.app/Business");
    }


    @Override
    public boolean equals(Object obj) {
        return this._ID.equals(((Business) obj)._ID);
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(AppContract.Business.BUSINESS_ID, this.get_ID());
        result.put(AppContract.Business.BUSINESS_NAME, this.getBusinessName());
    //    result.put(AppContract.Business.BUSINESS_LOGO, HelperClass.fromByteArraytoString(this.getBusinessLogo()));
        result.put(AppContract.Business.BUSINESS_CPUSER_ID, this.getCpuserID());
        result.put(AppContract.Business.BUSINESS_WEBSITE, this.getBusinessWebsite());
        result.put(AppContract.Business.BUSINESS_ADDRESS, this.getBusinessAddress());
        result.put(AppContract.Business.BUSINESS_EMAIL, this.getBusinessEmail());
        result.put(AppContract.Business.BUSINESS_PHONE_NUMBER, this.getBusinessPhoneNo());

        return result;
    }
}
