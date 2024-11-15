package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.LeaseAgreementDto;
import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.dto.tm.RequestTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LeaseAgreementModel {

    private final HouseStatusCheckModel houseStatusCheckModel = new HouseStatusCheckModel();
    private final TenantModel tenantModel = new TenantModel();

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

        String sql = "SELECT * FROM leaseagreement WHERE status != 'Deleted'";
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

            LocalDate endingDate = LocalDate.parse(endDate);

            if (LocalDate.now().isAfter(endingDate) && !status.equals("Canceled") && !status.equals("Deleted")) {

                status = "Expired";

                String sqlState = "UPDATE leaseagreement SET status = ? WHERE leaseId = ?";
                boolean res = CrudUtility.execute(sqlState,"Expired",leaseId);
            }


            LeaseAgreementTm leaseAgreement = new LeaseAgreementTm(leaseId,tenantId,houseId,leaseTurn,startDate,endDate,status);
            allAgreements.add(leaseAgreement);
        }

        return allAgreements;
    }



    public LeaseAgreementDto getSelectedAgreementDetails(LeaseAgreementTm selectedLeaseAgreement) throws SQLException, ClassNotFoundException {

        String sql = "select rentmount from leaseagreement where leaseId = ?";
        ResultSet result = CrudUtility.execute(sql,selectedLeaseAgreement.getLeaseId());

        LeaseAgreementDto leaseAgreementDto = new LeaseAgreementDto();

        if(result.next()){

            leaseAgreementDto.setMonthlyRent(result.getDouble("rentmount"));
        }

        return  leaseAgreementDto;

    }

    public HouseStatusCheckDto getLastHouseInspectCheckDetails(LeaseAgreementTm selectedLeaseAgreement) throws SQLException, ClassNotFoundException {

        return houseStatusCheckModel.getLastInspectCheckByTenant(selectedLeaseAgreement.getTenantId());
    }


    public String reSignAgreement(LeaseAgreementTm selectedLeaseAgreement, String leaseTurn) throws SQLException, ClassNotFoundException {

        LocalDate date = LocalDate.now();
        String today = String.valueOf(date);

        String endDate = "0";

        if(leaseTurn.equals("6 Months")){

            LocalDate dateAfterSixMonths = date.plusMonths(6);
            endDate = String.valueOf(dateAfterSixMonths);

        }
        else if(leaseTurn.equals("12 Months")){

            LocalDate dateAfterTowelMonths = date.plusMonths(12);
            endDate = String.valueOf(dateAfterTowelMonths);

        }
        else if(leaseTurn.equals("18 Months")){

            LocalDate dateAfterEighteenMonths = date.plusMonths(18);
            endDate = String.valueOf(dateAfterEighteenMonths);

        }
        else if(leaseTurn.equals("2 Year")){

            LocalDate dateAfterTwoYears = date.plusMonths(24);
            endDate = String.valueOf(dateAfterTwoYears);

        }

        String sql = "UPDATE leaseagreement SET startDate = ?, endDate = ?, leaseTurn = ?,status = ?  WHERE leaseId = ?";
        boolean result = CrudUtility.execute(sql,today,endDate,leaseTurn,"Active",selectedLeaseAgreement.getLeaseId());

        return result ? "The lease agreement has been successfully renewed." : "Failed to renew lease agreement, try again later";

    }

    public String getTenantEmail(LeaseAgreementTm selectedLeaseAgreement) throws SQLException, ClassNotFoundException {

        return tenantModel.getTenantEmailById(selectedLeaseAgreement);

    }

    public boolean makeSelectedLeaseAgreementCancel(String leaseId) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE leaseagreement SET status = ? WHERE leaseId = ?";
        boolean result = CrudUtility.execute(sql,"Canceled",leaseId);

        return result;
    }

    public String makeLeaseAgreementDeleted(LeaseAgreementTm selectedLeaseAgreement) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE leaseagreement SET status = ? WHERE leaseId = ?";
        boolean result = CrudUtility.execute(sql,"Deleted",selectedLeaseAgreement.getLeaseId());

        return result ? "The Lease Agreement Has Been Successfully Deleted" : "Unable To Delete The Lease Agreement, Please Try Again Later.";
    }

    public String getLeaseAgreementByTenantId(String tenantId) throws SQLException, ClassNotFoundException {

        String sql = "select leaseId from leaseagreement where tenantId = ?";
        ResultSet result = CrudUtility.execute(sql,tenantId);

        String leaseId = "";

        if(result.next()){

            leaseId = result.getString("leaseId");
        }

        return leaseId;
    }
}






