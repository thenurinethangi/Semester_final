package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.tm.HouseStatusCheckTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseStatusCheckModel {
    public ObservableList<HouseStatusCheckTm> getAllHouseInspectChecks() throws SQLException, ClassNotFoundException {

        String sql = "select * from houseStatusCheck";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<HouseStatusCheckTm> allHouseInspectChecks = FXCollections.observableArrayList();

        while(result.next()){
            String checkNo = result.getString(1);
            String livingRoomStatus = result.getString(2);
            String kitchenStatus = result.getString(3);
            String bedRoomsStatus = result.getString(4);
            String bathRoomsStatus = result.getString(5);
            String totalHouseStatus = result.getString(6);
            String tenantId = result.getString(7);
            String houseId = result.getString(8);
            String estimatedCostForRepair = result.getString(9);
            String isPaymentDone = result.getString(10);

            HouseStatusCheckTm houseStatusCheck = new HouseStatusCheckTm(checkNo,livingRoomStatus,kitchenStatus,bedRoomsStatus,bathRoomsStatus,totalHouseStatus,tenantId,houseId,estimatedCostForRepair,isPaymentDone);
            allHouseInspectChecks.add(houseStatusCheck);
        }

        return allHouseInspectChecks;
    }

    public HouseStatusCheckDto getLastInspectCheckByTenant(String tenantId) throws SQLException, ClassNotFoundException {

        String sql = "select totalHouseStatus,isPaymentDone from housestatuscheck where tenantId = ? order by checkNumber desc limit 1";
        ResultSet result = CrudUtility.execute(sql,tenantId);

        HouseStatusCheckDto houseStatusCheckDto = new HouseStatusCheckDto();

        if(result.next()){

            houseStatusCheckDto.setTotalHouseStatus(result.getString("totalHouseStatus"));
            houseStatusCheckDto.setIsPaymentDone(result.getString("isPaymentDone"));
        }

        return houseStatusCheckDto;
    }

    public String addNewHouseStatusCheck(HouseStatusCheckDto houseStatusCheckDto) throws SQLException, ClassNotFoundException {

        String id = generateNewHouseStatusCheckId();

        String sql = "INSERT INTO housestatuscheck VALUES(?,?,?,?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,id,houseStatusCheckDto.getLivingRoomStatus(),houseStatusCheckDto.getKitchenStatus(),houseStatusCheckDto.getBedRoomsStatus(),houseStatusCheckDto.getBathRoomsStatus(),houseStatusCheckDto.getTotalHouseStatus(),
                houseStatusCheckDto.getTenantId(),houseStatusCheckDto.getHouseId(),houseStatusCheckDto.getEstimatedCostForRepair(),houseStatusCheckDto.getIsPaymentDone());

        return result ? "Successfully Added New House Inspection" : "Failed To Add New House Inspection,Try Again Later";
    }


    public String generateNewHouseStatusCheckId() throws SQLException, ClassNotFoundException {

        String sql = "select checkNumber from housestatuscheck order by checkNumber desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("checkNumber");
            String subStr = lastId.substring(6);
            System.out.println(subStr);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("CHECK-%05d", id);

        }
        else{
            return "CHECK-00001";
        }

    }

    public boolean changeStatus(String checkNumber, String status) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE housestatuscheck SET isPaymentDone = ? WHERE checkNumber = ?";
        boolean result = CrudUtility.execute(sql,status,checkNumber);

        return result;
    }
}




