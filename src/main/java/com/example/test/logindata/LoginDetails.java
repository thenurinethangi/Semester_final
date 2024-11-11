package com.example.test.logindata;

public class LoginDetails {

    private  static String userName;
    private  static String password;
    private static  LoginDetails loginDetails;


    private LoginDetails(String u, String p){

        userName = u;
        password = p;
    }

    public static LoginDetails getInstance(String u, String p){
        if(loginDetails==null){

            loginDetails = new LoginDetails(u,p);
        }
        return loginDetails;

    }


    public static String getUserName() {

        return userName;
    }

    public static String getPassword() {

        return password;
    }

    public static void setUserName(String u) {

       userName = u;
    }

    public static void setPassword(String p) {

        password = p;
    }

}
