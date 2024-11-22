package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.HouseTypeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseTypeEditModel {

    private Connection connection;

    public HouseTypeEditModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }

    public String UpdateHouseTypeAll(HouseTypeDto houseTypeDto, HouseTypeDto housTypeDtails) throws SQLException {

        String sqlOne = "select * from housetype where houseType = ?";
        PreparedStatement pStatement = connection.prepareStatement(sqlOne);
        pStatement.setString(1,houseTypeDto.getHouseType());

        ResultSet result = pStatement.executeQuery();

        if(result.next()){
            return "This House Type is  already exist";
        }

        String sqlTwo = "UPDATE housetype SET houseType = ?, description = ? WHERE houseType = ?";
        PreparedStatement pst = connection.prepareStatement(sqlTwo);
        pst.setString(1,houseTypeDto.getHouseType());
        pst.setString(2,houseTypeDto.getDescription());
        pst.setString(3,housTypeDtails.getHouseType());

        int res = pst.executeUpdate();

        if(res>0){
            return "Successfully update the House Type";
        }
        else{
            return "Something went wrong, failed to update the House Type, try again";
        }
    }


    public String UpdateHouseTypeDescription(HouseTypeDto houseTypeDto, HouseTypeDto housTypeDtails) throws SQLException {

        String sql = "UPDATE housetype SET houseType = ?, description = ? WHERE houseType = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,houseTypeDto.getHouseType());
        pst.setString(2,houseTypeDto.getDescription());
        pst.setString(3,housTypeDtails.getHouseType());

        int res = pst.executeUpdate();

        if(res>0){
            return "Successfully update the House Type";
        }
        else{
            return "Something went wrong, failed to update the House Type, try again";
        }
    }


    public String UpdateHouseTypeHouseType(HouseTypeDto houseTypeDto, HouseTypeDto housTypeDtails) throws SQLException {

        String sqlOne = "select * from housetype where houseType = ?";
        PreparedStatement pStatement = connection.prepareStatement(sqlOne);
        pStatement.setString(1,houseTypeDto.getHouseType());

        ResultSet result = pStatement.executeQuery();

        if(result.next()){
            return "This House Type is  already exist";
        }

        String sqlTwo = "UPDATE housetype SET houseType = ?, description = ? WHERE houseType = ?";
        PreparedStatement pst = connection.prepareStatement(sqlTwo);
        pst.setString(1,houseTypeDto.getHouseType());
        pst.setString(2,houseTypeDto.getDescription());
        pst.setString(3,housTypeDtails.getHouseType());

        int res = pst.executeUpdate();

        if(res>0){
            return "Successfully update the House Type";
        }
        else{
            return "Something went wrong, failed to update the House Type, try again";
        }

    }

}



