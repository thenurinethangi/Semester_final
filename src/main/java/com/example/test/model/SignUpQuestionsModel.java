package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.SignInQuestionsDto;
import com.example.test.dto.SignUpDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpQuestionsModel {

    private Connection connection;

    public SignUpQuestionsModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }


    public String register(SignUpDto dto, SignInQuestionsDto signInQuestionsDto) throws SQLException {

        try {
            connection.setAutoCommit(false);
            String sqlOne = "insert into user values(?,?,?,?)";

            PreparedStatement pstOne = connection.prepareStatement(sqlOne);
            pstOne.setString(1, dto.getUserName());
            pstOne.setString(2, dto.getName());
            pstOne.setString(3, dto.getEmail());
            pstOne.setString(4, dto.getPassword());

            int no1 = pstOne.executeUpdate();

            if (no1 > 0) {

                String sqlTwo = "select userValidationNo from userValidation order by userValidationNo desc limit 1";

                PreparedStatement pstTwo = connection.prepareStatement(sqlTwo);

                ResultSet result = pstTwo.executeQuery();

                int validationNO = 0;

                if (result.next()) {

                    validationNO = result.getInt(1);
                    validationNO += 1;
                } else {
                    validationNO = 1;
                }

                String sqlThree = "insert into userValidation values(?,?,?,?,?)";

                PreparedStatement pstThree = connection.prepareStatement(sqlThree);
                pstThree.setInt(1, validationNO);
                pstThree.setString(2, signInQuestionsDto.getqOne());
                pstThree.setString(3, signInQuestionsDto.getqTwo());
                pstThree.setString(4, signInQuestionsDto.getqThree());
                pstThree.setString(5, signInQuestionsDto.getUserName());

                int no2 = pstThree.executeUpdate();

                if (no2 > 0) {

                    connection.commit();
                    return "All Done";

                } else {

                    connection.rollback();
                    return "Something went wrong with register the new admin, please try again later";
                }

            } else {
                connection.rollback();
                return "Something went wrong with register the new admin, please try again later";
            }
        } catch (Exception e) {

            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }

        return "0";
    }
}





