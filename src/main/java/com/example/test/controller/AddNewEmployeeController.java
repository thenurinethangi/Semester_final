package com.example.test.controller;

import com.example.test.dto.EmployeeDto;
import com.example.test.dto.tm.EmployeeTm;
import com.example.test.model.AddNewEmployeeModel;
import com.example.test.validation.UserInputValidation;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddNewEmployeeController implements Initializable {

    @FXML
    private Label employeeIdLabel;

    @FXML
    private TextField nametxt;

    @FXML
    private TextField addresstxt;

    @FXML
    private TextField basicSalarytxt;

    @FXML
    private TextField phoneNotxt;

    @FXML
    private TextField positiontxt;

    @FXML
    private TextField allowancestxt;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private Button addbtn;

    @FXML
    private Button clearbtn;

    @FXML
    private Button canclebtn;

    @FXML
    private Button editbtn;


    private EmployeeTm selectedItem;

    private final AddNewEmployeeModel addNewEmployeeModel = new AddNewEmployeeModel();


    @FXML
    void addOnAction(ActionEvent event) {

        String name = nametxt.getText();
        String address = addresstxt.getText();
        String phoneNo = phoneNotxt.getText();
        String basicSalary = basicSalarytxt.getText();
        String allowances = allowancestxt.getText();
        String position = positiontxt.getText();
        String date = String.valueOf(dobPicker.getValue());

        if(name.isEmpty() || address.isEmpty() || phoneNo.isEmpty() || basicSalary.isEmpty() || allowances.isEmpty() || position.isEmpty() || date.isEmpty()){

            getNotification("No field can be empty");
            return;

        }

        boolean result = validateUserInput(name,address,phoneNo,basicSalary,allowances,position,date);

        if(result){

            EmployeeDto employeeDto = new EmployeeDto(employeeIdLabel.getText(),name,address,phoneNo,Double.parseDouble(basicSalary),
                    Double.parseDouble(allowances),date,position);

            try {
                String response = addNewEmployeeModel.addNewEmployee(employeeDto);
                getNotification(response);

                if(response.equals("successfully add new employee")){
                    generateNextEmployeeId();
                }

            }
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while adding a new employee: " + e.getMessage());
                getNotification("An error occurred while adding a new employee, Please try again or contact support.");
            }

            clean();

        }

        else{
            getNotification("Your input field data not acceptable,please enter correct data!");
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


    public void clean(){

        nametxt.setText("");
        addresstxt.setText("");
        phoneNotxt.setText("");
        basicSalarytxt.setText("");
        allowancestxt.setText("");
        positiontxt.setText("");
        dobPicker.setValue(LocalDate.now());

    }

    @FXML
    void dobPickerOnAction(ActionEvent event) {

    }

    @FXML
    void editOnAction(ActionEvent event) {

        String name = nametxt.getText();
        String address = addresstxt.getText();
        String phoneNo = phoneNotxt.getText();
        String basicSalary = basicSalarytxt.getText();
        String allowances = allowancestxt.getText();
        String position = positiontxt.getText();
        String date = String.valueOf(dobPicker.getValue());

        if(name.isEmpty() || address.isEmpty() || phoneNo.isEmpty() || basicSalary.isEmpty() || allowances.isEmpty() || position.isEmpty() || date.isEmpty()){

            getNotification("No field can be empty");
        }

        else{
            boolean result = validateUserInput(name,address,phoneNo,basicSalary,allowances,position,date);

            if(!result){
                getNotification("Your input field data not acceptable,please enter correct data!");
                return;
            }

            String id = employeeIdLabel.getText();

            EmployeeDto employeeDto = new EmployeeDto(id,name,address,phoneNo,Double.parseDouble(basicSalary),Double.parseDouble(allowances),date,position);

            try {
                String response = addNewEmployeeModel.updateEmployee(employeeDto);
                getNotification(response);
            }
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while updating a employee: " + e.getMessage());
                getNotification("An error occurred while updating employee id: "+id+" Please try again or contact support.");
            }

            clean();

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNextEmployeeId();
        editbtn.setDisable(true);

    }


    public void generateNextEmployeeId(){

        try {
            String id = addNewEmployeeModel.generateNextEmployeeId();
            employeeIdLabel.setText(id);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while generating a new employee id: " + e.getMessage());
            getNotification("An error occurred while generating a new employee id. Please try again or contact support.");
        }

    }


    public void getNotification(String text){

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text(text);
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();

    }

    public void setSelectedItemData(EmployeeTm em){

        selectedItem = em;
        employeeIdLabel.setText(selectedItem.getEmployeeId());
        nametxt.setText(selectedItem.getName());
        addresstxt.setText(selectedItem.getAddress());
        phoneNotxt.setText(selectedItem.getPhoneNo());
        basicSalarytxt.setText(String.valueOf(selectedItem.getBasicSalary()));
        allowancestxt.setText(String.valueOf(selectedItem.getAllowances()));
        positiontxt.setText(selectedItem.getPosition());
        dobPicker.setValue(LocalDate.parse(selectedItem.getDob()));
        addbtn.setDisable(true);
        editbtn.setDisable(false);

    }


    public boolean validateUserInput(String name, String address, String phoneNo, String basicSalary, String allowances, String position, String date){

        boolean b1 = UserInputValidation.checkNameValidation(name);
        if(!b1){
            nametxt.setStyle(nametxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336); -fx-border-width: 1 1 1 1;");
        }
        else{
            nametxt.setStyle(nametxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");

            nametxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    nametxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 1 1 1 1;");
                } else {

                    nametxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");
                }
            });
        }

        boolean b2 = UserInputValidation.checkTextValidation(address);
        if(!b2){
            addresstxt.setStyle(addresstxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 1 1 1 1;");
        }
        else{

            addresstxt.setStyle(addresstxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");

            addresstxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    addresstxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 1 1 1 1;");
                } else {

                    addresstxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");
                }
            });
        }

        boolean b3 = UserInputValidation.checkPhoneNoValidation(phoneNo);
        if(!b3){
            phoneNotxt.setStyle(phoneNotxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 1 1 1 1;");
        }
        else {
            phoneNotxt.setStyle(phoneNotxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");

            phoneNotxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    phoneNotxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 1 1 1 1;");
                } else {

                    phoneNotxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");
                }
            });

        }

        boolean b4 = UserInputValidation.checkDecimalValidation(basicSalary);
        if(!b4){
            basicSalarytxt.setStyle(basicSalarytxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 1 1 1 1;");
        }
        else {
            basicSalarytxt.setStyle(basicSalarytxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");

            basicSalarytxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    basicSalarytxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 1 1 1 1;");
                } else {

                    basicSalarytxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");
                }
            });

        }

        boolean b5 = UserInputValidation.checkDecimalValidation(allowances);
        if(!b5){
            allowancestxt.setStyle(allowancestxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 1 1 1 1;");
        }
        else {
            allowancestxt.setStyle(allowancestxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");

            allowancestxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    allowancestxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 1 1 1 1;");
                } else {

                    allowancestxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");
                }
            });

        }

        boolean b6 = UserInputValidation.checkTextValidation(position);
        if(!b6){
            positiontxt.setStyle(positiontxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 1 1 1 1;");
        }
        else {
            positiontxt.setStyle(positiontxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");

            positiontxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    positiontxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 1 1 1 1;");
                } else {

                    positiontxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 1 1 1 1;");
                }
            });

        }

        boolean b7 = UserInputValidation.checkDateValidation(date);

        if(b1 && b2 && b3 && b4 && b5 && b6 && b7){
            return true;
        }
        return false;
    }
}







