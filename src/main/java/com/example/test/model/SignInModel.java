package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.SignInDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInModel {

    private Connection connection;

    public SignInModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();

    }
    public String signInAuthentication(SignInDto signInDto) throws SQLException {

        String sql = "select userName,password from user where userName = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,signInDto.getUserName());

        ResultSet result = pst.executeQuery();

        if(!result.next()){

            return "This User Name does not exit";

        }
        else{

            String pWord = result.getString("password");

            if(pWord.equals(signInDto.getPassword())){
                return "All Correct";
            }
            else{
                return "Your Password is incorrect, try again";
            }
        }

    }
}
