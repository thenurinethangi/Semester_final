package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.tm.FloorTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FloorEditModel {

    private Connection connection;

    public FloorEditModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }


    public String updateFloor(FloorTm floorTm) throws SQLException {

        String sql = "UPDATE floor SET noOfHouses = ? WHERE floorNo = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1,floorTm.getNoOfHouses());
        pst.setInt(2,floorTm.getFloorNo());

        int result = pst.executeUpdate();

        if(result>0){
            return "Successfully updated the floor";
        }
        else{
            return "Something went wrong, failed to update the floor";
        }
    }
}





