package com.example.test.controller;

import com.example.test.dto.tm.MaintenanceRequestTm;
import com.example.test.dto.tm.PaymentTm;
import com.example.test.model.MaintenanceRequestModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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


    private ObservableList<MaintenanceRequestTm> tableData;
    private final MaintenanceRequestModel maintenanceRequestModel = new MaintenanceRequestModel();


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
    void getSelectedRow(MouseEvent event) {//no need

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
        datePicker.setValue(LocalDate.now());
    }


    public void setTenantIdCmbValues(){

        try {
            ObservableList<String> tenantIds = maintenanceRequestModel.getDistinctTenantIds();
            tenantIdCmb.setItems(tenantIds);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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

                        ImageView makeComplete = new ImageView();
                        makeComplete.setImage(image1);
                        makeComplete.setFitHeight(19);
                        makeComplete.setFitWidth(19);


                        makeComplete.setStyle(" -fx-cursor: hand ;");

                        makeComplete.setOnMouseClicked((MouseEvent event) -> {

                            MaintenanceRequestTm selectedRequest = table.getSelectionModel().getSelectedItem();

                            if(selectedRequest.getStatus().equals("In Progress") && !selectedRequest.getActualCost().equals("-")){

                                try {
                                    String response = maintenanceRequestModel.setStatusComplete(selectedRequest);

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
                            }

                        });

                        HBox manageBtn = new HBox(makeComplete);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(makeComplete, new Insets(2, 2, 0, 3));
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void clean(){

        loadTable();
        setRowCmbValues();
        setRequestNoCmbValues();
        setTenantIdCmbValues();
        datePicker.setValue(LocalDate.now());
        statusCmb.getSelectionModel().selectFirst();
        searchTxt.setText("");
    }
}




