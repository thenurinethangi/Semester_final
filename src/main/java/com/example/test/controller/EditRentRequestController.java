package com.example.test.controller;

import com.example.test.dto.CustomerDto;
import com.example.test.dto.RequestDto;
import com.example.test.dto.tm.CustomerTm;
import com.example.test.dto.tm.HouseTypeTm;
import com.example.test.dto.tm.RequestTm;
import com.example.test.model.EditRentRequestModel;
import com.example.test.model.HouseTypeModel;
import com.example.test.validation.UserInputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditRentRequestController {

    @FXML
    private Label requestIdLabel;

    @FXML
    private TextField noOfMembersTxt;

    @FXML
    private ComboBox<String> houseTypeCmb;

    @FXML
    private ComboBox<String> leaseTurnCmb;

    @FXML
    private TextField annualIncomeTxt;

    @FXML
    private TextField monthlyIncomeTxt;

    @FXML
    private TextField reasonToMoveTxt;

    @FXML
    private TextField estimatedBudgetTxt;

    @FXML
    private TextField bankDetailsTxt;

    @FXML
    private TextField previousLandlordNoTxt;

    @FXML
    private ComboBox<String> docProvidedCmb;

    @FXML
    private ComboBox<String> isAgreedCmb;

    @FXML
    private ComboBox<String> customerRequestStatusCmb;

    @FXML
    private ComboBox<String> isQualifiedCmb;

    @FXML
    private ComboBox<String> paymentDoneCmb;

    @FXML
    private ComboBox<String> requestStatusCmb;

    @FXML
    private Label familyMembersCountLabel;

    @FXML
    private Label monthlyIncomeLabel;

    @FXML
    private Label annualIncomeLabel;

    @FXML
    private Label bankDetailsLabel;

    @FXML
    private Label reasonToMoveLabel;

    @FXML
    private Label monthlyBudgetLabel;

    @FXML
    private Label landlordNoLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Button editbtn;

    @FXML
    private Button cancelbtn;

    private final EditRentRequestModel editRentRequestModel= new EditRentRequestModel();
    private final HouseTypeModel houseTypeModel;

    {
        try {
            houseTypeModel = new HouseTypeModel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private RequestDto selectedRequestToEdit;


    @FXML
    void cancelOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    void editOnAction(ActionEvent event) {

        String requestId = requestIdLabel.getText();
        String houseType = houseTypeCmb.getSelectionModel().getSelectedItem();
        String familyMembersCount = noOfMembersTxt.getText();
        String monthlyIncome = monthlyIncomeTxt.getText();
        String annualIncome = annualIncomeTxt.getText();
        String bankDetails = bankDetailsTxt.getText();
        String reasonToMove = reasonToMoveTxt.getText();
        String estimatedBudget = estimatedBudgetTxt.getText();
        String leaseTurn = leaseTurnCmb.getSelectionModel().getSelectedItem();
        String landLordNumber = previousLandlordNoTxt.getText();
        String docProvided = docProvidedCmb.getSelectionModel().getSelectedItem();
        String isQualified = isQualifiedCmb.getSelectionModel().getSelectedItem();
        String isAgreed = isAgreedCmb.getSelectionModel().getSelectedItem();
        String isPaymentDone = paymentDoneCmb.getSelectionModel().getSelectedItem();
        String requestStatus = requestStatusCmb.getSelectionModel().getSelectedItem();
        String customerRequestStatus = customerRequestStatusCmb.getSelectionModel().getSelectedItem();

        if(selectedRequestToEdit.getRentOrBuy().equals("Rent")) {


            if (houseType == null || houseType.equals("Select") || familyMembersCount.isEmpty() || monthlyIncome.isEmpty() || annualIncome.isEmpty() || bankDetails.isEmpty() || reasonToMove.isEmpty() || estimatedBudget.isEmpty() || leaseTurn == null || leaseTurn.equals("Select")) {

                Notifications notifications = Notifications.create();
                notifications.title("Notification");
                notifications.text("Essential Required Field Can't Be Empty");
                notifications.hideCloseButton();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.CENTER);
                notifications.darkStyle();
                notifications.showInformation();
                return;
            }


            boolean b1 = UserInputValidation.checkNumberLessThanTenValidation(familyMembersCount);
            if (!b1) {
                familyMembersCountLabel.setText("Not a valid member count for a family");
            } else {
                familyMembersCountLabel.setText("");
            }
            boolean b2 = UserInputValidation.checkDecimalValidation(monthlyIncome);
            if (!b2) {
                monthlyIncomeLabel.setText("Not a valid monthly income value, please enter correct details");
            } else {
                monthlyIncomeLabel.setText("");
            }
            boolean b3 = UserInputValidation.checkDecimalValidation(annualIncome);
            if (!b3) {
                annualIncomeLabel.setText("Not a valid annual income, please enter correct details");
            } else {
                annualIncomeLabel.setText("");
            }
            boolean b4 = UserInputValidation.checkTextValidation(bankDetails);
            if (!b4) {
                bankDetailsLabel.setText("Not a valid input for this field, please enter correct details");
            } else {
                bankDetailsLabel.setText("");
            }
            boolean b5 = UserInputValidation.checkTextValidation(reasonToMove);
            if (!b5) {
                reasonToMoveLabel.setText("Not a valid input for this filed, please enter correct details");
            } else {
                reasonToMoveLabel.setText("");
            }
            boolean b6 = UserInputValidation.checkDecimalValidation(estimatedBudget);
            if (!b6) {
                monthlyBudgetLabel.setText("Not a valid input for this filed, please enter correct details");
            } else {
                monthlyBudgetLabel.setText("");
            }
            boolean b7 = false;
            if (!landLordNumber.isEmpty() && !landLordNumber.equals("N/A")) {
                b7 = UserInputValidation.checkPhoneNoValidation(landLordNumber);
                if (!b7) {
                    landlordNoLabel.setText("Not a valid phone number");
                } else {
                    landlordNoLabel.setText("");
                }
            }

            if (!landLordNumber.isEmpty() && !landLordNumber.equals("N/A")) {

                if (b1 && b2 && b3 && b4 && b5 && b6 && b7) {

                    RequestDto request = new RequestDto();

                    request.setRequestId(requestId);
                    request.setHouseType(houseType);
                    request.setNoOfFamilyMembers(Integer.parseInt(familyMembersCount));
                    request.setMonthlyIncome(Double.parseDouble(monthlyIncome));
                    request.setAnnualIncome(Double.parseDouble(annualIncome));
                    request.setBankAccountDetails(bankDetails);
                    request.setReasonForLeaving(reasonToMove);
                    request.setEstimatedMonthlyBudgetForRent(estimatedBudget);
                    request.setLeaseTurnDesire(leaseTurn);
                    request.setPreviousLandlordNumber(landLordNumber);
                    request.setAllDocumentsProvided(docProvided);
                    request.setQualifiedCustomerOrNot(isQualified);
                    request.setAgreesToAllTermsAndConditions(isAgreed);
                    request.setIsPaymentsCompleted(isPaymentDone);
                    request.setRequestStatus(requestStatus);
                    request.setCustomerRequestStatus(customerRequestStatus);

                    System.out.println(request.toString());

                    String response = null;
                    try {
                        response = editRentRequestModel.editRentRequest(request,selectedRequestToEdit);

                        Notifications notifications = Notifications.create();
                        notifications.title("Notification");
                        notifications.text(response);
                        notifications.hideCloseButton();
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.CENTER);
                        notifications.darkStyle();
                        notifications.showInformation();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                }


            } else {

                if (b1 && b2 && b3 && b4 && b5 && b6) {

                    RequestDto request = new RequestDto();

                    request.setRequestId(requestId);
                    request.setHouseType(houseType);
                    request.setNoOfFamilyMembers(Integer.parseInt(familyMembersCount));
                    request.setMonthlyIncome(Double.parseDouble(monthlyIncome));
                    request.setAnnualIncome(Double.parseDouble(annualIncome));
                    request.setBankAccountDetails(bankDetails);
                    request.setReasonForLeaving(reasonToMove);
                    request.setEstimatedMonthlyBudgetForRent(estimatedBudget);
                    request.setLeaseTurnDesire(leaseTurn);
                    request.setPreviousLandlordNumber("N/A");
                    request.setAllDocumentsProvided(docProvided);
                    request.setQualifiedCustomerOrNot(isQualified);
                    request.setAgreesToAllTermsAndConditions(isAgreed);
                    request.setIsPaymentsCompleted(isPaymentDone);
                    request.setRequestStatus(requestStatus);
                    request.setCustomerRequestStatus(customerRequestStatus);

                    String response = null;
                    try {
                        response = editRentRequestModel.editRentRequest(request,selectedRequestToEdit);

                        Notifications notifications = Notifications.create();
                        notifications.title("Notification");
                        notifications.text(response);
                        notifications.hideCloseButton();
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.CENTER);
                        notifications.darkStyle();
                        notifications.showInformation();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                }

            }
        }

        else{

            if (houseType == null || houseType.equals("Select") || familyMembersCount.isEmpty() || monthlyIncome.isEmpty() || annualIncome.isEmpty() || bankDetails.isEmpty() || reasonToMove.isEmpty()) {

                Notifications notifications = Notifications.create();
                notifications.title("Notification");
                notifications.text("Essential Required Field Can't Be Empty");
                notifications.hideCloseButton();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.CENTER);
                notifications.darkStyle();
                notifications.showInformation();
                return;
            }

            boolean b1 = UserInputValidation.checkNumberLessThanTenValidation(familyMembersCount);
            if (!b1) {
                familyMembersCountLabel.setText("Not a valid member count for a family");
            } else {
                familyMembersCountLabel.setText("");
            }
            boolean b2 = UserInputValidation.checkDecimalValidation(monthlyIncome);
            if (!b2) {
                monthlyIncomeLabel.setText("Not a valid monthly income value, please enter correct details");
            } else {
                monthlyIncomeLabel.setText("");
            }
            boolean b3 = UserInputValidation.checkDecimalValidation(annualIncome);
            if (!b3) {
                annualIncomeLabel.setText("Not a valid annual income, please enter correct details");
            } else {
                annualIncomeLabel.setText("");
            }
            boolean b4 = UserInputValidation.checkTextValidation(bankDetails);
            if (!b4) {
                bankDetailsLabel.setText("Not a valid input for this field, please enter correct details");
            } else {
                bankDetailsLabel.setText("");
            }
            boolean b5 = UserInputValidation.checkTextValidation(reasonToMove);
            if (!b5) {
                reasonToMoveLabel.setText("Not a valid input for this filed, please enter correct details");
            } else {
                reasonToMoveLabel.setText("");
            }
            boolean b7 = false;
            if (!landLordNumber.isEmpty() && !landLordNumber.equals("N/A")) {
                b7 = UserInputValidation.checkPhoneNoValidation(landLordNumber);
                if (!b7) {
                    landlordNoLabel.setText("Not a valid phone number");
                } else {
                    landlordNoLabel.setText("");
                }
            }

            if (!landLordNumber.isEmpty() && !landLordNumber.equals("N/A")) {

                if (b1 && b2 && b3 && b4 && b5 && b7) {

                    RequestDto request = new RequestDto();

                    request.setRequestId(requestId);
                    request.setHouseType(houseType);
                    request.setNoOfFamilyMembers(Integer.parseInt(familyMembersCount));
                    request.setMonthlyIncome(Double.parseDouble(monthlyIncome));
                    request.setAnnualIncome(Double.parseDouble(annualIncome));
                    request.setBankAccountDetails(bankDetails);
                    request.setReasonForLeaving(reasonToMove);
                    request.setEstimatedMonthlyBudgetForRent("N/A");
                    request.setLeaseTurnDesire("N/A");
                    request.setPreviousLandlordNumber(landLordNumber);
                    request.setAllDocumentsProvided(docProvided);
                    request.setQualifiedCustomerOrNot(isQualified);
                    request.setAgreesToAllTermsAndConditions(isAgreed);
                    request.setIsPaymentsCompleted(isPaymentDone);
                    request.setRequestStatus(requestStatus);
                    request.setCustomerRequestStatus(customerRequestStatus);


                    String response = null;
                    try {
                        response = editRentRequestModel.editRentRequest(request,selectedRequestToEdit);

                        Notifications notifications = Notifications.create();
                        notifications.title("Notification");
                        notifications.text(response);
                        notifications.hideCloseButton();
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.CENTER);
                        notifications.darkStyle();
                        notifications.showInformation();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                }


            } else {

                if (b1 && b2 && b3 && b4 && b5) {

                    RequestDto request = new RequestDto();

                    request.setRequestId(requestId);
                    request.setHouseType(houseType);
                    request.setNoOfFamilyMembers(Integer.parseInt(familyMembersCount));
                    request.setMonthlyIncome(Double.parseDouble(monthlyIncome));
                    request.setAnnualIncome(Double.parseDouble(annualIncome));
                    request.setBankAccountDetails(bankDetails);
                    request.setReasonForLeaving(reasonToMove);
                    request.setEstimatedMonthlyBudgetForRent("N/A");
                    request.setLeaseTurnDesire("N/A");
                    request.setPreviousLandlordNumber("N/A");
                    request.setAllDocumentsProvided(docProvided);
                    request.setQualifiedCustomerOrNot(isQualified);
                    request.setAgreesToAllTermsAndConditions(isAgreed);
                    request.setIsPaymentsCompleted(isPaymentDone);
                    request.setRequestStatus(requestStatus);
                    request.setCustomerRequestStatus(customerRequestStatus);

                    String response = null;
                    try {
                        response = editRentRequestModel.editRentRequest(request,selectedRequestToEdit);

                        Notifications notifications = Notifications.create();
                        notifications.title("Notification");
                        notifications.text(response);
                        notifications.hideCloseButton();
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.CENTER);
                        notifications.darkStyle();
                        notifications.showInformation();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                }

            }

        }

    }

    public void setSelectedRequestData(RequestTm selectedRequest) {

        try {
            selectedRequestToEdit = editRentRequestModel.getSelectedRequestAllDetails(selectedRequest.getRequestId());

            if(selectedRequestToEdit==null){
               return;
            }

            if(selectedRequestToEdit.getRentOrBuy().equals("Rent")){

                estimatedBudgetTxt.setDisable(false);
                leaseTurnCmb.setDisable(false);
            }
            else{

                estimatedBudgetTxt.setDisable(true);
                leaseTurnCmb.setDisable(true);
            }

            setRequestCurrentValues();
            setValuesToHouseTypeCmb();
            setValuesToLeaseTurnCmb();
            setValuesToAllDocProvidedCmb();
            setValuesToIsAgreedCmb();
            setValuesToIsQualifiedCmb();
            setValuesToRequestStatusCmb();
            setValuesToCustomerRequestStatusCmb();
            setValuesToPaymentDoneCmb();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public void setRequestCurrentValues() {

        requestIdLabel.setText(selectedRequestToEdit.getRequestId());
        dateLabel.setText(selectedRequestToEdit.getDate());
        houseTypeCmb.setValue(selectedRequestToEdit.getHouseType());
        noOfMembersTxt.setText(String.valueOf(selectedRequestToEdit.getNoOfFamilyMembers()));
        monthlyIncomeTxt.setText(String.valueOf(selectedRequestToEdit.getMonthlyIncome()));
        annualIncomeTxt.setText(String.valueOf(selectedRequestToEdit.getAnnualIncome()));
        bankDetailsTxt.setText(selectedRequestToEdit.getBankAccountDetails());
        reasonToMoveTxt.setText(selectedRequestToEdit.getReasonForLeaving());
        estimatedBudgetTxt.setText(selectedRequestToEdit.getEstimatedMonthlyBudgetForRent());
        leaseTurnCmb.setValue(selectedRequestToEdit.getLeaseTurnDesire());
        previousLandlordNoTxt.setText(selectedRequestToEdit.getPreviousLandlordNumber());
        docProvidedCmb.setValue(selectedRequestToEdit.getAllDocumentsProvided());
        isQualifiedCmb.setValue(selectedRequestToEdit.getQualifiedCustomerOrNot());
        isAgreedCmb.setValue(selectedRequestToEdit.getAgreesToAllTermsAndConditions());
        paymentDoneCmb.setValue(selectedRequestToEdit.getIsPaymentsCompleted());
        customerRequestStatusCmb.setValue(selectedRequestToEdit.getCustomerRequestStatus());
        requestStatusCmb.setValue(selectedRequestToEdit.getRequestStatus());


    }


    public void setValuesToHouseTypeCmb() {

        ObservableList<String> houseTypes = FXCollections.observableArrayList();
        ObservableList<HouseTypeTm> houseTypeTms;
        try {
            houseTypeTms = houseTypeModel.loadTableData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (HouseTypeTm x : houseTypeTms) {

            houseTypes.add(x.getHouseType());
        }

        houseTypeCmb.setItems(houseTypes);

    }


    public void setValuesToLeaseTurnCmb(){

        ObservableList<String> leaseTurns = FXCollections.observableArrayList("6 Months","12 Months","2 Year");
        leaseTurnCmb.setItems(leaseTurns);
    }


    public void setValuesToAllDocProvidedCmb(){

        ObservableList<String> isAllDocProvided = FXCollections.observableArrayList("Not Yet","Yes");
        docProvidedCmb.setItems(isAllDocProvided);

    }

    public void setValuesToIsQualifiedCmb(){

        ObservableList<String> isQualified = FXCollections.observableArrayList("-","No","Yes");
        isQualifiedCmb.setItems(isQualified);

    }

    public void setValuesToIsAgreedCmb(){

        ObservableList<String> isAgreed = FXCollections.observableArrayList("Not Yet","No","Yes");
        isAgreedCmb.setItems(isAgreed);

    }

    public void setValuesToPaymentDoneCmb(){

        ObservableList<String> isPaymentDone = FXCollections.observableArrayList("Not Yet","Yes");
        paymentDoneCmb.setItems(isPaymentDone);

    }

    public void setValuesToCustomerRequestStatusCmb(){

        ObservableList<String> customerRequestStatus = FXCollections.observableArrayList("Canceled","Confirmed","Awaiting Confirmation");
        customerRequestStatusCmb.setItems(customerRequestStatus);

    }

    public void setValuesToRequestStatusCmb(){

        ObservableList<String> requestStatus = FXCollections.observableArrayList("In Process","Rejected");
        requestStatusCmb.setItems(requestStatus);

    }
}



