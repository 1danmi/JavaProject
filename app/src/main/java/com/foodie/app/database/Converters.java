package com.foodie.app.database;

import android.content.ContentValues;

import com.foodie.app.backend.AppContract;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;

/**
 * Created by Daniel on 12/13/2016.
 */

public class Converters {

    public static ContentValues CPUserToContentValues(CPUser cpuser) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.CPUser.CPUSER_ID, cpuser.getUserId());
        contentValues.put(AppContract.CPUser.CPUSER_EMAIL, cpuser.getUserEmail());
        contentValues.put(AppContract.CPUser.CPUSER_FULL_NAME, cpuser.getUserFullName());
        contentValues.put(AppContract.CPUser.CPUSER_PWD, cpuser.getUserPwdHash());

        return contentValues;
    }


    public static ContentValues UserToContentValues(User user) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.User.USER_ID, user.getUserId());
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
        contentValues.put(AppContract.Business.BUSINESS_ID,business.getId());
        contentValues.put(AppContract.Business.BUSINESS_NAME,business.getBusinessName());
        contentValues.put(AppContract.Business.BUSINESS_ADDRESS,business.getBusinessAddress());
        contentValues.put(AppContract.Business.BUSINESS_PHONE_NUMBER,business.getBusinessPhoneNo());
        contentValues.put(AppContract.Business.BUSINESS_EMAIL,business.getBusinessEmail());
        contentValues.put(AppContract.Business.BUSINESS_CPUSER_ID,business.getCpuserID());
        contentValues.put(AppContract.Business.BUSINESS_WEBSITE,business.getBusinessWebsite());

        return contentValues;
    }


    public static ContentValues ActivityToContentValues(Activity activity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.Activity.ACTIVITY_ID, activity.get_ID());
        contentValues.put(AppContract.Activity.ACTIVITY_NAME, activity.getActivityName());
        contentValues.put(AppContract.Activity.ACTIVITY_DATE, activity.getActivityDate().toString());
        contentValues.put(AppContract.Activity.ACTIVITY_DESCRIPTION, activity.getActivityDescription());
        contentValues.put(AppContract.Activity.ACTIVITY_COST, activity.getActivityCost());
        contentValues.put(AppContract.Activity.ACTIVITY_BUSINESS_ID, activity.getBusinessId());
        contentValues.put(AppContract.Activity.ACTIVITY_Image, activity.getActivityImages());


        return contentValues;
    }



    public static CPUser ContentValuesToCPUser(ContentValues contentValues) throws Exception {

        CPUser cpuser = new CPUser();
        cpuser.set_ID(contentValues.getAsInteger(AppContract.CPUser.CPUSER_ID));
        cpuser.setUserFullName(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
        cpuser.setUserEmail(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
        cpuser.setUserPwdHash(contentValues.getAsByteArray(AppContract.CPUser.CPUSER_PWD));

        return cpuser;
    }

//  TODO: Finish the conversion methods

//    public static User ContentValuesToUser(ContentValues contentValues) throws Exception {
//
//        CPUser cpuser = new CPUser();
//        cpuser.set_ID(contentValues.getAsInteger(AppContract.CPUser.CPUSER_ID));
//        cpuser.setUserFullName(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
//        cpuser.setUserEmail(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
//        cpuser.setUserPwdHash(contentValues.getAsByteArray(AppContract.CPUser.CPUSER_PWD));
//
//        return cpuser;
//    }


//    public static Business ContentValuesToBusiness(ContentValues contentValues) throws Exception {
//
//        CPUser cpuser = new CPUser();
//        cpuser.set_ID(contentValues.getAsInteger(AppContract.CPUser.CPUSER_ID));
//        cpuser.setUserFullName(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
//        cpuser.setUserEmail(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
//        cpuser.setUserPwdHash(contentValues.getAsByteArray(AppContract.CPUser.CPUSER_PWD));
//
//        return cpuser;
//    }


//    public static Activity ContentValuesToActivity(ContentValues contentValues) throws Exception {
//
//        CPUser cpuser = new CPUser();
//        cpuser.set_ID(contentValues.getAsInteger(AppContract.CPUser.CPUSER_ID));
//        cpuser.setUserFullName(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
//        cpuser.setUserEmail(contentValues.getAsString(AppContract.CPUser.CPUSER_FULL_NAME));
//        cpuser.setUserPwdHash(contentValues.getAsByteArray(AppContract.CPUser.CPUSER_PWD));
//
//        return cpuser;
//    }
//
//
//    public static Cursor StudentListToCursor(List<Student> students) {
//        String[] columns = new String[]
//                {
//                        AcademyContract.Student.STUDENT_ID,
//                        AcademyContract.Student.STUDENT_NAME,
//                        AcademyContract.Student.STUDENT_PASSWORD
//                };
//
//        MatrixCursor matrixCursor = new MatrixCursor(columns);
//
//        for (Student s : students) {
//            matrixCursor.addRow(new Object[]{s.getId(), s.getName(), s.getPassword()});
//        }
//
//        return matrixCursor;
//    }


}
