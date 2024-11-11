package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.SignInQuestionsDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPasswordQuestionsModel {

    private Connection connection;


    public ForgotPasswordQuestionsModel() throws SQLException, ClassNotFoundException {

        connection= DBConnection.getInstance().getConnection();
    }


    public String getPassword(SignInQuestionsDto signInQuestionsDto) throws SQLException {

        String sql = "select questionOne,questionTwo,questionThree from userValidation where userName = ?";
        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setString(1,signInQuestionsDto.getUserName());
        ResultSet result = pst.executeQuery();

        if(!result.next()){
            return "This User Name does not exit";
        }
        else{

           String fQ = result.getString(1);
           String sQ = result.getString(2);
           String tQ = result.getString(3);

           if(fQ.equals(signInQuestionsDto.getqOne()) && sQ.equals(signInQuestionsDto.getqTwo()) && tQ.equals(signInQuestionsDto.getqThree())){

               String sqlTwo = "select password from user where userName = ?";

               PreparedStatement pstTwo = connection.prepareStatement(sqlTwo);
               pstTwo.setString(1,signInQuestionsDto.getUserName());

               ResultSet res = pstTwo.executeQuery();

               if(!res.next()){
                  return "Something wrong with getting password back, try again later";
               }
               else{
                   return res.getString(1);
               }
           }
           else{

               return "Can't get password because your answers are incorrect";

           }

        }
    }
}








