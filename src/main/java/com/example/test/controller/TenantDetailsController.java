package com.example.test.controller;

import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.TenantTm;
import com.example.test.model.TenantModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class TenantDetailsController {

    @FXML
    private Label lastPaidMonth;

    @FXML
    private Label houseId;

    @FXML
    private Label houseType;

    @FXML
    private Label rentForMonth;

    @FXML
    private Label rentStartDate;

    @FXML
    private Label membersCount;

    @FXML
    private Label email;

    @FXML
    private Label phoneNo;

    @FXML
    private Label name;

    @FXML
    private Label tenantId;

    @FXML
    private Label remainingSecurityPayment;

    private final TenantModel tenantModel = new TenantModel();
    private TenantTm selectedTenant;


    public void setSelectedTenantDetails(TenantTm tenant){

        selectedTenant = tenant;

        tenantId.setText(selectedTenant.getTenantId());
        name.setText(selectedTenant.getName());
        phoneNo.setText(tenant.getPhoneNo());
        membersCount.setText(String.valueOf(selectedTenant.getMembersCount()));
        rentStartDate.setText(selectedTenant.getRentStartDate());
        rentForMonth.setText(String.valueOf(selectedTenant.getMonthlyRent()));
        lastPaidMonth.setText(selectedTenant.getLastPaidMonth());
        houseId.setText(selectedTenant.getHouseId());

        setAdditionalDetails();

    }

    public void setAdditionalDetails(){

        try {
            TenantDto tenantDto = tenantModel.getMoreTenantDetails(selectedTenant.getTenantId());
            remainingSecurityPayment.setText(String.valueOf(tenantDto.getSecurityPaymentRemain()));
            email.setText(tenantDto.getEmail());

            String type = tenantModel.getHouseTypeByHouseId(selectedTenant.getHouseId());
            houseType.setText(type);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}





