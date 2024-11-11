package com.example.test.validation;

import java.util.regex.Pattern;

public class UserInputValidation {

    private static final String userName = "^[@\\w]{7,}$";
    private static final String password = "^[@\\-\\w]{7,}$";
    private static final String name = "^[a-z A-Z.\\s]{5,}$";
    private static final String email = "^[\\w0-9.-]+@{1}gmail{1}.com{1}$";
    private static final String floorNo = "^(?!0$)([1-9][0-9]?)$";
    private static final String noOfHouses = "^[1-9]$";
    private static final String text = "^[A-Za-z0-9\\s!@#$%^&*_\\-+.=,:;\"'{}\\[\\]?()/]+$";
    private static final String decimalValues = "^\\d+(\\.\\d+)?$";
    private static final  String notApplicable = "(?i)^n/a$";
    private static final String numberLessThanTen = "^[1-9]$";
    private static final String phoneNo = "^0[7][0-9]{8}$";
    private static final String date = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    private static final String nic = "^([0-9]{9}[Vv]|[0-9]{12})$";
    private static final String address = "^(?:([Nn]o\\.?\\s?)?\\d+[A-Za-z]?/?\\d*,?\\s?)?([A-Za-z0-9'\\.\\-\\s]+,?\\s?)+([A-Za-z]+),?\\s?(\\d{5})?$";


    public UserInputValidation() {
    }

    public static boolean checkUserNameValidation(String u){

        return  Pattern.matches(userName,u);

    }

    public static boolean checkPasswordValidation(String p){

        return  Pattern.matches(password,p);

    }


    public static boolean checkNameValidation(String n){

        return  Pattern.matches(name,n);

    }


    public static boolean checkEmailValidation(String e){

        return  Pattern.matches(email,e);

    }

    public static boolean checkFloorNoValidation(String f){

        return  Pattern.matches(floorNo,f);

    }

    public static boolean checkOnOfHousesValidation(String h){

        return  Pattern.matches(noOfHouses,h);

    }


    public static boolean checkTextValidation(String t){

        return  Pattern.matches(text,t);

    }

    public static boolean checkDecimalValidation(String d){

        return  Pattern.matches(decimalValues,d);

    }

    public static boolean checkNotApplicableValidation(String n){

        return  Pattern.matches(notApplicable,n);

    }

    public static boolean checkNumberLessThanTenValidation(String n){

        return  Pattern.matches(numberLessThanTen,n);

    }

    public static boolean checkPhoneNoValidation(String pNo){

        return  Pattern.matches(phoneNo,pNo);

    }

    public static boolean checkDateValidation(String d){

        return  Pattern.matches(date,d);

    }


    public static boolean checkNICValidation(String n){

        return  Pattern.matches(nic,n);

    }

    public static boolean checkAddressValidation(String ad){

        return  Pattern.matches(address,ad);

    }

}
