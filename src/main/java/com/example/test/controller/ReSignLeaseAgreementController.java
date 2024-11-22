package com.example.test.controller;

import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.LeaseAgreementDto;
import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.model.LeaseAgreementModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.time.LocalDate;

public class ReSignLeaseAgreementController {

    @FXML
    private Button reSignBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label monthlyRentLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label leaseIdLabel;

    @FXML
    private ComboBox<String> leaseTurnCmb;

    @FXML
    private Label tenantIdLabel;

    @FXML
    private Label houseIdLabel;

    @FXML
    private Label houseInspectCheckLabel;


    private LeaseAgreementTm selectedLeaseAgreement;
    private final LeaseAgreementModel leaseAgreementModel = new LeaseAgreementModel();


    @FXML
    void cancelBtnOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void reSignBtnOnAction(ActionEvent event) {

       String leaseTurn = leaseTurnCmb.getSelectionModel().getSelectedItem();

       if(leaseTurn==null){
          return;
       }
       else{
           try {
               String response = leaseAgreementModel.reSignAgreement(selectedLeaseAgreement,leaseTurn);
               notification(response);

           } catch (SQLException | ClassNotFoundException e) {
               e.printStackTrace();
               System.err.println("Error while Re-Sign the Lease Agreement: " + e.getMessage());
               notification("An error occurred while Re-Sign the Lease Agreement. Please try again or contact support.");
           }

       }

    }

    public void setSelectedAgreementDetails(LeaseAgreementTm leaseAgreement) {

        this.selectedLeaseAgreement = leaseAgreement;
        setDetailsToColumn();
        setValuesToLeaseTurnCmb();

    }


    private void setValuesToLeaseTurnCmb() {

        ObservableList<String> leaseTurn = FXCollections.observableArrayList("Select","6 Months","12 Months","18 Months","2 Year");
        leaseTurnCmb.setItems(leaseTurn);
        leaseTurnCmb.getSelectionModel().selectFirst();
    }


    public void setDetailsToColumn(){

       leaseIdLabel.setText(selectedLeaseAgreement.getLeaseId());
       tenantIdLabel.setText(selectedLeaseAgreement.getTenantId());
       houseIdLabel.setText(selectedLeaseAgreement.getHouseId());
       startDateLabel.setText(String.valueOf(LocalDate.now()));

        try {
            LeaseAgreementDto leaseAgreementDto = leaseAgreementModel.getSelectedAgreementDetails(selectedLeaseAgreement);
            monthlyRentLabel.setText(String.valueOf(leaseAgreementDto.getMonthlyRent()));

           HouseStatusCheckDto houseStatusCheckDto =  leaseAgreementModel.getLastHouseInspectCheckDetails(selectedLeaseAgreement);
           if(houseStatusCheckDto.getTotalHouseStatus()==null){

               houseInspectCheckLabel.setText("No Inspect For This Rented House");
           }
           else{
               if(houseStatusCheckDto.getIsPaymentDone().equals("N/A")){

                   houseInspectCheckLabel.setText("Total House Status: "+houseStatusCheckDto.getTotalHouseStatus() + "  ,  No Payments For Damages");
               }
               else{
                   houseInspectCheckLabel.setText("Total House Status: "+houseStatusCheckDto.getTotalHouseStatus() + "  ,  Is Payment Done For Damages: "+ houseStatusCheckDto.getIsPaymentDone());

               }
           }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading lease agreement details: " + e.getMessage());
            notification("An error occurred while loading lease agreement details. Please try again or contact support.");
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
