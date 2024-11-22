package com.example.test.controller;

import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.dto.tm.OwnerTm;
import com.example.test.dto.tm.PurchaseAgreementTm;
import com.example.test.dto.tm.TenantTm;
import com.example.test.model.PurchaseAgreementModel;
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
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

public class PurchaseAgreementController implements Initializable {

    @FXML
    private TableView<PurchaseAgreementTm> table;

    @FXML
    private TableColumn<PurchaseAgreementTm, String> agreementColumn;

    @FXML
    private TableColumn<PurchaseAgreementTm, String> ownerIdColumn;

    @FXML
    private TableColumn<PurchaseAgreementTm, String> houseIdColumn;

    @FXML
    private TableColumn<PurchaseAgreementTm, Double> purchasePriceColumn;

    @FXML
    private TableColumn<PurchaseAgreementTm, String> signedDateColumn;

    @FXML
    private TableColumn<PurchaseAgreementTm, String> statusColumn;

    @FXML
    private TableColumn<PurchaseAgreementTm, String> actionColumn;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private ComboBox<String> sortCmb;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private ComboBox<String> houseIdCmb;

    @FXML
    private ComboBox<String> agreementIdCmb;

    @FXML
    private ComboBox<String> ownerIdCmb;

    @FXML
    private TextField searchTxt;


    private final PurchaseAgreementModel purchaseAgreementModel = new PurchaseAgreementModel();
    private ObservableList<PurchaseAgreementTm> tableData;



    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();
    }

    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<PurchaseAgreementTm> searchedAgreements = FXCollections.observableArrayList();

        String selectedAgreementId = agreementIdCmb.getValue();
        String selectedOwnerId = ownerIdCmb.getValue();
        String selectedHouseId = houseIdCmb.getValue();

        boolean agreementIdSelected = selectedAgreementId != null && !selectedAgreementId.equals("Select");
        boolean ownerIdSelected = selectedOwnerId != null && !selectedOwnerId.equals("Select");
        boolean houseIdSelected = selectedHouseId != null && !selectedHouseId.equals("Select");

        if (agreementIdSelected) {

            ObservableList<PurchaseAgreementTm> agreementsById = getAgreementById(selectedAgreementId);

            if (agreementsById.isEmpty()) {
                table.setItems(agreementsById);
            } else {
                searchedAgreements.addAll(agreementsById);

                if (ownerIdSelected) {
                    searchedAgreements = filterAgreementsByOwnerId(searchedAgreements, selectedOwnerId);
                }

                if (houseIdSelected) {
                    searchedAgreements = filterAgreementsByHouseId(searchedAgreements, selectedHouseId);
                }

                table.setItems(searchedAgreements);
            }

        }
        else if (ownerIdSelected || houseIdSelected) {

            ObservableList<PurchaseAgreementTm> allAgreements = tableData;
            searchedAgreements.addAll(allAgreements);

            if (ownerIdSelected) {
                searchedAgreements = filterAgreementsByOwnerId(searchedAgreements, selectedOwnerId);
            }

            if (houseIdSelected) {
                searchedAgreements = filterAgreementsByHouseId(searchedAgreements, selectedHouseId);
            }

            table.setItems(searchedAgreements);

        } else {
            ObservableList<PurchaseAgreementTm> allAgreements = tableData;
            table.setItems(allAgreements);
        }

    }



    public ObservableList<PurchaseAgreementTm> getAgreementById(String agreementId) {

        return FXCollections.observableArrayList(
                tableData.stream()
                        .filter(agreement -> agreement.getPurchaseAgreementId().equalsIgnoreCase(agreementId))
                        .toList()
        );
    }

    public ObservableList<PurchaseAgreementTm> filterAgreementsByOwnerId(ObservableList<PurchaseAgreementTm> agreements, String ownerId) {

        return FXCollections.observableArrayList(
                agreements.stream()
                        .filter(agreement -> agreement.getHomeOwnerId().equalsIgnoreCase(ownerId))
                        .toList()
        );
    }

    public ObservableList<PurchaseAgreementTm> filterAgreementsByHouseId(ObservableList<PurchaseAgreementTm> agreements, String houseId) {

        return FXCollections.observableArrayList(
                agreements.stream()
                        .filter(agreement -> agreement.getHouseId().equalsIgnoreCase(houseId))
                        .toList()
        );
    }



    @FXML
    void sortCmbOnAction(ActionEvent event) {


        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<PurchaseAgreementTm> purchaseAgreementTms = FXCollections.observableArrayList(tableData);

        if (sortType == null) {
            return;
        }

        Comparator<PurchaseAgreementTm> comparator = null;

        switch (sortType) {
            case "Purchase Agreement ID (Ascending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getPurchaseAgreementId);
                break;

            case "Purchase Agreement ID (Descending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getPurchaseAgreementId).reversed();
                break;

            case "Owner ID (Ascending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getHomeOwnerId);
                break;

            case "Owner ID (Descending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getHomeOwnerId).reversed();
                break;

            case "Signed Date (Ascending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getSignedDate);
                break;

            case "Signed Date (Descending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getSignedDate).reversed();
                break;

            case "Purchase Price (Ascending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getPurchasePrice);
                break;

            case "Purchase Price (Descending)":
                comparator = Comparator.comparing(PurchaseAgreementTm::getPurchasePrice).reversed();
                break;

            default:
                break;
        }

        if (comparator != null) {
            FXCollections.sort(purchaseAgreementTms, comparator);
            table.setItems(purchaseAgreementTms);
        }
    }


    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<PurchaseAgreementTm> purchaseAgreementTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            purchaseAgreementTms.add(tableData.get(i));
        }

        table.setItems(purchaseAgreementTms);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumnsValues();
        setTableAction();
        setRowCmbValues();
        setAgreementIdCmbValues();
        setOwnerIdCmbValues();
        setHouseIdCmbValues();
        setSortCmbValues();
        tableSearch();
    }


    public void tableSearch() {

        FilteredList<PurchaseAgreementTm> filteredData = new FilteredList<>(tableData, b -> true);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(purchaseAgreement -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (purchaseAgreement.getPurchaseAgreementId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (purchaseAgreement.getHomeOwnerId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (purchaseAgreement.getHouseId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(purchaseAgreement.getPurchasePrice()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(purchaseAgreement.getSignedDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (purchaseAgreement.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<PurchaseAgreementTm> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }



    public void setSortCmbValues(){

        ObservableList<String> statuses = FXCollections.observableArrayList("Sort By","Purchase Agreement ID (Ascending)","Purchase Agreement ID (Descending)","Owner ID (Ascending)","Owner ID (Descending)","Signed Date (Ascending)","Signed Date (Descending)","Purchase Price (Ascending)","Purchase Price (Descending)");
        sortCmb.setItems(statuses);
        sortCmb.getSelectionModel().selectFirst();

    }


    public void setOwnerIdCmbValues(){

        try {
            ObservableList<String> ownerIds = purchaseAgreementModel.getAllOwnerIds();
            ownerIdCmb.setItems(ownerIds);
            ownerIdCmb.getSelectionModel().selectFirst();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting values to owner id combo box: " + e.getMessage());
            notification("An error occurred setting values to owner id combo box. Please try again or contact support.");
        }

    }


    public void setHouseIdCmbValues(){

        try {
            ObservableList<String> houseIds = purchaseAgreementModel.getAllHouseIds();
            houseIdCmb.setItems(houseIds);
            houseIdCmb.getSelectionModel().selectFirst();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting values to house id combo box: " + e.getMessage());
            notification("An error occurred setting values to house id combo box. Please try again or contact support.");
        }

    }


    public void setAgreementIdCmbValues(){

        ObservableList<String> agreementIds = FXCollections.observableArrayList();
        agreementIds.add("Select");

        for(PurchaseAgreementTm x : tableData){
            agreementIds.add(x.getPurchaseAgreementId());
        }

        agreementIdCmb.setItems(agreementIds);
        agreementIdCmb.getSelectionModel().selectFirst();
    }


    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (PurchaseAgreementTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }

    public void setTableColumnsValues(){

        agreementColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseAgreementId"));
        ownerIdColumn.setCellValueFactory(new PropertyValueFactory<>("homeOwnerId"));
        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        signedDateColumn.setCellValueFactory(new PropertyValueFactory<>("signedDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    }


    public void setTableAction(){

        Callback<TableColumn<PurchaseAgreementTm, String>, TableCell<PurchaseAgreementTm, String>> cellFoctory = (TableColumn<PurchaseAgreementTm, String> param) -> {

            final TableCell<PurchaseAgreementTm, String> cell = new TableCell<PurchaseAgreementTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\visibility.png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\send.png");

                        ImageView viewDetails = new ImageView();
                        viewDetails.setImage(image1);
                        viewDetails.setFitHeight(20);
                        viewDetails.setFitWidth(20);

                        ImageView sendAgreement = new ImageView();
                        sendAgreement.setImage(image2);
                        sendAgreement.setFitHeight(20);
                        sendAgreement.setFitWidth(20);

                        viewDetails.setStyle(" -fx-cursor: hand ;");
                        sendAgreement.setStyle(" -fx-cursor: hand ;");


                        viewDetails.setOnMouseClicked((MouseEvent event) -> {

                            PurchaseAgreementTm selectedAgreement = table.getSelectionModel().getSelectedItem();

                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PurchaseAgreementDetails.fxml"));
                                Parent root = fxmlLoader.load();
                                PurchaseAgreementDetailsController purchaseAgreementDetailsController = fxmlLoader.getController();
                                purchaseAgreementDetailsController.setSelectedAgreementDetails(selectedAgreement);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                                System.err.println("Error while loading the details of purchase id: "+selectedAgreement.getPurchaseAgreementId()+ " + e.getMessage()");
                                notification("An error occurred while loading the details of purchase id: "+selectedAgreement.getPurchaseAgreementId()+", Please try again or contact support.");
                            }

                        });

                        sendAgreement.setOnMouseClicked((MouseEvent event) -> {



                        });

                        HBox manageBtn = new HBox(viewDetails,sendAgreement);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(viewDetails, new Insets(2, 2, 0, 3));
                        HBox.setMargin(sendAgreement, new Insets(2, 3, 0, 3));
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


    public void loadTable(){

        try {
            tableData = purchaseAgreementModel.getAllAgreements();
            table.setItems(tableData);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while loading table data: " + e.getMessage());
            notification("An error occurred while loading table data. Please try again or contact support.");
        }
    }


    public void clean(){

        loadTable();
        setRowCmbValues();
        setAgreementIdCmbValues();
        setOwnerIdCmbValues();
        setHouseIdCmbValues();
        setSortCmbValues();
        table.getSelectionModel().clearSelection();
        searchTxt.clear();

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
