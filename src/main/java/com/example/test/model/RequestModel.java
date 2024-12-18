package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.CustomerDto;
import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.RequestTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestModel {

    private final UnitModel unitModel = new UnitModel();
    private final TenantModel tenantModel = new TenantModel();
    private final PaymentModel paymentModel = new PaymentModel();
    private final LeaseAgreementModel leaseAgreementModel = new LeaseAgreementModel();
    private final OwnerModel ownerModel = new OwnerModel();
    private final PurchaseAgreementModel purchaseAgreementModel = new PurchaseAgreementModel();


    public RequestModel() throws SQLException, ClassNotFoundException {
    }


    public ObservableList<RequestTm> getAllRequests() throws SQLException, ClassNotFoundException {

        String sql = "select requestId,customerId,rentOrBuy,houseType,lengthOfLeaseTurnDesire,allDocumentsProvided," +
                "qualifiedCustomerOrNot,agreesToAllTermsAndConditions,isPaymentsCompleted,customerRequestStatus,requestStatus," +
                "houseId from buyAndRentRequest";

        ResultSet result = CrudUtility.execute(sql);

        ObservableList<RequestTm> requests = FXCollections.observableArrayList();

        while (result.next()){
            String requestId = result.getString(1);
            System.out.println(requestId);
            String customerId = result.getString(2);
            System.out.println(customerId);
            String rentOrBuy = result.getString(3);
            System.out.println(rentOrBuy);
            String houseType = result.getString(4);
            System.out.println(houseType);
            String lengthOfLeaseTurnDesire = result.getString(5);
            System.out.println(lengthOfLeaseTurnDesire);
            String allDocumentsProvided = result.getString(6);
            System.out.println(allDocumentsProvided);
            if(allDocumentsProvided==null){
                allDocumentsProvided = "-";
                System.out.println(allDocumentsProvided);
            }
            String qualifiedCustomerOrNot = result.getString(7);
            System.out.println(qualifiedCustomerOrNot);
            if(qualifiedCustomerOrNot==null){
                qualifiedCustomerOrNot = "-";
                System.out.println(qualifiedCustomerOrNot);
            }
            String agreesToAllTermsAndConditions = result.getString(8);
            System.out.println(agreesToAllTermsAndConditions);
            if(agreesToAllTermsAndConditions==null){
                agreesToAllTermsAndConditions = "-";
                System.out.println(agreesToAllTermsAndConditions);
            }
            String isPaymentsCompleted = result.getString(9);
            System.out.println(isPaymentsCompleted);
            if(isPaymentsCompleted==null){
                isPaymentsCompleted = "-";
                System.out.println(isPaymentsCompleted);
            }
            String customerRequestStatus = result.getString(10);
            System.out.println(customerRequestStatus);
            String requestStatus = result.getString(11);
            System.out.println(requestStatus);
            String houseId = result.getString(12);
            System.out.println(houseId);
            if(houseId==null){
                houseId = "-";
                System.out.println(houseId);
            }

            RequestTm requestTm = new RequestTm(requestId,customerId,rentOrBuy,houseType,lengthOfLeaseTurnDesire,allDocumentsProvided,qualifiedCustomerOrNot,
                    agreesToAllTermsAndConditions,isPaymentsCompleted,customerRequestStatus,requestStatus,houseId);


            requests.add(requestTm);
        }

        return requests;
    }

    public String deleteSelectedRequest(RequestTm selectedRequest) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            String sql = "delete from buyandrentrequest where requestId = ? ";
            boolean result = CrudUtility.execute(sql, selectedRequest.getRequestId());

            if(!result){
              connection.rollback();
              return "Filed To Delete The Request, Try Again Later";
            }

            if(!selectedRequest.getHouseId().equals("-")){
                String sqlStat = "UPDATE house SET status = ? WHERE houseId = ?";
                boolean res = CrudUtility.execute(sql,"Available",selectedRequest.getHouseId());

                if(!res){
                    connection.rollback();
                    return "Filed To Delete The Request, Try Again Later";
                }

                connection.commit();
                return "Successfully Deleted The Request";
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while deleting the request: " + e.getMessage());
        }
        finally {
            connection.setAutoCommit(true);
        }
        return "0";
    }

    public String makeRequestStatusCompleted(RequestTm request) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE buyAndRentRequest SET requestStatus = ? WHERE requestId = ?";
        boolean result = CrudUtility.execute(sql,"Completed",request.getRequestId());

        return result ? "The change request status has been successfully updated to \"Completed\" You can now proceed with further actions." : "Failed To Update The Request Status As \"Completed\", Try Again Later";
    }


    public String addNewTenant(RequestTm request) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {

            String tenantId = tenantModel.generateNewTenantId();
            System.out.println("tenant id: " + tenantId);

            CustomerDto customerDetails = getCustomerDetails(request.getCustomerId());
            if(customerDetails==null){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }
            System.out.println("customer name: "+customerDetails.getName());

            int membersCount = getFamilyMembersCount(request.getRequestId());
            if(membersCount==0){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }
            System.out.println(membersCount);

            UnitDto rentingUnit = getRentingUnitDetails(request.getHouseId());
            if(rentingUnit==null){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }
            System.out.println("rent: "+ rentingUnit.getMonthlyRent());

            boolean isSavedNewCustomer = tenantModel.addTenantToSystem(tenantId,customerDetails,membersCount,rentingUnit,request);

            if(!isSavedNewCustomer){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }

            double securityPayment = Double.parseDouble(rentingUnit.getSecurityCharge());
            boolean isAddSecurityPayment = paymentModel.addNewFirstPayment(tenantId,securityPayment,"Security Deposit");
            if(!isAddSecurityPayment){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }

            double monthlyPayment = Double.parseDouble(rentingUnit.getMonthlyRent());
            boolean isAddFirstMonthPayment = paymentModel.addNewFirstPayment(tenantId,monthlyPayment,"Monthly Rent Payment");
            if(!isAddFirstMonthPayment){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }

           boolean isAddNewAgreement = leaseAgreementModel.addNewLeaseAgreement(tenantId,request,securityPayment,monthlyPayment);
            if(!isAddNewAgreement){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }

            boolean isUpdateRequestStatus = setRequestStausToFixed(request);
            if(!isUpdateRequestStatus){
                connection.rollback();
                return "Something Went Wrong With Add New Tenant,Try Again Later";
            }

            connection.commit();
            return "Successfully Add As New Tenant!";

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


    private boolean setRequestStausToFixed(RequestTm request) throws SQLException, ClassNotFoundException {

        String sql = "update buyandrentrequest set requestStatus = ? where requestId = ?";
        boolean result = CrudUtility.execute(sql,"Closed",request.getRequestId());

        return result;

    }


    private UnitDto getRentingUnitDetails(String houseId) throws SQLException, ClassNotFoundException {

        String sql = "select monthlyRent,security_charge from house where houseId = ?";
        ResultSet result = CrudUtility.execute(sql,houseId);

        UnitDto unit = null;

        if(result.next()){

            unit = new UnitDto();

            unit.setMonthlyRent(result.getString("monthlyRent"));
            unit.setSecurityCharge(result.getString("security_charge"));
        }

        return unit;
    }


    private Integer getFamilyMembersCount(String requestId) throws SQLException, ClassNotFoundException {

        String sql = "select noOfFamilyMembers from buyandrentrequest where requestId = ?";
        ResultSet result = CrudUtility.execute(sql,requestId);

        Integer membersCount = 0;

        if(result.next()){

            membersCount = result.getInt("noOfFamilyMembers");

        }
        return membersCount;
    }



    private CustomerDto getCustomerDetails(String customerId) throws SQLException, ClassNotFoundException {

        String sql = "select name,phoneNo,email from customer where customerId = ?";
        ResultSet result = CrudUtility.execute(sql,customerId);

        CustomerDto customer = null;

        if(result.next()){

            customer = new CustomerDto();

            customer.setName(result.getString("name"));
            customer.setPhoneNo(result.getString("phoneNo"));
            customer.setEmail(result.getString("email"));

        }
        return customer;
    }

    public String addNewOwner(RequestTm request) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {

            UnitDto unit = unitModel.getHouseDetailsByHouseId(request.getHouseId());
            if(unit.getTotalValue()==null){
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }



            boolean isAddNewPurchasePayment = paymentModel.addNewFirstPayment(null,Double.parseDouble(unit.getTotalValue()),"Full House Purchase Payment");
            if(!isAddNewPurchasePayment){
               connection.rollback();
               return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            String invoiceNo = paymentModel.getLastPayment();
            if(invoiceNo.equals("0")){
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            CustomerDto customerDetails = getCustomerDetails(request.getCustomerId());
            if(customerDetails==null){
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            int membersCount = getFamilyMembersCount(request.getRequestId());
            if(membersCount==0){
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            boolean isAddNewOwner = ownerModel.addNewOwner(customerDetails,membersCount,request,invoiceNo);
            if(!isAddNewOwner){
                connection.rollback();
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            String ownerId = ownerModel.getLastAddedOwnerId();
            if(ownerId.equals("0")){
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            boolean isAddNewPurchaseAgreement = purchaseAgreementModel.addNewPurchaseAgreement(unit,ownerId);
            if(!isAddNewPurchaseAgreement){
                connection.rollback();
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            boolean isUpdateRequestStatus = setRequestStausToFixed(request);
            if(!isUpdateRequestStatus){
                connection.rollback();
                return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
            }

            connection.commit();
            return "Successfully \"Closed\" The Request And Added New Owner To The System, Owner Id is "+ownerId;

        }
        catch (Exception e){
           connection.rollback();
           e.printStackTrace();
           return "Something Went Wrong With Process Of Adding A New Owner For Completed Purchase Request, Try Again Later";
        }
        finally {
            connection.setAutoCommit(true);
        }
    }

    public ObservableList<RequestTm> getOnlyClosedRequests() throws SQLException, ClassNotFoundException {

        String sql = "select requestId,customerId,rentOrBuy,houseType,lengthOfLeaseTurnDesire,allDocumentsProvided," +
                "qualifiedCustomerOrNot,agreesToAllTermsAndConditions,isPaymentsCompleted,customerRequestStatus,requestStatus," +
                "houseId from buyAndRentRequest where requestStatus = 'Closed'";

        ResultSet result = CrudUtility.execute(sql);

        ObservableList<RequestTm> requests = FXCollections.observableArrayList();

        while (result.next()){
            String requestId = result.getString(1);
            System.out.println(requestId);
            String customerId = result.getString(2);
            System.out.println(customerId);
            String rentOrBuy = result.getString(3);
            System.out.println(rentOrBuy);
            String houseType = result.getString(4);
            System.out.println(houseType);
            String lengthOfLeaseTurnDesire = result.getString(5);
            System.out.println(lengthOfLeaseTurnDesire);
            String allDocumentsProvided = result.getString(6);
            System.out.println(allDocumentsProvided);
            if(allDocumentsProvided==null){
                allDocumentsProvided = "-";
                System.out.println(allDocumentsProvided);
            }
            String qualifiedCustomerOrNot = result.getString(7);
            System.out.println(qualifiedCustomerOrNot);
            if(qualifiedCustomerOrNot==null){
                qualifiedCustomerOrNot = "-";
                System.out.println(qualifiedCustomerOrNot);
            }
            String agreesToAllTermsAndConditions = result.getString(8);
            System.out.println(agreesToAllTermsAndConditions);
            if(agreesToAllTermsAndConditions==null){
                agreesToAllTermsAndConditions = "-";
                System.out.println(agreesToAllTermsAndConditions);
            }
            String isPaymentsCompleted = result.getString(9);
            System.out.println(isPaymentsCompleted);
            if(isPaymentsCompleted==null){
                isPaymentsCompleted = "-";
                System.out.println(isPaymentsCompleted);
            }
            String customerRequestStatus = result.getString(10);
            System.out.println(customerRequestStatus);
            String requestStatus = result.getString(11);
            System.out.println(requestStatus);
            String houseId = result.getString(12);
            System.out.println(houseId);
            if(houseId==null){
                houseId = "-";
                System.out.println(houseId);
            }

            RequestTm requestTm = new RequestTm(requestId,customerId,rentOrBuy,houseType,lengthOfLeaseTurnDesire,allDocumentsProvided,qualifiedCustomerOrNot,
                    agreesToAllTermsAndConditions,isPaymentsCompleted,customerRequestStatus,requestStatus,houseId);


            requests.add(requestTm);
        }

        return requests;
    }

    public ObservableList<String> getDistinctCustomers() throws SQLException, ClassNotFoundException {

        String sql = "select  distinct customerId from buyandrentrequest order by customerId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> ids = FXCollections.observableArrayList();
        ids.add("Select");

        while(result.next()){

            ids.add(result.getString("customerId"));
        }

        return ids;
    }
}












