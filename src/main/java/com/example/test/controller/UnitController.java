package com.example.test.controller;

import com.example.test.dto.tm.FloorTm;
import com.example.test.dto.tm.HouseTypeTm;
import com.example.test.dto.tm.UnitTm;
import com.example.test.model.HouseTypeModel;
import com.example.test.model.UnitModel;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.invoke.VarHandle;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UnitController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<UnitTm> table;

    @FXML
    private TableColumn<UnitTm, String> houseIdColumn;

    @FXML
    private TableColumn<UnitTm, Integer> bedroomColumn;

    @FXML
    private TableColumn<UnitTm, Integer> bathroomColumn;

    @FXML
    private TableColumn<UnitTm, String> rentOrBuyColumn;

    @FXML
    private TableColumn<UnitTm, String> totalValueColumn;

    @FXML
    private TableColumn<UnitTm, String> securityChargeColumn;

    @FXML
    private TableColumn<UnitTm, String> monthlyRentColumn;

    @FXML
    private TableColumn<UnitTm, String> statusColumn;

    @FXML
    private TableColumn<UnitTm, String> houseTypeColumn;

    @FXML
    private TableColumn<UnitTm, Integer> floorNoColumn;

    @FXML
    private Button addNewUnitbtn;

    @FXML
    private Button addNewHouseTypebtn;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private Button deletebtn;

    @FXML
    private Button addNewFloorbtn;

    @FXML
    private ComboBox<String> sortCmb;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private ComboBox<String> houseIdCmb;

    @FXML
    private ComboBox<String> houseTypeCmb;

    @FXML
    private ComboBox<String> statusCmb;

    @FXML
    private ComboBox<String> rentOrBuyCmb;

    private ObservableList<UnitTm> tableData;
    private UnitModel unitModel;
    private HouseTypeModel houseTypeModel;
    private String houseId;
    private String status;
    private String houseType;
    private String rentOrSell;


    public UnitController(){

        try {
            houseTypeModel = new HouseTypeModel();
            unitModel = new UnitModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void addNewFloorOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Floor.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void addNewHouseTypeOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HouseType.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void addNewUnitOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewUnit.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    public void getSelectedRow(MouseEvent mouseEvent) {

        UnitTm selectItem = table.getSelectionModel().getSelectedItem();

        if(selectItem==null){
           return;
        }

        if(selectItem.getStatus().equals("unavailable")) {

            deletebtn.setDisable(true);
            editbtn.setDisable(true);
        }
        else{
            deletebtn.setDisable(false);
            editbtn.setDisable(false);
        }

    }


    @FXML
    void deleteOnAction(ActionEvent event) {

        UnitTm selectedRow = table.getSelectionModel().getSelectedItem();

        if(selectedRow==null){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select unit to delete!");
            alert.setContentText("You should first select unit to delete, tap on the table row you want to delete before tap on delete button");
            alert.showAndWait();
        }
        else {
            if (selectedRow.getStatus().equals("available")) {

                Alert a1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the floor?");
                Optional<ButtonType> options = a1.showAndWait();

                if (options.isPresent() && options.get() == ButtonType.OK) {

                    try {
                        String response = unitModel.deleteUnit(selectedRow);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                        alert.showAndWait();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }

                table.getSelectionModel().clearSelection();
                loadTable();
            }
        }

    }


    @FXML
    void editOnAction(ActionEvent event) {

        UnitTm selectedRow = table.getSelectionModel().getSelectedItem();

        if(selectedRow==null){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select unit to delete!");
            alert.setContentText("You should first select unit to edit, tap on the table row you want to update before tap on update button");
            alert.showAndWait();
        }
        else{

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewUnit.fxml"));
                Parent root = fxmlLoader.load();

                AddNewUnitController addNewUnitController = fxmlLoader.getController();
                addNewUnitController.setEditRowDetails(selectedRow);

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

        loadTable();

    }


    @FXML
    void houseIdCmbOnAction(ActionEvent event) {

        houseId = houseIdCmb.getSelectionModel().getSelectedItem();
        System.out.println(houseId);
    }

    @FXML
    void houseTypeCmbOnAction(ActionEvent event) {

        houseType = houseTypeCmb.getSelectionModel().getSelectedItem();
        System.out.println(houseType);
    }

    @FXML
    void rentOrBuyCmbOnAction(ActionEvent event) {

        rentOrSell = rentOrBuyCmb.getSelectionModel().getSelectedItem();
        System.out.println(rentOrSell);
    }

    @FXML
    void statusCmbOnAction(ActionEvent event) {

        status = statusCmb.getSelectionModel().getSelectedItem();
        System.out.println(status);
    }


    @FXML
    void searchOnAction(ActionEvent event) {

    }


    @FXML
    void sortCmbOnAction(ActionEvent event) {

        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<UnitTm> unitTmsAr = tableData;

        if(sortType.equals("bedroom asc")){

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (unitTmsAr.get(i).getBedroom() > unitTmsAr.get(i + 1).getBedroom()) {
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(unitTmsAr);
        }

        else if(sortType.equals("bedroom desc")){

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (unitTmsAr.get(i).getBedroom() < unitTmsAr.get(i + 1).getBedroom()) {
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(unitTmsAr);
        }

        else if(sortType.equals("bathroom asc")){

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (unitTmsAr.get(i).getBathroom() > unitTmsAr.get(i + 1).getBathroom()) {
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(unitTmsAr);
        }

        else if(sortType.equals("bathroom desc")){

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (unitTmsAr.get(i).getBathroom() < unitTmsAr.get(i + 1).getBathroom()) {
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            table.setItems(unitTmsAr);
        }

        else if(sortType.equals("total values asc")){

            for(UnitTm x : unitTmsAr){
                if(x.getTotalValue().equals("N/A")){

                    x.setTotalValue("2147483647");
                }
            }

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (Double.parseDouble(unitTmsAr.get(i).getTotalValue()) > Double.parseDouble(unitTmsAr.get(i + 1).getTotalValue())) {
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            for(UnitTm x : unitTmsAr){
                if(x.getTotalValue().equals("2147483647")){

                    x.setTotalValue("N/A");
                }
            }

            table.setItems(unitTmsAr);
        }


        else if(sortType.equals("total values desc")){

            for(UnitTm x : unitTmsAr){
                if(x.getTotalValue().equals("N/A")){

                    x.setTotalValue("0");
                }
            }

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (Double.parseDouble(unitTmsAr.get(i).getTotalValue()) < Double.parseDouble(unitTmsAr.get(i + 1).getTotalValue())) {
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            for(UnitTm x : unitTmsAr){
                if(x.getTotalValue().equals("0")){

                    x.setTotalValue("N/A");
                }
            }

            table.setItems(unitTmsAr);
        }

        else if(sortType.equals("monthly rent asc")){

            for(UnitTm x : unitTmsAr){
                if(x.getMonthlyRent().equals("N/A")){

                    x.setMonthlyRent("2147483647");
                }
            }

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (Double.parseDouble(unitTmsAr.get(i).getMonthlyRent()) > Double.parseDouble(unitTmsAr.get(i + 1).getMonthlyRent())) {
                        System.out.println(unitTmsAr.get(i).getMonthlyRent());
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            for(UnitTm x : unitTmsAr){
                if(x.getMonthlyRent().equals("2147483647")){

                    x.setMonthlyRent("N/A");
                }
            }

            table.setItems(unitTmsAr);
        }


        else if(sortType.equals("monthly rent desc")){

            for(UnitTm x : unitTmsAr){
                if(x.getMonthlyRent().equals("N/A")){

                    x.setMonthlyRent("0");
                }
            }

            for(int j = 0; j < unitTmsAr.size(); j++) {
                for (int i = 0; i < unitTmsAr.size()-1; i++) {
                    if (Double.parseDouble(unitTmsAr.get(i).getMonthlyRent()) < Double.parseDouble(unitTmsAr.get(i + 1).getMonthlyRent())) {
                        UnitTm temp = unitTmsAr.get(i);
                        unitTmsAr.set(i, unitTmsAr.get(i + 1));
                        unitTmsAr.set((i + 1), temp);

                    }
                }
            }
            for(UnitTm x : unitTmsAr){
                if(x.getMonthlyRent().equals("0")){

                    x.setMonthlyRent("N/A");
                }
            }

            table.setItems(unitTmsAr);
        }

    }

    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        int rows = tableRowsCmb.getSelectionModel().getSelectedItem();

        ObservableList<UnitTm> requireRows = FXCollections.observableArrayList();
        int count = 1;

        for(UnitTm x : tableData){
            if(count>rows){
                break;
            }
            requireRows.add(x);
            count++;
        }
        table.setItems(requireRows);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumn();
        loadTable();
        setItemsToStatusCmb();
        setItemsToRentOrBuyCmb();
        setItemsToHouseTypeCmb();
        setItemsToHouseIdCmb();
        setItemsToTableRowsCmb();
        setItemsToSortCmb();

    }

    public void setTableColumn(){

        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));
        bedroomColumn.setCellValueFactory(new PropertyValueFactory<>("bedroom"));
        bathroomColumn.setCellValueFactory(new PropertyValueFactory<>("bathroom"));
        rentOrBuyColumn.setCellValueFactory(new PropertyValueFactory<>("rentOrBuy"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        securityChargeColumn.setCellValueFactory(new PropertyValueFactory<>("securityCharge"));
        monthlyRentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyRent"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        houseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("houseType"));
        floorNoColumn.setCellValueFactory(new PropertyValueFactory<>("floorNo"));

    }

    public void loadTable(){

        try {
            tableData = unitModel.loadTable();
            table.setItems(tableData);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setItemsToStatusCmb(){

        ObservableList<String> status = FXCollections.observableArrayList();
        status.addAll("Available","Unavailable");
        statusCmb.setItems(status);

    }

    public void setItemsToRentOrBuyCmb(){

        ObservableList<String> rentOrBuy = FXCollections.observableArrayList();
        rentOrBuy.addAll("Rent","Sell");
        rentOrBuyCmb.setItems(rentOrBuy);

    }


    public void setItemsToSortCmb(){

        ObservableList<String> sortTableData = FXCollections.observableArrayList();
        sortTableData.addAll("bedroom asc","bedroom desc","bathroom asc","bathroom desc","total values asc","total values desc","monthly rent asc","monthly rent desc");
        sortCmb.setItems(sortTableData);

    }

    public void setItemsToTableRowsCmb(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0 ;

        for(UnitTm x : tableData){
            count++;
            rows.add(count);

        }
        tableRowsCmb.setItems(rows);

    }

    public void setItemsToHouseIdCmb(){

        ObservableList<String> houseId = FXCollections.observableArrayList();

        for(UnitTm x : tableData){
            houseId.add(x.getHouseId());
        }
        houseIdCmb.setItems(houseId);

    }

    public void setItemsToHouseTypeCmb(){

        ObservableList<String> houseType = FXCollections.observableArrayList();

        try{
            ObservableList<HouseTypeTm> allHouseTypes = houseTypeModel.loadTableData();
            for(HouseTypeTm x : allHouseTypes){
                houseType.add(x.getHouseType());
            }

        }
        catch (Exception e){
           e.printStackTrace();
        }

        houseTypeCmb.setItems(houseType);

    }

}
