package com.example.test.controller;

import com.example.test.dto.tm.EmployeeTm;
import com.example.test.dto.tm.ReturnHouseTm;
import com.example.test.model.ReturnHouseModel;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReturnHouseController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<ReturnHouseTm> table;

    @FXML
    private TableColumn<ReturnHouseTm, String> returnNoColumn;

    @FXML
    private TableColumn<ReturnHouseTm, String> reasonColumn;

    @FXML
    private TableColumn<ReturnHouseTm, String> dateColumn;

    @FXML
    private TableColumn<ReturnHouseTm, String> tenantIdColumn;

    @FXML
    private TableColumn<ReturnHouseTm, String> houseIdColumn;

    @FXML
    private TableColumn<ReturnHouseTm, String> refundedAmountColumn;

    @FXML
    private TableColumn<ReturnHouseTm, String> expenseNoColumn;

    @FXML
    private Button addNewReturnBtn;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private Button deletebtn;

    @FXML
    private ComboBox<String> sortCmb;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private ComboBox<String> returnNoCmb;

    @FXML
    private ComboBox<String> expenseCmb;

    @FXML
    private ComboBox<String> tenantIdCmb;

    @FXML
    private ComboBox<String> houseIdCmb;

    @FXML
    private TextField searchTxt;


    private ObservableList<ReturnHouseTm> tableData;
    private final ReturnHouseModel returnHouseModel = new ReturnHouseModel();



    @FXML
    void addNewReturnOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewHouseReturn.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    void deleteOnAction(ActionEvent event) {

    }

    @FXML
    void editOnAction(ActionEvent event) {

    }

    @FXML
    void getSelectedRow(MouseEvent event) {

    }

    @FXML
    void refreshOnAction(ActionEvent event) {

    }

    @FXML
    void searchOnAction(ActionEvent event) {

    }

    @FXML
    void sortCmbOnAction(ActionEvent event) {

    }

    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumnsValues();
        loadTable();
        setRowCmbValues();
    }


    public void setTableColumnsValues(){

        returnNoColumn.setCellValueFactory(new PropertyValueFactory<>("returnNo"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));
        refundedAmountColumn.setCellValueFactory(new PropertyValueFactory<>("refundedAmount"));
        expenseNoColumn.setCellValueFactory(new PropertyValueFactory<>("expenseNo"));

    }


    public void loadTable(){

        try {
            tableData = returnHouseModel.getAllReturns();
            table.setItems(tableData);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (ReturnHouseTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }
}





