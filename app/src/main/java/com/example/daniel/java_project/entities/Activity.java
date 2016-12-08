package com.example.daniel.java_project.entities;


import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 12/1/2016.
 */

public class Activity {

    private int _ID;

    private String activityName;

    private Calendar activityDate;

    //private Calendar activityEnd; //for future use

    private String activityDescription;

    private double activityCost;

    private double activityRating;

    private int businessId;

    //private Image activityImages; //for future use


    public Activity(String name, String activityDescription, double activityCost, double activityRating) throws Exception {
        setActivityName(activityName);
        activityDate = Calendar.getInstance();
        setActivityDescription(activityDescription);
        setActivityCost(activityCost);
        setActivityRating(activityRating);
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
        Matcher matcher =
                pattern.matcher(activityName);
        if(matcher.find())
            this.activityName = activityName;
        else
            throw new InputException("Activity name must contains at least 2 characters!", FIELD.NAME);
    }

    public Calendar getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Calendar activityDate) {
        this.activityDate = activityDate;
    }

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
        if(activityCost<0)
            throw new InputException("Cost must be a positive number", FIELD.COST);
        this.activityCost = activityCost;
    }

    public double getActivityRating() {
        return activityRating;
    }

    public void setActivityRating(double activityRating) throws Exception{
        if(activityRating>5 || activityRating<1)
            throw new InputException("Rating must be between 1 to 5", FIELD.RATING);
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


}
