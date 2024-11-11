package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.SignUpDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpModel {

    private Connection connection;

    public SignUpModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }
    public String checkUserNameAlreadyExist(SignUpDto signUpDto) throws SQLException {

        String sql = "select userName from user where userName = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,signUpDto.getUserName());

        ResultSet result = pst.executeQuery();

        if(!result.next()){
          return "this user name not exist";
        }
        else{
            return "this user name exist";
        }

    }
}








