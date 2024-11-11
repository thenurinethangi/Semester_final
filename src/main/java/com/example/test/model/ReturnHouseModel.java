package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.tm.ReturnHouseTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnHouseModel {
    public ObservableList<ReturnHouseTm> getAllReturns() throws SQLException, ClassNotFoundException {

        String sql = "select * from returnhouse";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<ReturnHouseTm> returnHouses = FXCollections.observableArrayList();

        while (result.next()){

            String returnNo = result.getString(1);
            String reason = result.getString(2);
            String date = result.getString(3);
            String tenantId = result.getString(4);
            String houseId = result.getString(5);
            String refundedAmount = result.getString(6);
            String expenseNo = result.getString(7);

            ReturnHouseTm returnHouse = new ReturnHouseTm(returnNo,reason,date,tenantId,houseId,refundedAmount,expenseNo);
            returnHouses.add(returnHouse);
        }

        return  returnHouses;
    }
}






