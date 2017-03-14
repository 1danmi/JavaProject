package com.foodie.app.database;

/**
 * Created by David on 16/12/2016.
 */

/**
 * A enum to especificate the status of an operation made by the class AsyncData
 */
public enum DataStatus {
    Success,
    Failed,
    AlreadyExist,
    InvalidArgumment,
    ConectionError,
    UnknowError
}
