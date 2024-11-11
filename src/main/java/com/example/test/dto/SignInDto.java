package com.example.test.dto;

public class SignInDto {

    private String userName;
    private String password;

    public SignInDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public SignInDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
