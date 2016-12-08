package com.example.daniel.java_project.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 11/30/2016.
 */

public class Address {
    private String street;
    private String city;
    private int houseNumber;
    private String zip;
    private String country;


    public Address() {

    }

    public Address(String street, String city, String houseNumber, String zip, String country) throws Exception {
        setStreet(street);
        setCity(city);
        setHouseNumber(houseNumber);
        setZip(zip);
        setCountry(country);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
        Matcher matcher =
                pattern.matcher(country);
        if(matcher.find())
            this.country = country;
        else
            throw new InputException("Country name must contain at least 2 characters!", FIELD.COUNTRY);
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
        Matcher matcher =
                pattern.matcher(street);
        if(matcher.find())
            this.street = street;
        else
            throw new InputException("Street name must contain at least 2 characters!", FIELD.STREET);

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
        Matcher matcher =
                pattern.matcher(city);
        if(matcher.find())
            this.city = city;
        else
            throw new InputException("City name must contain at least 2 characters!", FIELD.CITY);
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) throws Exception {
        Pattern pattern =
                Pattern.compile("^\\d{1,4}$");
        Matcher matcher =
                pattern.matcher(houseNumber);
        if(matcher.find())
            this.houseNumber = Integer.parseInt(houseNumber);
        else
            throw new InputException("House number can contain only digits", FIELD.HOUSE);

    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) throws Exception {
        Pattern pattern =
                Pattern.compile("^\\d{5,7}$");
        Matcher matcher =
                pattern.matcher(zip);
        if(matcher.find())
            this.zip = zip;
        else
            throw new InputException("Zip number must be 5-7 characters long", FIELD.ZIP);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                " houseNumber=" + houseNumber +
                " city='" + city + '\'' +
                " zip='" + zip + '\'' +
                '}';
    }
}
