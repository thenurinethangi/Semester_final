package com.example.test.controller;

import com.example.test.dto.FloorDto;
import com.example.test.dto.tm.FloorTm;
import com.example.test.model.FloorModel;
import com.example.test.validation.UserInputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class FloorController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<FloorTm> table;

    @FXML
    private TableColumn<FloorTm, Integer> floorNo;

    @FXML
    private TableColumn<FloorTm, Integer> noOfHouses;

    @FXML
    private Button deletebtn;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private ComboBox<String> floorNoCmb;

    @FXML
    private ComboBox<String> noOfHousesCmb;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private ComboBox<String> sortCmb;

    @FXML
    private TextField floorNotxt;

    @FXML
    private TextField noOfHousestxt;

    @FXML
    private Button clearbtn;

    @FXML
    private Button addbtn;

    @FXML
    private Label floorLable;

    private FloorModel floorModel;
    private ObservableList<FloorTm> tableData;
    private ObservableList<String> floorNumbers;
    private ObservableList<String> houseAmountPerFloor;
    private ObservableList<String> sortType;
    private ObservableList<Integer> rows;
    private String floor;
    private String houseAmount;


    public FloorController(){

        try {
            floorModel = new FloorModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void addOnAction(ActionEvent event) {

        String floor = floorNotxt.getText();
        String unitAmount = noOfHousestxt.getText();

        if(floor.isEmpty() || unitAmount.isEmpty()){

            notification("You should enter all the details of the new floor to add to the system");
        }
        else {
            boolean bool1 = UserInputValidation.checkFloorNoValidation(floor);
            boolean bool2 = UserInputValidation.checkOnOfHousesValidation(unitAmount);

            if(!bool1 || !bool2){
                if(!bool1 && bool2){
                    notification("Floor No is invalid, Floor numbers at Grand View Residences must be less than 100");
                }
                else if(!bool2 && bool1){
                    notification("The number of houses is invalid; Grand View Residences Floor may have fewer than 10 houses");
                }
                else if(!bool1 && !bool2){
                    notification("Invalid Floor Number and House Amount. Please enter valid values");
                }
                clean();
            }
            else{

                FloorDto floorDto = new FloorDto(Integer.parseInt(floor),Integer.parseInt(unitAmount));

                try {
                    String result = floorModel.saveNewFloor(floorDto);
                    notification(result);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error while saving new floor: " + e.getMessage());
                    notification("An error occurred while saving the new floor. Please try again or contact support.");
                }
                clean();
            }

        }

    }

    @FXML
    void clearOnAction(ActionEvent event) {

        floorNotxt.setText("");
        noOfHousestxt.setText("");
    }


    @FXML
    void deleteOnAction(ActionEvent event) {

        FloorTm floorData = table.getSelectionModel().getSelectedItem();

        if(floorData==null){
            return;
        }

        try {
            boolean isUsed = floorModel.checkThisFloorIsUsed(floorData);

            if(isUsed){
                notification("Unable to delete the selected floor number "+floorData.getFloorNo()+", as it is currently in use");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while deleting the floor: " + e.getMessage());
            notification("An error occurred while deleting the floor. Please try again or contact support.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while deleting the floor: " + e.getMessage());
            notification("An error occurred while deleting the floor. Please try again or contact support.");
        }

            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete the floor?");
            alert.getButtonTypes().setAll(yesButton, cancelButton);
            Optional<ButtonType> options = alert.showAndWait();

            if(options.isPresent() && options.get()==yesButton){

                try {
                    String response = floorModel.deleteFloor(floorData);
                    notification(response);
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.err.println("Error while deleting the floor: " + e.getMessage());
                    notification("An error occurred while deleting the floor. Please try again or contact support.");
                }
            }
            else{
                table.getSelectionModel().clearSelection();
            }

            clean();
            tableLoad();
    }


    @FXML
    void editOnAction(ActionEvent event) {

        FloorTm floorTm = table.getSelectionModel().getSelectedItem();

        if(floorTm==null){
            return;
        }

        else{
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/FloorEdit.fxml"));
                Parent root = fxmlLoader.load();

                FloorEditController floorEditController = fxmlLoader.getController();
                floorEditController.setFloorNo(floorTm);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while loading the floor update page: " + e.getMessage());
                notification("An error while loading the floor update page. Please try again or contact support.");
            }

        }


    }

    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();

    }

    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<FloorTm> filteredList = FXCollections.observableArrayList();

        boolean isFloorSelected = (floor != null && !floor.equals("Select"));
        boolean isHouseAmountSelected = (houseAmount != null && !houseAmount.equals("Select"));

        for (FloorTm floorTm : tableData) {
            boolean matchesFloor = !isFloorSelected || floor.equals(String.valueOf(floorTm.getFloorNo()));
            boolean matchesHouseAmount = !isHouseAmountSelected || houseAmount.equals(String.valueOf(floorTm.getNoOfHouses()));

            if (matchesFloor && matchesHouseAmount) {
                filteredList.add(floorTm);
            }
        }

        if (filteredList.isEmpty()) {
            notification("No results found for the selected criteria.");
            table.setItems(tableData);
            clean();
        } else {
            table.setItems(filteredList);
        }

        setNull();

    }



    @FXML
    void floorNoCmbOnAction(ActionEvent event) {

        floor = floorNoCmb.getSelectionModel().getSelectedItem();

    }


    @FXML
    void noOfHousesCmbOnAction(ActionEvent event) {

        houseAmount = noOfHousesCmb.getSelectionModel().getSelectedItem();
    }


    @FXML
    public void tableRowCmbOnAction(ActionEvent event) {

        Integer rows = tableRowsCmb.getSelectionModel().getSelectedItem();
        System.out.println(rows);

        if(rows==null) {
         return;
        }

        ObservableList<FloorTm> floorTms = FXCollections.observableArrayList();
        int count = 0;

        for(FloorTm x : tableData){
            if(rows==count){
               break;
            }
            count++;
            floorTms.add(x);
        }

        table.setItems(floorTms);
    }


    @FXML
    public void sortCmbOnAction(ActionEvent event) {

        String text = sortCmb.getSelectionModel().getSelectedItem();

        if(text==null){
            return;
        }

        if(text.equals("Retrieve by floor number (ascending)")){

            ObservableList<FloorTm> floorTmsAr = tableData;

            for(int j = 0; j < floorTmsAr.size(); j++) {
                for (int i = 0; i < floorTmsAr.size()-1; i++) {
                    if (floorTmsAr.get(i).getFloorNo() > floorTmsAr.get(i + 1).getFloorNo()) {
                        FloorTm temp = floorTmsAr.get(i);
                        floorTmsAr.set(i, floorTmsAr.get(i + 1));
                        floorTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(floorTmsAr);
        }
        else if(text.equals("Retrieve by floor number (descending)")){

            ObservableList<FloorTm> floorTmsAr = tableData;

            for(int j = 0; j < floorTmsAr.size(); j++) {
                for (int i = 0; i < floorTmsAr.size()-1; i++) {
                    if (floorTmsAr.get(i).getFloorNo() < floorTmsAr.get(i + 1).getFloorNo()) {
                        FloorTm temp = floorTmsAr.get(i);
                        floorTmsAr.set(i, floorTmsAr.get(i + 1));
                        floorTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(floorTmsAr);
        }
        else if(text.equals("Retrieve by house amount (ascending)")){

            ObservableList<FloorTm> floorTmsAr = tableData;

            for(int j = 0; j < floorTmsAr.size(); j++) {
                for (int i = 0; i < floorTmsAr.size()-1; i++) {
                    if (floorTmsAr.get(i).getNoOfHouses() > floorTmsAr.get(i + 1).getNoOfHouses()) {
                        FloorTm temp = floorTmsAr.get(i);
                        floorTmsAr.set(i, floorTmsAr.get(i + 1));
                        floorTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(floorTmsAr);
        }
        else if(text.equals("Retrieve by house amount (descending)")){

            ObservableList<FloorTm> floorTmsAr = tableData;

            for(int j = 0; j < floorTmsAr.size(); j++) {
                for (int i = 0; i < floorTmsAr.size()-1; i++) {
                    if (floorTmsAr.get(i).getNoOfHouses() < floorTmsAr.get(i + 1).getNoOfHouses()) {
                        FloorTm temp = floorTmsAr.get(i);
                        floorTmsAr.set(i, floorTmsAr.get(i + 1));
                        floorTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(floorTmsAr);

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        floorNo.setCellValueFactory(new PropertyValueFactory<>("floorNo"));
        noOfHouses.setCellValueFactory(new PropertyValueFactory<>("noOfHouses"));

        tableLoad();
        setItemsToNoOfHousesCmb();
        setItemsToSortTypeCmb();
        setItemsToRowsCmb();
        setFloorNoCmbValues();

    }

    public void setItemsToRowsCmb() {

        rows = FXCollections.observableArrayList();
        int count = 0;
        for(FloorTm x : tableData){
            count++;
            rows.add(count);
        }
        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }


    public void setItemsToSortTypeCmb() {

        sortType = FXCollections.observableArrayList();
        sortType.addAll("Select","Retrieve by floor number (ascending)","Retrieve by floor number (descending)","Retrieve by house amount (ascending)","Retrieve by house amount (descending)");
        sortCmb.setItems(sortType);
        sortCmb.getSelectionModel().selectFirst();
    }


    public void setItemsToNoOfHousesCmb(){

        houseAmountPerFloor = FXCollections.observableArrayList();
        houseAmountPerFloor.addAll("Select","1","2","3","4","5","6","7","8","9");
        noOfHousesCmb.setItems(houseAmountPerFloor);
        noOfHousesCmb.getSelectionModel().selectFirst();

    }


    public void tableLoad(){

        try {
            tableData = floorModel.loadTableData();
            table.setItems(tableData);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while loading the table data: " + e.getMessage());
            notification("An error while loading the table data, Please try again or contact support.");
        }


    }


    public void setFloorNoCmbValues(){

        try {
            floorNumbers = floorModel.getFloorNumbers();
            floorNoCmb.setItems(floorNumbers);
            floorNoCmb.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while loading the floor numbers: " + e.getMessage());
            notification("An error while loading the floor numbers. Please try again or contact support.");
        }

    }


    public void clean(){

        tableLoad();
        floorNotxt.setText("");
        noOfHousestxt.setText("");
        table.getSelectionModel().clearSelection();
        //setNull();
        setItemsToNoOfHousesCmb();
        setItemsToSortTypeCmb();
        setItemsToRowsCmb();
        setFloorNoCmbValues();

    }


    public void setNull(){

        floor = null;
        houseAmount = null;
    }


    public void setFloorTextInvisible() {

       floorLable.setStyle("-fx-text-fill: #ffffff");
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



