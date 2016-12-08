package com.example.daniel.java_project.entities;

/**
 * Created by Daniel on 11/30/2016.
 */
enum FIELD{NAME, EMAIL, PHONE, PWD, STREET, CITY, HOUSE, ZIP, COUNTRY, URL, COST, RATING}

public class InputException extends Exception {

    private FIELD field;

    public InputException(String message, FIELD field) {
        super(message);
        this.field=field;
    }
}
