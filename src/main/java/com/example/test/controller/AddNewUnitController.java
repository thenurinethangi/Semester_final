package com.example.test.controller;

import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.FloorTm;
import com.example.test.dto.tm.HouseTypeTm;
import com.example.test.dto.tm.UnitTm;
import com.example.test.model.AddNewUnitModel;
import com.example.test.model.FloorModel;
import com.example.test.model.HouseTypeModel;
import com.example.test.validation.UserInputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AddNewUnitController implements Initializable {

    @FXML
    public Button editbtn;

    @FXML
    private Label houseIdLable;

    @FXML
    private TextField bedRoomtxt;

    @FXML
    private ComboBox<String> rentOrSellCmb;

    @FXML
    private ComboBox<String> statusCmb;

    @FXML
    private ComboBox<String> houseTypeCmb;

    @FXML
    private ComboBox<Integer> floorNoCmb;

    @FXML
    private TextField bathRoomtxt;

    @FXML
    private TextField totalValuetxt;

    @FXML
    private TextField securityChargetxt;

    @FXML
    private TextField monthlyRenttxt;

    @FXML
    private Button addbtn;

    @FXML
    private Button clearbtn;

    @FXML
    private Button canclebtn;

    @FXML
    private Label bedRoomNoErrorMsg;

    @FXML
    private Label totalValueErrorMsg;

    @FXML
    private Label securityChargeErrorMsg;

    @FXML
    private Label monthlyRentErrorMsg;

    @FXML
    private Label bathRoomNoErrorMsg;

    private String newHouseId;
    private AddNewUnitModel addNewUnitModel;
    private HouseTypeModel houseTypeModel;
    private FloorModel floorModel;
    private Integer floorNo;
    private String houseType;
    private String status;
    private String rentOrSell;
    private UnitTm selectedUnit;

    private static final Logger logger = LoggerFactory.getLogger(AddNewUnitController.class);

    public AddNewUnitController(){

        try{
            floorModel = new FloorModel();
            houseTypeModel = new HouseTypeModel();
            addNewUnitModel = new AddNewUnitModel();
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error while loading the Add New Unit page: {}", e.getMessage(), e);
            System.err.println("Error while loading the Add New Unit page: " + e.getMessage());
            notification("An error occurred while loading the Add New Unit page. Please try again or contact support.");
        }

    }

    @FXML
    void addOnAction(ActionEvent event) {

        String id = houseIdLable.getText();
        String bedrooms = bedRoomtxt.getText();
        String bathroom = bathRoomtxt.getText();
        String totalValue = totalValuetxt.getText();
        String securityCharge = securityChargetxt.getText();
        String monthlyRent = monthlyRenttxt.getText();


        if(rentOrSell!=null && rentOrSell.equals("Rent")){
            if(!bedrooms.isEmpty() && !bathroom.isEmpty() && securityCharge!=null && monthlyRent!=null && floorNo!=null && houseType!=null && status!=null){
                if(UserInputValidation.checkNumberLessThanTenValidation(bedrooms) && UserInputValidation.checkNumberLessThanTenValidation(bathroom) && UserInputValidation.checkDecimalValidation(securityCharge) && UserInputValidation.checkDecimalValidation(monthlyRent)){

                    handleInputValidationErrors(bedrooms,bathroom,securityCharge,monthlyRent,null);

                    UnitDto newUnit = new UnitDto();
                    newUnit.setHouseId(id);
                    newUnit.setBedroom(Integer.parseInt(bedrooms));
                    newUnit.setBathroom(Integer.parseInt(bathroom));
                    newUnit.setRentOrBuy(rentOrSell);
                    newUnit.setTotalValue("N/A");
                    newUnit.setSecurityCharge(securityCharge);
                    newUnit.setMonthlyRent(monthlyRent);
                    newUnit.setStatus(status);
                    newUnit.setHouseType(houseType);
                    newUnit.setFloorNo(floorNo);

                    try {
                        String response = addNewUnitModel.addNewUnit(newUnit);//
                        notification(response);
                        if(response.equals("successfully add new unit to the system")){
                            setNewHouseId();
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error while adding the new unit: " + e.getMessage());
                        notification("An error occurred while adding the new unit, Please try again or contact support.");
                    }
                    clean();
                }
                else{
                    handleInputValidationErrors(bedrooms,bathroom,securityCharge,monthlyRent,null);
                }
            }
            else{
                handleInputValidationErrors(bedrooms,bathroom,securityCharge,monthlyRent,null);
                notification("Please provide all the details for the new unit to be added to the system");
            }
        }

        else if(rentOrSell!=null && rentOrSell.equals("Sell")){

            if(!bedrooms.isEmpty() && !bathroom.isEmpty() && totalValue!=null && floorNo!=null && houseType!=null && status!=null){
                if(UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bedrooms)) && UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bathroom)) && UserInputValidation.checkDecimalValidation(totalValue)){

                    handleInputValidationErrors(bedrooms,bathroom,null,null,totalValue);

                    UnitDto newUnit = new UnitDto();
                    newUnit.setHouseId(id);
                    newUnit.setBedroom(Integer.parseInt(bedrooms));
                    newUnit.setBathroom(Integer.parseInt(bathroom));
                    newUnit.setRentOrBuy(rentOrSell);
                    newUnit.setTotalValue(totalValue);
                    newUnit.setSecurityCharge("N/A");
                    newUnit.setMonthlyRent("N/A");
                    newUnit.setStatus(status);
                    newUnit.setHouseType(houseType);
                    newUnit.setFloorNo(floorNo);

                    try {
                        String response = addNewUnitModel.addNewUnit(newUnit);
                        notification(response);
                        if(response.equals("successfully add new unit to the system")){
                            setNewHouseId();
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error while adding the new unit: " + e.getMessage());
                        notification("An error occurred while adding the new unit, Please try again or contact support.");
                    }
                    clean();
                }
                else{
                    handleInputValidationErrors(bedrooms,bathroom,null,null,totalValue);
                }
            }
            else{
                handleInputValidationErrors(bedrooms,bathroom,null,null,totalValue);
               notification("Please provide all the details for the new unit to be added to the system");
            }
        }
        else{
            notification("Please provide all the details for the new unit to be added to the system");
        }
    }


    private void notification(String message) {

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text(message);
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();
    }


    public void clean(){

        floorNo = null;
        houseType = null;
        status = null;
        rentOrSell = null;
        bedRoomtxt.setText("");
        bathRoomtxt.setText("");
        totalValuetxt.setText("");
        securityChargetxt.setText("");
        monthlyRenttxt.setText("");
        rentOrSellCmb.getSelectionModel().clearSelection();
        statusCmb.getSelectionModel().clearSelection();
        houseTypeCmb.getSelectionModel().clearSelection();
        floorNoCmb.getSelectionModel().clearSelection();

        bedRoomNoErrorMsg.setText("");
        bathRoomNoErrorMsg.setText("");
        monthlyRentErrorMsg.setText("");
        securityChargeErrorMsg.setText("");
        totalValueErrorMsg.setText("");

    }


    @FXML
    void cancleOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void clearOnAction(ActionEvent event) {

        clean();
    }

    @FXML
    void floorNoCmbOnAction(ActionEvent event) {

        floorNo = floorNoCmb.getSelectionModel().getSelectedItem();
        System.out.println(floorNo);
    }


    @FXML
    void houseTypeCmbOnAction(ActionEvent event) {

        houseType = houseTypeCmb.getSelectionModel().getSelectedItem();
        System.out.println(houseType);
    }


    @FXML
    void rentOrSellCmbOnAction(ActionEvent event) {

        rentOrSell = rentOrSellCmb.getSelectionModel().getSelectedItem();
        System.out.println(rentOrSell);
    }


    @FXML
    void statusCmbOnAction(ActionEvent event) {

        status = statusCmb.getSelectionModel().getSelectedItem();
        System.out.println(status);
    }


    public void setItemsFloorNoCmb(){

        ObservableList<Integer> floorNumbers = FXCollections.observableArrayList();

        try{
            ObservableList<FloorTm> allFloors = floorModel.loadTableData();
            for(FloorTm x : allFloors){
                floorNumbers.add(x.getFloorNo());
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while setting the floor numbers: " + e.getMessage());
            notification("An error occurred while setting the floor numbers, Please try again or contact support.");
        }

        floorNoCmb.setItems(floorNumbers);

    }


    public void setItemsToHouseTypeCmb(){

        ObservableList<String> houseType = FXCollections.observableArrayList();

        try{
            ObservableList<HouseTypeTm> allHouseTypes = houseTypeModel.loadTableData();
            for(HouseTypeTm x : allHouseTypes){
                houseType.add(x.getHouseType());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while setting the house types: " + e.getMessage());
            notification("An error occurred while setting the house types, Please try again or contact support.");
        }

        houseTypeCmb.setItems(houseType);

    }

    public void setItemsToRentOrBuyCmb(){

        ObservableList<String> rentOrBuy = FXCollections.observableArrayList();
        rentOrBuy.addAll("Rent","Sell");
        rentOrSellCmb.setItems(rentOrBuy);

    }

    public void setItemsToStatusCmb(){

        ObservableList<String> status = FXCollections.observableArrayList();
        status.addAll("Available");
        statusCmb.setItems(status);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setNewHouseId();
        setItemsToStatusCmb();
        setItemsToRentOrBuyCmb();
        setItemsToHouseTypeCmb();
        setItemsFloorNoCmb();
        editbtn.setDisable(true);

    }


    public void setNewHouseId(){

        try {
            newHouseId = addNewUnitModel.getNewHouseId();
            houseIdLable.setText(newHouseId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while setting the house ids: " + e.getMessage());
            notification("An error occurred while setting the house ids, Please try again or contact support.");
        }


    }

    public void setEditRowDetails(UnitTm unitTm){

        selectedUnit = unitTm;

        houseIdLable.setText(selectedUnit.getHouseId());
        bedRoomtxt.setText(String.valueOf(selectedUnit.getBedroom()));
        bathRoomtxt.setText(String.valueOf(selectedUnit.getBathroom()));
        totalValuetxt.setText(selectedUnit.getTotalValue());
        securityChargetxt.setText(selectedUnit.getSecurityCharge());
        monthlyRenttxt.setText(selectedUnit.getMonthlyRent());
        rentOrSellCmb.getSelectionModel().select(selectedUnit.getRentOrBuy());
        statusCmb.getSelectionModel().select(selectedUnit.getStatus());
        houseTypeCmb.getSelectionModel().select(selectedUnit.getHouseType());
        floorNoCmb.getSelectionModel().select(selectedUnit.getFloorNo());

        addbtn.setDisable(true);
        editbtn.setDisable(false);

        rentOrSell = selectedUnit.getRentOrBuy();
        status = selectedUnit.getStatus();
        houseType = selectedUnit.getHouseType();
        floorNo = selectedUnit.getFloorNo();

    }

    @FXML
    public void editOnAction(ActionEvent event) {


        String id = houseIdLable.getText();
        String bedrooms = bedRoomtxt.getText();
        String bathroom = bathRoomtxt.getText();
        String totalValue = totalValuetxt.getText();
        String securityCharge = securityChargetxt.getText();
        String monthlyRent = monthlyRenttxt.getText();


        if(rentOrSell!=null && rentOrSell.equals("Rent")){
            if(!bedrooms.isEmpty() && !bathroom.isEmpty() && securityCharge!=null && monthlyRent!=null && floorNo!=null && houseType!=null && status!=null){
                if(UserInputValidation.checkNumberLessThanTenValidation(bedrooms) && UserInputValidation.checkNumberLessThanTenValidation(bathroom) && UserInputValidation.checkDecimalValidation(securityCharge) && UserInputValidation.checkDecimalValidation(securityCharge)){

                    handleInputValidationErrors(bedrooms,bathroom,securityCharge,monthlyRent,null);

                    UnitDto newUnit = new UnitDto();
                    newUnit.setHouseId(id);
                    newUnit.setBedroom(Integer.parseInt(bedrooms));
                    newUnit.setBathroom(Integer.parseInt(bathroom));
                    newUnit.setRentOrBuy(rentOrSell);
                    newUnit.setTotalValue("N/A");
                    newUnit.setSecurityCharge(securityCharge);
                    newUnit.setMonthlyRent(monthlyRent);
                    newUnit.setStatus(status);
                    newUnit.setHouseType(houseType);
                    newUnit.setFloorNo(floorNo);

                    try {
                        String response = addNewUnitModel.editUnit(newUnit);
                        notification(response);

                        if(response.equals("successfully update the unit")){
                            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            stage.close();
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error while updating the unit: " + e.getMessage());
                        notification("An error occurred while updating the unit: "+newUnit.getHouseId()+", Please try again or contact support.");
                    }
                    clean();
                }
                else{
                    handleInputValidationErrors(bedrooms,bathroom,securityCharge,monthlyRent,null);
                }
            }
            else{
                handleInputValidationErrors(bedrooms,bathroom,securityCharge,monthlyRent,null);
                notification("No Field can be empty");
            }
        }

        else if(rentOrSell!=null && rentOrSell.equals("Sell")){

            if(!bedrooms.isEmpty() && !bathroom.isEmpty() && totalValue!=null && floorNo!=null && houseType!=null && status!=null){
                if(UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bedrooms)) && UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bathroom)) && UserInputValidation.checkDecimalValidation(totalValue)){

                    handleInputValidationErrors(bedrooms,bathroom,null,null,totalValue);

                    UnitDto newUnit = new UnitDto();
                    newUnit.setHouseId(id);
                    newUnit.setBedroom(Integer.parseInt(bedrooms));
                    newUnit.setBathroom(Integer.parseInt(bathroom));
                    newUnit.setRentOrBuy(rentOrSell);
                    newUnit.setTotalValue(totalValue);
                    newUnit.setSecurityCharge("N/A");
                    newUnit.setMonthlyRent("N/A");
                    newUnit.setStatus(status);
                    newUnit.setHouseType(houseType);
                    newUnit.setFloorNo(floorNo);

                    try {
                        String response = addNewUnitModel.editUnit(newUnit);
                        notification(response);

                        if(response.equals("successfully update the unit")){
                            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            stage.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error while updating the unit: " + e.getMessage());
                        notification("An error occurred while updating the unit: "+newUnit.getHouseId()+", Please try again or contact support.");
                    }

                    clean();
                }
                else{
                  handleInputValidationErrors(bedrooms,bathroom,null,null,totalValue);

                }
            }
            else{
                handleInputValidationErrors(bedrooms,bathroom,null,null,totalValue);
                notification("No Field can be empty");
            }
        }
        else{
            notification("No Field can be empty");
        }

    }


    private void handleInputValidationErrors(String bedrooms, String bathroom, String securityCharge, String monthlyRent,String totalValue) {

        if (!UserInputValidation.checkNumberLessThanTenValidation(bedrooms)) {
            bedRoomNoErrorMsg.setText("The bedroom count you provided is invalid");
        }
        else{
            bedRoomNoErrorMsg.setText("");
        }
        if (!UserInputValidation.checkNumberLessThanTenValidation(bathroom)) {
            bathRoomNoErrorMsg.setText("The bathroom count you provided is invalid");
        }
        else{
            bathRoomNoErrorMsg.setText("");
        }
        if (securityCharge != null && !UserInputValidation.checkDecimalValidation(securityCharge)) {
            securityChargeErrorMsg.setText("The security charge you provided is invalid");
        }
        else{
            securityChargeErrorMsg.setText("");
        }
        if (monthlyRent != null && !UserInputValidation.checkDecimalValidation(monthlyRent)) {
            monthlyRentErrorMsg.setText("The monthly rent you provided is invalid");
        }
        else{
            monthlyRentErrorMsg.setText("");
        }
        if (totalValue != null && !UserInputValidation.checkDecimalValidation(totalValue)) {
            totalValueErrorMsg.setText("The total value of the you provided is invalid");
        }
        else{
            totalValueErrorMsg.setText("");
        }
    }
}
