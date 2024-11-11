package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.RequestDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditRentRequestModel {
    public RequestDto getSelectedRequestAllDetails(String requestId) throws SQLException, ClassNotFoundException {

        String sql = "select * from buyandrentrequest where requestId = ?";
        ResultSet result = CrudUtility.execute(sql,requestId);

        RequestDto requestDto = new RequestDto();

        while(result.next()){

            String id = result.getString(1);
            requestDto.setRequestId(id);
            String customerId = result.getString(2);
            requestDto.setCustomerId(customerId);
            String date = result.getString(3);
            requestDto.setDate(date);
            String rentOrBuy = result.getString(4);
            requestDto.setRentOrBuy(rentOrBuy);
            String houseType = result.getString(5);
            requestDto.setHouseType(houseType);
            int familyMembersCount = result.getInt(6);
            requestDto.setNoOfFamilyMembers(familyMembersCount);
            double monthlyIncome = result.getDouble(7);
            requestDto.setMonthlyIncome(monthlyIncome);
            double annualIncome = result.getDouble(8);
            requestDto.setAnnualIncome(annualIncome);
            String bankDetails= result.getString(9);
            requestDto.setBankAccountDetails(bankDetails);
            String reasonToMove = result.getString(10);
            requestDto.setReasonForLeaving(reasonToMove);
            String estimatedBudget = result.getString(11);
            requestDto.setEstimatedMonthlyBudgetForRent(estimatedBudget);
            String leaseTurn = result.getString(12);
            requestDto.setLeaseTurnDesire(leaseTurn);
            String landLordNumber = result.getString(13);
            requestDto.setPreviousLandlordNumber(landLordNumber);
            String docProvided = result.getString(17);
            requestDto.setAllDocumentsProvided(docProvided);
            String isQualified = result.getString(18);
            requestDto.setQualifiedCustomerOrNot(isQualified);
            String isAgreed = result.getString(19);
            requestDto.setAgreesToAllTermsAndConditions(isAgreed);
            String isPaymentDone = result.getString(20);
            requestDto.setIsPaymentsCompleted(isPaymentDone);
            String requestStatus = result.getString(21);
            requestDto.setRequestStatus(requestStatus);
            String customerRequestStatus = result.getString(22);
            requestDto.setCustomerRequestStatus(customerRequestStatus);
            String houseId = result.getString(23);
            if(houseId==null){
              houseId = "-";
            }
            System.out.println("house id: "+houseId);
            requestDto.setHouseId(houseId);

        }

        return requestDto;
    }

    public String editRentRequest(RequestDto requestDto,RequestDto request) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();

        connection.setAutoCommit(false);

        try {
            if (requestDto.getRequestStatus().equals("Rejected") && !request.getHouseId().equals("-")) {

                String sqlStatement = "UPDATE house SET status = ? WHERE houseId = ?";
                boolean resultOne = CrudUtility.execute(sqlStatement, "Available", request.getHouseId());

                if(resultOne){

                    String sqlState = "UPDATE buyandrentrequest SET houseId = ? WHERE requestId = ?";
                    boolean resultTwo = CrudUtility.execute(sqlState,null,requestDto.getRequestId());

                    if(resultTwo){

                        String sql = "UPDATE buyAndRentRequest SET \n" +
                                "    houseType = ?,\n" +
                                "    noOfFamilyMembers = ?,\n" +
                                "    monthlyIncome = ?,\n" +
                                "    annualIncome = ?,\n" +
                                "    bankAccountDetails = ?,\n" +
                                "    reasonForLeaving = ?,\n" +
                                "    estimatedMonthlyBudgetForRent = ?,\n" +
                                "    lengthOfLeaseTurnDesire = ?,\n" +
                                "    previousLandlordNumber = ?,\n" +
                                "    allDocumentsProvided = ?,\n" +
                                "    qualifiedCustomerOrNot = ?,\n" +
                                "    agreesToAllTermsAndConditions = ?,\n" +
                                "    isPaymentsCompleted = ?,\n" +
                                "    requestStatus = ?,\n" +
                                "    customerRequestStatus = ?\n" + // Removed the comma here
                                "WHERE requestId = ?";

                        boolean result = CrudUtility.execute(sql,
                                requestDto.getHouseType(),
                                requestDto.getNoOfFamilyMembers(),
                                requestDto.getMonthlyIncome(),
                                requestDto.getAnnualIncome(),
                                requestDto.getBankAccountDetails(),
                                requestDto.getReasonForLeaving(),
                                requestDto.getEstimatedMonthlyBudgetForRent(),
                                requestDto.getLeaseTurnDesire(),
                                requestDto.getPreviousLandlordNumber(),
                                requestDto.getAllDocumentsProvided(),
                                requestDto.getQualifiedCustomerOrNot(),
                                requestDto.getAgreesToAllTermsAndConditions(),
                                requestDto.getIsPaymentsCompleted(),
                                requestDto.getRequestStatus(),
                                requestDto.getCustomerRequestStatus(),
                                requestDto.getRequestId()
                        );

                        if(result){
                            connection.commit();
                            return "Successfully Updated Request";
                        }
                        else {
                            connection.rollback();
                            return "Failed To Update Request, Try Again Later";
                        }
                    }
                    else{

                        connection.rollback();
                        return "Something Wrong With Wrong, Try Again Later";
                    }

                }
                else{
                    connection.rollback();
                    return "Something Wrong With Wrong, Try Again Later";
                }

            }
            else{

                String sql = "UPDATE buyAndRentRequest SET \n" +
                        "    houseType = ?,\n" +
                        "    noOfFamilyMembers = ?,\n" +
                        "    monthlyIncome = ?,\n" +
                        "    annualIncome = ?,\n" +
                        "    bankAccountDetails = ?,\n" +
                        "    reasonForLeaving = ?,\n" +
                        "    estimatedMonthlyBudgetForRent = ?,\n" +
                        "    lengthOfLeaseTurnDesire = ?,\n" +
                        "    previousLandlordNumber = ?,\n" +
                        "    allDocumentsProvided = ?,\n" +
                        "    qualifiedCustomerOrNot = ?,\n" +
                        "    agreesToAllTermsAndConditions = ?,\n" +
                        "    isPaymentsCompleted = ?,\n" +
                        "    requestStatus = ?,\n" +
                        "    customerRequestStatus = ?\n" + // Removed the comma here
                        "WHERE requestId = ?";

                boolean result = CrudUtility.execute(sql,
                        requestDto.getHouseType(),
                        requestDto.getNoOfFamilyMembers(),
                        requestDto.getMonthlyIncome(),
                        requestDto.getAnnualIncome(),
                        requestDto.getBankAccountDetails(),
                        requestDto.getReasonForLeaving(),
                        requestDto.getEstimatedMonthlyBudgetForRent(),
                        requestDto.getLeaseTurnDesire(),
                        requestDto.getPreviousLandlordNumber(),
                        requestDto.getAllDocumentsProvided(),
                        requestDto.getQualifiedCustomerOrNot(),
                        requestDto.getAgreesToAllTermsAndConditions(),
                        requestDto.getIsPaymentsCompleted(),
                        requestDto.getRequestStatus(),
                        requestDto.getCustomerRequestStatus(),
                        requestDto.getRequestId()
                );

                if(result){
                    connection.commit();
                    return "Successfully Updated Request";
                }
                else {
                    return "Failed To Update Request, Try Again Later";
                }

            }


        }
        catch (Exception e){
            connection.rollback();
           e.printStackTrace();
        }
        finally {
            connection.setAutoCommit(true);
        }

        return "0";
    }
}
