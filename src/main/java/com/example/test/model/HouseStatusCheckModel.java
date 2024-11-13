package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.tm.HouseStatusCheckTm;
import com.example.test.dto.tm.LeaseAgreementTm;
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

    public HouseStatusCheckDto getLastInspectCheckByTenant(LeaseAgreementTm selectedLeaseAgreement) throws SQLException, ClassNotFoundException {

        String sql = "select totalHouseStatus,isPaymentDone from housestatuscheck where tenantId = ? order by checkNumber desc limit 1";
        ResultSet result = CrudUtility.execute(sql,selectedLeaseAgreement.getTenantId());

        HouseStatusCheckDto houseStatusCheckDto = new HouseStatusCheckDto();

        if(result.next()){

            houseStatusCheckDto.setTotalHouseStatus(result.getString("totalHouseStatus"));
            houseStatusCheckDto.setIsPaymentDone(result.getString("isPaymentDone"));
        }

        return houseStatusCheckDto;
    }
}




