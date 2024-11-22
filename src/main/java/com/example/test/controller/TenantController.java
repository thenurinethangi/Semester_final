package com.example.test.controller;

import com.example.test.dto.tm.CustomerTm;
import com.example.test.dto.tm.RequestTm;
import com.example.test.dto.tm.TenantTm;
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
import javafx.scene.input.KeyEvent;
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

public class TenantController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<TenantTm> table;

    @FXML
    private TableColumn<TenantTm, String> tenantIdColumn;

    @FXML
    private TableColumn<TenantTm, String> nameColumn;

    @FXML
    private TableColumn<TenantTm, String> phoneNoColumn;

    @FXML
    private TableColumn<TenantTm, Integer> membersCountColumn;

    @FXML
    private TableColumn<TenantTm, String> startDateColumn;

    @FXML
    private TableColumn<TenantTm, Double> monthlyRentColumn;

    @FXML
    private TableColumn<TenantTm, String> lastPaidMonthColumn;

    @FXML
    private TableColumn<TenantTm, String> houseIdColumn;

    @FXML
    private TableColumn<TenantTm, String> actionColumn;

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
    private ComboBox<String> monthsCmb;

    @FXML
    private TextField nameTxt;

    @FXML
    private ListView<String> nameList;

    @FXML
    private TextField membersCountTxt;

    @FXML
    private ComboBox<String> tenantIdCmb;

    @FXML
    private ComboBox<String> houseIdCmb;

    @FXML
    private TextField searchTxt;

    private final TenantModel tenantModel = new TenantModel();
    private ObservableList<TenantTm> tableData;
    private ObservableList<String> names = FXCollections.observableArrayList();



    @FXML
    void nameTxtKeyReleased(KeyEvent event) {

        String input = nameTxt.getText();


        try {
            names = tenantModel.getNameSuggestions(input);
            nameList.setItems(names);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while getting tenants names suggestions: " + e.getMessage());
            notification("An error occurred while getting tenants names suggestions. Please try again or contact support.");
        }

        if(input.isEmpty()){
            names.clear();
        }

    }

    @FXML
    void nameListOnMouseClicked(MouseEvent event) {

        nameTxt.setText(nameList.getSelectionModel().getSelectedItem());
        nameList.getItems().clear();
    }


    @FXML
    void refreshOnAction(ActionEvent event) {

       clean();
    }


    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<TenantTm> searchedTenants = FXCollections.observableArrayList();

        String selectedTenantId = tenantIdCmb.getValue();
        String selectedName = nameTxt.getText();
        String selectedMembersCount = membersCountTxt.getText();
        String selectedLastPaymentMonth = monthsCmb.getValue();
        String selectedHouseId = houseIdCmb.getValue();

        boolean tenantIdSelected = selectedTenantId != null && !selectedTenantId.equals("Select");
        boolean nameSelected = selectedName != null && !selectedName.isEmpty();
        boolean membersCountSelected = selectedMembersCount != null && !selectedMembersCount.isEmpty();
        boolean lastPaymentMonthSelected = selectedLastPaymentMonth != null && !selectedLastPaymentMonth.equals("Select");
        boolean houseIdSelected = selectedHouseId != null && !selectedHouseId.equals("Select");


        if (tenantIdSelected) {
            ObservableList<TenantTm> tenantsById = getTenantById(selectedTenantId);

            if (tenantsById.isEmpty()) {
                table.setItems(tenantsById);
            } else {
                searchedTenants.addAll(tenantsById);

                if (nameSelected) {
                    ObservableList<TenantTm> filteredByName = filterTenantsByName(searchedTenants, selectedName);
                    searchedTenants.clear();
                    searchedTenants.addAll(filteredByName);
                }

                if (membersCountSelected) {
                    ObservableList<TenantTm> filteredByMembersCount = filterTenantsByMembersCount(searchedTenants, selectedMembersCount);
                    searchedTenants.clear();
                    searchedTenants.addAll(filteredByMembersCount);
                }

                if (lastPaymentMonthSelected) {
                    ObservableList<TenantTm> filteredByLastPaymentMonth = filterTenantsByLastPaymentMonth(searchedTenants, selectedLastPaymentMonth);
                    searchedTenants.clear();
                    searchedTenants.addAll(filteredByLastPaymentMonth);
                }

                if (houseIdSelected) {
                    ObservableList<TenantTm> filteredByHouseId = filterTenantsByHouseId(searchedTenants, selectedHouseId);
                    searchedTenants.clear();
                    searchedTenants.addAll(filteredByHouseId);
                }

                table.setItems(searchedTenants);
            }


        } else if (nameSelected || membersCountSelected || lastPaymentMonthSelected || houseIdSelected) {
            ObservableList<TenantTm> allTenants = tableData;
            searchedTenants.addAll(allTenants);

            if (nameSelected) {
                searchedTenants = filterTenantsByName(searchedTenants, selectedName);
            }

            if (membersCountSelected) {
                searchedTenants = filterTenantsByMembersCount(searchedTenants, selectedMembersCount);
            }

            if (lastPaymentMonthSelected) {
                searchedTenants = filterTenantsByLastPaymentMonth(searchedTenants, selectedLastPaymentMonth);
            }

            if (houseIdSelected) {
                searchedTenants = filterTenantsByHouseId(searchedTenants, selectedHouseId);
            }

            table.setItems(searchedTenants);

        } else {
            ObservableList<TenantTm> allTenants = tableData;
            table.setItems(allTenants);
        }
    }


    private ObservableList<TenantTm> getTenantById(String tenantId) {
        return FXCollections.observableArrayList(
                tableData.stream()
                        .filter(tenant -> tenant.getTenantId().equalsIgnoreCase(tenantId))
                        .toList()
        );
    }


    private ObservableList<TenantTm> filterTenantsByName(ObservableList<TenantTm> tenants, String name) {
        return FXCollections.observableArrayList(
                tenants.stream()
                        .filter(tenant -> tenant.getName().toLowerCase().contains(name.toLowerCase()))
                        .toList()
        );
    }

    private ObservableList<TenantTm> filterTenantsByMembersCount(ObservableList<TenantTm> tenants, String membersCount) {
        int count = Integer.parseInt(membersCount);
        return FXCollections.observableArrayList(
                tenants.stream()
                        .filter(tenant -> tenant.getMembersCount() == count)
                        .toList()
        );
    }

    private ObservableList<TenantTm> filterTenantsByLastPaymentMonth(ObservableList<TenantTm> tenants, String lastPaymentMonth) {
        return FXCollections.observableArrayList(
                tenants.stream()
                        .filter(tenant -> tenant.getLastPaidMonth().equalsIgnoreCase(lastPaymentMonth))
                        .toList()
        );
    }

    private ObservableList<TenantTm> filterTenantsByHouseId(ObservableList<TenantTm> tenants, String houseId) {
        return FXCollections.observableArrayList(
                tenants.stream()
                        .filter(tenant -> tenant.getHouseId().equalsIgnoreCase(houseId))
                        .toList()
        );
    }



    @FXML
    void sortCmbOnAction(ActionEvent event) {

        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<TenantTm> tenantTms = FXCollections.observableArrayList(tableData);

        if (sortType == null) {
            return;
        }

        Comparator<TenantTm> comparator = null;

        switch (sortType) {
            case "Tenant ID (Ascending)":
                comparator = Comparator.comparing(TenantTm::getTenantId);
                break;

            case "Tenant ID (Descending)":
                comparator = Comparator.comparing(TenantTm::getTenantId).reversed();
                break;

            case "Tenant Name (Ascending)":
                comparator = Comparator.comparing(TenantTm::getName);
                break;

            case "Tenant Name (Descending)":
                comparator = Comparator.comparing(TenantTm::getName).reversed();
                break;

            case "Resident Count (Ascending)":
                comparator = Comparator.comparing(TenantTm::getMembersCount);
                break;

            case "Resident Count (Descending)":
                comparator = Comparator.comparing(TenantTm::getMembersCount).reversed();
                break;

            case "Rent Start Date (Ascending)":
                comparator = Comparator.comparing(TenantTm::getRentStartDate);
                break;

            case "Rent Start Date (Descending)":
                comparator = Comparator.comparing(TenantTm::getRentStartDate).reversed();
                break;

            case "Rent For Month (Ascending)":
                comparator = Comparator.comparing(TenantTm::getMonthlyRent);
                break;

            case "Rent For Month (Descending)":
                comparator = Comparator.comparing(TenantTm::getMonthlyRent).reversed();
                break;

            default:
                break;
        }

        if (comparator != null) {
            FXCollections.sort(tenantTms, comparator);
            table.setItems(tenantTms);
        }

    }


    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<TenantTm> tenantTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            tenantTms.add(tableData.get(i));
        }

        table.setItems(tenantTms);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumnsValue();
        setRowCmbValues();
        setTenantIdCmbValues();
        setMonthsCmbValues();
        setHouseIdCmbValues();
        setSortCmbValues();

        tableSearch();

    }


    public void tableSearch() {

        FilteredList<TenantTm> filteredData = new FilteredList<>(tableData, b -> true);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tenant -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();


                if (tenant.getTenantId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tenant.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tenant.getPhoneNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(tenant.getMembersCount()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(tenant.getRentStartDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(tenant.getMonthlyRent()).contains(lowerCaseFilter)) {
                    return true;
                } else if (tenant.getLastPaidMonth().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tenant.getHouseId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<TenantTm> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }


    public void setSortCmbValues(){

        ObservableList<String> sortTypes = FXCollections.observableArrayList("Sort By","Tenant ID (Ascending)","Tenant ID (Descending)","Tenant Name (Ascending)","Tenant Name (Descending)","Resident Count (Ascending)","Resident Count (Descending)","Rent Start Date (Ascending)","Rent Start Date (Descending)","Rent For Month (Ascending)","Rent For Month (Descending)");
        sortCmb.setItems(sortTypes);
        sortCmb.getSelectionModel().selectFirst();

    }


    public void setHouseIdCmbValues(){

        try {
            ObservableList<String> houseIds = tenantModel.getHouseIds();
            houseIdCmb.setItems(houseIds);
            houseIdCmb.getSelectionModel().selectFirst();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting the house id combo box values: " + e.getMessage());
            notification("An error occurred while setting the house id combo box values. Please try again or contact support.");
        }

    }


    public void setMonthsCmbValues(){

        ObservableList<String> months = FXCollections.observableArrayList( "Select","January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");

        monthsCmb.setItems(months);
        monthsCmb.getSelectionModel().selectFirst();

    }

    public void setTenantIdCmbValues(){

        ObservableList<String> tenantIds = FXCollections.observableArrayList();
        tenantIds.add("Select");

        for(TenantTm x : tableData){
          tenantIds.add(x.getTenantId());
        }

        tenantIdCmb.setItems(tenantIds);
        tenantIdCmb.getSelectionModel().selectFirst();

    }

    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (TenantTm x : tableData){
            count++;
            rows.add(count);

        }
        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }

    public void setTableColumnsValue(){

        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNoColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        membersCountColumn.setCellValueFactory(new PropertyValueFactory<>("membersCount"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentStartDate"));
        monthlyRentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyRent"));
        lastPaidMonthColumn.setCellValueFactory(new PropertyValueFactory<>("lastPaidMonth"));
        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));


        Callback<TableColumn<TenantTm, String>, TableCell<TenantTm, String>> cellFoctory = (TableColumn<TenantTm, String> param) -> {

            final TableCell<TenantTm, String> cell = new TableCell<TenantTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\visibility.png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\editText.png");
                        Image image3 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\accounting (3).png");

                        ImageView viewDetails = new ImageView();
                        viewDetails.setImage(image1);
                        viewDetails.setFitHeight(20);
                        viewDetails.setFitWidth(20);

                        ImageView edit = new ImageView();
                        edit.setImage(image2);
                        edit.setFitHeight(20);
                        edit.setFitWidth(20);

                        ImageView paymentHistory = new ImageView();
                        paymentHistory.setImage(image3);
                        paymentHistory.setFitHeight(20);
                        paymentHistory.setFitWidth(20);


                        viewDetails.setStyle(" -fx-cursor: hand ;");
                        edit.setStyle(" -fx-cursor: hand ;");
                        paymentHistory.setStyle(" -fx-cursor: hand ;");


                        viewDetails.setOnMouseClicked((MouseEvent event) -> {

                            TenantTm SelectedTenant = table.getSelectionModel().getSelectedItem();

                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TenantDetails.fxml"));
                                Parent root = fxmlLoader.load();
                                TenantDetailsController tenantDetailsController = fxmlLoader.getController();
                                tenantDetailsController.setSelectedTenantDetails(SelectedTenant);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                                System.err.println("Error while loading the details of tenant id: "+SelectedTenant.getTenantId()+ " + e.getMessage()");
                                notification("An error occurred while loading the details of tenant id: "+SelectedTenant.getTenantId()+", Please try again or contact support.");
                            }

                        });

                        edit.setOnMouseClicked((MouseEvent event) -> {

                            TenantTm SelectedTenant = table.getSelectionModel().getSelectedItem();

                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/EditTenant.fxml"));
                                Parent root = fxmlLoader.load();
                                TenantEditController tenantEditController = fxmlLoader.getController();
                                tenantEditController.setSelectedTenantDetails(SelectedTenant);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                                System.err.println("Error while loading the Edit Tenant page: " + e.getMessage());
                                notification("An error occurred while loading the Edit Tenant page. Please try again or contact support.");
                            }


                        });

                        paymentHistory.setOnMouseClicked((MouseEvent event) -> {

                            System.out.println("hi thenuri");

                        });


                        HBox manageBtn = new HBox(viewDetails,edit,paymentHistory);


                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(viewDetails, new Insets(2, 2, 0, 3));
                        HBox.setMargin(edit, new Insets(2, 3, 0, 3));
                        HBox.setMargin(paymentHistory, new Insets(2, 3, 0, 3));
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
            tableData = tenantModel.getAllTenants();
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
        setTenantIdCmbValues();
        setHouseIdCmbValues();
        monthsCmb.getSelectionModel().selectFirst();
        sortCmb.getSelectionModel().selectFirst();
        nameTxt.setText("");
        membersCountTxt.setText("");
        searchTxt.clear();
        table.getSelectionModel().clearSelection();

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




