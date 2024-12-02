package com.example.test.controller;

import com.example.test.SendMail;
import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.MaintenanceRequestTm;
import com.example.test.dto.tm.PaymentTm;
import com.example.test.dto.tm.TenantTm;
import com.example.test.model.MaintenanceRequestModel;
import com.example.test.model.TenantModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaintenanceRequestController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<MaintenanceRequestTm> table;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> requestNoColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> descriptionColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, Double> estimatedCostColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> actualCostColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> dateColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> technicianColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> tenantIdColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> statusColumn;

    @FXML
    private TableColumn<MaintenanceRequestTm, String> actionColumn;

    @FXML
    private Button addNewMaintanceRequestBtn;

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
    private ComboBox<String> requestNoCmb;

    @FXML
    private ComboBox<String> statusCmb;

    @FXML
    private ComboBox<String> tenantIdCmb;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField searchTxt;


    private boolean isOnlyInProgressRequest = false;
    private ObservableList<MaintenanceRequestTm> tableData;
    private final MaintenanceRequestModel maintenanceRequestModel = new MaintenanceRequestModel();
    private final TenantModel tenantModel = new TenantModel();


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
            System.err.println("Error while loading the Add New Maintenance Request Form: " + e.getMessage());
            notification("An error occurred while loading the Add New Maintenance Request Form. Please try again or contact support.");
        }
    }

    @FXML
    void deleteOnAction(ActionEvent event) {

        MaintenanceRequestTm selectedRequest = table.getSelectionModel().getSelectedItem();

        if(selectedRequest==null){
           return;
        }

        if(selectedRequest.getStatus().equals("Completed") || selectedRequest.getStatus().equals("Rejected")){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Please Confirm First");
            alert.setContentText("Are you sure you want to delete this (Completed,Rejected) Maintenance Request?");

            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonCancel = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(buttonYes, buttonCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonYes) {

                try {
                   String response =  maintenanceRequestModel.deActiveSelectedMaintenanceRequest(selectedRequest);
                   notification(response);
                   loadTable();

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.err.println("Error while deleting the maintenance request: " + e.getMessage());
                    notification("An error occurred while deleting the maintenance request id: "+selectedRequest.getMaintenanceRequestNo()+", Please try again or contact support.");
                }

            } else {
                table.getSelectionModel().clearSelection();
            }
        }
    }


    @FXML
    void editOnAction(ActionEvent event) {

        MaintenanceRequestTm selectedRequest = table.getSelectionModel().getSelectedItem();

        if(selectedRequest==null){
            return;
        }

        if(selectedRequest.getStatus().equals("In Progress") && selectedRequest.getActualCost().equals("-")) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewMaintenanceRequest.fxml"));
                Parent root = fxmlLoader.load();
                AddNewMaintenanceRequestController addNewMaintenanceRequestController = fxmlLoader.getController();
                addNewMaintenanceRequestController.setSelectedRequestDetailsToUpdate(selectedRequest);
                Scene scene = new Scene(root);
                Stage s1 = new Stage();
                s1.setScene(scene);
                s1.show();
            }
            catch (IOException e){
                e.printStackTrace();
                System.err.println("Error while loading the Add New Maintenance Request Form: " + e.getMessage());
                notification("An error occurred while loading the Add New Maintenance Request Form. Please try again or contact support.");
            }
        }
    }

    @FXML
    void getSelectedRow(MouseEvent event) {//no need

    }

    @FXML
    void refreshOnAction(ActionEvent event) {

        if(!isOnlyInProgressRequest) {
            clean();
        }
        else{
            setOnlyProgressRequest();
        }
    }

    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<MaintenanceRequestTm> searchedRequests = FXCollections.observableArrayList();

        String selectedRequestNo = requestNoCmb.getValue();
        String selectedStatus = statusCmb.getValue();
        String selectedTenantId = tenantIdCmb.getValue();
        String selectedDate = String.valueOf(datePicker.getValue());

        boolean requestNoSelected = selectedRequestNo != null && !selectedRequestNo.equals("Select");
        boolean statusSelected = selectedStatus != null && !selectedStatus.equals("Select");
        boolean tenantIdSelected = selectedTenantId != null && !selectedTenantId.equals("Select");
        boolean dateSelected = selectedDate != null && !selectedDate.equals("1990-10-10");


        if (requestNoSelected) {
            ObservableList<MaintenanceRequestTm> requestsByRequestNo = getRequestByRequestNo(selectedRequestNo);

            if (requestsByRequestNo.isEmpty()) {
                table.setItems(requestsByRequestNo);
            } else {
                searchedRequests.addAll(requestsByRequestNo);

                if (statusSelected) {
                    ObservableList<MaintenanceRequestTm> filteredByStatus = filterRequestsByStatus(searchedRequests, selectedStatus);
                    searchedRequests.clear();
                    searchedRequests.addAll(filteredByStatus);
                }

                if (tenantIdSelected) {
                    ObservableList<MaintenanceRequestTm> filteredByTenantId = filterRequestsByTenantId(searchedRequests, selectedTenantId);
                    searchedRequests.clear();
                    searchedRequests.addAll(filteredByTenantId);
                }

                if (dateSelected) {
                    ObservableList<MaintenanceRequestTm> filteredByDate = filterRequestsByDate(searchedRequests, selectedDate);
                    searchedRequests.clear();
                    searchedRequests.addAll(filteredByDate);
                }

                table.setItems(searchedRequests);
            }

        } else if (statusSelected || tenantIdSelected || dateSelected) {
            ObservableList<MaintenanceRequestTm> allRequests = tableData;
            searchedRequests.addAll(allRequests);

            if (statusSelected) {
                searchedRequests = filterRequestsByStatus(searchedRequests, selectedStatus);
            }

            if (tenantIdSelected) {
                searchedRequests = filterRequestsByTenantId(searchedRequests, selectedTenantId);
            }

            if (dateSelected) {
                searchedRequests = filterRequestsByDate(searchedRequests, selectedDate);
            }

            table.setItems(searchedRequests);

        } else {
            ObservableList<MaintenanceRequestTm> allRequests = tableData;
            table.setItems(allRequests);
        }
    }



    private ObservableList<MaintenanceRequestTm> getRequestByRequestNo(String requestNo) {
        return FXCollections.observableArrayList(
                tableData.stream()
                        .filter(request -> request.getMaintenanceRequestNo().equalsIgnoreCase(requestNo))
                        .toList()
        );
    }


    private ObservableList<MaintenanceRequestTm> filterRequestsByStatus(ObservableList<MaintenanceRequestTm> requests, String status) {
        return FXCollections.observableArrayList(
                requests.stream()
                        .filter(request -> request.getStatus().equalsIgnoreCase(status))
                        .toList()
        );
    }


    private ObservableList<MaintenanceRequestTm> filterRequestsByTenantId(ObservableList<MaintenanceRequestTm> requests, String tenantId) {
        return FXCollections.observableArrayList(
                requests.stream()
                        .filter(request -> request.getTenantId().equalsIgnoreCase(tenantId))
                        .toList()
        );
    }


    private ObservableList<MaintenanceRequestTm> filterRequestsByDate(ObservableList<MaintenanceRequestTm> requests, String date) {
        return FXCollections.observableArrayList(
                requests.stream()
                        .filter(request -> request.getDate().toString().equalsIgnoreCase(date))
                        .toList()
        );
    }


    @FXML
    void sortCmbOnAction(ActionEvent event) {


        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<MaintenanceRequestTm> maintenanceRequestTms = FXCollections.observableArrayList(tableData);

        if (sortType == null) {
            return;
        }

        Comparator<MaintenanceRequestTm> comparator = null;

        switch (sortType) {
            case "Maintenance Request ID (Ascending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getMaintenanceRequestNo);
                break;

            case "Maintenance Request ID (Descending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getMaintenanceRequestNo).reversed();
                break;

            case "Estimated Cost (Ascending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getEstimatedCost);
                break;

            case "Estimated Cost (Descending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getEstimatedCost).reversed();
                break;

            case "Actual Cost (Ascending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getActualCost);
                break;

            case "Actual Cost (Descending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getActualCost).reversed();
                break;

            case "Date (Ascending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getDate);
                break;

            case "Date (Descending)":
                comparator = Comparator.comparing(MaintenanceRequestTm::getDate).reversed();
                break;

            default:
                break;
        }

        if (comparator != null) {
            FXCollections.sort(maintenanceRequestTms, comparator);
            table.setItems(maintenanceRequestTms);
        }
    }



    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<MaintenanceRequestTm> maintenanceRequestTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            maintenanceRequestTms.add(tableData.get(i));
        }
        table.setItems(maintenanceRequestTms);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumns();
        setTableColumnsValue();
        setRowCmbValues();
        setRequestNoCmbValues();
        setStatusCmbValues();
        setTenantIdCmbValues();
        setSortCmbValues();
        datePicker.setValue(LocalDate.parse("1990-10-10"));
        tableSearch();
    }



    public void tableSearch() {

        FilteredList<MaintenanceRequestTm> filteredData = new FilteredList<>(tableData, b -> true);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(request -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (request.getMaintenanceRequestNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (request.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(request.getEstimatedCost()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(request.getActualCost()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(request.getDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (request.getAssignedTechnician().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (request.getTenantId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (request.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<MaintenanceRequestTm> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }




    public void setTenantIdCmbValues(){

        try {
            ObservableList<String> tenantIds = maintenanceRequestModel.getDistinctTenantIds();
            tenantIdCmb.setItems(tenantIds);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting values to tenant id combo box: " + e.getMessage());
            notification("An error occurred while setting values to tenant id combo box. Please try again or contact support.");
        }

    }



    public  void setStatusCmbValues(){

        ObservableList<String> status = FXCollections.observableArrayList("Select","In Progress","Completed");
        statusCmb.setItems(status);
        statusCmb.getSelectionModel().selectFirst();

    }


    public void setRequestNoCmbValues(){

        ObservableList<String> requestNos = FXCollections.observableArrayList();
        requestNos.add("Select");

        for (MaintenanceRequestTm x : tableData){
            requestNos.add(x.getMaintenanceRequestNo());
        }

        requestNoCmb.setItems(requestNos);
        requestNoCmb.getSelectionModel().selectFirst();

    }



    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (MaintenanceRequestTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }


    public void setSortCmbValues(){

        ObservableList<String> sortTypes = FXCollections.observableArrayList("Sort By","Maintenance Request ID (Ascending)","Maintenance Request ID (Descending)","Estimated Cost (Ascending)","Estimated Cost (Descending)","Actual Cost (Ascending)","Actual Cost (Descending)","Date (Ascending)","Date (Descending)");
        sortCmb.setItems(sortTypes);
        sortCmb.getSelectionModel().selectFirst();

    }


    public void setTableColumns(){

        requestNoColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceRequestNo"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        estimatedCostColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));
        actualCostColumn.setCellValueFactory(new PropertyValueFactory<>("actualCost"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        technicianColumn.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    }


    public void setTableColumnsValue(){

        Callback<TableColumn<MaintenanceRequestTm, String>, TableCell<MaintenanceRequestTm, String>> cellFoctory = (TableColumn<MaintenanceRequestTm, String> param) -> {

            final TableCell<MaintenanceRequestTm, String> cell = new TableCell<MaintenanceRequestTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\ethics.png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\file.png");

                        ImageView makeComplete = new ImageView();
                        makeComplete.setImage(image1);
                        makeComplete.setFitHeight(19);
                        makeComplete.setFitWidth(19);

                        ImageView makeRejected = new ImageView();
                        makeRejected.setImage(image2);
                        makeRejected.setFitHeight(19);
                        makeRejected.setFitWidth(19);

                        makeComplete.setStyle(" -fx-cursor: hand ;");
                        makeRejected.setStyle(" -fx-cursor: hand ;");

                        makeComplete.setOnMouseClicked((MouseEvent event) -> {

                            MaintenanceRequestTm selectedRequest = table.getSelectionModel().getSelectedItem();

                            if(selectedRequest.getStatus().equals("In Progress") && !selectedRequest.getActualCost().equals("-")){

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText("Please Confirm First");
                                alert.setContentText("Are you sure you want to Complete selected Maintenance request ?");

                                ButtonType buttonYes = new ButtonType("Yes");
                                ButtonType buttonCancel = new ButtonType("Cancel");

                                alert.getButtonTypes().setAll(buttonYes, buttonCancel);

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == buttonYes) {
                                    try {
                                        String response = maintenanceRequestModel.setStatusComplete(selectedRequest);
                                        notification(response);
                                        loadTable();

                                        notification("Sent Email To Tenant ID: "+selectedRequest.getTenantId()+" , upon completing maintenance request");

                                        String email = tenantModel.getTenantEmailById(selectedRequest.getTenantId());
                                        SendMail sendMail = new SendMail();
                                        new Thread(() -> sendMail.sendMail(email,"Completing Maintenance requirement: "+selectedRequest.getDescription(),"Happy to say that Your Maintenance Request successfully completed,\nHope you find it helpful,\nThank You!\n\n\nThe Grand View Residences\nColombo 08")).start();

                                    } catch (SQLException | ClassNotFoundException e) {
                                        e.printStackTrace();
                                        System.err.println("Error while completing the maintenance request: " + e.getMessage());
                                        notification("An error occurred while completing the maintenance request. Please try again or contact support.");
                                    }
                                }
                                else{
                                    table.getSelectionModel().clearSelection();
                                }
                            }

                        });

                        makeRejected.setOnMouseClicked((MouseEvent event) -> {

                            MaintenanceRequestTm selectedRequest = table.getSelectionModel().getSelectedItem();

                            if(selectedRequest.getStatus().equals("In Progress") && selectedRequest.getActualCost().equals("-")) {

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText("Please Confirm First");
                                alert.setContentText("Are you sure you want to reject selected Maintenance request ?");

                                ButtonType buttonYes = new ButtonType("Yes");
                                ButtonType buttonCancel = new ButtonType("Cancel");

                                alert.getButtonTypes().setAll(buttonYes, buttonCancel);

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == buttonYes) {

                                    try {
                                        String response = maintenanceRequestModel.makeRequestRejected(selectedRequest);
                                        notification(response);
                                        loadTable();

                                    } catch (SQLException | ClassNotFoundException e) {
                                        e.printStackTrace();
                                        System.err.println("Error while rejecting the maintenance request: " + e.getMessage());
                                        notification("An error occurred while rejecting the maintenance request. Please try again or contact support.");
                                    }
                                } else {
                                    table.getSelectionModel().clearSelection();
                                }
                            }

                        });

                        HBox manageBtn = new HBox(makeComplete,makeRejected);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(makeComplete, new Insets(2, 2, 0, 3));
                        HBox.setMargin(makeRejected, new Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);

                        setText(null);

                    }
                }
            };

            return cell;
        };

        actionColumn.setCellFactory(cellFoctory);
        loadTable();


    }


    public void loadTable() {

        try {
            tableData = maintenanceRequestModel.getAllRequests();
            table.setItems(tableData);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading the table data: " + e.getMessage());
            notification("An error occurred while loading the table data. Please try again or contact support.");
        }
    }


    public void clean(){

        loadTable();
        setRowCmbValues();
        setRequestNoCmbValues();
        setTenantIdCmbValues();
        setSortCmbValues();
        datePicker.setValue(LocalDate.parse("1990-10-10"));
        statusCmb.getSelectionModel().selectFirst();
        searchTxt.setText("");
    }


    public void setOnlyProgressRequest() {

        isOnlyInProgressRequest = true;

        try {
            tableData = maintenanceRequestModel.getAllInProgressRequests();
            table.setItems(tableData);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading the table data: " + e.getMessage());
            notification("An error occurred while loading the table data. Please try again or contact support.");
        }

        setRowCmbValues();
        setRequestNoCmbValues();
        setTenantIdCmbValues();
        setSortCmbValues();
        datePicker.setValue(LocalDate.now());
        statusCmb.getSelectionModel().selectFirst();
        searchTxt.setText("");
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




