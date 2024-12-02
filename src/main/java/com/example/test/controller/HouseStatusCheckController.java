package com.example.test.controller;

import com.example.test.SendMail;
import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.*;
import com.example.test.model.HouseStatusCheckModel;
import com.example.test.model.PaymentModel;
import com.example.test.model.TenantModel;
import com.example.test.model.UnitModel;
import com.example.test.validation.UserInputValidation;
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
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class HouseStatusCheckController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<HouseStatusCheckTm> table;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> checkNoColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> livingRoomStatusColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> kitchenStatusColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> bedRoomStatusColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> bathRoomStatusColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> totalHouseStatusColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> tenantIdColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> houseIdColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> costColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> isPaymentDoneColumn;

    @FXML
    private TableColumn<HouseStatusCheckTm, String> actionColumn;

    @FXML
    private Button addNewHouseStatusCheckBtn;

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
    private ComboBox<String> houseStatusCmb;

    @FXML
    private ComboBox<String> checkNoCmb;

    @FXML
    private ComboBox<String> paymentDoneCmb;

    @FXML
    private ComboBox<String> houseIdCmb;

    @FXML
    private ComboBox<String> tenantIdCmb;

    @FXML
    private TextField searchTxt;


    private ObservableList<HouseStatusCheckTm> tableData;
    private final HouseStatusCheckModel houseStatusCheckModel = new HouseStatusCheckModel();
    private final PaymentModel paymentModel = new PaymentModel();
    private UnitModel unitModel;
    private final TenantModel tenantModel = new TenantModel();


    public HouseStatusCheckController() {

        try{
            unitModel = new UnitModel();
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting tenant id combo box values: " + e.getMessage());
            notification("An error occurred while setting tenant id combo box values. Please try again or contact support.");
        }
    }



    @FXML
    void addNewHouseStatusCheckOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewHouseInspectCheck.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading Add New House Inspection page: " + e.getMessage());
            notification("An error occurred while loading Add New House Inspection page. Please try again or contact support.");
        }


    }

    @FXML
    void deleteOnAction(ActionEvent event) {

        HouseStatusCheckTm selectedHouseCheck = table.getSelectionModel().getSelectedItem();

        if(selectedHouseCheck==null){
           return;
        }

        if(selectedHouseCheck.getIsPaymentDone().equals("Not Yet")){
            return;
        }

        try {
            boolean isLastCheck = houseStatusCheckModel.checkIfThisCheckLastCheck(selectedHouseCheck);

            if(isLastCheck){
               notification("Can't Delete Selected House Inspection, Because It's The Latest House Inspection Of That House");
            }
            else{
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Please confirm first");
                alert.setContentText("Are you sure, Do you want to delete the selected HouseInspection? ");
                alert.getButtonTypes().setAll(yesButton, cancelButton);
                Optional<ButtonType> options = alert.showAndWait();

                if(options.isPresent() && options.get()==yesButton) {
                    String response = houseStatusCheckModel.deleteSelectedHouseInspection(selectedHouseCheck);
                    notification(response);
                    loadTable();
                }
                else{
                    table.getSelectionModel().clearSelection();
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while checking is selected check last check of the tenant: " + e.getMessage());
            notification("An error occurred while checking is selected check last check of the tenant. Please try again or contact support.");
        }
    }



    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();
    }



    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<HouseStatusCheckTm> searchedChecks = FXCollections.observableArrayList();

        String selectedCheckNumber = checkNoCmb.getValue();
        String selectedTenantId = tenantIdCmb.getValue();
        String selectedHouseId = houseIdCmb.getValue();
        String selectedTotalHouseStatus = houseStatusCmb.getValue();
        String selectedIsPaymentDone = paymentDoneCmb.getValue();

        boolean checkNumberSelected = selectedCheckNumber != null && !selectedCheckNumber.equals("Select");
        boolean tenantIdSelected = selectedTenantId != null && !selectedTenantId.equals("Select");
        boolean houseIdSelected = selectedHouseId != null && !selectedHouseId.equals("Select");
        boolean totalHouseStatusSelected = selectedTotalHouseStatus != null && !selectedTotalHouseStatus.equals("Select");
        boolean isPaymentDoneSelected = selectedIsPaymentDone != null && !selectedIsPaymentDone.equals("Select");


        if (checkNumberSelected) {
            ObservableList<HouseStatusCheckTm> checksByCheckNumber = getChecksByCheckNumber(selectedCheckNumber);

            if (checksByCheckNumber.isEmpty()) {
                table.setItems(checksByCheckNumber);
            } else {
                searchedChecks.addAll(checksByCheckNumber);

                if (tenantIdSelected) {
                    searchedChecks = filterChecksByTenantId(searchedChecks, selectedTenantId);
                }

                if (houseIdSelected) {
                    searchedChecks = filterChecksByHouseId(searchedChecks, selectedHouseId);
                }

                if (totalHouseStatusSelected) {
                    searchedChecks = filterChecksByTotalHouseStatus(searchedChecks, selectedTotalHouseStatus);
                }

                if (isPaymentDoneSelected) {
                    searchedChecks = filterChecksByIsPaymentDone(searchedChecks, selectedIsPaymentDone);
                }

                table.setItems(searchedChecks);
            }

        } else if (tenantIdSelected || houseIdSelected || totalHouseStatusSelected || isPaymentDoneSelected) {
            ObservableList<HouseStatusCheckTm> allChecks = tableData;
            searchedChecks.addAll(allChecks);

            if (tenantIdSelected) {
                searchedChecks = filterChecksByTenantId(searchedChecks, selectedTenantId);
            }

            if (houseIdSelected) {
                searchedChecks = filterChecksByHouseId(searchedChecks, selectedHouseId);
            }

            if (totalHouseStatusSelected) {
                searchedChecks = filterChecksByTotalHouseStatus(searchedChecks, selectedTotalHouseStatus);
            }

            if (isPaymentDoneSelected) {
                searchedChecks = filterChecksByIsPaymentDone(searchedChecks, selectedIsPaymentDone);
            }

            table.setItems(searchedChecks);

        }
        else {
            ObservableList<HouseStatusCheckTm> allChecks = tableData;
            table.setItems(allChecks);
        }
    }



    private ObservableList<HouseStatusCheckTm> getChecksByCheckNumber(String checkNumber) {
        return FXCollections.observableArrayList(
                tableData.stream()
                        .filter(check -> check.getCheckNumber().equalsIgnoreCase(checkNumber))
                        .toList()
        );
    }


    private ObservableList<HouseStatusCheckTm> filterChecksByTenantId(ObservableList<HouseStatusCheckTm> checks, String tenantId) {
        return FXCollections.observableArrayList(
                checks.stream()
                        .filter(check -> check.getTenantId().equalsIgnoreCase(tenantId))
                        .toList()
        );
    }


    private ObservableList<HouseStatusCheckTm> filterChecksByHouseId(ObservableList<HouseStatusCheckTm> checks, String houseId) {
        return FXCollections.observableArrayList(
                checks.stream()
                        .filter(check -> check.getHouseId().equalsIgnoreCase(houseId))
                        .toList()
        );
    }


    private ObservableList<HouseStatusCheckTm> filterChecksByTotalHouseStatus(ObservableList<HouseStatusCheckTm> checks, String totalHouseStatus) {
        return FXCollections.observableArrayList(
                checks.stream()
                        .filter(check -> check.getTotalHouseStatus().equalsIgnoreCase(totalHouseStatus))
                        .toList()
        );
    }


    private ObservableList<HouseStatusCheckTm> filterChecksByIsPaymentDone(ObservableList<HouseStatusCheckTm> checks, String isPaymentDone) {
        return FXCollections.observableArrayList(
                checks.stream()
                        .filter(check -> check.getIsPaymentDone().equalsIgnoreCase(isPaymentDone))
                        .toList()
        );
    }


    @FXML
    void sortCmbOnAction(ActionEvent event) {

        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<HouseStatusCheckTm> houseStatusCheckTms = FXCollections.observableArrayList(tableData);

        if (sortType == null) {
            return;
        }

        Comparator<HouseStatusCheckTm> comparator = null;

        switch (sortType) {
            case "Check No (Ascending)":
                comparator = Comparator.comparing(HouseStatusCheckTm::getCheckNumber);
                break;

            case "Check No (Descending)":
                comparator = Comparator.comparing(HouseStatusCheckTm::getCheckNumber).reversed();
                break;

            case "Tenant ID (Ascending)":
                comparator = Comparator.comparing(HouseStatusCheckTm::getTenantId);
                break;

            case "Tenant ID (Descending)":
                comparator = Comparator.comparing(HouseStatusCheckTm::getTenantId).reversed();
                break;

            case "House ID (Ascending)":
                comparator = Comparator.comparing(HouseStatusCheckTm::getHouseId);
                break;

            case "House ID (Descending)":
                comparator = Comparator.comparing(HouseStatusCheckTm::getHouseId).reversed();
                break;

            default:
                break;
        }

        if (comparator != null) {
            FXCollections.sort(houseStatusCheckTms, comparator);
            table.setItems(houseStatusCheckTms);
        }
    }



    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<HouseStatusCheckTm> houseStatusCheckTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            houseStatusCheckTms.add(tableData.get(i));
        }

        table.setItems(houseStatusCheckTms);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumns();
        setTableColumnsValue();
        setRowCmbValues();
        setTenantIdCmbValues();
        setTenantIdCmbValues();
        setHouseIdCmbValues();
        setCheckNoCmbValues();
        setHouseStatusCmbValues();
        setPaymentDoneCmbValues();
        setSortCmbValues();
        tableSearch();
    }


    public void tableSearch() {

        FilteredList<HouseStatusCheckTm> filteredData = new FilteredList<>(tableData, b -> true);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(check -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (check.getCheckNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getLivingRoomStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getKitchenStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getBedRoomsStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getBathRoomsStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getTotalHouseStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getTenantId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getHouseId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getEstimatedCostForRepair().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (check.getIsPaymentDone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<HouseStatusCheckTm> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }



    public void setSortCmbValues(){

        ObservableList<String> sortTypes = FXCollections.observableArrayList("Sort By","Check No (Ascending)","Check No (Descending)","Tenant ID (Ascending)","Tenant ID (Descending)","House ID (Ascending)","House ID (Descending)");
        sortCmb.setItems(sortTypes);
        sortCmb.getSelectionModel().selectFirst();
    }


    public void setPaymentDoneCmbValues(){

        ObservableList<String> isPaymentDone = FXCollections.observableArrayList("Select","N/A","Paid","Reduced from Security Deposit","Not Yet");
        paymentDoneCmb.setItems(isPaymentDone);
        paymentDoneCmb.getSelectionModel().selectFirst();

    }

    public void setHouseStatusCmbValues(){

        ObservableList<String> finalHouseStatus = FXCollections.observableArrayList("Select","Excellent","Good","Moderate","Damaged");
        houseStatusCmb.setItems(finalHouseStatus);
        houseStatusCmb.getSelectionModel().selectFirst();

    }

    public void setCheckNoCmbValues(){

        ObservableList<String> checkNos = FXCollections.observableArrayList();
        checkNos.add("Select");

        for (HouseStatusCheckTm x : tableData){
            checkNos.add(x.getCheckNumber());
        }

        checkNoCmb.setItems(checkNos);
        checkNoCmb.getSelectionModel().selectFirst();

    }


    public void setHouseIdCmbValues(){

        ObservableList<String> houseIds = FXCollections.observableArrayList();
        houseIds.add("Select");

        try {
            ObservableList<UnitTm> allUnits = unitModel.loadTable();

            for (UnitTm x: allUnits){
                houseIds.add(x.getHouseId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        houseIdCmb.setItems(houseIds);
        houseIdCmb.getSelectionModel().selectFirst();

    }


    public void setTenantIdCmbValues(){

        ObservableList<String> tenantIds = FXCollections.observableArrayList();
        tenantIds.add("Select");
        try {
            ObservableList<TenantTm> allTenants = paymentModel.getAllTenantIds();

            for (TenantTm x: allTenants){
                tenantIds.add(x.getTenantId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting tenant id combo box values: " + e.getMessage());
            notification("An error occurred while setting tenant id combo box values. Please try again or contact support.");
        }

        tenantIdCmb.setItems(tenantIds);
        tenantIdCmb.getSelectionModel().selectFirst();

    }


    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (HouseStatusCheckTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();
    }


    public void setTableColumns(){

        checkNoColumn.setCellValueFactory(new PropertyValueFactory<>("checkNumber"));
        livingRoomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("livingRoomStatus"));
        kitchenStatusColumn.setCellValueFactory(new PropertyValueFactory<>("kitchenStatus"));
        bedRoomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("bedRoomsStatus"));
        bathRoomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("bathRoomsStatus"));
        totalHouseStatusColumn.setCellValueFactory(new PropertyValueFactory<>("totalHouseStatus"));
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedCostForRepair"));
        isPaymentDoneColumn.setCellValueFactory(new PropertyValueFactory<>("isPaymentDone"));

    }


    public void setTableColumnsValue(){

        Callback<TableColumn<HouseStatusCheckTm, String>, TableCell<HouseStatusCheckTm, String>> cellFoctory = (TableColumn<HouseStatusCheckTm, String> param) -> {

            final TableCell<HouseStatusCheckTm, String> cell = new TableCell<HouseStatusCheckTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\email (2).png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\cutting.png");


                        ImageView mail = new ImageView();
                        mail.setImage(image1);
                        mail.setFitHeight(19);
                        mail.setFitWidth(19);

                        ImageView reduceFromSecurityCharge = new ImageView();
                        reduceFromSecurityCharge.setImage(image2);
                        reduceFromSecurityCharge.setFitHeight(20);
                        reduceFromSecurityCharge.setFitWidth(20);

                        mail.setStyle(" -fx-cursor: hand ;");
                        reduceFromSecurityCharge.setStyle(" -fx-cursor: hand ;");


                        mail.setOnMouseClicked((MouseEvent event) -> {

                            HouseStatusCheckTm selectedHouseCheck = table.getSelectionModel().getSelectedItem();

                            String costOfRepair = selectedHouseCheck.getEstimatedCostForRepair();
                            boolean costOfRepairValidation = UserInputValidation.checkDecimalValidation(costOfRepair);
                            boolean isEnough = false;

                            if(costOfRepairValidation){
                                try {
                                    isEnough = tenantModel.checkRemainingSecurityFundEnoughOrNot(selectedHouseCheck.getTenantId(),costOfRepair);
                                }
                                catch (SQLException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                    System.err.println("Error while checking remaining security charge balance: " + e.getMessage());
                                    notification("An error occurred while checking remaining security charge balance. Please try again or contact support.");
                                }

                                if(selectedHouseCheck.getIsPaymentDone().equals("Not Yet") && !isEnough){

                                    try{
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SendMail.fxml"));
                                        Parent root = fxmlLoader.load();
                                        SendMailController sendMailController = fxmlLoader.getController();
                                        sendMailController.prepareMail(selectedHouseCheck);
                                        Scene scene = new Scene(root);
                                        Stage stage = new Stage();
                                        stage.setScene(scene);
                                        stage.initStyle(StageStyle.UNDECORATED);
                                        stage.setX(50);
                                        stage.setY(50);
                                        stage.show();

                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                        System.err.println("Error while loading Add Send Mail page " + e.getMessage());
                                        notification("An error occurred while loading Add Send Mail page. Please try again or contact support.");
                                    }
                                }

                            }

                        });

                        reduceFromSecurityCharge.setOnMouseClicked((MouseEvent event) -> {

                            HouseStatusCheckTm selectedHouseCheck = table.getSelectionModel().getSelectedItem();

                            String costOfRepair = selectedHouseCheck.getEstimatedCostForRepair();
                            boolean costOfRepairValidation = UserInputValidation.checkDecimalValidation(costOfRepair);
                            boolean isEnough = false;

                            if(costOfRepairValidation) {
                                try {
                                    isEnough = tenantModel.checkRemainingSecurityFundEnoughOrNot(selectedHouseCheck.getTenantId(), costOfRepair);
                                }
                                catch (SQLException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                    System.err.println("Error while checking remaining security charge balance is enough or not: " + e.getMessage());
                                    notification("An error occurred while checking remaining security charge balance is enough or not. Please try again or contact support.");
                                }

                                if(selectedHouseCheck.getIsPaymentDone().equals("Not Yet") && isEnough) {

                                    ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                                    ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Confirmation");
                                    alert.setHeaderText("Please confirm first");
                                    alert.setContentText("Do you want to reduce damage cost from the tenant security charge?");

                                    alert.getButtonTypes().setAll(yesButton, cancelButton);
                                    Optional<ButtonType> options = alert.showAndWait();

                                    if(options.isPresent() && options.get()==yesButton){

                                        try {
                                            String response = tenantModel.reduceRepairCostFromSecurityCharge(selectedHouseCheck.getTenantId(), costOfRepair);
                                            notification(response);

                                            if(response.equals("Repair costs were successfully deducted from the security deposit.")){
                                                boolean isChangeTheStatus = houseStatusCheckModel.changeStatus(selectedHouseCheck.getCheckNumber(),"Reduced from Security Deposit");
                                                loadTable();

                                                TenantDto tenantDto = tenantModel.getMoreTenantDetails(selectedHouseCheck.getTenantId());

                                                notification("Tenant : "+tenantDto.getTenantId()+ ", deducted amount is: "+costOfRepair+ " Remaining Security Deposit Is: "+ tenantDto.getSecurityPaymentRemain());
                                                notification("Sent Email To Tenant ID: "+tenantDto.getTenantId()+" , regarding deduction frm security payment");

                                                SendMail sendMail = new SendMail();
                                                new Thread(() -> sendMail.sendMail(tenantDto.getEmail(),"Reduced House Damage Repair Cost From Security payment","We would like to inform you we have deduced Rs. "+ costOfRepair+" from your security payment,\nupon the property damage of last house inspection,\nCurrent security payment balance is: "+ tenantDto.getSecurityPaymentRemain()+"\nThank You attending for this matter!\n\n\nThe Grand View Residences\nColombo 08")).start();
                                            }

                                        } catch (SQLException | ClassNotFoundException e) {
                                            e.printStackTrace();
                                            System.err.println("Error while deducting repair cost from security charge: " + e.getMessage());
                                            notification("An error occurred while deducting repair cost from security charge. Please try again or contact support.");
                                        }
                                    }
                                    else{

                                        table.getSelectionModel().clearSelection();
                                    }

                                }
                                else if(selectedHouseCheck.getIsPaymentDone().equals("Not Yet") && !isEnough){

                                    notification("The security deposit doesn't cover repairs. Please email the relevant parties for prompt payment");
                                }
                            }

                        });

                        HBox manageBtn = new HBox(reduceFromSecurityCharge,mail);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(reduceFromSecurityCharge, new Insets(2, 3, 0, 3));
                        HBox.setMargin(mail, new Insets(2, 2, 0, 3));
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
            tableData = houseStatusCheckModel.getAllHouseInspectChecks();
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
        setHouseStatusCmbValues();
        setCheckNoCmbValues();
        setSortCmbValues();
        sortCmb.getSelectionModel().selectFirst();
        houseStatusCmb.getSelectionModel().selectFirst();
        paymentDoneCmb.getSelectionModel().selectFirst();
        table.getSelectionModel().clearSelection();

    }


    public void notification(String message){

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text(message);
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();
    }
}




