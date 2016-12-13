package com.foodie.app.backend;

import android.net.Uri;

/**
 * Created by Daniel on 12/13/2016.
 */

public class AppContract {
    /**
     * The authority for the contacts provider
     */
    public static final String AUTHORITY = "com.example.daniel.app.provider";
    /**
     * A content:// style uri to the authority for the contacts provider
     */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static class Student {
        public static final String STUDENT_ID = "_id";
        public static final String STUDENT_NAME = "name";
        public static final String STUDENT_PASSWORD = "password";
        /**
         * The content:// style URI for this table
         */
        public static final Uri STUDENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "students");
    }

    public static class Course {
        public static final String COURSE_ID = "_id";
        public static final String COURSE_NAME = "name";

        /**
         * The content:// style URI for this table
         */
        public static final Uri COURSE_URI = Uri.withAppendedPath(AUTHORITY_URI, "courses");
    }

}
