package com.example.test.controller;

import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.TenantDto;
import com.example.test.model.HouseStatusCheckModel;
import com.example.test.model.PaymentModel;
import com.example.test.model.TenantModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class AddNewPaymentController {

    @FXML
    private TextField tenantTxt;

    @FXML
    private ImageView searchTenantId;

    @FXML
    private Label tenantNameLabel;

    @FXML
    private Label paymentMonthLabel;

    @FXML
    private Label houseIdLabel;

    @FXML
    private Label montlyRentAmountLabel;

    @FXML
    private TextField houseStatusCheckNoTxt;

    @FXML
    private ImageView searchHouseStatusNo;

    @FXML
    private Label tenantIdLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label damageChargeLabel;

    @FXML
    private Label damagedHouseIdLabel;

    @FXML
    private ListView<String> houseStatusCheckNoList;


    private final PaymentModel paymentModel = new PaymentModel();
    private final TenantModel tenantModel = new TenantModel();
    private final HouseStatusCheckModel houseStatusCheckModel = new HouseStatusCheckModel();
    private TenantDto tenant;
    private HouseStatusCheckDto houseStatusCheck;
    private ObservableList<String> checkNos;


    @FXML
    void houseStatusCheckNoListOnMouseClicked(MouseEvent event) {

        houseStatusCheckNoTxt.setText(houseStatusCheckNoList.getSelectionModel().getSelectedItem());
        houseStatusCheckNoList.getItems().clear();
    }


    @FXML
    void houseStatusCheckNoTxtOnKeyReleased(KeyEvent event) {

        String input = houseStatusCheckNoTxt.getText();

        try {
            checkNos = houseStatusCheckModel.getHouseCheckNumbersSuggestions(input);
            houseStatusCheckNoList.setItems(checkNos);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while getting house status check id suggestions: " + e.getMessage());
            notification("An error occurred while getting house status check id suggestions. Please try again or contact support.");
        }

        if(input.isEmpty()){
            checkNos.clear();
        }
    }


    @FXML
    void payPropertyDamageBtnOnAction(ActionEvent event) {

        if(houseStatusCheck==null){
            return;
        }

        try {
            String response = paymentModel.addNewPropertyDamagePayment(houseStatusCheck);

            notification(response);
            clean();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while paying property damage payment: " + e.getMessage());
            notification("An error occurred while paying property damage payment. Please try again or contact support.");
        }
    }


    @FXML
    void searchHouseStatusNoOnMouseClicked(MouseEvent event) {

        String houseInspectionNumber = houseStatusCheckNoTxt.getText();

        try {
            houseStatusCheck = houseStatusCheckModel.getHouseInspectionDetailsById(houseInspectionNumber);
            tenantIdLabel.setText(houseStatusCheck.getTenantId());
            dateLabel.setText(houseStatusCheck.getDate());
            damagedHouseIdLabel.setText(houseStatusCheck.getHouseId());
            damageChargeLabel.setText(houseStatusCheck.getEstimatedCostForRepair());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while searching house status check id: " + e.getMessage());
            notification("An error occurred while searching house status check id. Please try again or contact support.");
        }
    }

    @FXML
    void payMonthlyRentBtnOnAction(ActionEvent event) {

        if(tenant==null){
            return;
        }

        try {
           String response =  paymentModel.addNewMonthlyPayment(tenant);

            notification(response);
            clean();

        }  catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while paying monthly rent: " + e.getMessage());
            notification("An error occurred while paying monthly rent. Please try again or contact support.");
        }
    }


    @FXML
    void searchTenantIdOnMouseClicked(MouseEvent event) {

        String tenantDetail = tenantTxt.getText();

        if(tenantDetail.isEmpty()){
            notification("Enter Tenant Id Or Phone No for Search Tenant Payment Details");
            return;
        }

        if(tenantDetail.length()==10){
            try {
                tenant = tenantModel.checkTenantPhoneNo(tenantDetail);
            }
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while searching tenant details: " + e.getMessage());
                notification("An error occurred while searching tenant details. Please try again or contact support.");
            }

        }
        else if(tenantDetail.length()==5){
            try {
                tenant = tenantModel.getMoreTenantDetails(tenantDetail);
            }
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while searching tenant details: " + e.getMessage());
                notification("An error occurred while searching tenant details. Please try again or contact support.");
            }
        }
        else{
            notification("Not Valid Tenant Id Or Phone Number");
            return;
        }

        if(tenant.getName()==null){

            notification("Not Valid Tenant Id Or Phone No, Please Enter Correct Tenant Id Or Phone No To Add A New Payment");
            return;
        }

        tenantNameLabel.setText(tenant.getName());
        houseIdLabel.setText(tenant.getHouseId());
        montlyRentAmountLabel.setText(String.valueOf(tenant.getMonthlyRent()));

        String lastPaidMonthAsString = tenant.getLastPaidMonth();

        try {
            Month month = Month.valueOf(lastPaidMonthAsString.toUpperCase());
            Month nextMonth = month.plus(1);
            String nextPaymentMonthAsString = String.valueOf(nextMonth).toLowerCase();
            paymentMonthLabel.setText(nextPaymentMonthAsString);
            tenant.setLastPaidMonth(nextPaymentMonthAsString);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("Error while searching tenant details: " + e.getMessage());
            notification("An error occurred while searching tenant details. Please try again or contact support.");
        }

    }


    public void clean(){

        tenantTxt.clear();
        tenantNameLabel.setText("");
        houseIdLabel.setText("");
        paymentMonthLabel.setText("");
        montlyRentAmountLabel.setText("0.00");
        houseStatusCheckNoTxt.clear();
        tenantIdLabel.setText("");
        dateLabel.setText("");
        damagedHouseIdLabel.setText("");
        damageChargeLabel.setText("0.00");
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



