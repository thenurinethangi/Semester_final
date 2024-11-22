package com.example.test.controller;

import com.example.test.dto.tm.PurchaseAgreementTm;
import com.example.test.model.OwnerModel;
import com.example.test.model.TenantModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class PurchaseAgreementDetailsController {

    @FXML
    private Label houseType;

    @FXML
    private Label houseId;

    @FXML
    private Label purchasePrice;

    @FXML
    private Label purchaseDate;

    @FXML
    private Label name;

    @FXML
    private Label ownerId;

    @FXML
    private Label agreementId;


    private PurchaseAgreementTm purchaseAgreement;
    private final TenantModel tenantModel = new TenantModel();
    private final OwnerModel ownerModel = new OwnerModel();

    public void setSelectedAgreementDetails(PurchaseAgreementTm selectedAgreement) {

        purchaseAgreement = selectedAgreement;

        agreementId.setText(purchaseAgreement.getPurchaseAgreementId());
        purchaseDate.setText(purchaseAgreement.getSignedDate());
        purchasePrice.setText(String.valueOf(purchaseAgreement.getPurchasePrice()));
        houseId.setText(purchaseAgreement.getHouseId());
        ownerId.setText(purchaseAgreement.getHomeOwnerId());

        try{
            String type = tenantModel.getHouseTypeByHouseId(purchaseAgreement.getHouseId());
            houseType.setText(type);

            String ownerName = ownerModel.getOwnerDetailsById(purchaseAgreement.getHomeOwnerId());
            name.setText(ownerName);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading Purchase Agreement data: " + e.getMessage());
        }

    }
}







