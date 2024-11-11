package com.example.test.controller;

import com.example.test.dto.SignUpDto;
import com.example.test.model.UserProfileModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileDetailsController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private Label name;

    @FXML
    private Label userName;

    @FXML
    private Label email;

    @FXML
    private Label password;

    @FXML
    private Button exitbtn;

    private SignUpDto signUpDto;

    private UserProfileModel userProfileModel;

    public  UserProfileDetailsController(){

        try {
            userProfileModel = new UserProfileModel();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }

    @FXML
    void exitOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getUserDetails();

        if(signUpDto!=null){

            userName.setText(signUpDto.getUserName());
            name.setText(signUpDto.getName());
            email.setText(signUpDto.getEmail());
            password.setText(signUpDto.getPassword());

        }
        else {
            return;
        }

    }

    public void getUserDetails(){

        try{

            signUpDto = userProfileModel.getUserProfileDetails();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
