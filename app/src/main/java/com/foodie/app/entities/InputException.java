package com.foodie.app.entities;

/**
 * Created by Daniel on 11/30/2016.
 */
enum FIELD {
    NAME, EMAIL, PHONE, PWD, STREET, CITY, HOUSE, ZIP, COUNTRY, URL, COST, RATING
}

class InputException extends Exception {

    private FIELD field;

    InputException(String message, FIELD field) {
        super(message);
        this.field = field;
    }
}
