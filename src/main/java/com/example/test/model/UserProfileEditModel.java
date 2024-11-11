package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.logindata.LoginDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserProfileEditModel {

    private Connection connection;
    private final String userName = LoginDetails.getUserName();

    public UserProfileEditModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }


    public String changeUserDetailsOne(String[] data, String[] column) throws SQLException {

        String sql = "UPDATE user SET "+column[0] +" = ? WHERE userName = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,data[0]);
        pst.setString(2,userName);

        int result = pst.executeUpdate();

        if(result>0){
            return "Successfully updated your profile";
        }
        else{
            return "something went wrong, failed to update your profile, try again later";
        }

    }

    public String changeUserDetailsTwo(String[] data, String[] column) throws SQLException {

        String sql = "UPDATE user SET "+column[0] +" = ?, "+ column[1] +" = ? WHERE userName = ?";

        System.out.println(sql);

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,data[0]);
        pst.setString(2,data[1]);
        pst.setString(3,userName);

        int result = pst.executeUpdate();

        if(result>0){
            return "Successfully updated your profile";
        }
        else{
            return "something went wrong, failed to update your profile, try again later";
        }
    }

    public String changeUserDetailsThree(String[] data, String[] column) throws SQLException {

        String sql = "UPDATE user SET "+column[0] +" = ?, "+ column[1] +" = ?, "+ column[2]+" = ? WHERE userName = ?";

        System.out.println(sql);

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,data[0]);
        pst.setString(2,data[1]);
        pst.setString(3,data[2]);
        pst.setString(4,userName);

        int result = pst.executeUpdate();

        if(result>0){
            return "Successfully updated your profile";
        }
        else{
            return "something went wrong, failed to update your profile, try again later";
        }
    }

    public String changeUserDetailsFour(String[] data, String[] column) throws SQLException {

        String sql = "UPDATE user SET "+column[0] +" = ?, "+ column[1] +" = ?, "+ column[2]+" = ?, "+column[3]+" =? WHERE userName = ?";

        System.out.println(sql);

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,data[0]);
        pst.setString(2,data[1]);
        pst.setString(3,data[2]);
        pst.setString(4,data[3]);
        pst.setString(5,userName);

        int result = pst.executeUpdate();

        if(result>0){
            return "Successfully updated your profile";
        }
        else{
            return "something went wrong, failed to update your profile, try again later";
        }
    }
}
