package com.example.test.controller;

import com.example.test.dto.RequestDto;
import com.example.test.dto.tm.CustomerTm;
import com.example.test.model.AddNewRentRequestModel;
import com.example.test.model.CustomerModel;
import com.example.test.validation.UserInputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AddNewRentRequestController implements Initializable {

    @FXML
    private Label requestIdLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private TextField noOfMembersTxt;

    @FXML
    private ComboBox<String> houseTypeCmb;

    @FXML
    private ComboBox<String> leaseTurnCmb;

    @FXML
    private ComboBox<String> smokingCmb;

    @FXML
    private ComboBox<String> criminalBackgroundCmb;

    @FXML
    private ComboBox<String> petCmb;

    @FXML
    private TextField customerIdTxt;

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
    private ImageView seachIcon;

    @FXML
    private Label noOfFamilyMembersLabel;

    @FXML
    private Label customerIdLabel;

    @FXML
    private Label monthlyIncomeLabel;

    @FXML
    private Label annualIncomeLabel;

    @FXML
    private Label bankDetailsLabel;

    @FXML
    private Label reasonForMoveLabel;

    @FXML
    private Label monthlybudgetLabel;

    @FXML
    private Label landlordNoLabel;

    @FXML
    private Button addbtn;

    @FXML
    private Button clearbtn;

    @FXML
    private Button cancelbtn;
    private final AddNewRentRequestModel addNewRentRequestModel = new AddNewRentRequestModel();
    private final CustomerModel customerModel = new CustomerModel();
    private ObservableList<String> yesNo = FXCollections.observableArrayList("Select","Yes","No");


    @FXML
    void addOnAction(ActionEvent event) {

        String requestId = requestIdLabel.getText();
        String customerId = customerIdTxt.getText();
        String houseType = houseTypeCmb.getSelectionModel().getSelectedItem();
        String familyMembersCount = noOfMembersTxt.getText();
        String monthlyIncome = monthlyIncomeTxt.getText();
        String annualIncome = annualIncomeTxt.getText();
        String bankDetails= bankDetailsTxt.getText();
        String reasonToMove = reasonToMoveTxt.getText();
        String estimatedBudget = estimatedBudgetTxt.getText();
        String leaseTurn = leaseTurnCmb.getSelectionModel().getSelectedItem();
        String landLordNumber = previousLandlordNoTxt.getText();
        String smoking= smokingCmb.getSelectionModel().getSelectedItem();
        String criminalBackground = criminalBackgroundCmb.getSelectionModel().getSelectedItem();
        String pets = petCmb.getSelectionModel().getSelectedItem();

        if(customerId.isEmpty() || houseType==null || houseType.equals("Select") || familyMembersCount.isEmpty() || monthlyIncome.isEmpty() ||
        annualIncome.isEmpty() || bankDetails.isEmpty() || reasonToMove.isEmpty() || estimatedBudget.isEmpty() || leaseTurn==null || leaseTurn.equals("Select") || smoking==null || smoking.equals("Select") || criminalBackground==null || criminalBackground.equals("Select") || pets==null || pets.equals("Select")){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Please Enter Require Field To Add New House Rent Request");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();
            return;
        }

        try {
            ObservableList<CustomerTm> cus = customerModel.getCustomerById(customerId);

            if(cus.isEmpty()){
                customerIdLabel.setText("This Customer Id does not exist");
                return;
            }
            else{
                customerIdLabel.setText("");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        boolean b1 = UserInputValidation.checkNumberLessThanTenValidation(familyMembersCount);
        if(!b1){
            noOfFamilyMembersLabel.setText("Not a valid member count for a family");
        }
        else{
            noOfFamilyMembersLabel.setText("");
        }
        boolean b2 = UserInputValidation.checkDecimalValidation(monthlyIncome);
        if(!b2){
            monthlyIncomeLabel.setText("Not a valid monthly income value, please enter correct details");
        }
        else{
            monthlyIncomeLabel.setText("");
        }
        boolean b3 = UserInputValidation.checkDecimalValidation(annualIncome);
        if(!b3){
            annualIncomeLabel.setText("Not a valid annual income, please enter correct details");
        }
        else{
            annualIncomeLabel.setText("");
        }
        boolean b4 = UserInputValidation.checkTextValidation(bankDetails);
        if(!b4){
            bankDetailsLabel.setText("Not a valid input for this field, please enter correct details");
        }
        else{
            bankDetailsLabel.setText("");
        }
        boolean b5 = UserInputValidation.checkTextValidation(reasonToMove);
        if(!b5){
            reasonForMoveLabel.setText("Not a valid input for this filed, lease enter correct details");
        }
        else{
            reasonForMoveLabel.setText("");
        }
        boolean b6 = UserInputValidation.checkDecimalValidation(estimatedBudget);
        if(!b6){
            monthlybudgetLabel.setText("Not a valid input for this filed, lease enter correct details");
        }
        else{
            monthlybudgetLabel.setText("");
        }
        boolean b7 = false;
        if(!landLordNumber.isEmpty()) {
            b7 = UserInputValidation.checkPhoneNoValidation(landLordNumber);
            if (!b7) {
                landlordNoLabel.setText("Not a valid phone number");
            } else {
                landlordNoLabel.setText("");
            }
        }

        if(!landLordNumber.isEmpty()){

            if(b1 && b2 && b3 && b4 && b5 && b6 && b7){

                RequestDto requestDto = new RequestDto(requestId,customerId,dateLabel.getText(),"Rent",houseType,Integer.parseInt(familyMembersCount),
                                        Double.parseDouble(monthlyIncome),Double.parseDouble(annualIncome),bankDetails,reasonToMove,estimatedBudget,
                                        leaseTurn,landLordNumber,smoking,criminalBackground,pets);

                String response = null;
                try {
                    response = addNewRentRequestModel.addNewRentRequest(requestDto);
                    if(response.equals("Successfully Added New Rent Request")){
                        generateNewRequestId();
                    }
                    clean();

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

        else if(landLordNumber.isEmpty()){

            if(b1 && b2 && b3 && b4 && b5 && b6){

                RequestDto requestDto = new RequestDto(requestId,customerId,dateLabel.getText(),"Rent",houseType,Integer.parseInt(familyMembersCount),
                        Double.parseDouble(monthlyIncome),Double.parseDouble(annualIncome),bankDetails,reasonToMove,estimatedBudget,
                        leaseTurn,"N/A",smoking,criminalBackground,pets);

                String response = null;
                try {
                    response = addNewRentRequestModel.addNewRentRequest(requestDto);
                    if(response.equals("Successfully Added New Rent Request")){
                        generateNewRequestId();
                    }
                    clean();

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


    @FXML
    void cancelOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    void clearOnAction(ActionEvent event) {

        clean();
    }


    @FXML
    void searchOnMouseClicked(MouseEvent event) {

        String cusId = customerIdTxt.getText();

        if(cusId.isEmpty()){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Please Enter Customer Phone Number or NIC Number");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

        }
        else{

            boolean b1 = UserInputValidation.checkPhoneNoValidation(cusId);
            boolean b2 = UserInputValidation.checkNICValidation(cusId);

            if(b1){

                try {
                   ObservableList<CustomerTm> customer =  customerModel.searchCustomerAlreadyExistOrNot(cusId);

                   if(customer.isEmpty()){

                       Notifications notifications = Notifications.create();
                       notifications.title("Notification");
                       notifications.text("Not Registered Customer, Please Add As New Customer");
                       notifications.hideCloseButton();
                       notifications.hideAfter(Duration.seconds(5));
                       notifications.position(Pos.CENTER);
                       notifications.darkStyle();
                       notifications.showInformation();

                       try{
                           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewCustomer.fxml"));
                           Parent root = fxmlLoader.load();
                           Scene scene = new Scene(root);
                           Stage stage = new Stage();
                           stage.setScene(scene);
                           stage.show();

                       } catch (IOException e) {
                           e.printStackTrace();
                       }

                   }
                   else{

                       customerIdTxt.setText(customer.get(0).getCustomerId());
                   }

                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }

            else if(b2){

                try {
                    String customerDetails = customerModel.searchCustomerAlreadyExistOrNotByNic(cusId);
                    if(customerDetails.isEmpty()){

                        Notifications notifications = Notifications.create();
                        notifications.title("Notification");
                        notifications.text("Not Registered Customer, Please Add As New Customer");
                        notifications.hideCloseButton();
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.CENTER);
                        notifications.darkStyle();
                        notifications.showInformation();

                        try{
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewCustomer.fxml"));
                            Parent root = fxmlLoader.load();
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        customerIdTxt.setText(customerDetails);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }

        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNewRequestId();
        setDate();
        setValuesToLeaseTurnCmb();
        setValuesToHouseTypeCmb();
        smokingCmb.setItems(yesNo);
        criminalBackgroundCmb.setItems(yesNo);
        petCmb.setItems(yesNo);
    }


    public void setDate(){

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        dateLabel.setText(formattedDate);

    }


    public void generateNewRequestId(){

        String requestId = null;
        try {
            requestId = addNewRentRequestModel.generateNewRequestId();
            requestIdLabel.setText(requestId);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    public void setValuesToHouseTypeCmb(){

        try {
            ObservableList<String> houseTypes = addNewRentRequestModel.getAllHouseTypes();
            houseTypeCmb.setItems(houseTypes);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void setValuesToLeaseTurnCmb(){

        ObservableList<String> leaseTurn = FXCollections.observableArrayList("Select","6 Months","12 Months","18 Months","2 Year");
        leaseTurnCmb.setItems(leaseTurn);

    }

    public void clean(){

        customerIdTxt.setText("");
        noOfMembersTxt.setText("");
        monthlyIncomeTxt.setText("");
        annualIncomeTxt.setText("");
        bankDetailsTxt.setText("");
        reasonToMoveTxt.setText("");
        estimatedBudgetTxt.setText("");
        previousLandlordNoTxt.setText("");
        leaseTurnCmb.getSelectionModel().selectFirst();
        houseTypeCmb.getSelectionModel().selectFirst();
        smokingCmb.getSelectionModel().selectFirst();
        criminalBackgroundCmb.getSelectionModel().selectFirst();
        petCmb.getSelectionModel().selectFirst();
        customerIdLabel.setText("");
        noOfFamilyMembersLabel.setText("");
        monthlyIncomeLabel.setText("");
        annualIncomeLabel.setText("");
        bankDetailsLabel.setText("");
        reasonForMoveLabel.setText("");
        monthlybudgetLabel.setText("");
        landlordNoLabel.setText("");

    }
}
