package com.example.test.controller;

import com.example.test.dto.tm.HouseTypeTm;
import com.example.test.dto.tm.UnitTm;
import com.example.test.model.HouseTypeModel;
import com.example.test.model.UnitModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    private TextField searchTxt;

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
    private boolean isOnlyAvailableUnits = false;
    private ObservableList<UnitTm> dataList = FXCollections.observableArrayList();


    public UnitController(){

        try {
            houseTypeModel = new HouseTypeModel();
            unitModel = new UnitModel();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading the unit page: " + e.getMessage());
            notification("An error occurred while loading the unit page. Please try again or contact support.");
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
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the floor page: " + e.getMessage());
            notification("An error occurred while loading the floor page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the house type page: " + e.getMessage());
            notification("An error occurred while loading the house type page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the add new unit page: " + e.getMessage());
            notification("An error occurred while loading the add new unit page. Please try again or contact support.");
        }
    }


    @FXML
    public void getSelectedRow(MouseEvent mouseEvent) {

        UnitTm selectItem = table.getSelectionModel().getSelectedItem();

        if(selectItem==null){
           return;
        }

        if(selectItem.getStatus().equals("Unavailable")) {
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
            return;
        }

        else {
            if (selectedRow.getStatus().equals("Available")) {

                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert a1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete selected unit?");
                a1.getButtonTypes().setAll(yesButton, cancelButton);
                Optional<ButtonType> options = a1.showAndWait();

                if (options.isPresent() && options.get() == yesButton) {
                    try {
                        String response = unitModel.deleteUnit(selectedRow);
                        notification(response);

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error while deleting the unit: " + e.getMessage());
                        notification("An error occurred while deleting the unit. Please try again or contact support.");
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
            return;
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
            catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while loading the unit edit page: " + e.getMessage());
                notification("An error occurred while loading the unit edit page. Please try again or contact support.");
            }

        }
    }


    @FXML
    void refreshOnAction(ActionEvent event) {

        if(!isOnlyAvailableUnits){
            clean();
        }
        else {
            setTableToOnlyAvailableUnits();
        }

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

        ObservableList<UnitTm> searchedHouses = FXCollections.observableArrayList();

        String selectedHouseId = houseIdCmb.getValue();
        String selectedHouseType = houseTypeCmb.getValue();
        String selectedStatus = statusCmb.getValue();
        String selectedRentSell = rentOrBuyCmb.getValue();

        boolean houseIdSelected = selectedHouseId != null && !selectedHouseId.equals("Select");
        boolean houseTypeSelected = selectedHouseType != null && !selectedHouseType.equals("Select");
        boolean statusSelected = selectedStatus != null && !selectedStatus.equals("Select");
        boolean rentSellSelected = selectedRentSell != null && !selectedRentSell.equals("Select");

        if (houseIdSelected) {

                ObservableList<UnitTm> housesById = getHouseById(selectedHouseId);

                if (housesById.isEmpty()) {
                    table.setItems(housesById);
                }
                else {
                    searchedHouses.addAll(housesById);

                    if (houseTypeSelected) {
                        ObservableList<UnitTm> filteredByType = filterHousesByType(searchedHouses, selectedHouseType);
                        searchedHouses.clear();
                        searchedHouses.addAll(filteredByType);
                    }

                    if (statusSelected) {
                        ObservableList<UnitTm> filteredByStatus = filterHousesByStatus(searchedHouses, selectedStatus);
                        searchedHouses.clear();
                        searchedHouses.addAll(filteredByStatus);
                    }

                    if (rentSellSelected) {
                        ObservableList<UnitTm> filteredByRentSell = filterHousesByRentSell(searchedHouses, selectedRentSell);
                        searchedHouses.clear();
                        searchedHouses.addAll(filteredByRentSell);
                    }

                    table.setItems(searchedHouses);
                }

        }

        else if (houseTypeSelected || statusSelected || rentSellSelected) {

                ObservableList<UnitTm> allHouses = tableData;
                searchedHouses.addAll(allHouses);

                if (houseTypeSelected) {
                    searchedHouses = filterHousesByType(searchedHouses, selectedHouseType);
                }

                if (statusSelected) {
                    searchedHouses = filterHousesByStatus(searchedHouses, selectedStatus);
                }

                if (rentSellSelected) {
                    searchedHouses = filterHousesByRentSell(searchedHouses, selectedRentSell);
                }

                table.setItems(searchedHouses);

        } else {
                ObservableList<UnitTm> allHouses = tableData;
                table.setItems(allHouses);
        }
    }


    public ObservableList<UnitTm> getHouseById(String houseId) {

        ObservableList<UnitTm> filteredHouses = FXCollections.observableArrayList();
        for (UnitTm house : tableData) {
            if (house.getHouseId().equals(houseId)) {
                filteredHouses.add(house);
            }
        }
        return filteredHouses;
    }


    public ObservableList<UnitTm> filterHousesByType(ObservableList<UnitTm> houses, String houseType) {

        ObservableList<UnitTm> filteredHouses = FXCollections.observableArrayList();
        for (UnitTm house : houses) {
            if (house.getHouseType().equals(houseType)) {
                filteredHouses.add(house);
            }
        }
        return filteredHouses;
    }


    public ObservableList<UnitTm> filterHousesByStatus(ObservableList<UnitTm> houses, String status) {

        ObservableList<UnitTm> filteredHouses = FXCollections.observableArrayList();
        for (UnitTm house : houses) {
            if (house.getStatus().equals(status)) {
                filteredHouses.add(house);
            }
        }
        return filteredHouses;
    }


    public ObservableList<UnitTm> filterHousesByRentSell(ObservableList<UnitTm> houses, String rentSell) {

        ObservableList<UnitTm> filteredHouses = FXCollections.observableArrayList();
        for (UnitTm house : houses) {
            if (house.getRentOrBuy().equals(rentSell)) {
                filteredHouses.add(house);
            }
        }
        return filteredHouses;
    }


    @FXML
    void sortCmbOnAction(ActionEvent event) {

        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<UnitTm> unitTmsAr = tableData;

        if(sortType.equals("Retrieve by bedroom (ascending)")){

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

        else if(sortType.equals("Retrieve by bedroom (descending)")){

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

        else if(sortType.equals("Retrieve by bathroom (ascending)")){

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

        else if(sortType.equals("Retrieve by bathroom (descending)")){

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

        else if(sortType.equals("Retrieve by total unit value (ascending)")){

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


        else if(sortType.equals("Retrieve by total unit value (descending)")){

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

        else if(sortType.equals("Retrieve by monthly rent (ascending)")){

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


        else if(sortType.equals("Retrieve by monthly rent (descending)")){

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

        Integer rows = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(rows==null){
           return;
        }

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

        tableSearch();

    }


    public void tableSearch(){

        FilteredList<UnitTm> filteredData = new FilteredList<>(tableData, b -> true);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(house -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (house.getHouseId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(house.getBedroom()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(house.getBathroom()).contains(lowerCaseFilter)) {
                    return true;
                } else if (house.getRentOrBuy().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (house.getTotalValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (house.getSecurityCharge().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (house.getMonthlyRent().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (house.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (house.getHouseType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(house.getFloorNo()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<UnitTm> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);

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
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while loading data to the table: " + e.getMessage());
            notification("An error occurred while loading data to the table. Please try again or contact support.");
        }


    }


    public void setItemsToStatusCmb(){

        ObservableList<String> status = FXCollections.observableArrayList();
        status.addAll("Select","Available","Unavailable");
        statusCmb.setItems(status);
        statusCmb.getSelectionModel().selectFirst();

    }


    public void setItemsToRentOrBuyCmb(){

        ObservableList<String> rentOrBuy = FXCollections.observableArrayList();
        rentOrBuy.addAll("Select","Rent","Sell");
        rentOrBuyCmb.setItems(rentOrBuy);
        rentOrBuyCmb.getSelectionModel().selectFirst();

    }


    public void setItemsToSortCmb(){

        ObservableList<String> sortTableData = FXCollections.observableArrayList();
        sortTableData.addAll("Sort By","Retrieve by bedroom (ascending)","Retrieve by bedroom (descending)","Retrieve by bathroom (ascending)","Retrieve by bathroom (descending)","Retrieve by total unit value (ascending)","Retrieve by total unit value (descending)","Retrieve by monthly rent (ascending)","Retrieve by monthly rent (descending)");
        sortCmb.setItems(sortTableData);
        sortCmb.getSelectionModel().selectFirst();

    }


    public void setItemsToTableRowsCmb(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0 ;

        for(UnitTm x : tableData){
            count++;
            rows.add(count);

        }
        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }


    public void setItemsToHouseIdCmb(){

        ObservableList<String> houseId = FXCollections.observableArrayList();
        houseId.add("Select");

        for(UnitTm x : tableData){
            houseId.add(x.getHouseId());
        }
        houseIdCmb.setItems(houseId);
        houseIdCmb.getSelectionModel().selectFirst();

    }


    public void setItemsToHouseTypeCmb(){

        ObservableList<String> houseType = FXCollections.observableArrayList();
        houseType.add("Select");

        try{
            ObservableList<HouseTypeTm> allHouseTypes = houseTypeModel.loadTableData();
            for(HouseTypeTm x : allHouseTypes){
                houseType.add(x.getHouseType());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while setting house types: " + e.getMessage());
            notification("An error occurred while setting house types. Please try again or contact support.");
        }


        houseTypeCmb.setItems(houseType);
        houseTypeCmb.getSelectionModel().selectFirst();

    }


    public void clean(){

        loadTable();
        statusCmb.getSelectionModel().selectFirst();
        rentOrBuyCmb.getSelectionModel().selectFirst();
        sortCmb.getSelectionModel().selectFirst();
        setItemsToHouseTypeCmb();
        setItemsToHouseIdCmb();
        setItemsToTableRowsCmb();
        searchTxt.clear();
        table.getSelectionModel().clearSelection();

    }


    public void setTableToOnlyAvailableUnits() {

        isOnlyAvailableUnits = true;

        try {
            tableData = unitModel.getOnlyAvailableUnits();
            table.setItems(tableData);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading data to the table: " + e.getMessage());
            notification("An error occurred while loading data to the table. Please try again or contact support.");
        }

        setItemsToHouseIdCmb();
        setItemsToTableRowsCmb();
        setItemsToStatusCmb();
        setItemsToRentOrBuyCmb();
        setItemsToHouseTypeCmb();
        setItemsToSortCmb();
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


