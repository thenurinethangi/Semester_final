package com.example.test.controller;

import com.example.test.dto.HouseReturnDto;
import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.UnitDto;
import com.example.test.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;


public class AddNewHouseReturnController {

    @FXML
    private TextField tenantIdTxt;

    @FXML
    private Button refundAndReclaimBtn;

    @FXML
    private Button reclaimBtn;

    @FXML
    private Label tenantNameLabel;

    @FXML
    private Label houseIdLabel;

    @FXML
    private Label houseTypeLabel;

    @FXML
    private TextField reasonToLeaveTxt;

    @FXML
    private Label remainingSecurityFundLabel;

    @FXML
    private Label houseInspectDetailsLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label lastPaidMonthLabel;

    private final AddNewHouseReturnModel addNewHouseReturnModel = new AddNewHouseReturnModel();
    private final TenantModel tenantModel = new TenantModel();
    private final UnitModel unitModel = new UnitModel();
    private final HouseStatusCheckModel houseStatusCheckModel = new HouseStatusCheckModel();
    private final ReturnHouseModel returnHouseModel = new ReturnHouseModel();
    private final LeaseAgreementModel leaseAgreementModel = new LeaseAgreementModel();
    private HouseReturnDto houseReturnDto;
    private TenantDto tenantDetails;


    public AddNewHouseReturnController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    void reclaimOnAction(ActionEvent event) {

        houseReturnDto.setReasonToLeave(reasonToLeaveTxt.getText());

        if(houseReturnDto.getTenantId()==null || houseReturnDto.getReasonToLeave()==null){
            return;
        }

        try {
            String response = returnHouseModel.reclaimHouse(houseReturnDto);

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text(response);
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

            if(response.equals("Successfully Reclaiming The House!")){
                clean();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void refundAndReclaimOnAction(ActionEvent event) {

        houseReturnDto.setReasonToLeave(reasonToLeaveTxt.getText());

        if(houseReturnDto.getTenantId()==null || houseReturnDto.getReasonToLeave()==null){
            return;
        }

        try {
            String response = returnHouseModel.reclaimHouseWithRefundSecurityDeposit(houseReturnDto,tenantDetails);

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text(response);
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

            if(response.equals("Successfully Refund The Security Payment And Reclaiming The House!")){
                clean();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void searchIcon(MouseEvent event) {

        String tenantId = tenantIdTxt.getText();

        if(!tenantId.isEmpty()){
            try {
                TenantDto tenant = tenantModel.getMoreTenantDetails(tenantId);

                if(tenant.getName()==null){

                    clean();

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("Please Enter Correct Tenant ID");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();
                    return;
                }
                tenantDetails = tenantModel.getMoreTenantDetails(tenant.getTenantId());

                tenantNameLabel.setText(tenant.getName());
                houseIdLabel.setText(tenant.getHouseId());
                remainingSecurityFundLabel.setText(String.valueOf(tenant.getSecurityPaymentRemain()));
                lastPaidMonthLabel.setText(tenant.getLastPaidMonth());

                UnitDto unit =  unitModel.getHouseDetailsByHouseId(tenant.getHouseId());
                houseTypeLabel.setText(unit.getHouseType());

                houseReturnDto = new HouseReturnDto();
                houseReturnDto.setTenantId(tenant.getTenantId());
                houseReturnDto.setHouseId(tenant.getHouseId());

                String leaseAgreementId = leaseAgreementModel.getLeaseAgreementByTenantId(tenant.getTenantId());
                houseReturnDto.setAgreementId(leaseAgreementId);

                HouseStatusCheckDto houseStatusCheckDto = houseStatusCheckModel.getLastInspectCheckByTenant(tenant.getTenantId());
                if(houseStatusCheckDto.getTotalHouseStatus()==null){

                    houseInspectDetailsLabel.setText("No Inspect For This Rented House");
                }
                else{
                    if(houseStatusCheckDto.getIsPaymentDone().equals("N/A")){

                        houseInspectDetailsLabel.setText("Total House Status: "+houseStatusCheckDto.getTotalHouseStatus() + "  ,  No Damages Noted In The Last Inspection");
                    }
                    else{
                        houseInspectDetailsLabel.setText("Total House Status: "+houseStatusCheckDto.getTotalHouseStatus() + "  ,  Is Payment Done For Damages: "+ houseStatusCheckDto.getIsPaymentDone());

                        if(houseStatusCheckDto.getTotalHouseStatus().equals("Damaged") && houseStatusCheckDto.getIsPaymentDone().equals("Not Yet")){
                            messageLabel.setText("The tenant has damaged the house without compensation.\nIt is advised to seek payment and reclaim the property");
                            refundAndReclaimBtn.setDisable(true);
                        }

                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    }


    public void clean(){

       tenantIdTxt.clear();
       tenantNameLabel.setText("");
       houseTypeLabel.setText("");
       houseIdLabel.setText("");
       lastPaidMonthLabel.setText("");
       houseInspectDetailsLabel.setText("");
       messageLabel.setText("");
       remainingSecurityFundLabel.setText("");
       reasonToLeaveTxt.clear();
       refundAndReclaimBtn.setDisable(false);
       reclaimBtn.setDisable(false);

    }

}
