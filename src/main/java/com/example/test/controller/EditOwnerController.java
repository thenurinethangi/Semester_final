package com.example.test.controller;

import com.example.test.dto.OwnerDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.OwnerTm;
import com.example.test.model.OwnerModel;
import com.example.test.validation.UserInputValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;

public class EditOwnerController {

    @FXML
    private Label ownerIdLabel;

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


    private OwnerTm owner;
    private final OwnerModel ownerModel = new OwnerModel();


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

            String id = ownerIdLabel.getText();

            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(id);
            ownerDto.setName(name);
            ownerDto.setPhoneNo(phoneNo);
            ownerDto.setMembersCount(Integer.parseInt(membersCount));
            ownerDto.setEmail(email);


            try {
                String response = ownerModel.updateOwner(ownerDto);
                notification(response);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while updating tenant id: "+owner.getOwnerId()+" details"+ e.getMessage());
                notification("An error occurred while updating tenant id: "+owner.getOwnerId()+" details, Please try again or contact support.");
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

            notification("No field can be empty");
            return false;
        }

        return true;
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


    public void setSelectedOwnerDetailsToUpdate(OwnerTm selectedOwner) {

        owner = selectedOwner;

        ownerIdLabel.setText(owner.getOwnerId());
        nameTxt.setText(owner.getName());
        phoneNoTxt.setText(owner.getPhoneNo());
        membersCountTxt.setText(String.valueOf(owner.getMembersCount()));

        try {
            String email = ownerModel.getOwnerEmailById(owner.getOwnerId());
            emailTxt.setText(email);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting owner id: "+owner.getOwnerId()+" details"+ e.getMessage());
            notification("An error occurred while setting owner id: "+owner.getOwnerId()+" details, Please try again or contact support.");
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
