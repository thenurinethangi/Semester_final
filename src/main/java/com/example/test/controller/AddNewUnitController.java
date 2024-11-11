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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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

    private String newHouseId;
    private AddNewUnitModel addNewUnitModel;
    private HouseTypeModel houseTypeModel;
    private FloorModel floorModel;
    private Integer floorNo;
    private String houseType;
    private String status;
    private String rentOrSell;
    private UnitTm selectedUnit;


    public AddNewUnitController(){

        try{

            floorModel = new FloorModel();
            houseTypeModel = new HouseTypeModel();
            addNewUnitModel = new AddNewUnitModel();
        }
        catch (Exception e){
            e.printStackTrace();
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
                if(UserInputValidation.checkNumberLessThanTenValidation(bedrooms) && UserInputValidation.checkNumberLessThanTenValidation(bathroom) && UserInputValidation.checkDecimalValidation(securityCharge) && UserInputValidation.checkDecimalValidation(securityCharge)){

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
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                        alert.show();
                        if(response.equals("successfully add new unit to the system")){
                            setNewHouseId();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clean();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"new unit entered details not valid please double check before add to the system");
                    alert.show();
                    clean();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all the detail of new unit to add to the system");
                alert.show();
            }
        }
        else if(rentOrSell!=null && rentOrSell.equals("Sell")){

            if(!bedrooms.isEmpty() && !bathroom.isEmpty() && totalValue!=null && floorNo!=null && houseType!=null && status!=null){
                if(UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bedrooms)) && UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bathroom)) && UserInputValidation.checkDecimalValidation(totalValue)){

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
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                        alert.show();
                        if(response.equals("successfully add new unit to the system")){
                            setNewHouseId();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clean();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"new unit entered details not valid please double check before add to the system");
                    alert.show();
                    clean();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all the detail of new unit to add to the system");
                alert.show();
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all the detail of new unit to add to the system");
            alert.show();
        }


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
        catch (Exception e){
            e.printStackTrace();
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

        }
        catch (Exception e){
            e.printStackTrace();
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
        }
        catch (Exception e){
            e.printStackTrace();
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
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                        alert.show();

                        if(response.equals("successfully update the unit")){
                            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            stage.close();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clean();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"new unit entered details not valid please double check before add to the system");
                    alert.show();
                    //clean();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all the detail of new unit to add to the system");
                alert.show();
            }
        }
        else if(rentOrSell!=null && rentOrSell.equals("Sell")){

            if(!bedrooms.isEmpty() && !bathroom.isEmpty() && totalValue!=null && floorNo!=null && houseType!=null && status!=null){
                if(UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bedrooms)) && UserInputValidation.checkNumberLessThanTenValidation(String.valueOf(bathroom)) && UserInputValidation.checkDecimalValidation(totalValue)){

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
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                        alert.show();

                        if(response.equals("successfully update the unit")){
                            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            stage.close();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    clean();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"new unit entered details not valid please double check before add to the system");
                    alert.show();
                    //clean();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all the detail of new unit to add to the system");
                alert.show();
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all the detail of new unit to add to the system");
            alert.show();
        }

    }
}



