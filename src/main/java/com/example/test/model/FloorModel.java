package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.FloorDto;
import com.example.test.dto.tm.FloorTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloorModel {

    private Connection connection;

    public FloorModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }


    public ObservableList<FloorTm> loadTableData() throws SQLException {

        String sql = "select * from floor";

        PreparedStatement pst = connection.prepareStatement(sql);

        ResultSet result = pst.executeQuery();

        ObservableList<FloorTm> observableList = FXCollections.observableArrayList();

        while (result.next()){

            int floor = result.getInt("floorNo");
            int houseCount = result.getInt("noOfHouses");

            FloorTm f1 = new FloorTm(floor,houseCount);

            observableList.add(f1);

        }

        return observableList;

    }

    public String deleteFloor(FloorTm floorData) throws SQLException {

        String sql = "delete from floor where floorNo = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1,floorData.getFloorNo());

        int result = pst.executeUpdate();

        if(result>0){
            return "Successfully deleted the floor";
        }
        else{
            return "Failed to delete the floor, Try again later";
        }
    }

    public String saveNewFloor(FloorDto floorDto) throws SQLException {

        String checkSql = "select * from floor where floorNo = ?";

        PreparedStatement checkPst = connection.prepareStatement(checkSql);
        checkPst.setInt(1,floorDto.getFloorNo());

        ResultSet res = checkPst.executeQuery();

        if(res.next()){

            return "This Floor No Already Exits, Try new Floor No";
        }

        String sql = "insert into floor values(?,?)";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1,floorDto.getFloorNo());
        pst.setInt(2,floorDto.getNoOfHouses());

        int result = pst.executeUpdate();

        if(result>0){
            return "successfully add the new floor to the system";
        }
        else{
            return "something went wrong, failed to add new floor, please try again later";
        }

    }

    public ObservableList<String> getFloorNumbers() throws SQLException {

        String sql = "select floorNo from floor order by floorNo asc";

        PreparedStatement pst = connection.prepareStatement(sql);

        ResultSet result = pst.executeQuery();

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.add("Select");

        while (result.next()){

           int floor =  result.getInt("floorNo");
           observableList.add(String.valueOf(floor));

        }

        return observableList;
    }

    public boolean checkThisFloorIsUsed(FloorTm floorData) throws SQLException, ClassNotFoundException {

        String sql = "select * from house where floorNo = ?";
        ResultSet result = CrudUtility.execute(sql,floorData.getFloorNo());

        return result.next();
    }
}






