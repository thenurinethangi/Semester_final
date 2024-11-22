package com.example.test.controller;

import com.example.test.dto.tm.FloorTm;
import com.example.test.model.FloorEditModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FloorEditController implements Initializable {

    @FXML
    private Label floorNoLabel;

    @FXML
    private ComboBox<Integer> houseAmountcmb;

    @FXML
    private Button editbtn;

    @FXML
    private Button clearbtn;

    @FXML
    private Button canclebtn;

    private int floor;
    private ObservableList<Integer> ob;
    private Integer selectedHouseAmount;
    private FloorEditModel floorEditModel;

    public FloorEditController(){

        try {
            floorEditModel = new FloorEditModel();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while loading the floor update page: " + e.getMessage());
            notification("An error while loading the floor update page. Please try again or contact support.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading the floor update page: " + e.getMessage());
            notification("An error while loading the floor update page. Please try again or contact support.");
        }

    }

    @FXML
    void clearOnAction(ActionEvent event) {

        houseAmountcmb.getSelectionModel().clearSelection();
        selectedHouseAmount = null;
    }

    @FXML
    void editOnAction(ActionEvent event) {

        if(selectedHouseAmount==null){
            return;

        }
        else{
            FloorTm floorTm = new FloorTm(floor,selectedHouseAmount);

            try {
                String result = floorEditModel.updateFloor(floorTm);

                notification(result);
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error while updating a floor: " + e.getMessage());
                notification("An error occurred while updating a floor number :"+floor +" Please try again or contact support.");
            }

        }

    }

    @FXML
    void houseAmountcmbOnAction(ActionEvent event) {

        selectedHouseAmount = houseAmountcmb.getSelectionModel().getSelectedItem();
    }


    @FXML
    public void cancleOnAction(ActionEvent event) {

          Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
          stage.close();
    }


    public void setFloorNo(FloorTm floorTm){

        floor = floorTm.getFloorNo();
        floorNoLabel.setText(String.valueOf(floor));
        houseAmountcmb.setValue(floorTm.getNoOfHouses());

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


       ob = FXCollections.observableArrayList();
       ob.setAll(1,2,3,4,5,6,7,8,9);

       houseAmountcmb.setItems(ob);
       floorNoLabel.setText(String.valueOf(floor));


    }

    private void notification(String message) {

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
