package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.UnitDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewUnitModel {

    private Connection connection;

    public AddNewUnitModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }

    public String getNewHouseId() throws SQLException {

        String sql = "select houseId from house order by houseId desc limit 1;";
        PreparedStatement pst = connection.prepareStatement(sql);

        ResultSet result = pst.executeQuery();

        if(!result.next()){
           return "H001";
        }
        else{

            String lastId = result.getString("houseId");
            String subStr = lastId.substring(1);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("H%03d", id);

        }
    }

    public String addNewUnit(UnitDto newUnit) throws SQLException {

        String sql = "insert into house values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,newUnit.getHouseId());
        pst.setInt(2,newUnit.getBedroom());
        pst.setInt(3,newUnit.getBathroom());
        pst.setString(4,newUnit.getRentOrBuy());
        pst.setString(5,newUnit.getTotalValue());
        pst.setString(6,newUnit.getSecurityCharge());
        pst.setString(7,newUnit.getMonthlyRent());
        pst.setString(8,newUnit.getStatus());
        pst.setString(9,newUnit.getHouseType());
        pst.setInt(10,newUnit.getFloorNo());

        int result = pst.executeUpdate();

        if(result>0){
            return "successfully add new unit to the system";
        }
        else{
            return "something went wrong, failed to add new unit to the system, please try again later";
        }
    }

    public String editUnit(UnitDto newUnit) throws SQLException {

        String sql = "UPDATE house SET bedroomAmount = ?, bathroomAmount = ?, RentOrBuy = ?, totalValue = ?, security_charge = ?, monthlyRent = ?, status = ?, houseType = ?, floorNo = ? WHERE houseId = ?";
        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setInt(1,newUnit.getBedroom());
        pst.setInt(2,newUnit.getBathroom());
        pst.setString(3,newUnit.getRentOrBuy());
        pst.setString(4,newUnit.getTotalValue());
        pst.setString(5,newUnit.getSecurityCharge());
        pst.setString(6,newUnit.getMonthlyRent());
        pst.setString(7,newUnit.getStatus());
        pst.setString(8,newUnit.getHouseType());
        pst.setInt(9,newUnit.getFloorNo());
        pst.setString(10,newUnit.getHouseId());

        int result = pst.executeUpdate();

        if(result>0){
            return "successfully update the unit";
        }
        else{
            return "something went wrong, failed to update the unit, please try again later";
        }

    }
}












