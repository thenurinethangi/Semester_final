package com.example.test.controller;

import com.example.test.dto.SignUpDto;
import com.example.test.logindata.LoginDetails;
import com.example.test.model.UserProfileModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button editbtn;

    @FXML
    private AnchorPane bodyPane;

    private SignUpDto signUpDto;
    private UserProfileModel userProfileModel;

    public UserProfileController(){

        try {
            userProfileModel = new UserProfileModel();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    @FXML
    void editOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfileEdit.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().clear();
            bodyPane.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfileDetails.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            bodyPane.getChildren().clear();
            bodyPane.getChildren().add(anchorPane);
        }
        catch (Exception e){

            e.printStackTrace();
        }


        getUserDetails();

        if(signUpDto!=null){

            nameLabel.setText(signUpDto.getName());
            emailLabel.setText(signUpDto.getEmail());

        }
        else {
            nameLabel.setText("ABCD Nanayakkara");
            emailLabel.setText("abcd@gmail.com");
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
