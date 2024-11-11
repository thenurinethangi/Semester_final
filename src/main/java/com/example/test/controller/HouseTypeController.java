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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
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

    private HouseTypeModel houseTypeModel;
    private ObservableList<HouseTypeTm> tableData;
    private ObservableList<Integer> rows;
    private ObservableList<String> sort;
    private ObservableList<String> allHouseTypes;
    private String selectHouseType;


    public HouseTypeController(){

        try{
            houseTypeModel = new HouseTypeModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void addOnAction(ActionEvent event) {

        String houseType = houseTypetxt.getText();
        String desc = descriptiontxt.getText();

        if(houseType.isEmpty() || desc.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.WARNING,"You should enter all new house type details to" +
                    " add new house type to the system");
            alert.showAndWait();

        }
        else{
            boolean bool1 = UserInputValidation.checkTextValidation(houseType);
            boolean bool2 = UserInputValidation.checkTextValidation(desc);

            if(bool1 && bool2){

                HouseTypeDto houseTypeDto = new HouseTypeDto(houseType,desc);

                try {
                    String response = houseTypeModel.addNewHouseType(houseTypeDto);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                    alert.showAndWait();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                if(!bool1 && bool2){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"House type input is not valid format please enter valid house type");
                    alert.showAndWait();
                }
                else if(!bool2 && bool1){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"House description input is not valid format please enter valid house description");
                    alert.showAndWait();
                }
                else if(!bool1 && !bool2){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"House type input is not valid format please enter valid house type");
                    alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.WARNING,"You should select house type to delete, tap on the table row you want to delete");
            alert.showAndWait();
        }
        else{

            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this house type?");
            Optional<ButtonType> options = a1.showAndWait();

            if(options.isPresent() && options.get()==ButtonType.OK) {

                try {
                    String response = houseTypeModel.deleteHouseType(selectHouseType);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                    alert.showAndWait();
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

    }



    @FXML
    void editOnAction(ActionEvent event) {

        HouseTypeTm selectHouseType = table.getSelectionModel().getSelectedItem();

        if(selectHouseType==null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"You should select house type to edit, tap on the table row you want to edit");
            alert.showAndWait();
        }
        else {
            try {
                HouseTypeDto houseTypeDto = new HouseTypeDto(selectHouseType.getHouseType(),selectHouseType.getDescription());
                HouseTypeEditController.setData(houseTypeDto);
                System.out.println(houseTypeDto.getHouseType());

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HouseTypeEdit.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);

                //HouseTypeEditController houseTypeEditController = fxmlLoader.getController();
                //houseTypeEditController.setData(houseTypeDto);

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

        loadTableData();
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

        if(text.equals("get by house type asc")){

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
        else if(text.equals("get by house type desc")){

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
            if(tableData.isEmpty()){
                System.out.println("empty");
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


    public void setItemsToRowsCmb() {

        rows = FXCollections.observableArrayList();
        int count = 0;
        for(HouseTypeTm x : tableData){
            count++;
            rows.add(count);
        }
        tableRowsCmb.setItems(rows);

    }


    public void setItemsToSortCmb() {

        sort = FXCollections.observableArrayList();
        sort.addAll("get by house type asc","get by house type desc");
        sortCmb.setItems(sort);
    }


      public void setItemsToHouseTypeCmb(){

        getHouseTypes();
        houseTypeCmb.setItems(allHouseTypes);

      }

      public void getHouseTypes(){

        allHouseTypes = FXCollections.observableArrayList();
        for(HouseTypeTm x : tableData){

            allHouseTypes.add(x.getHouseType());
        }

      }


    public void clean(){


    }
}
