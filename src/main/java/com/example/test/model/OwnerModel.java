package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.CustomerDto;
import com.example.test.dto.OwnerDto;
import com.example.test.dto.tm.OwnerTm;
import com.example.test.dto.tm.PurchaseAgreementTm;
import com.example.test.dto.tm.RequestTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OwnerModel {
    public ObservableList<OwnerTm> getAllOwners() throws SQLException, ClassNotFoundException {

        String sql = "select * from homeowner";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<OwnerTm> owners = FXCollections.observableArrayList();

        while(result.next()){

            OwnerTm owner = new OwnerTm();
            owner.setOwnerId(result.getString(1));
            owner.setName(result.getString(2));
            owner.setPhoneNo(result.getString(3));
            owner.setMembersCount(result.getInt(4));
            owner.setPurchaseDate(result.getString(5));
            owner.setHouseId(result.getString(6));
            owner.setInvoiceNo(result.getString(7));

            owners.add(owner);
        }

        return owners;
    }

    public ObservableList<String> getAllDistinctInvoiceNos() throws SQLException, ClassNotFoundException {

        String sql = "select distinct invoiceNo from homeowner order by invoiceNo asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> distinctInvoices = FXCollections.observableArrayList();
        distinctInvoices.add("Select");

        while(result.next()){

            distinctInvoices.add(result.getString("invoiceNo"));
        }

        return distinctInvoices;
    }

    public ObservableList<String> getAllHouseIds() throws SQLException, ClassNotFoundException {

        String sql = "select houseId from homeowner order by houseId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> houseIds = FXCollections.observableArrayList();
        houseIds.add("Select");

        while(result.next()){

            houseIds.add(result.getString("houseId"));
        }

        return houseIds;
    }

    public ObservableList<String> getNameSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT distinct ownerName FROM homeowner WHERE ownerName LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> names = FXCollections.observableArrayList();

        while (result.next()) {
            names.add(result.getString("ownerName"));
        }

        return names;
    }

    public ObservableList<String> getPhoneNosSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT phoneNo FROM homeowner WHERE phoneNo LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> phoneNos = FXCollections.observableArrayList();

        while (result.next()) {
            phoneNos.add(result.getString("phoneNo"));
        }

        return phoneNos;
    }

    public boolean addNewOwner(CustomerDto customerDetails, int membersCount, RequestTm request, String invoiceNo) throws SQLException, ClassNotFoundException {

        String newOwnerId = generateNewOwnerId();

        String sql = "insert into homeowner values(?,?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,newOwnerId,customerDetails.getName(),customerDetails.getPhoneNo(),membersCount, String.valueOf(LocalDate.now()),request.getHouseId(),invoiceNo,customerDetails.getEmail());

        return result;
    }


    public String generateNewOwnerId() throws SQLException, ClassNotFoundException {

        String sql = "select homeOwnerId from homeowner order by homeOwnerId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("homeOwnerId");
            String subStr = lastId.substring(3);
            int id = Integer.parseInt(subStr);
            id+=1;
            String newId = String.format("HO-%04d", id);
            return newId;

        }

        return "HO-0001";

    }

    public String getLastAddedOwnerId() throws SQLException, ClassNotFoundException {

        String sql = "select homeOwnerId from homeowner order by homeOwnerId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            return result.getString("homeOwnerId");
        }
        return "0";
    }

    public String getOwnerEmailById(String ownerId) throws SQLException, ClassNotFoundException {

        String sql = "select email from homeowner where homeOwnerId = ?";
        ResultSet result = CrudUtility.execute(sql,ownerId);

        if(result.next()){
            return result.getString("email");
        }
        return "0";
    }

    public PurchaseAgreementTm getAgreementDetailsById(String ownerId) throws SQLException, ClassNotFoundException {

        String sql = "select purchaseAgreementId,purchasePrice from purchaseagreement where purchaseAgreementId = ?";
        ResultSet result = CrudUtility.execute(sql,ownerId);

        PurchaseAgreementTm purchaseAgreementTm = new PurchaseAgreementTm();

        if(result.next()){

            purchaseAgreementTm.setPurchaseAgreementId(result.getString("purchaseAgreementId"));
            purchaseAgreementTm.setPurchasePrice(result.getDouble("purchasePrice"));
        }

        return purchaseAgreementTm;
    }

    public String getPurchasePriceByHouseId(String houseId) throws SQLException, ClassNotFoundException {

        String sql = "select totalValue from house where houseId = ?";
        ResultSet result = CrudUtility.execute(sql,houseId);

        if(result.next()){

            return result.getString("totalValue");
        }
        return "0.0";
    }

    public String updateOwner(OwnerDto ownerDto) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE homeowner SET ownerName = ?, phoneNo = ?, membersCount = ?, email = ? WHERE homeOwnerId = ?";
        boolean result = CrudUtility.execute(sql,ownerDto.getName(),ownerDto.getPhoneNo(),ownerDto.getMembersCount(),ownerDto.getEmail(),ownerDto.getOwnerId());

        return result ? "Successfully Updated The Owner ID: "+ownerDto.getOwnerId() : "Failed To Update Owner ID: "+ownerDto.getOwnerId()+", Please Try Again Later.";
    }

    public String getOwnerDetailsById(String homeOwnerId) throws SQLException, ClassNotFoundException {

        String sql = "select ownerName from homeowner where homeOwnerId = ?";
        ResultSet result = CrudUtility.execute(sql,homeOwnerId);

        if(result.next()){
            return result.getString("ownerName");
        }
        return "-";
    }
}



