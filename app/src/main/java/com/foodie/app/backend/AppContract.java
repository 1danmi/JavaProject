package com.foodie.app.backend;

import android.net.Uri;

/**
 * Created by Daniel on 12/13/2016.
 */

public class AppContract {
    /**
     * The authority for the contacts provider
     */
    public static final String AUTHORITY = "com.foodie.app.provider";
    /**
     * A content:// style uri to the authority for the contacts provider
     */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static class CPUser {
        public static final String CPUSER_ID = "_ID";
        public static final String CPUSER_FULL_NAME = "userFullName";
        public static final String CPUSER_EMAIL = "userEmail";
        public static final String CPUSER_PWD = "userPwdHash";
        /**
         * The content:// style URI for this table
         */
        public static final Uri STUDENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "cpuser");
    }

    public static class User {
        public static final String USER_ID = "_ID";
        public static final String USER_FULL_NAME = "userFullName";
        public static final String USER_EMAIL = "userEmail";
        public static final String USER_PWD = "userPwdHash";
        public static final String USER_PHONE_NUMBER = "userPhoneNumber";
        public static final String USER_ADDRESS = "userAddress";
        public static final String USER_IMAGE = "userImage";


        /**
         * The content:// style URI for this table
         */
        public static final Uri COURSE_URI = Uri.withAppendedPath(AUTHORITY_URI, "user");
    }

    public static class Business {
        public static final String BUSINESS_ID = "_ID";
        public static final String BUSINESS_NAME = "businessName";
        public static final String BUSINESS_EMAIL = "businessEmail";
        public static final String BUSINESS_WEBSITE = "businessWebsite";
        public static final String BUSINESS_PHONE_NUMBER = "businessPhoneNo";
        public static final String BUSINESS_ADDRESS = "businessAddress";
        public static final String BUSINESS_CPUSER_ID = "cpuserID";
        public static final String BUSINESS_LOGO = "businessLogo";


        /**
         * The content:// style URI for this table
         */
        public static final Uri STUDENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "business");
    }


    public static class Activity {
        public static final String ACTIVITY_ID = "_ID";
        public static final String ACTIVITY_NAME = "activityName";
        public static final String ACTIVITY_DATE = "activityDate";
        public static final String ACTIVITY_DESCRIPTION = "activityDescription";
        public static final String ACTIVITY_COST = "activityCost";
        public static final String ACTIVITY_RATING = "activityRating";
        public static final String ACTIVITY_BUSINESS_ID = "businessID";
        public static final String ACTIVITY_Image = "activityImage";
        public static final String ACTIVITY_FEATURE = "activityFeature";

        /**
         * The content:// style URI for this table
         */
        public static final Uri STUDENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "activity");
    }
}
