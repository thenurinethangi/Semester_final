package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.dto.tm.RequestTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LeaseAgreementModel {

    public boolean addNewLeaseAgreement(String tenantId, RequestTm request, double securityPayment, double monthlyPayment) throws SQLException, ClassNotFoundException {

        String leaseId = generateNewLeaseId();

        LocalDate date = LocalDate.now();
        String today = String.valueOf(date);

        String endDate = "0";

        if(request.getLeaseTurnDesire().equals("6 Months")){

            LocalDate dateAfterSixMonths = date.plusMonths(6);
            endDate = String.valueOf(dateAfterSixMonths);

        }
        else if(request.getLeaseTurnDesire().equals("12 Months")){

            LocalDate dateAfterTowelMonths = date.plusMonths(12);
            endDate = String.valueOf(dateAfterTowelMonths);

        }
        else if(request.getLeaseTurnDesire().equals("18 Months")){

            LocalDate dateAfterEighteenMonths = date.plusMonths(18);
            endDate = String.valueOf(dateAfterEighteenMonths);

        }
        else if(request.getLeaseTurnDesire().equals("2 Year")){

            LocalDate dateAfterTwoYears = date.plusMonths(24);
            endDate = String.valueOf(dateAfterTwoYears);

        }

        String sql = "insert into leaseagreement values(?,?,?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,leaseId,tenantId,request.getHouseId(),request.getLeaseTurnDesire(),today,endDate,monthlyPayment,securityPayment,"Active");

        return result;

    }


    public String generateNewLeaseId() throws SQLException, ClassNotFoundException {

        String sql = "select leaseId from leaseagreement order by leaseId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("leaseId");
            String subStr = lastId.substring(1);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("L%04d", id);

        }

        return  "L0001";
    }

    public ObservableList<LeaseAgreementTm> getAllAgreements() throws SQLException, ClassNotFoundException {

        String sql = "select * from leaseagreement";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<LeaseAgreementTm> allAgreements = FXCollections.observableArrayList();

        while(result.next()){

            String leaseId = result.getString(1);
            String tenantId = result.getString(2);
            String houseId = result.getString(3);
            String leaseTurn = result.getString(4);
            String startDate = result.getString(5);
            String endDate = result.getString(6);
            String status = result.getString(9);

            LeaseAgreementTm leaseAgreement = new LeaseAgreementTm(leaseId,tenantId,houseId,leaseTurn,startDate,endDate,status);
            allAgreements.add(leaseAgreement);
        }

        return allAgreements;
    }
}






