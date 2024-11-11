package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.RequestTm;
import com.example.test.dto.tm.UnitTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitModel {

    private Connection connection;

    public UnitModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }

    public ObservableList<UnitTm> loadTable() throws SQLException {

        String sql = "select * from house";

        PreparedStatement pst = connection.prepareStatement(sql);

        ResultSet result = pst.executeQuery();

        ObservableList<UnitTm> tableData = FXCollections.observableArrayList();

        while (result.next()){

            String houseId = result.getString("houseId");
            int bedroom = result.getInt("bedroomAmount");
            int bathroom = result.getInt("bathroomAmount");
            String rentOrBuy = result.getString("RentOrBuy");
            String totalValue = result.getString("totalValue");
            String securityCharge = result.getString("security_charge");
            String rent = result.getString("monthlyRent");
            String status = result.getString("status");
            String houseType = result.getString("houseType");
            int floorNo = result.getInt("floorNo");

            UnitTm unitTm = new UnitTm(houseId,bedroom,bathroom,rentOrBuy,totalValue,securityCharge,rent,status,houseType,floorNo);
            tableData.add(unitTm);

        }
        return  tableData;
    }

    public String deleteUnit(UnitTm selectedRow) throws SQLException {

        String sql = "delete from house where houseId = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,selectedRow.getHouseId());

        int result = pst.executeUpdate();

        if(result>0){
           return "successfully deleted the unit";
        }
        else{
            return "something went wrong, failed to delete, try again later";
        }
    }

    public ObservableList<String> getAvailableRentHouses(RequestTm requestTm, String estimatedMonthlyBudgetForRent) throws SQLException, ClassNotFoundException {

        String sql = "select houseId,bedroomAmount,bathroomAmount,security_charge,monthlyRent,houseType,floorNo from house where RentOrBuy = ? and status = ? and houseType = ? and monthlyRent<=?";
        ResultSet result = CrudUtility.execute(sql,requestTm.getRentOrBuy(),"Available",requestTm.getHouseType(),estimatedMonthlyBudgetForRent);

        ObservableList<String> units = FXCollections.observableArrayList();

        while(result.next()) {

            String houseId = result.getString(1);
            int bedRooms = result.getInt(2);
            int bathRooms = result.getInt(3);
            String securityCharge = result.getString(4);
            String monthlyRent = result.getString(5);
            String houseType = result.getString(6);
            int floorNo = result.getInt(7);

            String unit = "House ID: " + houseId + "\nBedRooms: " + bedRooms + "\nBathRooms: " + bathRooms + "\nSecurity Payment: " + securityCharge + "\nMonthly Rent: " + monthlyRent + "\nHouseType: " + houseType + "\nFloor No: " + floorNo;
            System.out.println(unit);
            units.add(unit);
        }

        return units;
    }

    public ObservableList<String> getRecommendedRentHouses(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        String sql = "select houseId,bedroomAmount,bathroomAmount,security_charge,monthlyRent,houseType,floorNo from house where RentOrBuy = ? and status = ? and houseType = ?";

        ResultSet result = CrudUtility.execute(sql,requestTm.getRentOrBuy(),"Available",requestTm.getHouseType());

        ObservableList<String> units = FXCollections.observableArrayList();

        while(result.next()) {

            String houseId = result.getString(1);
            int bedRooms = result.getInt(2);
            int bathRooms = result.getInt(3);
            String securityCharge = result.getString(4);
            String monthlyRent = result.getString(5);
            String houseType = result.getString(6);
            int floorNo = result.getInt(7);

            String unit = "House ID: " + houseId + "\nBedRooms: " + bedRooms + "\nBathRooms: " + bathRooms + "\nSecurity Payment: " + securityCharge + "\nMonthly Rent: " + monthlyRent + "\nHouseType: " + houseType + "\nFloor No: " + floorNo;
            System.out.println(unit);
            units.add(unit);
        }

        return units;
    }

    public ObservableList<String> getAvailableSellHouses(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        System.out.println(requestTm.getRentOrBuy());
        System.out.println(requestTm.getHouseType());
        String sql = "select houseId,bedroomAmount,bathroomAmount,totalValue,houseType,floorNo from house where RentOrBuy = ? and status = ? and houseType = ?";

        ResultSet result = CrudUtility.execute(sql,"Sell","Available",requestTm.getHouseType());

        ObservableList<String> units = FXCollections.observableArrayList();

        while(result.next()) {

            String houseId = result.getString(1);
            int bedRooms = result.getInt(2);
            int bathRooms = result.getInt(3);
            String totalValue = result.getString(4);
            String houseType = result.getString(5);
            int floorNo = result.getInt(6);

            String unit = "House ID: " + houseId + "\nBedRooms: " + bedRooms + "\nBathRooms: " + bathRooms + "\nTotal Value Of The House: " + totalValue + "\nHouseType: " + houseType + "\nFloor No: " + floorNo;
            units.add(unit);
        }

        return units;
    }


    public ObservableList<UnitDto> getAvailableRentHousesAsUnitDto(RequestTm requestTm, String estimatedMonthlyBudgetForRent) throws SQLException, ClassNotFoundException {

        String sql = "select houseId,bedroomAmount,bathroomAmount,security_charge,monthlyRent,houseType,floorNo from house where RentOrBuy = ? and status = ? and houseType = ? and monthlyRent<=?";
        ResultSet result = CrudUtility.execute(sql,requestTm.getRentOrBuy(),"Available",requestTm.getHouseType(),estimatedMonthlyBudgetForRent);

        ObservableList<UnitDto> units = FXCollections.observableArrayList();

        while(result.next()) {

            String houseId = result.getString(1);
            int bedRooms = result.getInt(2);
            int bathRooms = result.getInt(3);
            String securityCharge = result.getString(4);
            String monthlyRent = result.getString(5);
            String houseType = result.getString(6);
            int floorNo = result.getInt(7);

            UnitDto unit = new UnitDto();
            unit.setHouseId(houseId);
            unit.setBedroom(bedRooms);
            unit.setBathroom(bathRooms);
            unit.setSecurityCharge(securityCharge);
            unit.setMonthlyRent(monthlyRent);
            unit.setHouseType(houseType);
            unit.setFloorNo(floorNo);

            units.add(unit);
        }

        return units;
    }

    public ObservableList<UnitDto> getRecommendedRentHousesAsUnitDto(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        String sql = "select houseId,bedroomAmount,bathroomAmount,security_charge,monthlyRent,houseType,floorNo from house where RentOrBuy = ? and status = ? and houseType = ?";

        ResultSet result = CrudUtility.execute(sql,requestTm.getRentOrBuy(),"Available",requestTm.getHouseType());

        ObservableList<UnitDto> units = FXCollections.observableArrayList();

        while(result.next()) {

            String houseId = result.getString(1);
            int bedRooms = result.getInt(2);
            int bathRooms = result.getInt(3);
            String securityCharge = result.getString(4);
            String monthlyRent = result.getString(5);
            String houseType = result.getString(6);
            int floorNo = result.getInt(7);

            UnitDto unit = new UnitDto();
            unit.setHouseId(houseId);
            unit.setBedroom(bedRooms);
            unit.setBathroom(bathRooms);
            unit.setSecurityCharge(securityCharge);
            unit.setMonthlyRent(monthlyRent);
            unit.setHouseType(houseType);
            unit.setFloorNo(floorNo);

            units.add(unit);
        }

        return units;
    }

    public ObservableList<UnitDto> getAvailableSellHousesAsUnitDto(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        System.out.println(requestTm.getRentOrBuy());
        System.out.println(requestTm.getHouseType());
        String sql = "select houseId,bedroomAmount,bathroomAmount,totalValue,houseType,floorNo from house where RentOrBuy = ? and status = ? and houseType = ?";

        ResultSet result = CrudUtility.execute(sql,"Sell","Available",requestTm.getHouseType());

        ObservableList<UnitDto> units = FXCollections.observableArrayList();

        while(result.next()) {

            String houseId = result.getString(1);
            int bedRooms = result.getInt(2);
            int bathRooms = result.getInt(3);
            String totalValue = result.getString(4);
            String houseType = result.getString(5);
            int floorNo = result.getInt(6);

            UnitDto unit = new UnitDto();
            unit.setHouseId(houseId);
            unit.setBedroom(bedRooms);
            unit.setBathroom(bathRooms);
            unit.setTotalValue(totalValue);
            unit.setHouseType(houseType);
            unit.setFloorNo(floorNo);

            units.add(unit);
        }

        return units;
    }

}
