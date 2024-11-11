package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.RequestDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RentRequestDetailsModel {
    public RequestDto getRentRequestDetails(String id) throws SQLException, ClassNotFoundException {

        String sql = "select date,noOfFamilyMembers,monthlyIncome,annualIncome,bankAccountDetails,reasonForLeaving," +
                     "estimatedMonthlyBudgetForRent,previousLandlordNumber,isSmorking,hasCriminalBackground,hasPets from buyandrentrequest where requestId = ?";

        ResultSet result = CrudUtility.execute(sql,id);

        RequestDto requestDetails = new RequestDto();

        if(result.next()){

            requestDetails.setDate(result.getString(1));
            requestDetails.setNoOfFamilyMembers(result.getInt(2));
            requestDetails.setMonthlyIncome(result.getDouble(3));
            requestDetails.setAnnualIncome(result.getDouble(4));
            requestDetails.setBankAccountDetails(result.getString(5));
            requestDetails.setReasonForLeaving(result.getString(6));
            requestDetails.setEstimatedMonthlyBudgetForRent(result.getString(7));
            requestDetails.setPreviousLandlordNumber(result.getString(8));
            System.out.println("landholder no: " + result.getString( 8));
            requestDetails.setIsSmoking(result.getString(9));
            requestDetails.setHasCriminalBackground(result.getString(10));
            requestDetails.setHasPets(result.getString(11));
        }

        return  requestDetails;
    }


}


