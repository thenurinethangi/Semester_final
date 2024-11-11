package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.SignUpDto;
import com.example.test.logindata.LoginDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserProfileModel {

    private Connection connection;

    public UserProfileModel() throws SQLException, ClassNotFoundException {
        connection = DBConnection.getInstance().getConnection();
    }

    public SignUpDto getUserProfileDetails() throws SQLException {

        String sql = "select * from user where userName = ?";

        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setString(1,LoginDetails.getUserName() );

        ResultSet result = pst.executeQuery();

        if(result.next()){

            SignUpDto signUpDto = new SignUpDto();

            signUpDto.setUserName(result.getString(1));
            signUpDto.setName(result.getString(2));
            signUpDto.setEmail(result.getString(3));
            signUpDto.setPassword(result.getString(4));

            return  signUpDto;
        }
        else{
            return null;
        }
    }
}






