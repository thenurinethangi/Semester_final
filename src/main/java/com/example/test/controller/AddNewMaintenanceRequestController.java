package com.example.test.controller;

import com.example.test.dto.TenantDto;
import com.example.test.dto.UnitDto;
import com.example.test.model.EmployeeModel;
import com.example.test.model.MaintenanceRequestModel;
import com.example.test.model.TenantModel;
import com.example.test.model.UnitModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewMaintenanceRequestController implements Initializable {

    @FXML
    public Label estimatedCostErrorMsgLabel;

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label houseIdLabel;

    @FXML
    private Label houseTypeLabel;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private Label requestIdLabel;

    @FXML
    private TextField tenantIdTxt;

    @FXML
    private TextField estimatedCostTxt;

    @FXML
    private ComboBox<String> technicianCmb;

    private final MaintenanceRequestModel maintenanceRequestModel = new MaintenanceRequestModel();
    private final TenantModel tenantModel = new TenantModel();
    private UnitModel unitModel;
    private TenantDto tenant;
    private EmployeeModel employeeModel;

    public AddNewMaintenanceRequestController() {

        try{
            employeeModel = new EmployeeModel();
            unitModel = new UnitModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void addOnAction(ActionEvent event) {

    }

    @FXML
    void cancelOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void searchIcon(MouseEvent event) {

        String tenantId = tenantIdTxt.getText();

        if (!tenantId.isEmpty()) {
            try {
                tenant = tenantModel.getMoreTenantDetails(tenantId);

                if (tenant.getName() == null) {

                    clean();

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("Please Enter Correct Tenant ID");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();
                    return;
                }

                houseIdLabel.setText(tenant.getHouseId());
                UnitDto unit = unitModel.getHouseDetailsByHouseId(tenant.getHouseId());
                houseTypeLabel.setText(unit.getHouseType());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setNewId();
        getAllTechnician();
    }

    public void setNewId(){

        try {
            String newId = maintenanceRequestModel.generateNewRequestId();
            requestIdLabel.setText(newId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void getAllTechnician(){

        try {
            ObservableList<String> technicians = employeeModel.getTechnicians();
            technicianCmb.setItems(technicians);
            technicianCmb.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void clean(){

        tenantIdTxt.clear();
        houseIdLabel.setText("");
        houseTypeLabel.setText("");
        descriptionTxt.clear();
        estimatedCostTxt.clear();
        getAllTechnician();

    }
}
