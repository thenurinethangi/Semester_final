package com.example.test.controller;

import com.example.test.dto.MaintenanceRequestDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.MaintenanceRequestTm;
import com.example.test.model.EmployeeModel;
import com.example.test.model.MaintenanceRequestModel;
import com.example.test.model.TenantModel;
import com.example.test.model.UnitModel;
import com.example.test.validation.UserInputValidation;
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
import java.time.LocalDate;
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
    private MaintenanceRequestTm selectedRequest;

    public AddNewMaintenanceRequestController() {

        try{
            employeeModel = new EmployeeModel();
            unitModel = new UnitModel();
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading Add New Maintenance Request Form: " + e.getMessage());
            notification("An error occurred while loading Add New Maintenance Request Form. Please try again or contact support.");
        }

    }


    @FXML
    void addOnAction(ActionEvent event) {

        if(selectedRequest!=null){

            String desc = descriptionTxt.getText();
            String assignedTechnician = technicianCmb.getSelectionModel().getSelectedItem();
            String estimatedCost = estimatedCostTxt.getText();
            String actualCost = "0";
            if(estimatedCost.equals("0.0") || estimatedCost.equals("0")){
                actualCost = estimatedCost;
            }
            else{
                actualCost = "-";
            }

            MaintenanceRequestDto maintenanceRequest = new MaintenanceRequestDto();
            maintenanceRequest.setMaintenanceRequestNo(selectedRequest.getMaintenanceRequestNo());
            maintenanceRequest.setDescription(desc);
            maintenanceRequest.setAssignedTechnician(assignedTechnician);
            maintenanceRequest.setEstimatedCost(Double.parseDouble(estimatedCost));
            maintenanceRequest.setActualCost(actualCost);

            if(desc.isEmpty() || estimatedCost.isEmpty() || assignedTechnician==null || assignedTechnician.equals("Select")){

                notification("No Field Can Be Empty");
                return;
            }
            boolean b1 = UserInputValidation.checkDecimalValidation(estimatedCost);
            if(!b1){
                estimatedCostErrorMsgLabel.setText("This not valid input for this field");
                return;
            }
            else{
                estimatedCostErrorMsgLabel.setText("");
            }

            String response = null;
            try {
                response = maintenanceRequestModel.updateMaintenanceRequest(maintenanceRequest);
                notification(response);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while updating the maintenance request No: "+maintenanceRequest.getMaintenanceRequestNo() + e.getMessage());
                notification("An error occurred while updating the maintenance request No: "+maintenanceRequest.getMaintenanceRequestNo()+", Please try again or contact support.");
            }
        }


        else {
            MaintenanceRequestDto maintenanceRequest = new MaintenanceRequestDto();
            maintenanceRequest.setMaintenanceRequestNo(requestIdLabel.getText());
            maintenanceRequest.setTenantId(tenant.getTenantId());
            maintenanceRequest.setDate(String.valueOf(LocalDate.now()));
            maintenanceRequest.setStatus("In Progress");

            String description = descriptionTxt.getText();
            String estimatedCost = estimatedCostTxt.getText();
            String actualCost = "0";
            if(estimatedCost.equals("0.0") || estimatedCost.equals("0")){
                actualCost = estimatedCost;
            }
            else{
                actualCost = "-";
            }

            String technician = technicianCmb.getSelectionModel().getSelectedItem();

            if (description.isEmpty() || estimatedCost.isEmpty() || technician == null || technician.equals("Select")) {

                notification("Please Fill All The Field To Add New Maintenance Request");
                return;
            }

            boolean b1 = UserInputValidation.checkDecimalValidation(estimatedCost);
            if (!b1) {
                estimatedCostErrorMsgLabel.setText("This not valid input for this field");
                return;
            } else {
                estimatedCostErrorMsgLabel.setText("");
            }

            maintenanceRequest.setDescription(description);
            maintenanceRequest.setAssignedTechnician(technician);
            maintenanceRequest.setEstimatedCost(Double.parseDouble(estimatedCost));
            maintenanceRequest.setActualCost(actualCost);

            try {
                String response = maintenanceRequestModel.addNewMaintenanceRequest(maintenanceRequest);
                notification(response);

                if(response.equals("Successfully Added New Maintenance Request By Tenant ID: "+maintenanceRequest.getTenantId())){
                    setNewId();
                }

                clean();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while updating the maintenance request No: "+maintenanceRequest.getMaintenanceRequestNo() + e.getMessage());
                notification("An error occurred while updating the maintenance request No: "+maintenanceRequest.getMaintenanceRequestNo()+", Please try again or contact support.");
            }
        }
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
                    notification("Please Enter Correct Tenant ID");
                    return;
                }

                houseIdLabel.setText(tenant.getHouseId());
                UnitDto unit = unitModel.getHouseDetailsByHouseId(tenant.getHouseId());
                houseTypeLabel.setText(unit.getHouseType());
            }
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error while searching tenant id: "+tenantId+" details" + e.getMessage());
                notification("An error occurred while searching tenant id: "+tenantId+" details: , Please try again or contact support.");
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while generating new maintenance request id "+ e.getMessage());
            notification("An error occurred while generating new maintenance request id, Please try again or contact support.");
        }
    }


    public void getAllTechnician(){

        try {
            ObservableList<String> technicians = employeeModel.getTechnicians();
            technicianCmb.setItems(technicians);
            technicianCmb.getSelectionModel().selectFirst();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while getting the all technicians "+ e.getMessage());
            notification("An error occurred while getting the all technicians, Please try again or contact support.");
        }
    }


    public void clean(){

        tenantIdTxt.clear();
        houseIdLabel.setText("");
        houseTypeLabel.setText("");
        descriptionTxt.clear();
        estimatedCostTxt.clear();
        estimatedCostErrorMsgLabel.setText("");
        getAllTechnician();

    }

    public void setSelectedRequestDetailsToUpdate(MaintenanceRequestTm request) {

        selectedRequest = request;

        addBtn.setText("Update");
        requestIdLabel.setText(selectedRequest.getMaintenanceRequestNo());
        tenantIdTxt.setText(selectedRequest.getTenantId());
        descriptionTxt.setText(selectedRequest.getDescription());
        estimatedCostTxt.setText(String.valueOf(selectedRequest.getEstimatedCost()));
        technicianCmb.setValue(selectedRequest.getAssignedTechnician());

        try {
            TenantDto tenantDto = tenantModel.getMoreTenantDetails(selectedRequest.getTenantId());
            houseIdLabel.setText(tenantDto.getHouseId());

            UnitDto unit = unitModel.getHouseDetailsByHouseId(tenantDto.getHouseId());
            houseTypeLabel.setText(unit.getHouseType());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while getting tenant/house details "+ e.getMessage());
            notification("An error occurred getting tenant/house details, Please try again or contact support.");
        }
        tenantIdTxt.setDisable(true);
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







