package com.example.test.controller;

import com.example.test.dto.CustomerDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.TenantTm;
import com.example.test.model.TenantModel;
import com.example.test.validation.UserInputValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.util.Stack;

public class TenantEditController {

    @FXML
    private Label tenantIdLabel;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField phoneNoTxt;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField membersCountTxt;

    @FXML
    private Label nameAlert;

    @FXML
    private Label phoneNoAlert;

    @FXML
    private Label membersCountAlert;

    @FXML
    private Label emailAlert;

    @FXML
    private Button editbtn;

    @FXML
    private Button clearbtn;

    @FXML
    private Button canclebtn;


    private TenantTm selectedTenant;
    private final TenantModel tenantModel = new TenantModel();


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

        String name = nameTxt.getText();
        String phoneNo = phoneNoTxt.getText();
        String membersCount = membersCountTxt.getText();
        String email = emailTxt.getText();

        boolean isEmpty = checkAllFieldNotEmpty(name,phoneNo,membersCount,email);

        if(!isEmpty){
            return;
        }

        boolean result = getAlertDependOnUserInput(name,phoneNo,membersCount,email);
        if(!result) {
            return;
        }

        else{
            nameAlert.setText("");
            phoneNoAlert.setText("");
            membersCountAlert.setText("");
            emailAlert.setText("");

            String id = tenantIdLabel.getText();

            TenantDto tenantDto = new TenantDto();
            tenantDto.setTenantId(id);
            tenantDto.setName(name);
            tenantDto.setPhoneNo(phoneNo);
            tenantDto.setMembersCount(Integer.parseInt(membersCount));
            tenantDto.setEmail(email);


            try {
                String response = tenantModel.updateTenant(tenantDto);

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
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
                alert.showAndWait();
                return;

            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"class not found");
                alert.showAndWait();
                return;
            }

            clean();

        }

    }


    public boolean getAlertDependOnUserInput(String name,String phoneNo,String membersCount, String email){

        boolean b1 = UserInputValidation.checkNameValidation(name);
        boolean b2 = UserInputValidation.checkPhoneNoValidation(phoneNo);
        boolean b3 = UserInputValidation.checkNumberLessThanTenValidation(membersCount);
        boolean b4 = UserInputValidation.checkEmailValidation(email);

        if(!b1 || !b2 || !b3 || !b4){
            if(!b1){
                nameAlert.setText("The name format you provided is incorrect, please provide the correct name");
            }
            else{
                nameAlert.setText("");
            }
            if(!b2){
                phoneNoAlert.setText("The Phone Number format you provided is incorrect, please provide the correct Phone number");
            }
            else{
                phoneNoAlert.setText("");
            }
            if(!b3){
                membersCountAlert.setText("The Resident Count you provided is illegal, please provide the correct Resident Count");
            }
            else{
                membersCountAlert.setText("");
            }
            if(!b4){
                emailAlert.setText("The email format you provided is incorrect, please provide the correct email");
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



    public boolean checkAllFieldNotEmpty(String name,String phoneNo,String membersCount, String email){

        if(name.isEmpty() || phoneNo.isEmpty() || membersCount.isEmpty() || email.isEmpty()){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("No field can be empty");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

            return false;
        }

        return true;
    }

    public void setSelectedTenantDetails(TenantTm tenantTm) {

        selectedTenant = tenantTm;

        tenantIdLabel.setText(selectedTenant.getTenantId());
        nameTxt.setText(selectedTenant.getName());
        phoneNoTxt.setText(selectedTenant.getPhoneNo());
        membersCountTxt.setText(String.valueOf(selectedTenant.getMembersCount()));

        try {
            TenantDto tenantDto = tenantModel.getMoreTenantDetails(selectedTenant.getTenantId());
            emailTxt.setText(tenantDto.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void clean(){

        nameTxt.setText("");
        phoneNoTxt.setText("");
        membersCountTxt.setText("");
        emailTxt.setText("");
        nameAlert.setText("");
        phoneNoAlert.setText("");
        membersCountAlert.setText("");
        emailAlert.setText("");
    }

}
