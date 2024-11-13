package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.ExpenseDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.dto.tm.ReturnHouseTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReturnHouseModel {

    private UnitModel unitModel;
    private final TenantModel tenantModel = new TenantModel();
    private final LeaseAgreementModel leaseAgreementModel = new LeaseAgreementModel();
    private final ExpenseModel expenseModel = new ExpenseModel();

    public ReturnHouseModel(){

        try{
           unitModel = new UnitModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

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

    public String reclaimHouse(LeaseAgreementTm selectedLeaseAgreementDetails) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
           boolean isMakeAvailable = unitModel.setHouseAvailable(selectedLeaseAgreementDetails);
           if(!isMakeAvailable){
              connection.rollback();
              return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
           }

           boolean isMakeTenantDeactivate = tenantModel.makeTenantDeactivate(selectedLeaseAgreementDetails.getTenantId());
            if(!isMakeTenantDeactivate){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isMakeCancelled = leaseAgreementModel.makeSelectedLeaseAgreementCancel(selectedLeaseAgreementDetails.getLeaseId());
            if(!isMakeCancelled){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isAddNewReturnDetails = addNewHouseReturnWithoutRefund(selectedLeaseAgreementDetails);
            if(!isAddNewReturnDetails){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            connection.commit();
            return "Successfully Reclaiming The House!";

        }
        catch (Exception e){
           e.printStackTrace();
           connection.rollback();
        }
        finally {
            connection.setAutoCommit(true);
        }
        return "0";
    }

    private boolean addNewHouseReturnWithoutRefund(LeaseAgreementTm selectedLeaseAgreementDetails) throws SQLException, ClassNotFoundException {

        String newReturnId =  generateNewReturnId();
        String today = String.valueOf(LocalDate.now());

        String sql = "insert into returnhouse values(?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,newReturnId,"Expiration of the Lease Turn", today,selectedLeaseAgreementDetails.getTenantId(),selectedLeaseAgreementDetails.getHouseId(),"N/A","N/A");

        return result;
    }


    public String generateNewReturnId() throws SQLException, ClassNotFoundException {

        String sql = "select returnNo from returnhouse order by returnNo desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("returnNo");
            String subStr = lastId.substring(3);
            System.out.println("sub string: "+subStr);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("RT-%04d", id);

        }
        else{
            return "RT-0001";
        }

    }

    public String reclaimHouseWithRefundSecurityDeposit(LeaseAgreementTm selectedLeaseAgreementDetails, TenantDto tenant) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isMakeAvailable = unitModel.setHouseAvailable(selectedLeaseAgreementDetails);
            if(!isMakeAvailable){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isMakeTenantDeactivate = tenantModel.makeTenantDeactivate(selectedLeaseAgreementDetails.getTenantId());
            if(!isMakeTenantDeactivate){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isMakeCancelled = leaseAgreementModel.makeSelectedLeaseAgreementCancel(selectedLeaseAgreementDetails.getLeaseId());
            if(!isMakeCancelled){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isAddNewExpense = expenseModel.addNewExpense(tenant);
            if(!isAddNewExpense){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isAddNewReturnDetails = addNewHouseReturnWithRefund(selectedLeaseAgreementDetails);
            if(!isAddNewReturnDetails){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            connection.commit();
            return "Successfully Refund The Security Payment And Reclaiming The House!";

        }
        catch (Exception e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.setAutoCommit(true);
        }
        return "0";
    }


    private boolean addNewHouseReturnWithRefund(LeaseAgreementTm selectedLeaseAgreementDetails) throws SQLException, ClassNotFoundException {

        String newReturnId =  generateNewReturnId();
        String today = String.valueOf(LocalDate.now());

        ExpenseDto expenseDto = expenseModel.getLastExpenseDetails();

        String sql = "insert into returnhouse values(?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,newReturnId,"Expiration of the Lease Turn", today,selectedLeaseAgreementDetails.getTenantId(),selectedLeaseAgreementDetails.getHouseId(),expenseDto.getAmount(),expenseDto.getExpenseNo());

        return result;
    }
}






