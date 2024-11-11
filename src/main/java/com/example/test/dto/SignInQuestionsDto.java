package com.example.test.dto;

public class SignInQuestionsDto {

    private String qOne;
    private String qTwo;
    private String qThree;
    private String userName;

    public SignInQuestionsDto(String qOne, String qTwo, String qThree, String userName) {
        this.qOne = qOne;
        this.qTwo = qTwo;
        this.qThree = qThree;
        this.userName = userName;
    }

    public SignInQuestionsDto(String qOne, String qTwo, String qThree) {
        this.qOne = qOne;
        this.qTwo = qTwo;
        this.qThree = qThree;
    }

    public SignInQuestionsDto() {
    }

    public String getqOne() {
        return qOne;
    }

    public void setqOne(String qOne) {
        this.qOne = qOne;
    }

    public String getqTwo() {
        return qTwo;
    }

    public void setqTwo(String qTwo) {
        this.qTwo = qTwo;
    }

    public String getqThree() {
        return qThree;
    }

    public void setqThree(String qThree) {
        this.qThree = qThree;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "SignInQuestionsDto{" +
                "qOne='" + qOne + '\'' +
                ", qTwo='" + qTwo + '\'' +
                ", qThree='" + qThree + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
