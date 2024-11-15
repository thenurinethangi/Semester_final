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

        ReturnHouseTm selectedRow = table.getSelectionModel().getSelectedItem();

        if(selectedRow==null){
            return;
        }

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Please Confirm First");
        alert.setContentText("Are you sure you want to delete selected return house details?");

        alert.getButtonTypes().setAll(yesButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            String response = null;
            try {
                response = returnHouseModel.setSelectedReturnDetailDeactivate(selectedRow);

                Notifications notifications = Notifications.create();
                notifications.title("Notification");
                notifications.text(response);
                notifications.hideCloseButton();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.CENTER);
                notifications.darkStyle();
                notifications.showInformation();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } else {
            return;
        }
    }


    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();

    }

    @FXML
    void searchOnAction(ActionEvent event) {

    }

    @FXML
    void sortCmbOnAction(ActionEvent event) {

    }

    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<ReturnHouseTm> returnHouseTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            returnHouseTms.add(tableData.get(i));
        }

        table.setItems(returnHouseTms);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumnsValues();
        loadTable();
        setRowCmbValues();
        setReturnNoCmbValues();
        setExpenseCmbValues();
        setTenantIdCmbValues();
        setHouseIdCmbValues();

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


    public void setReturnNoCmbValues(){

        ObservableList<String> returnNos = FXCollections.observableArrayList();
        returnNos.add("Select");

        for(ReturnHouseTm x : tableData){
            returnNos.add(x.getReturnNo());
        }

        returnNoCmb.setItems(returnNos);
        returnNoCmb.getSelectionModel().selectFirst();

    }


    public void setExpenseCmbValues(){

        ObservableList<String> expenseNos = FXCollections.observableArrayList();
        expenseNos.add("Select");

        for(ReturnHouseTm x : tableData){
            if(!x.getExpenseNo().equals("N/A")) {
                expenseNos.add(x.getExpenseNo());
            }
        }

        expenseCmb.setItems(expenseNos);
        expenseCmb.getSelectionModel().selectFirst();

    }


    public void setTenantIdCmbValues(){

        try {
            ObservableList<String> distinctTenantIds = returnHouseModel.getAllDistinctTenantIds();
            tenantIdCmb.setItems(distinctTenantIds);
            tenantIdCmb.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void setHouseIdCmbValues(){

        try {
            ObservableList<String> distinctHouseIds = returnHouseModel.getAllDistinctHouseIds();
            houseIdCmb.setItems(distinctHouseIds);
            houseIdCmb.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void clean(){

        loadTable();
        setRowCmbValues();
        setReturnNoCmbValues();
        setExpenseCmbValues();
        setTenantIdCmbValues();
        setHouseIdCmbValues();
        table.getSelectionModel().clearSelection();

    }
}





