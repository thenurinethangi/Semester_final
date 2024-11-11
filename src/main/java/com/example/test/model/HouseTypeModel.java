package com.example.test.model;

import com.example.test.db.DBConnection;
import com.example.test.dto.HouseTypeDto;
import com.example.test.dto.tm.HouseTypeTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseTypeModel {

    private Connection connection;

    public HouseTypeModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }

    public ObservableList<HouseTypeTm> loadTableData() throws SQLException {

        String sql = "select * from housetype";

        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet result = pst.executeQuery();

        ObservableList<HouseTypeTm> tableData = FXCollections.observableArrayList();

        while(result.next()){

            String houseType = result.getString("houseType");
            String desc = result.getString("description");

            HouseTypeTm houseTypeTm = new HouseTypeTm(houseType,desc);

            tableData.add(houseTypeTm);
        }

        return tableData;
    }

    public String addNewHouseType(HouseTypeDto houseTypeDto) throws SQLException {

        String sql = "insert into housetype values(?,?)";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,houseTypeDto.getHouseType());
        pst.setString(2,houseTypeDto.getDescription());

        int result = pst.executeUpdate();

        if(result>0){
            return "successfully added new house type to the system";
        }
        else {
            return "something went wrong, failed to add new house type to the system, try again later";
        }
    }

    public String deleteHouseType(HouseTypeTm selectHouseType) throws SQLException {

        String sql = "delete from housetype where houseType = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,selectHouseType.getHouseType());

        int result = pst.executeUpdate();

        if(result>0){
            return "successfully delete the house type";
        }
        else{
            return "failed to delete the house type,try again later";
        }
    }
}






