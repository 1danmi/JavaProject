package com.foodie.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.foodie.app.backend.AppContract;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.foodie.app.backend.AppContract.CPUser.CPUSER_EMAIL;
import static com.foodie.app.backend.AppContract.CPUser.CPUSER_FULL_NAME;
import static com.foodie.app.backend.AppContract.CPUser.CPUSER_ID;


/**
 * Created by Daniel on 12/13/2016.
 */

public class Converters {

    public static ContentValues CPUserToContentValues(CPUser cpuser) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CPUSER_ID, cpuser.get_ID());
        contentValues.put(AppContract.CPUser.CPUSER_EMAIL, cpuser.getUserEmail());
        contentValues.put(CPUSER_FULL_NAME, cpuser.getUserFullName());
        contentValues.put(AppContract.CPUser.CPUSER_PWD, cpuser.getUserPwdHash());

        return contentValues;
    }


    public static ContentValues UserToContentValues(User user) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.User.USER_ID, user.get_ID());
        contentValues.put(AppContract.User.USER_EMAIL, user.getUserEmail());
        contentValues.put(AppContract.User.USER_FULL_NAME, user.getUserFullName());
        contentValues.put(AppContract.User.USER_PWD, user.getUserPwdHash());
        contentValues.put(AppContract.User.USER_PHONE_NUMBER, user.getUserPhoneNumber());
        contentValues.put(AppContract.User.USER_ADDRESS, user.getUserAddress());
        contentValues.put(AppContract.User.USER_IMAGE, user.getUserImage());

        return contentValues;
    }


    public static ContentValues BusinessToContentValues(Business business) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.Business.BUSINESS_ID, business.get_ID());
        contentValues.put(AppContract.Business.BUSINESS_NAME, business.getBusinessName());
        contentValues.put(AppContract.Business.BUSINESS_ADDRESS, business.getBusinessAddress());
        contentValues.put(AppContract.Business.BUSINESS_PHONE_NUMBER, business.getBusinessPhoneNo());
        contentValues.put(AppContract.Business.BUSINESS_EMAIL, business.getBusinessEmail());
        contentValues.put(AppContract.Business.BUSINESS_CPUSER_ID, business.getCpuserID());
        contentValues.put(AppContract.Business.BUSINESS_WEBSITE, business.getBusinessWebsite());
        contentValues.put(AppContract.Business.BUSINESS_LOGO, business.getBusinessLogo());

        return contentValues;
    }


    public static ContentValues ActivityToContentValues(Activity activity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.Activity.ACTIVITY_ID, activity.get_ID());
        contentValues.put(AppContract.Activity.ACTIVITY_NAME, activity.getActivityName());
        contentValues.put(AppContract.Activity.ACTIVITY_DESCRIPTION, activity.getActivityDescription());
        contentValues.put(AppContract.Activity.ACTIVITY_COST, activity.getActivityCost());
        contentValues.put(AppContract.Activity.ACTIVITY_BUSINESS_ID, activity.getBusinessId());
        contentValues.put(AppContract.Activity.ACTIVITY_IMAGE, activity.getActivityImages());
        contentValues.put(AppContract.Activity.ACTIVITY_RATING, activity.getActivityRating());
        contentValues.put(AppContract.Activity.ACTIVITY_FEATURE, activity.getFeature());


        return contentValues;
    }


    public static CPUser ContentValuesToCPUser(ContentValues contentValues) throws Exception {

        CPUser cpuser = new CPUser();
        cpuser.set_ID(contentValues.getAsInteger(CPUSER_ID));
        cpuser.setUserFullName(contentValues.getAsString(CPUSER_FULL_NAME));
        cpuser.setUserEmail(contentValues.getAsString(CPUSER_EMAIL));
        cpuser.setUserPwdHash(contentValues.getAsString(AppContract.CPUser.CPUSER_PWD));

        return cpuser;
    }


    public static User ContentValuesToUser(ContentValues contentValues) throws Exception {

        User user = new User();
        user.set_ID(contentValues.getAsInteger(AppContract.User.USER_ID));
        user.setUserFullName(contentValues.getAsString(AppContract.User.USER_FULL_NAME));
        user.setUserEmail(contentValues.getAsString(AppContract.User.USER_FULL_NAME));
        user.setUserPwdHash(contentValues.getAsByteArray(AppContract.User.USER_PWD));
        user.setUserPhoneNumber(contentValues.getAsString(AppContract.User.USER_PHONE_NUMBER));
        user.setUserAddress(contentValues.getAsString(AppContract.User.USER_ADDRESS));
        user.setUserImage(contentValues.getAsByteArray(AppContract.User.USER_IMAGE));

        return user;
    }


    public static Business ContentValuesToBusiness(ContentValues contentValues) throws Exception {

        Business business = new Business();
        business.set_ID(contentValues.getAsInteger(AppContract.Business.BUSINESS_ID));
        business.setBusinessName(contentValues.getAsString(AppContract.Business.BUSINESS_NAME));
        business.setBusinessAddress(contentValues.getAsString(AppContract.Business.BUSINESS_ADDRESS));
        business.setBusinessPhoneNo(contentValues.getAsString(AppContract.Business.BUSINESS_PHONE_NUMBER));
        business.setBusinessEmail(contentValues.getAsString(AppContract.Business.BUSINESS_EMAIL));
        business.setBusinessWebsite(contentValues.getAsString(AppContract.Business.BUSINESS_WEBSITE));
        business.setCpuserID(contentValues.getAsInteger(AppContract.Business.BUSINESS_CPUSER_ID));
        business.setBusinessLogo(contentValues.getAsByteArray(AppContract.Business.BUSINESS_LOGO));

        return business;
    }


    public static Activity ContentValuesToActivity(ContentValues contentValues) throws Exception {

        Activity activity = new Activity();
        activity.set_ID(contentValues.getAsInteger(AppContract.Activity.ACTIVITY_ID));
        activity.setActivityName(contentValues.getAsString(AppContract.Activity.ACTIVITY_NAME));
        activity.setActivityDescription(contentValues.getAsString(AppContract.Activity.ACTIVITY_DESCRIPTION));
        activity.setActivityCost(contentValues.getAsDouble(AppContract.Activity.ACTIVITY_COST));
        activity.setActivityRating(contentValues.getAsDouble(AppContract.Activity.ACTIVITY_RATING));
        activity.setBusinessId(contentValues.getAsInteger(AppContract.Activity.ACTIVITY_BUSINESS_ID));
        activity.setActivityImages(contentValues.getAsByteArray(AppContract.Activity.ACTIVITY_IMAGE));
        activity.setFeature(contentValues.getAsString(AppContract.Activity.ACTIVITY_FEATURE));

        return activity;
    }


    public static Cursor CPUserListToCursor(List<CPUser> cpusers) {
        String[] columns = new String[]
                {
                        AppContract.CPUser.CPUSER_ID,
                        AppContract.CPUser.CPUSER_FULL_NAME,
                        AppContract.CPUser.CPUSER_EMAIL,
                        AppContract.CPUser.CPUSER_PWD
                };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (CPUser cp : cpusers) {
            matrixCursor.addRow(new Object[]{cp.get_ID(), cp.getUserFullName(), cp.getUserEmail(), cp.getUserPwdHash()});
        }

        return matrixCursor;
    }


    public static Cursor UserListToCursor(List<User> users) {
        String[] columns = new String[]
                {
                        AppContract.User.USER_ID,
                        AppContract.User.USER_FULL_NAME,
                        AppContract.User.USER_EMAIL,
                        AppContract.User.USER_PWD,
                        AppContract.User.USER_PHONE_NUMBER,
                        AppContract.User.USER_ADDRESS,
                        AppContract.User.USER_IMAGE
                };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (User u : users) {
            matrixCursor.addRow(new Object[]{u.get_ID(), u.getUserFullName(), u.getUserEmail(), u.getUserPwdHash(),
                    u.getUserPhoneNumber(), u.getUserAddress(), u.getUserImage()});
        }

        return matrixCursor;
    }


    public static Cursor BusinessListToCursor(List<Business> businesses) {
        String[] columns = new String[]
                {
                        AppContract.Business.BUSINESS_ID,
                        AppContract.Business.BUSINESS_NAME,
                        AppContract.Business.BUSINESS_EMAIL,
                        AppContract.Business.BUSINESS_WEBSITE,
                        AppContract.Business.BUSINESS_PHONE_NUMBER,
                        AppContract.Business.BUSINESS_ADDRESS,
                        AppContract.Business.BUSINESS_CPUSER_ID,
                        AppContract.Business.BUSINESS_LOGO
                };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (Business b : businesses) {
            matrixCursor.addRow(new Object[]{b.get_ID(), b.getBusinessName(), b.getBusinessEmail(), b.getBusinessWebsite(),
                    b.getBusinessPhoneNo(), b.getBusinessAddress(), b.getCpuserID(), b.getBusinessLogo()});
        }

        return matrixCursor;
    }


    public static Cursor ActivitiesListToCursor(List<Activity> activities) {
        String[] columns = new String[]
                {
                        AppContract.Activity.ACTIVITY_ID,
                        AppContract.Activity.ACTIVITY_NAME,
                        AppContract.Activity.ACTIVITY_DESCRIPTION,
                        AppContract.Activity.ACTIVITY_COST,
                        AppContract.Activity.ACTIVITY_RATING,
                        AppContract.Activity.ACTIVITY_BUSINESS_ID,
                        AppContract.Activity.ACTIVITY_IMAGE,
                        AppContract.Activity.ACTIVITY_FEATURE
                };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (Activity a : activities) {
            matrixCursor.addRow(new Object[]{a.get_ID(), a.getActivityName(),  a.getActivityDescription(),
                    a.getActivityCost(), a.getActivityRating(), a.getBusinessId(), a.getActivityImages(), a.getFeature()});
        }

        return matrixCursor;
    }

    public static List<CPUser> cursorToCPUserList(Cursor cursor) {


        List<CPUser> result = new ArrayList<>();


        while(cursor.moveToNext()) {
            result.add(new CPUser(cursor.getInt(cursor.getColumnIndex(AppContract.CPUser.CPUSER_ID)),
                    cursor.getString(cursor.getColumnIndex( AppContract.CPUser.CPUSER_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndex( AppContract.CPUser.CPUSER_EMAIL)),
                    cursor.getString(cursor.getColumnIndex( AppContract.CPUser.CPUSER_PWD))
            ));
        }

        cursor.close();

        return  result;
    }


}
