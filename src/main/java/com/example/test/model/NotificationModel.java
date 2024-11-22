package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.TenantDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationModel {


    public ObservableList<TenantDto> getTenantWhoNotDonePayment() throws SQLException, ClassNotFoundException {

        String sql = "select * from tenant where isActiveTenant = 1";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<TenantDto> tenants = FXCollections.observableArrayList();

        try {
            while (result.next()) {
                String tenantId = result.getString("tenantId");
                String name = result.getString("headOfHouseholdName");
                String lastPaymentMonth = result.getString("lastPayementMonth");

                TenantDto tenant = new TenantDto();
                tenant.setTenantId(tenantId);
                tenant.setName(name);
                tenant.setLastPaidMonth(lastPaymentMonth);
                tenants.add(tenant);
            }

            LocalDate currentDate = LocalDate.now();
            String currentMonth = currentDate.getMonth().toString().toLowerCase();
            int currentYear = currentDate.getYear();

            List<TenantDto> notPaidTenants = tenants.stream()
                    .filter(tenant -> !tenant.getLastPaidMonth().toLowerCase().equals(currentMonth))
                    .collect(Collectors.toList());

            System.out.println("Tenants who haven't paid for " + currentMonth + " " + currentYear + ":");
            for (TenantDto tenant : notPaidTenants) {
                System.out.println("Tenant ID: " + tenant.getTenantId() + ", Name: " + tenant.getName());
            }

            return FXCollections.observableArrayList(notPaidTenants);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while getting payment not done tenants: " + e.getMessage());
            return FXCollections.observableArrayList();
        }

    }


    public ObservableList<TenantDto> checkTenantsWhoHaveNotPaidForEarlierMonths() throws SQLException, ClassNotFoundException {

        String sql = "select * from tenant where isActiveTenant = 1";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<TenantDto> tenants = FXCollections.observableArrayList();

        try {
            while (result.next()) {
                String tenantId = result.getString("tenantId");
                String name = result.getString("headOfHouseholdName");
                String lastPaymentMonth = result.getString("lastPayementMonth");

                TenantDto tenant = new TenantDto();
                tenant.setTenantId(tenantId);
                tenant.setName(name);
                tenant.setLastPaidMonth(lastPaymentMonth);

                tenants.add(tenant);
            }

            LocalDate currentDate = LocalDate.now();
            Month currentMonth = currentDate.getMonth();
            int currentYear = currentDate.getYear();

            List<TenantDto> notPaidTenants = tenants.stream()
                    .filter(tenant -> {

                        Month lastPaymentMonthEnum = Month.valueOf(tenant.getLastPaidMonth().toUpperCase());
                        return lastPaymentMonthEnum.compareTo(currentMonth) < 0;

                    })
                    .collect(Collectors.toList());

            System.out.println("Tenants who haven't paid for any earlier months:");
            for (TenantDto tenant : notPaidTenants) {
                System.out.println("Tenant ID: " + tenant.getTenantId() + ", Name: " + tenant.getName());
            }

            return FXCollections.observableArrayList(notPaidTenants);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while getting tenants who haven't paid for earlier months: " + e.getMessage());
            return FXCollections.observableArrayList();

        }
    }

    public ObservableList<String> getExpiredAgreements() throws SQLException, ClassNotFoundException {

        String sql = "select * from leaseagreement where status = 'Expired'";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> expiredAgreements = FXCollections.observableArrayList();

        while(result.next()){

            String id = result.getString("leaseId");
            String tenantId = result.getString("tenantId");
            String endDate = result.getString("endDate");
            String status = result.getString("status");
            expiredAgreements.add("Lease ID: "+id+"  ,Tenant ID: "+tenantId+"  ,End Date: "+endDate+"\n->This Lease Agreement Has Expired, Take Necessary Actions");

        }

        return expiredAgreements;
    }

    public ObservableList<String> getSoonExpiredAgreements() throws SQLException, ClassNotFoundException {

        String sql = "select * from leaseagreement where status = 'Active'";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> expiredSoonAgreements = FXCollections.observableArrayList();

        while(result.next()){

            String id = result.getString("leaseId");
            String tenantId = result.getString("tenantId");
            String endD = result.getString("endDate");
            String status = result.getString("status");

            boolean isNearToEndLease = false;

            String end = endD;
            LocalDate endDate = LocalDate.parse(end);

            LocalDate currentDate = LocalDate.now();

            long daysDifference = ChronoUnit.DAYS.between(currentDate, endDate);

            if (daysDifference >= 0 && daysDifference <= 30) {
                System.out.println("The dates are near (within one month).");
                isNearToEndLease = true;
            } else {
                System.out.println("The dates are not near (not within one month).");
                isNearToEndLease = false;
            }

            if(isNearToEndLease) {
                expiredSoonAgreements.add("Lease ID: " + id + "  ,Tenant ID: " + tenantId + "  ,End Date: " + endDate + "\n->This Lease Agreement Will Soon Expire, Take Necessary Actions");
            }

        }

        return expiredSoonAgreements;
    }

    public ObservableList<String> getInProcessMaintenanceRequest() throws SQLException, ClassNotFoundException {

        String sql = "select * from maintainrequest where status = 'In Progress'";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> notCompleteRequest = FXCollections.observableArrayList();

        while(result.next()){

            String id = result.getString("requestNo");
            String desc = result.getString("description");
            String tenantId = result.getString("tenantId");

            notCompleteRequest.add("Maintenance Request ID: "+id+",  Request Description: "+desc+",  tenant ID: "+tenantId+ "\n->Pending Maintenance Request from Tenant ID: "+tenantId+", Immediate Action Required.");
        }

        return notCompleteRequest;
    }

    public ObservableList<String> getWhoNotPaidDamageCost() throws SQLException, ClassNotFoundException {

        String sql = "select * from housestatuscheck where isPaymentDone = 'Not Yet'";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> notDamageCostPaidTenants = FXCollections.observableArrayList();

        while(result.next()){

            String id = result.getString("checkNumber");
            String tenantId = result.getString("tenantId");
            String houseId = result.getString("houseId");
            String cost = result.getString("estimatedCostForRepair");

            notDamageCostPaidTenants.add("Inspection NO: "+id+",  Tenant ID: "+tenantId+",  House ID: "+houseId+",  Estimated Cost For Repair: "+cost+"\n->The tenant has not paid the house damage costs identified in the latest inspection. Please take necessary action.");
        }

        return notDamageCostPaidTenants;
    }
}




