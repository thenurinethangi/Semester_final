package com.example.test.controller;

import com.example.test.dto.EmployeeDto;
import com.example.test.dto.tm.EmployeeTm;
import com.example.test.model.AddNewEmployeeModel;
import com.example.test.validation.UserInputValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
        System.out.println(date);

        if(name.isEmpty() || address.isEmpty() || phoneNo.isEmpty() || basicSalary.isEmpty() || allowances.isEmpty() || position.isEmpty() || date.isEmpty()){

          getNotification("No field can be empty");
          return;

        }

        boolean b1 = UserInputValidation.checkNameValidation(name);
        if(!b1){
            nametxt.setStyle(nametxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336); -fx-border-width: 0 0 2 0;");
        }
        else{
            nametxt.setStyle(nametxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");

            nametxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    nametxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 0 0 2 0;");
                } else {

                    nametxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");
                }
            });
        }

        boolean b2 = UserInputValidation.checkTextValidation(address);
        if(!b2){
            addresstxt.setStyle(addresstxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 0 0 2 0;");
        }
        else{

            addresstxt.setStyle(addresstxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");

            addresstxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    addresstxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 0 0 2 0;");
                } else {

                    addresstxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");
                }
            });
        }

        boolean b3 = UserInputValidation.checkPhoneNoValidation(phoneNo);
        if(!b3){
            phoneNotxt.setStyle(phoneNotxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 0 0 2 0;");
        }
        else {
            phoneNotxt.setStyle(phoneNotxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");

            phoneNotxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    phoneNotxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 0 0 2 0;");
                } else {

                    phoneNotxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");
                }
            });

        }
        boolean b4 = UserInputValidation.checkDecimalValidation(basicSalary);
        if(!b4){
            basicSalarytxt.setStyle(basicSalarytxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 0 0 2 0;");
        }
        else {
            basicSalarytxt.setStyle(basicSalarytxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");

            basicSalarytxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    basicSalarytxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 0 0 2 0;");
                } else {

                    basicSalarytxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");
                }
            });

        }

        boolean b5 = UserInputValidation.checkDecimalValidation(allowances);
        if(!b5){
            allowancestxt.setStyle(allowancestxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 0 0 2 0;");
        }
        else {
            allowancestxt.setStyle(allowancestxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");

            allowancestxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    allowancestxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 0 0 2 0;");
                } else {

                    allowancestxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");
                }
            });

        }

        boolean b6 = UserInputValidation.checkTextValidation(position);
        if(!b6){
            positiontxt.setStyle(positiontxt.getStyle()+";-fx-border-color: linear-gradient(to right, #C62828, #F44336);-fx-border-width: 0 0 2 0;");
        }
        else {
            positiontxt.setStyle(positiontxt.getStyle()+";-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");

            positiontxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {

                    positiontxt.setStyle("-fx-border-color: linear-gradient(to right, #007AFF, #E1F5FE); -fx-border-width: 0 0 2 0;");
                } else {

                    positiontxt.setStyle("-fx-border-color: #57606f; -fx-border-width: 0 0 1 0;");
                }
            });

        }

        boolean b7 = UserInputValidation.checkDateValidation(date);

        if(b1 && b2 && b3 && b4 && b5 && b6 && b7){

            EmployeeDto employeeDto = new EmployeeDto(employeeIdLabel.getText(),name,address,phoneNo,Double.parseDouble(basicSalary),
                    Double.parseDouble(allowances),date,position);

            try {
                String response = addNewEmployeeModel.addNewEmployee(employeeDto);
                getNotification(response);
                if(response.equals("successfully add new employee")){
                    generateNextEmployeeId();
                }

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
                alert.showAndWait();

            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at class not found");
                alert.showAndWait();

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

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("No field can be empty");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();
        }
        else{

            String id = employeeIdLabel.getText();

            EmployeeDto employeeDto = new EmployeeDto(id,name,address,phoneNo,Double.parseDouble(basicSalary),Double.parseDouble(allowances),date,position);
            String response = null;
            try {
                response = addNewEmployeeModel.updateEmployee(employeeDto);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
                alert.showAndWait();
                return;

            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"class not found");
                alert.showAndWait();
                return;
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

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"class not found");
            alert.showAndWait();
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
}







