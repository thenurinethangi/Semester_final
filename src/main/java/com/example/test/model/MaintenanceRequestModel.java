package com.example.test.model;

import com.example.test.CrudUtility;
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

        String sql = "select Distinct tenantId from maintainrequest order by tenantId asc";
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
}





