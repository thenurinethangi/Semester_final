package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.ExpenseDto;
import com.example.test.dto.MaintenanceRequestDto;
import com.example.test.dto.tm.ExpenseTm;
import com.example.test.dto.tm.MaintenanceRequestTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaintenanceRequestModel {
    public ObservableList<MaintenanceRequestTm> getAllRequests() throws SQLException, ClassNotFoundException {

        String sql = "select * from maintainrequest where isActiveRequest = 1";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<MaintenanceRequestTm> allRequests = FXCollections.observableArrayList();

        while(result.next()){

            String requestId = result.getString(1);
            String desc = result.getString(2);
            double estimatedCost = result.getDouble(3);
            String actualCost = result.getString(4);
            if(actualCost==null){
                actualCost = "-";
            }
            String date = result.getString(5);
            String technician = result.getString(6);
            String tenantId = result.getString(7);
            String status = result.getString(8);

            MaintenanceRequestTm maintenanceRequestTm = new MaintenanceRequestTm(requestId,desc,estimatedCost,actualCost,date,technician,tenantId,status);
            allRequests.add(maintenanceRequestTm);

        }
        return allRequests;
    }

    public ObservableList<String> getDistinctTenantIds() throws SQLException, ClassNotFoundException {

        String sql = "select Distinct tenantId from maintainrequest where isActiveRequest = 1 order by tenantId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> tenantIds = FXCollections.observableArrayList();

        while(result.next()){

            tenantIds.add(result.getString("tenantId"));
        }

        return tenantIds;
    }

    public String setStatusComplete(MaintenanceRequestTm selectedRequest) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE maintainrequest SET status = ? WHERE requestNo = ?";
        boolean result = CrudUtility.execute(sql,"Completed",selectedRequest.getMaintenanceRequestNo());

        return result ? "Successfully Completed The Maintenance Request No :"+selectedRequest.getMaintenanceRequestNo() : "Failed To Complete Maintenance Request No :"+selectedRequest.getMaintenanceRequestNo()+" Try Again Later";
    }

    public String generateNewRequestId() throws SQLException, ClassNotFoundException {

        String sql = "select requestNo from maintainrequest order by requestNo desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("requestNo");
            String subStr = lastId.substring(4);
            System.out.println(subStr);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("REQ-%05d", id);

        }
        else{
            return "REQ-00001";
        }
    }


    public String addNewMaintenanceRequest(MaintenanceRequestDto maintenanceRequest) throws SQLException, ClassNotFoundException {

        String sql = "insert into maintainrequest values(?,?,?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,maintenanceRequest.getMaintenanceRequestNo(),maintenanceRequest.getDescription(),maintenanceRequest.getEstimatedCost(),
                maintenanceRequest.getActualCost(),maintenanceRequest.getDate(),maintenanceRequest.getAssignedTechnician(),maintenanceRequest.getTenantId(),maintenanceRequest.getStatus(),1);

        return result ? "Successfully Added New Maintenance Request By Tenant ID: "+maintenanceRequest.getTenantId() : "Failed To Add New Maintenance Request, Try Again Later";
    }


    public String deActiveSelectedMaintenanceRequest(MaintenanceRequestTm selectedRequest) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE maintainrequest SET isActiveRequest = ? WHERE requestNo = ?";
        boolean result = CrudUtility.execute(sql,0,selectedRequest.getMaintenanceRequestNo());

        return result ? "Successfully Deleted The Maintenance Request No :"+selectedRequest.getMaintenanceRequestNo() : "Failed To Delete The Maintenance Request, Try Again Later";
    }

    public String updateMaintenanceRequest(MaintenanceRequestDto maintenanceRequest) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE maintainrequest SET description = ?,estimatedCost = ?,actualCost = ?,assignedTechnician = ? WHERE requestNo = ?";
        boolean result = CrudUtility.execute(sql,maintenanceRequest.getDescription(),maintenanceRequest.getEstimatedCost(),maintenanceRequest.getActualCost(),maintenanceRequest.getAssignedTechnician(),maintenanceRequest.getMaintenanceRequestNo());

        return result ? "Successfully Updated The Request No: "+maintenanceRequest.getMaintenanceRequestNo() : "Failed To Update The Request No: "+maintenanceRequest.getMaintenanceRequestNo()+" Try Again Later";

    }

    public String makeRequestRejected(MaintenanceRequestTm selectedRequest) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE maintainrequest SET status = ? WHERE requestNo = ?";
        boolean result = CrudUtility.execute(sql,"Rejected",selectedRequest.getMaintenanceRequestNo());

        return result ? "Successfully Rejected The Request No: "+selectedRequest.getMaintenanceRequestNo() : "Failed To Reject The Request No: "+selectedRequest.getMaintenanceRequestNo()+" Try Again Later";
    }

    public boolean setRequestNotComplete(ExpenseTm selectedExpense) throws SQLException, ClassNotFoundException {

        String sqlStatementOne = "select actualCost from maintainrequest where requestNo = ?";
        ResultSet resultSet = CrudUtility.execute(sqlStatementOne,selectedExpense.getRequestNo());

        if(resultSet.next()){

            double actualCost = Double.parseDouble(resultSet.getString("actualCost"));
            double deletedCost = selectedExpense.getAmount();

            if(actualCost>deletedCost){

                double newActualCost = actualCost-deletedCost;
                String newActualCostAsString = String.valueOf(newActualCost);

                String sql = "UPDATE maintainrequest SET actualCost = ?, status = ?,isActiveRequest = ? WHERE requestNo = ?";
                boolean result = CrudUtility.execute(sql,newActualCost,"In Progress",1,selectedExpense.getRequestNo());

                return result;
            }
            else{

                String sql = "UPDATE maintainrequest SET actualCost = ?, status = ?,isActiveRequest = ? WHERE requestNo = ?";
                boolean result = CrudUtility.execute(sql,"-","In Progress",1,selectedExpense.getRequestNo());

                return result;
            }
        }
        else{
            return false;
        }
    }

    public ObservableList<String> getRequestIdSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT requestNo FROM maintainrequest WHERE requestNo LIKE ? and isActiveRequest = ? and status = ?";
        ResultSet result = CrudUtility.execute(sql, input + "%",1,"In Progress");

        ObservableList<String> requestIds = FXCollections.observableArrayList();

        while (result.next()) {
            requestIds.add(result.getString("requestNo"));
        }

        return requestIds;
    }

    public boolean checkEnteredRequestIdValid(String maintenanceRequestNo) throws SQLException, ClassNotFoundException {

        String sql = "select * from maintainrequest where requestNo = ? and isActiveRequest = ? and status = ?";
        ResultSet result = CrudUtility.execute(sql,maintenanceRequestNo,1,"In Progress");

        if(result.next()){
            return true;
        }
        return false;
    }

    public boolean setActualCost(ExpenseDto newExpense) throws SQLException, ClassNotFoundException {

        String sql = "select actualCost from maintainrequest where requestNo = ?";
        ResultSet resultSet = CrudUtility.execute(sql,newExpense.getMaintenanceRequestNo());

        if(resultSet.next()){
            String actualCost = resultSet.getString("actualCost");

            if(actualCost.equals("-")){

                String sqlStatementOne = "UPDATE maintainrequest SET actualCost = ? WHERE requestNo = ?";
                return CrudUtility.execute(sqlStatementOne,newExpense.getAmount(),newExpense.getMaintenanceRequestNo());
            }
            else {

                String sqlStatementTwo = "UPDATE maintainrequest SET actualCost = actualCost+ ? WHERE requestNo = ?";
                return CrudUtility.execute(sqlStatementTwo,newExpense.getAmount(),newExpense.getMaintenanceRequestNo());
            }

        }
        else{
            return false;
        }
    }

    public ObservableList<MaintenanceRequestTm> getAllInProgressRequests() throws SQLException, ClassNotFoundException {

        String sql = "select * from maintainrequest where status = 'In Progress'";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<MaintenanceRequestTm> allRequests = FXCollections.observableArrayList();

        while(result.next()){

            String requestId = result.getString(1);
            String desc = result.getString(2);
            double estimatedCost = result.getDouble(3);
            String actualCost = result.getString(4);
            if(actualCost==null){
                actualCost = "-";
            }
            String date = result.getString(5);
            String technician = result.getString(6);
            String tenantId = result.getString(7);
            String status = result.getString(8);

            MaintenanceRequestTm maintenanceRequestTm = new MaintenanceRequestTm(requestId,desc,estimatedCost,actualCost,date,technician,tenantId,status);
            allRequests.add(maintenanceRequestTm);

        }
        return allRequests;
    }
}





