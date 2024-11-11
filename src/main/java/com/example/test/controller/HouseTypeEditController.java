package com.example.test.controller;

import com.example.test.dto.HouseTypeDto;
import com.example.test.dto.tm.HouseTypeTm;
import com.example.test.model.HouseTypeEditModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
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


    public HouseTypeEditController(){
        try{
            houseTypeEditModel = new HouseTypeEditModel();
        }
        catch (Exception e){
            e.printStackTrace();
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
            Alert alert = new Alert(Alert.AlertType.WARNING,"Enter the new details to update the House Type");
            alert.show();
            return;
        }
        else if(hType.equals(housTypeDtails.getHouseType()) && desc.equals(housTypeDtails.getDescription())){
            return;
        }
        else if(!hType.equals(housTypeDtails.getHouseType()) && !desc.equals(housTypeDtails.getDescription())){

            try {
                String response = houseTypeEditModel.UpdateHouseTypeAll(houseTypeDto, housTypeDtails);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                alert.show();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(hType.equals(housTypeDtails.getHouseType()) && !desc.equals(housTypeDtails.getDescription())){

            try {
                String response = houseTypeEditModel.UpdateHouseTypeDescription(houseTypeDto, housTypeDtails);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                alert.show();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(!hType.equals(housTypeDtails.getHouseType()) && desc.equals(housTypeDtails.getDescription())){

            try {
                String response = houseTypeEditModel.UpdateHouseTypeHouseType(houseTypeDto, housTypeDtails);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                alert.show();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        clearOnAction(new ActionEvent());

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(housTypeDtails!=null) {
            houseTypetxt.setText(housTypeDtails.getHouseType());
            descriptiontxt.setText(housTypeDtails.getDescription());
        }
        else {
            return;
        }

    }

    public static void setData(HouseTypeDto houseTypeDto){

        housTypeDtails = houseTypeDto;

    }
}
