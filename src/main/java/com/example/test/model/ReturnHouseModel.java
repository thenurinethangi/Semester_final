package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.ExpenseDto;
import com.example.test.dto.HouseReturnDto;
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

        String sql = "select * from returnhouse where isActive!=0";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<ReturnHouseTm> returnHouses = FXCollections.observableArrayList();

        while (result.next()){

            String returnNo = result.getString(1);
            String reason = result.getString(2);
            String date = result.getString(3);
            String tenantId = result.getString(4);
            String houseId = result.getString(5);
            String refundedAmount = result.getString(6);
            if(refundedAmount==null){
                refundedAmount = "N/A";
            }
            String expenseNo = result.getString(7);
            if(expenseNo==null){
                expenseNo = "N/A";
            }

            ReturnHouseTm returnHouse = new ReturnHouseTm(returnNo,reason,date,tenantId,houseId,refundedAmount,expenseNo);
            returnHouses.add(returnHouse);
        }

        return  returnHouses;
    }

    public String reclaimHouse(HouseReturnDto houseReturnDto) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
           boolean isMakeAvailable = unitModel.setHouseAvailable(houseReturnDto);
           if(!isMakeAvailable){
              connection.rollback();
              return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
           }

           boolean isMakeTenantDeactivate = tenantModel.makeTenantDeactivate(houseReturnDto.getTenantId());
            if(!isMakeTenantDeactivate){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isMakeCancelled = leaseAgreementModel.makeSelectedLeaseAgreementCancel(houseReturnDto.getAgreementId());
            if(!isMakeCancelled){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isAddNewReturnDetails = addNewHouseReturnWithoutRefund(houseReturnDto);
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

    private boolean addNewHouseReturnWithoutRefund(HouseReturnDto houseReturnDto) throws SQLException, ClassNotFoundException {

        String newReturnId =  generateNewReturnId();
        String today = String.valueOf(LocalDate.now());

        String sql = "INSERT INTO returnhouse (returnNo, reason, date, tenantId, houseId, isActive)\n" +
                "VALUES (?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,newReturnId,houseReturnDto.getReasonToLeave(), today,houseReturnDto.getTenantId(),houseReturnDto.getHouseId(),1);

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

    public String reclaimHouseWithRefundSecurityDeposit(HouseReturnDto houseReturnDto, TenantDto tenant) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isMakeAvailable = unitModel.setHouseAvailable(houseReturnDto);
            if(!isMakeAvailable){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isMakeTenantDeactivate = tenantModel.makeTenantDeactivate(houseReturnDto.getTenantId());
            if(!isMakeTenantDeactivate){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isMakeCancelled = leaseAgreementModel.makeSelectedLeaseAgreementCancel(houseReturnDto.getAgreementId());
            if(!isMakeCancelled){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isAddNewExpense = expenseModel.addNewExpense(tenant);
            if(!isAddNewExpense){
                connection.rollback();
                return "Something Went Wrong With Reclaiming The House. Please Try Again Later";
            }

            boolean isAddNewReturnDetails = addNewHouseReturnWithRefund(houseReturnDto);
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


    private boolean addNewHouseReturnWithRefund(HouseReturnDto houseReturnDto) throws SQLException, ClassNotFoundException {

        String newReturnId =  generateNewReturnId();
        String today = String.valueOf(LocalDate.now());

        ExpenseDto expenseDto = expenseModel.getLastExpenseDetails();

        String sql = "INSERT INTO returnhouse (returnNo, reason, date, tenantId, houseId, refundedAmount, expenseNo, isActive)\n" +
                "VALUES (?,?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,newReturnId,houseReturnDto.getReasonToLeave(),today,houseReturnDto.getTenantId(),houseReturnDto.getHouseId(),expenseDto.getAmount(),expenseDto.getExpenseNo(),1);

        return result;
    }


    public ObservableList<String> getAllDistinctTenantIds() throws SQLException, ClassNotFoundException {

        String sql = "SELECT DISTINCT tenantId FROM returnhouse order by tenantId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> distinctTenantIds = FXCollections.observableArrayList();
        distinctTenantIds.add("Select");

        while (result.next()){

            distinctTenantIds.add(result.getString("tenantId"));
        }

        return distinctTenantIds;
    }


    public ObservableList<String> getAllDistinctHouseIds() throws SQLException, ClassNotFoundException {

        String sql = "SELECT DISTINCT houseId FROM returnhouse order by houseId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> distinctHouseIds = FXCollections.observableArrayList();
        distinctHouseIds.add("Select");

        while (result.next()){

            distinctHouseIds.add(result.getString("houseId"));
        }

        return distinctHouseIds;
    }

    public String setSelectedReturnDetailDeactivate(ReturnHouseTm selectedRow) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE returnhouse SET isActive = ? WHERE returnNo = ?";
        boolean result = CrudUtility.execute(sql,0,selectedRow.getReturnNo());

        return result ? "Successfully Deleted The House Return Details" : "Failed To Delete The House Return Details,Try Again Later";
    }
}






