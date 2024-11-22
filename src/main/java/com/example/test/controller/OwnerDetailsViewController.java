package com.example.test.controller;

import com.example.test.dto.tm.OwnerTm;
import com.example.test.dto.tm.PurchaseAgreementTm;
import com.example.test.dto.tm.UnitTm;
import com.example.test.model.OwnerModel;
import com.example.test.model.TenantModel;
import com.example.test.model.UnitModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class OwnerDetailsViewController {

    @FXML
    private Label houseId;

    @FXML
    private Label houseType;

    @FXML
    private Label purchaseDate;

    @FXML
    private Label membersCount;

    @FXML
    private Label email;

    @FXML
    private Label phoneNo;

    @FXML
    private Label name;

    @FXML
    private Label ownerId;

    @FXML
    private Label invoiceNo;

    @FXML
    private Label purchasePrice;


    private OwnerTm owner;
    private final OwnerModel ownerModel = new OwnerModel();
    private final TenantModel tenantModel = new TenantModel();


    public void setSelectedOwnerDetails(OwnerTm selectedOwner) {

        owner = selectedOwner;

        ownerId.setText(owner.getOwnerId());
        name.setText(owner.getName());
        phoneNo.setText(owner.getPhoneNo());
        membersCount.setText(String.valueOf(owner.getMembersCount()));
        invoiceNo.setText(owner.getInvoiceNo());
        purchaseDate.setText(owner.getPurchaseDate());
        houseId.setText(owner.getOwnerId());

        try {
            String mail =  ownerModel.getOwnerEmailById(owner.getOwnerId());
            email.setText(mail);

            String type = tenantModel.getHouseTypeByHouseId(owner.getHouseId());
            houseType.setText(type);

            String price = ownerModel.getPurchasePriceByHouseId(owner.getHouseId());
            purchasePrice.setText(price);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while getting the owner details: " + e.getMessage());
        }

    }

}




