package com.example.test.controller;

import com.example.test.dto.HouseReturnDto;
import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.model.HouseStatusCheckModel;
import com.example.test.model.ReturnHouseModel;
import com.example.test.model.TenantModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;

public class HouseReturnConformationController {

    @FXML
    private Button rePayAndReturnBtn;

    @FXML
    private Button returnBtn;

    @FXML
    private Label tenantNameLabel;

    @FXML
    private Label houseIdLabel;

    @FXML
    private Label lastPaidDateLabel;

    @FXML
    private Label remainingDepositLabel;

    @FXML
    private Label lastHouseStatusCheckLabel;

    @FXML
    private Label tenantIdLabel;

    @FXML
    private TextArea messageLabel;

    private LeaseAgreementTm selectedLeaseAgreementDetails;
    private final TenantModel tenantModel = new TenantModel();
    private final HouseStatusCheckModel houseStatusCheckModel = new HouseStatusCheckModel();
    private final ReturnHouseModel returnHouseModel = new ReturnHouseModel();
    private  HouseReturnDto houseReturnDto;
    private TenantDto tenant;

    @FXML
    void onlyReturnOnAction(ActionEvent event) {

        try {
            String response = returnHouseModel.reclaimHouse(houseReturnDto);
            notification(response);

            if(response.equals("Successfully Reclaiming The House!")){
                returnBtn.setDisable(true);
            }

        }  catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while reclaiming the house: " + e.getMessage());
            notification("An error occurred while reclaiming the house. Please try again or contact support.");
        }

    }


    @FXML
    void rePayAndReturnOnAction(ActionEvent event) {

        try {
            String response = returnHouseModel.reclaimHouseWithRefundSecurityDeposit(houseReturnDto,tenant);
            notification(response);

            if(response.equals("Successfully Refund The Security Payment And Reclaiming The House!")){
                returnBtn.setDisable(true);
                rePayAndReturnBtn.setDisable(true);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while reclaiming the house: " + e.getMessage());
            notification("An error occurred while reclaiming the house. Please try again or contact support.");
        }

    }


    public void setSelectedAgreementDetailsToReturn(LeaseAgreementTm selectedLeaseAgreement) {

        this.selectedLeaseAgreementDetails = selectedLeaseAgreement;

        houseReturnDto = new HouseReturnDto(selectedLeaseAgreementDetails.getTenantId(),selectedLeaseAgreementDetails.getHouseId(),selectedLeaseAgreementDetails.getLeaseId(),"Expiration of the Lease Turn");

        tenantIdLabel.setText(selectedLeaseAgreement.getTenantId());
        houseIdLabel.setText(selectedLeaseAgreement.getHouseId());

        try{

            tenant = tenantModel.getMoreTenantDetails(selectedLeaseAgreement.getTenantId());
            tenantNameLabel.setText(tenant.getName());
            lastPaidDateLabel.setText(tenant.getLastPaidMonth());
            remainingDepositLabel.setText(String.valueOf(tenant.getSecurityPaymentRemain()));

            HouseStatusCheckDto houseStatusCheckDto = houseStatusCheckModel.getLastInspectCheckByTenant(selectedLeaseAgreementDetails.getTenantId());
            if(houseStatusCheckDto.getTotalHouseStatus()==null){

                lastHouseStatusCheckLabel.setText("No Inspect For This Rented House");
            }
            else{
                if(houseStatusCheckDto.getIsPaymentDone().equals("N/A")){

                    lastHouseStatusCheckLabel.setText("Total House Status: "+houseStatusCheckDto.getTotalHouseStatus() + "  ,  No Damages Noted In The Last Inspection");
                }
                else{
                    lastHouseStatusCheckLabel.setText("Total House Status: "+houseStatusCheckDto.getTotalHouseStatus() + "  ,  Is Payment Done For Damages: "+ houseStatusCheckDto.getIsPaymentDone());

                    if(houseStatusCheckDto.getTotalHouseStatus().equals("Damaged") && houseStatusCheckDto.getIsPaymentDone().equals("Not Yet")){
                        messageLabel.setText("The tenant has damaged the house without compensation.\nIt is advised to seek payment and reclaim the property");
                        rePayAndReturnBtn.setDisable(true);
                    }

                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting the agreement details to return: " + e.getMessage());
            notification("An error occurred while setting the agreement details to return. Please try again or contact support.");
        }

    }


    public void notification(String message){

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text(message);
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(4));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();
    }
}
