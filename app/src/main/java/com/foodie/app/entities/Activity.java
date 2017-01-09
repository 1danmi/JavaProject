package com.foodie.app.entities;


import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Daniel on 12/1/2016.
 */

public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int _ID = 0;

    private String activityName;

    private String activityDescription;

    private double activityCost;

    private double activityRating;

    private int businessId;

    private byte[] activityImages; //for future use

    private String feature;

    public Activity() {
    }

    public Activity(String activityName, String activityDescription, double activityCost, double activityRating, int businessId, byte[] activityImages, String feature) throws Exception {
        set_ID(_ID+1);
        _ID++;
        setActivityName(activityName);
        setActivityDescription(activityDescription);
        setActivityCost(activityCost);
        setActivityRating(activityRating);
        setBusinessId(businessId);
        setActivityImages(activityImages);
        setFeature(feature);
    }

    public Activity(int id,String activityName, String activityDescription, double activityCost, double activityRating, int businessId, byte[] activityImages, String feature) throws Exception {
        set_ID(id);
        setActivityName(activityName);
        setActivityDescription(activityDescription);
        setActivityCost(activityCost);
        setActivityRating(activityRating);
        setBusinessId(businessId);
        setActivityImages(activityImages);
        setFeature(feature);
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
        Matcher matcher =
                pattern.matcher(activityName);
        if (matcher.find())
            this.activityName = activityName;
        else
            throw new Exception("Activity name must contains at least 2 characters!");
    }

//    public Calendar getActivityDate() {
//        return activityDate;
//    }
//
//    public void setActivityDate(Calendar activityDate) {
//        this.activityDate = activityDate;
//    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public double getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(double activityCost) throws Exception {
        if (activityCost < 0)
            throw new Exception("Cost must be a positive number");
        this.activityCost = activityCost;
    }

    public double getActivityRating() {
        return activityRating;
    }

    public void setActivityRating(double activityRating) throws Exception {
        if (activityRating > 5 || activityRating < 1)
            throw new Exception("Rating must be between 1 to 5");
        this.activityRating = activityRating;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        //if(!isExist(businessId, TYPE.BUSINESS)
        //throw new Exception("Business does not exist");
        this.businessId = businessId;
    }

    public byte[] getActivityImages() {
        return activityImages;
    }

    public void setActivityImages(byte[] activityImages) {
        this.activityImages = activityImages;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
