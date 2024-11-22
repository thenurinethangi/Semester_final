package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.CustomerDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.PaymentTm;
import com.example.test.dto.tm.RequestTm;
import com.example.test.dto.tm.TenantTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TenantModel {

    public String generateNewTenantId() throws SQLException, ClassNotFoundException {

        String sql = "select tenantId from tenant order by tenantId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("tenantId");
            String subStr = lastId.substring(1);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("T%04d", id);

        }

        return "T0001";

    }

    public boolean addTenantToSystem(String tenantId, CustomerDto customerDetails, int membersCount, UnitDto rentingUnit, RequestTm request) throws SQLException, ClassNotFoundException {

        String sql = "insert into tenant values(?,?,?,?,?,?,?,?,?,?,?)";

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();

        String today = date.format(formatter);
        System.out.println("today: "+today);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
        String currentMonth = date.format(monthFormatter);
        System.out.println("Current month: " + currentMonth);

        double monthlyPayment = Double.parseDouble(rentingUnit.getMonthlyRent());
        double securityPayment = Double.parseDouble(rentingUnit.getSecurityCharge());

        boolean result = CrudUtility.execute(sql,tenantId,customerDetails.getName(),customerDetails.getPhoneNo(),membersCount,today,monthlyPayment,currentMonth,request.getHouseId(),customerDetails.getEmail(),securityPayment,1);

        return result;
    }

    public ObservableList<TenantTm> getAllTenants() throws SQLException, ClassNotFoundException {

        String sql = "select * from tenant where isActiveTenant!=0";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<TenantTm> allTenants = FXCollections.observableArrayList();

        while(result.next()){

            TenantTm tenant = new TenantTm();

            String id = result.getString(1);
            String name = result.getString(2);
            String phoneNo = result.getString(3);
            int membersCount = result.getInt(4);
            String startDate = result.getString(5);
            double monthlyRent = result.getDouble(6);
            String lastPaidMonth = result.getString(7);
            String houseId = result.getString(8);

            tenant.setTenantId(id);
            tenant.setName(name);
            tenant.setPhoneNo(phoneNo);
            tenant.setMembersCount(membersCount);
            tenant.setRentStartDate(startDate);
            tenant.setMonthlyRent(monthlyRent);
            tenant.setLastPaidMonth(lastPaidMonth);
            tenant.setHouseId(houseId);

            allTenants.add(tenant);
        }

        return allTenants;
    }

    public ObservableList<String> getHouseIds() throws SQLException, ClassNotFoundException {

        String sql = "select houseId from tenant order by houseId desc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> houseIds = FXCollections.observableArrayList();
        houseIds.add("Select");

        while(result.next()){
            houseIds.add(result.getString("houseId"));
        }

        return houseIds;
    }

    public ObservableList<String> getNameSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT headOfHouseholdName FROM tenant WHERE headOfHouseholdName LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> names = FXCollections.observableArrayList();

        while (result.next()) {
            names.add(result.getString("headOfHouseholdName"));
        }

        return names;
    }

    public TenantDto getMoreTenantDetails(String tenantId) throws SQLException, ClassNotFoundException {

        String sql = "select tenantId,headOfHouseholdName,rentForMonth,lastPayementMonth,email,securityPaymentRemain,houseId from tenant where tenantId = ? and isActiveTenant = ?";
        ResultSet result = CrudUtility.execute(sql,tenantId,1);

        TenantDto tenantDto = new TenantDto();

        if(result.next()){
            tenantDto.setTenantId(result.getString("tenantId"));
            tenantDto.setName(result.getString("headOfHouseholdName"));
            tenantDto.setMonthlyRent(result.getDouble("rentForMonth"));
            tenantDto.setLastPaidMonth(result.getString("lastPayementMonth"));
            tenantDto.setSecurityPaymentRemain(result.getDouble("securityPaymentRemain"));
            tenantDto.setEmail(result.getString("email"));
            tenantDto.setHouseId(result.getString("houseId"));
        }

        return tenantDto;

    }

    public String getHouseTypeByHouseId(String houseId) throws SQLException, ClassNotFoundException {

        String sql = "select houseType from house where houseId = ?";
        ResultSet result = CrudUtility.execute(sql,houseId);
        String houseType = "";

        if(result.next()){
            houseType = result.getString("houseType");
        }

        return houseType;

    }

    public String updateTenant(TenantDto tenantDto) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE tenant SET headOfHouseholdName = ?, phoneNo = ?, membersCount = ?, email = ? WHERE tenantId = ?";
        boolean result = CrudUtility.execute(sql,tenantDto.getName(),tenantDto.getPhoneNo(),tenantDto.getMembersCount(),tenantDto.getEmail(),tenantDto.getTenantId());

        return result ? "Tenant Updated Successfully" : "Failed To Update Tenant, Please Try Again Later.";
    }

    public boolean updateLastPaymentMonth(PaymentTm selectedPayment) throws SQLException, ClassNotFoundException {

        String sqlOne = "select lastPayementMonth from tenant where tenantId = ?";
        ResultSet resultOne = CrudUtility.execute(sqlOne,selectedPayment.getTenantId());

        String lastPaidMonth = "January";

        if(resultOne.next()){

            lastPaidMonth = resultOne.getString("lastPayementMonth");
            System.out.println(lastPaidMonth);
        }

        Month month = Month.valueOf(lastPaidMonth.toUpperCase());
        Month previousMonth = month.minus(1);
        System.out.println("The previous month is: " + previousMonth);
        String updatedMonth = String.valueOf(previousMonth).toLowerCase();

        String sqlTwo = "UPDATE tenant SET lastPayementMonth = ? WHERE tenantId = ?";
        boolean resultTwo = CrudUtility.execute(sqlTwo,updatedMonth,selectedPayment.getTenantId());

        return resultTwo;
    }

    public String getTenantEmailById(String tenantId) throws SQLException, ClassNotFoundException {

        String sql = "select email from tenant where tenantId = ?";
        ResultSet result = CrudUtility.execute(sql,tenantId);

        if(result.next()){

            return result.getString("email");
        }
        return "0";
    }

    public boolean makeTenantDeactivate(String tenantId) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE tenant SET isActiveTenant = ? WHERE tenantId = ?";
        boolean result = CrudUtility.execute(sql,0,tenantId);

        return result;
    }

    public boolean checkRemainingSecurityFundEnoughOrNot(String tenantId, String costOfRepair) throws SQLException, ClassNotFoundException {

        String sql = "select securityPaymentRemain from tenant where tenantId = ?";
        ResultSet result = CrudUtility.execute(sql,tenantId);

        double remainingSecurityDeposit = 0.0;
        if(result.next()){

            remainingSecurityDeposit = result.getDouble("securityPaymentRemain");
        }

        if(remainingSecurityDeposit>Double.valueOf(costOfRepair)){
            return true;
        }
        return false;
    }

    public String reduceRepairCostFromSecurityCharge(String tenantId, String costOfRepair) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE tenant SET securityPaymentRemain = securityPaymentRemain - ? WHERE tenantId = ?";
        boolean result = CrudUtility.execute(sql,Double.parseDouble(costOfRepair),tenantId);

        return result ? "Repair costs were successfully deducted from the security deposit." : "Failed to deduct repair cost from security deposit, try again later";
    }

    public TenantDto checkTenantPhoneNo(String tenantDetail) throws SQLException, ClassNotFoundException {

        String sql = "select * from tenant where phoneNo = ? and isActiveTenant = ?";
        ResultSet result = CrudUtility.execute(sql,tenantDetail,1);

        TenantDto tenant = new TenantDto();

        if(result.next()){

            String id = result.getString(1);
            String name = result.getString(2);
            String phoneNo = result.getString(3);
            int membersCount = result.getInt(4);
            String startDate = result.getString(5);
            double monthlyRent = result.getDouble(6);
            String lastPaidMonth = result.getString(7);
            String houseId = result.getString(8);
            String email = result.getString(9);

            tenant.setTenantId(id);
            tenant.setName(name);
            tenant.setPhoneNo(phoneNo);
            tenant.setMembersCount(membersCount);
            tenant.setRentStartDate(startDate);
            tenant.setMonthlyRent(monthlyRent);
            tenant.setLastPaidMonth(lastPaidMonth);
            tenant.setHouseId(houseId);
            tenant.setEmail(email);

        }

        return tenant;
    }

    public boolean setNewLastPaidMonth(TenantDto tenant) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE tenant SET lastPayementMonth = ? WHERE tenantId = ?";
        boolean result = CrudUtility.execute(sql,tenant.getLastPaidMonth(),tenant.getTenantId());

        return result;
    }
}





