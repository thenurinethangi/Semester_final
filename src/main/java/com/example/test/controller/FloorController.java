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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
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
    private ComboBox<Integer> floorNoCmb;

    @FXML
    private ComboBox<Integer> noOfHousesCmb;

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
    private ObservableList<Integer> floorNumbers;
    private ObservableList<Integer> houseAmountPerFloor;
    private ObservableList<String> sortType;
    private ObservableList<Integer> rows;
    private Integer floor;
    private Integer houseAmount;


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

            Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all the details of the new floor to add to the system");
            alert.showAndWait();
        }
        else {

            boolean bool1 = UserInputValidation.checkFloorNoValidation(floor);
            boolean bool2 = UserInputValidation.checkOnOfHousesValidation(unitAmount);

            if( bool1==false || bool2==false){
                if(bool1==false && bool2==true){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Floor NO is invalid, Grand View Residences Floor Numbers should be less than 100 as its size");
                    alert.showAndWait();
                }
                else if(bool2==false && bool1==true){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"No Of Houses are invalid, Grand View Residences Floor can have less than 10 no of houses");
                    alert.showAndWait();
                }
                else if(bool1==false && bool2==false){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Floor NO and On Of Houses are invalid, Please enter valued Floor No and House amount");
                    alert.showAndWait();
                }
                clean();

            }
            else{

                FloorDto floorDto = new FloorDto(Integer.parseInt(floor),Integer.parseInt(unitAmount));

                try {
                    String result = floorModel.saveNewFloor(floorDto);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, result);
                    alert.showAndWait();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                clean();
                tableLoad();
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

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select floor to delete!");
            alert.setContentText("You should first select floor to delete, tap on the table floor field to before tap on delete button");
            alert.showAndWait();
        }
        else{

            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete the floor?");
            Optional<ButtonType> options = a1.showAndWait();

            if(options.isPresent() && options.get()==ButtonType.OK){

                try {
                    String response = floorModel.deleteFloor(floorData);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                    alert.showAndWait();
                }
                catch (Exception e){

                    e.printStackTrace();
                }

            }

            clean();
            tableLoad();

        }
    }

    @FXML
    void editOnAction(ActionEvent event) {

        FloorTm floorTm = table.getSelectionModel().getSelectedItem();

        if(floorTm==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select floor to update!");
            alert.setContentText("You should first select floor to update, tap on the table floor field to before tap on edit button");
            alert.showAndWait();

        }
        else{
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/FloorEdit.fxml"));
                Parent root = fxmlLoader.load();

                FloorEditController floorEditController = fxmlLoader.getController();
                floorEditController.setFloorNo(floorTm.getFloorNo());

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();

    }

    @FXML
    void searchOnAction(ActionEvent event) {

        if(floor==null){
            if(houseAmount==null){
                return;
            }
            else{
                ObservableList<FloorTm> ob = FXCollections.observableArrayList();

                for(FloorTm x : tableData){
                    if(houseAmount==x.getNoOfHouses()){
                        ob.add(x);
                    }
                }
                if(ob.isEmpty()){

                    Alert alert = new Alert(Alert.AlertType.INFORMATION," There is no floor with "+houseAmount+" houses");
                    alert.showAndWait();
                }
                else{
                    table.setItems(ob);
                }

                setNull();

            }

        }
        else{
            if(houseAmount==null){

                ObservableList<FloorTm> ob = FXCollections.observableArrayList();

                for(FloorTm x : tableData){
                    if(floor==x.getFloorNo()){
                        ob.add(x);
                        break;
                    }
                }
                if(ob.isEmpty()){

                    Alert alert = new Alert(Alert.AlertType.INFORMATION," There is no floor with "+floor+" floor no");
                    alert.showAndWait();
                }
                else{
                    table.setItems(ob);
                }

                setNull();

            }
            else{

                ObservableList<FloorTm> ob = FXCollections.observableArrayList();

                for(FloorTm x : tableData){
                    if(floor==x.getFloorNo() && houseAmount==x.getNoOfHouses()){
                        ob.add(x);
                    }
                }
                if(ob.isEmpty()){

                    Alert alert = new Alert(Alert.AlertType.INFORMATION," There is no floor with floor no "+floor+"  and house amount "+ houseAmount);
                    alert.showAndWait();
                }
                else{
                    table.setItems(ob);
                }

                setNull();
            }
        }
//        floorNoCmb.getSelectionModel().selectFirst();
//        noOfHousesCmb.getSelectionModel().selectFirst();

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

        if(text.equals("get by floor no asc")){

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
        else if(text.equals("get by floor no desc")){

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
        else if(text.equals("get by house amount asc")){

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
        else if(text.equals("get by house amount desc")){

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

        setItemsToFloorNoCmb();

        setItemsToSortTypeCmb();

        setItemsToRowsCmb();

    }

    public void setItemsToRowsCmb() {

        rows = FXCollections.observableArrayList();
        int count = 0;
        for(FloorTm x : tableData){
            count++;
            rows.add(count);
        }
        tableRowsCmb.setItems(rows);

    }


    public void setItemsToSortTypeCmb() {

        sortType = FXCollections.observableArrayList();
        sortType.addAll("get by floor no asc","get by floor no desc","get by house amount asc","get by house amount desc");
        sortCmb.setItems(sortType);
    }


    public void setItemsToFloorNoCmb(){

        getFloorNumbers();
        floorNoCmb.setItems(floorNumbers);

    }

    public void setItemsToNoOfHousesCmb(){

        houseAmountPerFloor = FXCollections.observableArrayList();
        houseAmountPerFloor.addAll(1,2,3,4,5,6,7,8,9);
        noOfHousesCmb.setItems(houseAmountPerFloor);

    }


    public void tableLoad(){

        try {
            tableData = floorModel.loadTableData();
            if(tableData.isEmpty()){
                return;
            }
            else {

                table.setItems(tableData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public void getFloorNumbers(){

        try {
            floorNumbers = floorModel.getFloorNumbers();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void clean(){

        floorNotxt.setText("");
        noOfHousestxt.setText("");
        table.getSelectionModel().clearSelection();
        setNull();
        floorNoCmb.getSelectionModel().clearSelection();
        noOfHousesCmb.getSelectionModel().clearSelection();
        tableRowsCmb.getSelectionModel().clearSelection();
        sortCmb.getSelectionModel().clearSelection();
        //tableRowsCmb.getSelectionModel().selectLast();
//      floorNoCmb.getSelectionModel().selectFirst();
//      noOfHousesCmb.getSelectionModel().selectFirst();
        tableLoad();

    }


    public void setNull(){

        floor = null;
        houseAmount = null;
    }


    public void setFloorTextInvisible() {

       floorLable.setStyle("-fx-text-fill: #ffffff");
    }
}



