package com.example.test.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class ExpenseController {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> expenseNoColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> amountColumn;

    @FXML
    private TableColumn<?, ?> requestNoColumn;

    @FXML
    private Button addNewExpenseBtn;

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
    private ComboBox<?> expenseNoCmb;

    @FXML
    private ComboBox<?> requestNoCmb;

    @FXML
    private TextField searchTxt;

    @FXML
    void addNewExpenseOnAction(ActionEvent event) {

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
