package com.example.test.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MaintainRequestController {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> requestNoColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> estimatedCostColumn;

    @FXML
    private TableColumn<?, ?> actualCostColumn;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> technicianColumn;

    @FXML
    private TableColumn<?, ?> tenantIdColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> actionColumn;

    @FXML
    private Button addNewMaintanceRequestBtn;

    @FXML
    private ComboBox<?> tableRowsCmb;

    @FXML
    private Button deletebtn;

    @FXML
    private ComboBox<?> sortCmb;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private ComboBox<?> requestNoCmb;

    @FXML
    private ComboBox<?> statusCmb;

    @FXML
    private ComboBox<?> tenantIdCmb;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField searchTxt;


    @FXML
    void addNewMaintenanceRequestOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewMaintenanceRequest.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage s1 = new Stage();
            s1.setScene(scene);
            s1.show();
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
}
