package com.example.test.controller;

import com.example.test.dto.HouseTypeDto;
import com.example.test.dto.tm.HouseTypeTm;
import com.example.test.model.HouseTypeEditModel;
import com.example.test.model.HouseTypeModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HouseTypeEditController implements Initializable {


    @FXML
    private TextField houseTypetxt;

    @FXML
    private TextField descriptiontxt;

    @FXML
    private Button clearbtn;

    @FXML
    private Button canclebtn;

    @FXML
    private Button editbtn;

    private static HouseTypeDto housTypeDtails;
    private HouseTypeEditModel houseTypeEditModel;
    private HouseTypeModel houseTypeModel;


    public HouseTypeEditController(){
        try{
            houseTypeEditModel = new HouseTypeEditModel();
            houseTypeModel = new HouseTypeModel();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while loading house type edit page: " + e.getMessage());
            notification("An error occurred while loading a house type edit page. Please try again or contact support.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading house type edit page: " + e.getMessage());
            notification("An error occurred while loading a house type edit page. Please try again or contact support.");
        }

    }


    @FXML
    void cancleOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();

    }


    @FXML
    void clearOnAction(ActionEvent event) {

        houseTypetxt.setText("");
        descriptiontxt.setText("");
    }


    @FXML
    void editOnAction(ActionEvent event) {

        String hType = houseTypetxt.getText();
        String desc = descriptiontxt.getText();

        HouseTypeDto houseTypeDto = new HouseTypeDto(hType,desc);

        if(hType.isEmpty() || desc.isEmpty()){
            notification("Enter the new details to update the House Type");
        }
        else if(hType.equals(housTypeDtails.getHouseType()) && desc.equals(housTypeDtails.getDescription())){
            return;
        }
        else if(!hType.equals(housTypeDtails.getHouseType()) && !desc.equals(housTypeDtails.getDescription())){

            try {
                String response = houseTypeEditModel.UpdateHouseTypeAll(houseTypeDto, housTypeDtails);
                notification(response);

            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error while editing the house type: " + e.getMessage());
                notification("An error occurred while editing the house type. Please try again or contact support.");
            }

        }
        else if(hType.equals(housTypeDtails.getHouseType()) && !desc.equals(housTypeDtails.getDescription())){

            try {
                String response = houseTypeEditModel.UpdateHouseTypeDescription(houseTypeDto, housTypeDtails);
                notification(response);

            }
            catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error while editing the house type: " + e.getMessage());
                notification("An error occurred while editing the house type. Please try again or contact support.");
            }

        }
        else if(!hType.equals(housTypeDtails.getHouseType()) && desc.equals(housTypeDtails.getDescription())){

            try {
                String response = houseTypeEditModel.UpdateHouseTypeHouseType(houseTypeDto, housTypeDtails);
                notification(response);

            }
            catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error while editing the house type: " + e.getMessage());
                notification("An error occurred while editing the house type. Please try again or contact support.");
            }

        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(housTypeDtails!=null) {
            houseTypetxt.setText(housTypeDtails.getHouseType());
            descriptiontxt.setText(housTypeDtails.getDescription());

            try {
                boolean isUsing = houseTypeModel.isThisHouseTypeUsing(housTypeDtails.getHouseType());

                houseTypetxt.setDisable(isUsing);

            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error while setting house type details: " + e.getMessage());
                notification("An error occurred while setting house type details. Please try again or contact support.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while setting house type details: " + e.getMessage());
                notification("An error occurred while setting house type details. Please try again or contact support.");
            }
        }
        else {
            return;
        }

    }

    public static void setData(HouseTypeDto houseTypeDto){

        housTypeDtails = houseTypeDto;

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
