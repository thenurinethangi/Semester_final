package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.PurchaseAgreementTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PurchaseAgreementModel {
    public ObservableList<PurchaseAgreementTm> getAllAgreements() throws SQLException, ClassNotFoundException {

        String sql = "select * from purchaseagreement where status = 'Active'";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<PurchaseAgreementTm> allAgreements = FXCollections.observableArrayList();

        while(result.next()){

           PurchaseAgreementTm purchaseAgreement = new PurchaseAgreementTm();
           purchaseAgreement.setPurchaseAgreementId(result.getString(1));
           purchaseAgreement.setHomeOwnerId(result.getString(2));
           purchaseAgreement.setHouseId(result.getString(3));
           purchaseAgreement.setPurchasePrice(result.getDouble(4));
           purchaseAgreement.setSignedDate(result.getString(5));
           purchaseAgreement.setStatus(result.getString(6));

           allAgreements.add(purchaseAgreement);
        }

        return allAgreements;
    }

    public ObservableList<String> getAllHouseIds() throws SQLException, ClassNotFoundException {

        String sql = "select houseId from purchaseagreement order by houseId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> houseIds = FXCollections.observableArrayList();
        houseIds.add("Select");

        while(result.next()){

            houseIds.add(result.getString("houseId"));
        }

        return houseIds;
    }

    public ObservableList<String> getAllOwnerIds() throws SQLException, ClassNotFoundException {

        String sql = "select homeOwnerId from purchaseagreement order by homeOwnerId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> ownerIds = FXCollections.observableArrayList();
        ownerIds.add("Select");

        while(result.next()){

            ownerIds.add(result.getString("homeOwnerId"));
        }

        return ownerIds;
    }


    public boolean addNewPurchaseAgreement(UnitDto unit, String ownerId) throws SQLException, ClassNotFoundException {

        String id = generateNewAgreementId();

        String sql = "insert into purchaseagreement values(?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,id,ownerId,unit.getHouseId(),Double.parseDouble(unit.getTotalValue()),String.valueOf(LocalDate.now()),"Active");
        return result;
    }


    private String generateNewAgreementId() throws SQLException, ClassNotFoundException {

        String sql = "select purchaseAgreementId from purchaseagreement order by purchaseAgreementId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("purchaseAgreementId");
            String subStr = lastId.substring(3);
            int id = Integer.parseInt(subStr);
            id+=1;
            String newId = String.format("PA-%04d", id);
            return newId;

        }

        return "PA-0001";
    }
}




