package com.example.test.controller;

import com.example.test.dto.HouseTypeDto;
import com.example.test.dto.tm.FloorTm;
import com.example.test.dto.tm.HouseTypeTm;
import com.example.test.model.HouseTypeModel;
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

public class HouseTypeController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private Button deletebtn;

    @FXML
    private Button clearbtn;

    @FXML
    private Button addbtn;

    @FXML
    private TableView<HouseTypeTm> table;

    @FXML
    private TableColumn<HouseTypeTm, String> houseTypeColumn;

    @FXML
    private TableColumn<HouseTypeTm, String> descriptionColumn;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private ComboBox<String> sortCmb;

    @FXML
    private ComboBox<String> houseTypeCmb;

    @FXML
    private Button refreshbtn;

    @FXML
    private Button searchbtn;

    @FXML
    private TextField houseTypetxt;

    @FXML
    private TextField descriptiontxt;

    @FXML
    private Label HouseTypeLabel;

    private HouseTypeModel houseTypeModel;
    private ObservableList<HouseTypeTm> tableData;
    private ObservableList<Integer> rows;
    private ObservableList<String> sort;
    private ObservableList<String> allHouseTypes;
    private String selectHouseType;


    public HouseTypeController(){

            try {
                houseTypeModel = new HouseTypeModel();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error while loading a house type page: " + e.getMessage());
                notification("An error occurred while loading a house type page. Please try again or contact support.");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while while loading a house type page: " + e.getMessage());
                notification("An error occurred while loading a house type page. Please try again or contact support.");

            }
    }


    @FXML
    void addOnAction(ActionEvent event) {

        String houseType = houseTypetxt.getText();
        String desc = descriptiontxt.getText();

        if(houseType.isEmpty() || desc.isEmpty()){

            notification("You should enter all new house type details to" +
                    " add new house type to the system");

        }
        else{
            boolean bool1 = UserInputValidation.checkTextValidation(houseType);
            boolean bool2 = UserInputValidation.checkTextValidation(desc);

            if(bool1 && bool2){

                HouseTypeDto houseTypeDto = new HouseTypeDto(houseType,desc);
                try {
                    String response = houseTypeModel.addNewHouseType(houseTypeDto);
                    notification(response);
                    clean();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error while saving new House Type: " + e.getMessage());
                    notification("An error occurred while saving the new House Type. Please try again or contact support.");
                }

            }
            else{
                if(!bool1 && bool2){
                    notification("The house type you entered is in an invalid format. Please enter a valid house type.");
                }
                else if(!bool2 && bool1){
                    notification("The description you entered is in an invalid format. Please enter a valid description.");
                }
                else if(!bool1 && !bool2){
                    notification("The house type and description you entered is in an invalid format. Please enter a valid details.");
                }
            }

        }

    }

    @FXML
    void clearOnAction(ActionEvent event) {

        houseTypetxt.setText("");
        descriptiontxt.setText("");
    }

    @FXML
    void deleteOnAction(ActionEvent event) {

        HouseTypeTm selectHouseType = table.getSelectionModel().getSelectedItem();

        if(selectHouseType==null){
            return;
        }

        try {
            boolean isUsing = houseTypeModel.isThisHouseTypeUsing(selectHouseType.getHouseType());

            if(isUsing){
                notification("House type: "+selectHouseType.getHouseType()+" is already in use and cannot be deleted.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this house type?");
        a1.getButtonTypes().setAll(yesButton, cancelButton);
        Optional<ButtonType> options = a1.showAndWait();

            if(options.isPresent() && options.get()==yesButton) {

                try {
                    String response = houseTypeModel.deleteHouseType(selectHouseType);
                    notification(response);
                    clean();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error while deleting the house type: " + e.getMessage());
                    notification("An error occurred while deleting the house type. Please try again or contact support.");
                }
            }
    }



    @FXML
    void editOnAction(ActionEvent event) {

        HouseTypeTm selectHouseType = table.getSelectionModel().getSelectedItem();

        if(selectHouseType==null){
            return;
        }
        else {
            try {
                HouseTypeDto houseTypeDto = new HouseTypeDto(selectHouseType.getHouseType(),selectHouseType.getDescription());
                HouseTypeEditController.setData(houseTypeDto);
                System.out.println(houseTypeDto.getHouseType());

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HouseTypeEdit.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while loading house type edit page: " + e.getMessage());
                notification("An error occurred while loading a house type edit page. Please try again or contact support.");
            }


        }
    }


    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();
    }



    @FXML
    void searchOnAction(ActionEvent event) {

        if(selectHouseType==null){
            return;
        }
        else{

            ObservableList<HouseTypeTm> selectHouseTypeTms = FXCollections.observableArrayList();
            for (HouseTypeTm x : tableData){
                if(x.getHouseType().equals(selectHouseType)){
                    selectHouseTypeTms.add(x);
                }
            }
            table.setItems(selectHouseTypeTms);
            selectHouseType = null;
        }
    }


    @FXML
    void houseTypeCmbOnAction(ActionEvent event) {

        selectHouseType = houseTypeCmb.getSelectionModel().getSelectedItem();

    }


    @FXML
    void sortCmbOnAction(ActionEvent event) {

        String text = sortCmb.getSelectionModel().getSelectedItem();

        ObservableList<HouseTypeTm> HouseTypeTmsAr = tableData;

        if(text==null){
            return;
        }

        if(text.equals("Retrieve by house type (ascending)")){

            for(int j = 0; j < HouseTypeTmsAr.size(); j++) {
                for (int i = 0; i < HouseTypeTmsAr.size()-1; i++) {
                    if (HouseTypeTmsAr.get(i).getHouseType().compareTo(HouseTypeTmsAr.get(i + 1).getHouseType())>0) {
                        HouseTypeTm temp = HouseTypeTmsAr.get(i);
                        HouseTypeTmsAr.set(i, HouseTypeTmsAr.get(i + 1));
                        HouseTypeTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(HouseTypeTmsAr);
        }
        else if(text.equals("Retrieve by house type (descending)")){

            for(int j = 0; j < HouseTypeTmsAr.size(); j++) {
                for (int i = 0; i < HouseTypeTmsAr.size()-1; i++) {
                    if (HouseTypeTmsAr.get(i).getHouseType().compareTo(HouseTypeTmsAr.get(i + 1).getHouseType())<0) {
                        HouseTypeTm temp = HouseTypeTmsAr.get(i);
                        HouseTypeTmsAr.set(i, HouseTypeTmsAr.get(i + 1));
                        HouseTypeTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(HouseTypeTmsAr);
        }


    }



    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer rows = tableRowsCmb.getSelectionModel().getSelectedItem();
        System.out.println(rows);

        if(rows==null) {
            return;
        }

        ObservableList<HouseTypeTm> houseTypeTms = FXCollections.observableArrayList();
        int count = 0;

        for(HouseTypeTm x : tableData){
            if(rows==count){
                break;
            }
            count++;
            houseTypeTms.add(x);
        }

        table.setItems(houseTypeTms);

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        houseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("houseType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadTableData();
        setItemsToRowsCmb();
        setItemsToSortCmb();
        setItemsToHouseTypeCmb();
    }


    public void loadTableData(){

        try {
            tableData = houseTypeModel.loadTableData();
            table.setItems(tableData);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while loading table data: " + e.getMessage());
            notification("An error occurred while loading table data. Please try again or contact support.");

        }

    }


    public void setItemsToRowsCmb() {

        rows = FXCollections.observableArrayList();
        int count = 0;
        for(HouseTypeTm x : tableData){
            count++;
            rows.add(count);
        }
        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }


    public void setItemsToSortCmb() {

        sort = FXCollections.observableArrayList();
        sort.addAll("Select","Retrieve by house type (ascending)","Retrieve by house type (descending)");
        sortCmb.setItems(sort);
        sortCmb.getSelectionModel().selectFirst();
    }


      public void setItemsToHouseTypeCmb(){

        getHouseTypes();
        houseTypeCmb.setItems(allHouseTypes);
        houseTypeCmb.getSelectionModel().selectFirst();

      }

      public void getHouseTypes(){

        allHouseTypes = FXCollections.observableArrayList();
        allHouseTypes.add("Select");

        for(HouseTypeTm x : tableData){
            allHouseTypes.add(x.getHouseType());
        }

      }


    public void clean(){

        loadTableData();
        setItemsToSortCmb();
        setItemsToHouseTypeCmb();
        setItemsToRowsCmb();
        houseTypetxt.clear();
        descriptiontxt.clear();
        table.getSelectionModel().clearSelection();

    }


    public void setHouseTypeTextInvisible() {

        HouseTypeLabel.setStyle("-fx-text-fill: #ffffff");
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


