package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.RequestDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewRentRequestModel {
    public String generateNewRequestId() throws SQLException, ClassNotFoundException {

        String sql = "select requestId from buyAndRentRequest order by requestId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("requestId");
            String subStr = lastId.substring(1);
            System.out.println(subStr);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("R%05d", id);

        }
        else{
            return "R00001";
        }

    }

    public ObservableList<String> getAllHouseTypes() throws SQLException, ClassNotFoundException {

        String sql = "select houseType from houseType";

        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> houseTypes = FXCollections.observableArrayList("Select");

        while(result.next()){

            houseTypes.add(result.getString("houseType"));
        }

        return houseTypes;
    }

    public String addNewRentRequest(RequestDto requestDto) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO buyAndRentRequest (\n" +
                "    requestId, customerId, date, rentOrBuy, houseType, noOfFamilyMembers,\n" +
                "    monthlyIncome, annualIncome, bankAccountDetails, reasonForLeaving, \n" +
                "    estimatedMonthlyBudgetForRent, lengthOfLeaseTurnDesire, previousLandlordNumber, \n" +
                "    isSmorking, hasCriminalBackground, hasPets, allDocumentsProvided, \n" +
                "    qualifiedCustomerOrNot, agreesToAllTermsAndConditions, isPaymentsCompleted, \n" +
                "    requestStatus, customerRequestStatus, houseId\n" +
                ") VALUES (\n" +
                "   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?\n" +
                ")";

        boolean result = CrudUtility.execute(sql,
                requestDto.getRequestId(), requestDto.getCustomerId(), requestDto.getDate(),
                requestDto.getRentOrBuy(), requestDto.getHouseType(), requestDto.getNoOfFamilyMembers(),
                requestDto.getMonthlyIncome(), requestDto.getAnnualIncome(), requestDto.getBankAccountDetails(),
                requestDto.getReasonForLeaving(), requestDto.getEstimatedMonthlyBudgetForRent(),
                requestDto.getLeaseTurnDesire(), requestDto.getPreviousLandlordNumber(),
                requestDto.getIsSmoking(), requestDto.getHasCriminalBackground(), requestDto.getHasPets(),
                "Not Yet", "-", "Not Yet", "Not Yet", "In Process", "Awaiting Confirmation", null);

        return result ? "Successfully Added New Rent Request" : "Failed To Add New Request, Try Again Later";

    }
}










