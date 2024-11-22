package com.example.test.controller;

import com.example.test.dto.CustomerDto;
import com.example.test.dto.tm.CustomerTm;
import com.example.test.model.AddNewCustomerModel;
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

public class AddNewCustomerController implements Initializable {

    @FXML
    private Label customerIdLabel;

    @FXML
    private TextField nametxt;

    @FXML
    private TextField nictxt;

    @FXML
    private TextField phoneNotxt;

    @FXML
    private TextField addresstxt;

    @FXML
    private TextField jobTitletxt;

    @FXML
    private TextField emailTxt;

    @FXML
    private ComboBox<String> livingArrangementCmb;

    @FXML
    private Button addbtn;

    @FXML
    private Button clearbtn;

    @FXML
    private Button canclebtn;

    @FXML
    private Button editbtn;

    @FXML
    private Label nameAlert;

    @FXML
    private Label nicAlert;

    @FXML
    private Label addressAlert;

    @FXML
    private Label phoneNoAlert;

    @FXML
    private Label jobTitleAlert;

    @FXML
    private Label emailAlert;


    private CustomerTm selectedCustomerToEdit;
    private final AddNewCustomerModel addNewCustomerModel = new AddNewCustomerModel();


    @FXML
    void addOnAction(ActionEvent event) {

        String name = nametxt.getText();
        String nic = nictxt.getText();
        String address = addresstxt.getText();
        String phoneNo = phoneNotxt.getText();
        String jobTitle = jobTitletxt.getText();
        String livingArrangement = livingArrangementCmb.getSelectionModel().getSelectedItem();
        String email = emailTxt.getText();

        boolean isEmpty = checkAllFieldNotEmpty(name,nic,address,phoneNo,jobTitle,livingArrangement,email);

        if(!isEmpty){
           return;
        }

        boolean result = getAlertDependOnUserInput(name,nic,address,phoneNo,jobTitle,email);
        if(!result) {
            return;
        }

        else{

            nameAlert.setText("");
            nicAlert.setText("");
            addressAlert.setText("");
            phoneNoAlert.setText("");
            jobTitleAlert.setText("");
            emailAlert.setText("");

            String id = customerIdLabel.getText();

            CustomerDto customerDto = new CustomerDto(id,name,nic,address,phoneNo,jobTitle,livingArrangement,email);

            try {
               String  response = addNewCustomerModel.addCustomer(customerDto);
               notification(response);

               if(response.equals("Successfully add the new customer")){
                   generateNextCustomerId();
               }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while generating new customer id: " + e.getMessage());
                notification("An error occurred while generating new customer id. Please try again or contact support.");
            }

            clean();
        }

    }


    @FXML
    void cancleOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void clearOnAction(ActionEvent event) {

        clean();

    }

    @FXML
    void editOnAction(ActionEvent event) {

        String name = nametxt.getText();
        String nic = nictxt.getText();
        String address = addresstxt.getText();
        String phoneNo = phoneNotxt.getText();
        String jobTitle = jobTitletxt.getText();
        String livingArrangement = livingArrangementCmb.getSelectionModel().getSelectedItem();
        String email = emailTxt.getText();

        boolean isEmpty = checkAllFieldNotEmpty(name,nic,address,phoneNo,jobTitle,livingArrangement,email);

        if(!isEmpty){
            return;
        }

        boolean result = getAlertDependOnUserInput(name,nic,address,phoneNo,jobTitle,email);
        if(!result) {
            return;
        }

        else{
            nameAlert.setText("");
            nicAlert.setText("");
            addressAlert.setText("");
            phoneNoAlert.setText("");
            jobTitleAlert.setText("");

            String id = customerIdLabel.getText();

            CustomerDto customerDto = new CustomerDto(id,name,nic,address,phoneNo,jobTitle,livingArrangement,email);
            String response = null;
            try {
                response = addNewCustomerModel.updateCustomer(customerDto);
                notification(response);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while editing a customer: " + e.getMessage());
                notification("An error occurred while editing the customer id: "+id+", Please try again or contact support.");
            }

            //clean();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNextCustomerId();
        editbtn.setDisable(true);
        setValuesToLivingArrangementCmb();

    }


    public void setValuesToLivingArrangementCmb(){

        ObservableList<String> livingArrangements = FXCollections.observableArrayList("Select","Single Living Arrangement","Couple Living Arrangement","Family Living Arrangement");
        livingArrangementCmb.setItems(livingArrangements);
        livingArrangementCmb.getSelectionModel().selectFirst();

    }

    public void clean(){

        nametxt.setText("");
        nameAlert.setText("");
        nictxt.setText("");
        nicAlert.setText("");
        addresstxt.setText("");
        addressAlert.setText("");
        phoneNotxt.setText("");
        phoneNoAlert.setText("");
        jobTitletxt.setText("");
        jobTitleAlert.setText("");
        livingArrangementCmb.getSelectionModel().selectFirst();
        emailTxt.setText("");
        emailAlert.setText("");

    }


    public void generateNextCustomerId(){

        try {
            String id = addNewCustomerModel.generateNextCustomerId();
            customerIdLabel.setText(id);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while generating new customer id: " + e.getMessage());
            notification("An error occurred while generating new customer id. Please try again or contact support.");
        }

    }


    public void selectedCustomerData(CustomerTm customerTm){

        selectedCustomerToEdit = customerTm;
        customerIdLabel.setText(selectedCustomerToEdit.getCustomerId());
        nametxt.setText(selectedCustomerToEdit.getName());
        nictxt.setText(selectedCustomerToEdit.getNic());
        addresstxt.setText(selectedCustomerToEdit.getAddress());
        phoneNotxt.setText(selectedCustomerToEdit.getPhoneNo());
        jobTitletxt.setText(selectedCustomerToEdit.getJobTitle());
        livingArrangementCmb.setValue(selectedCustomerToEdit.getLivingArrangement());

        try {
           String  email = addNewCustomerModel.getEmailByCustomerId(selectedCustomerToEdit.getCustomerId());
            emailTxt.setText(email);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while finding the email of a customer: " + e.getMessage());
            notification("An error occurred while finding the email of a customer id: "+selectedCustomerToEdit.getCustomerId()+", Please try again or contact support.");
        }

        addbtn.setDisable(true);
        editbtn.setDisable(false);

    }


    public boolean getAlertDependOnUserInput(String name, String nic, String address, String phoneNo, String jobTitle,String email){

        boolean b1 = UserInputValidation.checkNameValidation(name);
        boolean b2 = UserInputValidation.checkNICValidation(nic);
        boolean b3 = UserInputValidation.checkAddressValidation(address);
        boolean b4 = UserInputValidation.checkPhoneNoValidation(phoneNo);
        boolean b5 = UserInputValidation.checkTextValidation(jobTitle);
        boolean b6 = UserInputValidation.checkEmailValidation(email);

        if(!b1 || !b2 || !b3 || !b4 || !b5 || !b6){
            if(!b1){
                nameAlert.setText("The name format you provided is incorrect, please provide the correct name");
            }
            else{
                nameAlert.setText("");
            }
            if(!b2){
                nicAlert.setText("The NIC format you provided is incorrect, please provide the correct NIC number");
            }
            else{
                nicAlert.setText("");
            }
            if(!b3){
                addressAlert.setText("The address format you provided is incorrect, please provide the correct address");
            }
            else{
                addressAlert.setText("");
            }
            if(!b4){
                phoneNoAlert.setText("The phone no format you provided is incorrect, please provide the correct phone no");
            }
            else{
                phoneNoAlert.setText("");
            }
            if(!b5){
                jobTitleAlert.setText("The job title format you provided is incorrect, please provide the correct job title");
            }
            else{
                jobTitleAlert.setText("");
            }
            if(!b6){
                emailAlert.setText("Email format you provided is incorrect, please provide the correct Email");
            }
            else{
                emailAlert.setText("");
            }
            return false;

        }
        else{
            return true;
        }
    }


    public boolean checkAllFieldNotEmpty(String name,String nic,String address,String phoneNo,String jobTitle,String livingArrangement,String email){

        if(name.isEmpty() || address.isEmpty() || phoneNo.isEmpty() || nic.isEmpty() || jobTitle.isEmpty() || livingArrangement.isEmpty() || email.isEmpty()){

            notification("No field can be empty");

            nameAlert.setText("");
            nicAlert.setText("");
            addressAlert.setText("");
            phoneNoAlert.setText("");
            jobTitleAlert.setText("");
            emailAlert.setText("");

            return false;
        }
        return true;
    }


    public void notification(String message){

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text(message);
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();
    }
}


