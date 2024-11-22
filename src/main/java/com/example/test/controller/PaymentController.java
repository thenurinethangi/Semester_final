package com.example.test.controller;

import com.example.test.dto.tm.PaymentTm;
import com.example.test.dto.tm.TenantTm;
import com.example.test.model.PaymentModel;
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

public class PaymentController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<PaymentTm> table;

    @FXML
    private TableColumn<PaymentTm, String> invoiceNoColumn;

    @FXML
    private TableColumn<PaymentTm, Double> amountColumn;

    @FXML
    private TableColumn<PaymentTm, String> dateColumn;

    @FXML
    private TableColumn<PaymentTm, String> paymentTypeColumn;

    @FXML
    private TableColumn<PaymentTm, String> tenantIdColumn;

    @FXML
    private TableColumn<PaymentTm, String> actionColumn;

    @FXML
    private Button addNewPaymentBtn;

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
    private ComboBox<String> paymentTypeCmb;

    @FXML
    private TextField invoiceNoTxt;

    @FXML
    private ListView<String> invoiceNoList;

    @FXML
    private ComboBox<String> tenantIdCmb;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField searchTxt;

    private final PaymentModel paymentModel = new PaymentModel();
    private ObservableList<PaymentTm> tableData;
    private ObservableList<String> invoices;


    @FXML
    void addNewPaymentOnAction(ActionEvent event) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewPayment.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading Add New Payment form: " + e.getMessage());
            notification("An error occurred while loading Add New Payment form. Please try again or contact support.");
        }

    }


    @FXML
    void invoiceNoListOnMouseClicked(MouseEvent event) {

        invoiceNoTxt.setText(invoiceNoList.getSelectionModel().getSelectedItem());
        invoiceNoList.getItems().clear();
    }


    @FXML
    void invoiceNoTxtKeyReleased(KeyEvent event) {

        String input = invoiceNoTxt.getText();


        try {
            invoices = paymentModel.getInvoiceNoSuggestions(input);
            invoiceNoList.setItems(invoices);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while getting invoice number suggestions: " + e.getMessage());
            notification("An error occurred while getting invoice number suggestions. Please try again or contact support.");
        }

        if(input.isEmpty()){
            invoices.clear();
        }
    }


    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();
    }


    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<PaymentTm> searchedPayments = FXCollections.observableArrayList();

        String selectedTenantId = tenantIdCmb.getValue();
        String selectedInvoiceNo = invoiceNoTxt.getText();
        String selectedPaymentType = paymentTypeCmb.getValue();
        String selectedDate = String.valueOf(datePicker.getValue());

        boolean tenantIdSelected = selectedTenantId != null && !selectedTenantId.equals("Select");
        boolean invoiceNoSelected = selectedInvoiceNo != null && !selectedInvoiceNo.isEmpty();
        boolean paymentTypeSelected = selectedPaymentType != null && !selectedPaymentType.equals("Select");
        boolean dateSelected = selectedDate != null && !selectedDate.equals("1990-10-10");


        if (tenantIdSelected) {
            ObservableList<PaymentTm> paymentsByTenantId = getPaymentsByTenantId(selectedTenantId);

            if (paymentsByTenantId.isEmpty()) {
                table.setItems(paymentsByTenantId);
            } else {
                searchedPayments.addAll(paymentsByTenantId);

                if (invoiceNoSelected) {
                    ObservableList<PaymentTm> filteredByInvoiceNo = filterPaymentsByInvoiceNo(searchedPayments, selectedInvoiceNo);
                    searchedPayments.clear();
                    searchedPayments.addAll(filteredByInvoiceNo);
                }

                if (paymentTypeSelected) {
                    ObservableList<PaymentTm> filteredByPaymentType = filterPaymentsByPaymentType(searchedPayments, selectedPaymentType);
                    searchedPayments.clear();
                    searchedPayments.addAll(filteredByPaymentType);
                }

                if (dateSelected) {
                    ObservableList<PaymentTm> filteredByDate = filterPaymentsByDate(searchedPayments, selectedDate);
                    searchedPayments.clear();
                    searchedPayments.addAll(filteredByDate);
                }

                table.setItems(searchedPayments);
            }

        }
        else if (invoiceNoSelected || paymentTypeSelected || dateSelected) {
            ObservableList<PaymentTm> allPayments = tableData;
            searchedPayments.addAll(allPayments);

            if (invoiceNoSelected) {
                searchedPayments = filterPaymentsByInvoiceNo(searchedPayments, selectedInvoiceNo);
            }

            if (paymentTypeSelected) {
                searchedPayments = filterPaymentsByPaymentType(searchedPayments, selectedPaymentType);
            }

            if (dateSelected) {
                searchedPayments = filterPaymentsByDate(searchedPayments, selectedDate);
            }

            table.setItems(searchedPayments);

        } else {
            ObservableList<PaymentTm> allPayments = tableData;
            table.setItems(allPayments);
        }
    }



    private ObservableList<PaymentTm> getPaymentsByTenantId(String tenantId) {
        return FXCollections.observableArrayList(
                tableData.stream()
                        .filter(payment -> payment.getTenantId() != null && payment.getTenantId().equalsIgnoreCase(tenantId))
                        .toList()
        );
    }


    private ObservableList<PaymentTm> filterPaymentsByInvoiceNo(ObservableList<PaymentTm> payments, String invoiceNo) {
        return FXCollections.observableArrayList(
                payments.stream()
                        .filter(payment -> payment.getInvoiceNo().equalsIgnoreCase(invoiceNo))
                        .toList()
        );
    }


    private ObservableList<PaymentTm> filterPaymentsByPaymentType(ObservableList<PaymentTm> payments, String paymentType) {
        return FXCollections.observableArrayList(
                payments.stream()
                        .filter(payment -> payment.getPaymentType().equalsIgnoreCase(paymentType))
                        .toList()
        );
    }


    private ObservableList<PaymentTm> filterPaymentsByDate(ObservableList<PaymentTm> payments, String date) {
        return FXCollections.observableArrayList(
                payments.stream()
                        .filter(payment -> payment.getDate().toString().equals(date))
                        .toList()
        );
    }


    @FXML
    void sortCmbOnAction(ActionEvent event) {


        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<PaymentTm> paymentTms = FXCollections.observableArrayList(tableData);

        if (sortType == null) {
            return;
        }

        Comparator<PaymentTm> comparator = null;

        switch (sortType) {
            case "Invoice No (Ascending)":
                comparator = Comparator.comparing(PaymentTm::getInvoiceNo);
                break;

            case "Invoice No (Descending)":
                comparator = Comparator.comparing(PaymentTm::getInvoiceNo).reversed();
                break;

            case "Amount (Ascending)":
                comparator = Comparator.comparing(PaymentTm::getAmount);
                break;

            case "Amount (Descending)":
                comparator = Comparator.comparing(PaymentTm::getAmount).reversed();
                break;

            case "Date (Ascending)":
                comparator = Comparator.comparing(PaymentTm::getDate);
                break;

            case "Date (Descending)":
                comparator = Comparator.comparing(PaymentTm::getDate).reversed();
                break;

            case "Tenant ID (Ascending)":
                comparator = Comparator.comparing(PaymentTm::getTenantId);
                break;

            case "Tenant ID (Descending)":
                comparator = Comparator.comparing(PaymentTm::getTenantId).reversed();
                break;

            default:
                break;
        }

        if (comparator != null) {
            FXCollections.sort(paymentTms, comparator);
            table.setItems(paymentTms);
        }
    }


    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<PaymentTm> paymentTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            paymentTms.add(tableData.get(i));
        }

        table.setItems(paymentTms);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumns();
        setTableColumnsValue();
        setRowCmbValues();
        setSortCmbValues();
        setPaymentTypeCmbValues();
        setTenantIdCmbValues();
        datePicker.setValue(LocalDate.parse("1990-10-10"));
        tableSearch();
    }



    public void tableSearch() {

        FilteredList<PaymentTm> filteredData = new FilteredList<>(tableData, b -> true);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(payment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (payment.getInvoiceNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(payment.getAmount()).contains(lowerCaseFilter)) {
                    return true;
                } else if (payment.getDate().contains(lowerCaseFilter)) {
                    return true;
                } else if (payment.getPaymentType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (payment.getTenantId() != null && payment.getTenantId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<PaymentTm> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
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

        for (PaymentTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }


    public void setSortCmbValues(){

        ObservableList<String> sortTypes = FXCollections.observableArrayList("Sort By","Invoice No (Ascending)","Invoice No (Descending)","Amount (Ascending)","Amount (Descending)","Date (Ascending)","Date (Descending)","Tenant ID (Ascending)","Tenant ID (Descending)");
        sortCmb.setItems(sortTypes);
        sortCmb.getSelectionModel().selectFirst();

    }

    public  void setPaymentTypeCmbValues(){

        ObservableList<String> paymentTypes = FXCollections.observableArrayList("Select","Monthly Rent Payment","Full House Purchase Payment","Security Deposit","Property Damage Charges");
        paymentTypeCmb.setItems(paymentTypes);
        paymentTypeCmb.getSelectionModel().selectFirst();

    }


    public void setTableColumns(){

        invoiceNoColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));

    }

    public void setTableColumnsValue(){

        Callback<TableColumn<PaymentTm, String>, TableCell<PaymentTm, String>> cellFoctory = (TableColumn<PaymentTm, String> param) -> {

            final TableCell<PaymentTm, String> cell = new TableCell<PaymentTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\email (2).png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\bin.png");


                        ImageView mail = new ImageView();
                        mail.setImage(image1);
                        mail.setFitHeight(19);
                        mail.setFitWidth(19);

                        ImageView delete = new ImageView();
                        delete.setImage(image2);
                        delete.setFitHeight(20);
                        delete.setFitWidth(20);

                        mail.setStyle(" -fx-cursor: hand ;");
                        delete.setStyle(" -fx-cursor: hand ;");


                        mail.setOnMouseClicked((MouseEvent event) -> {

                            PaymentTm selectedPayment = table.getSelectionModel().getSelectedItem();

                            System.out.println("mail");

                            try{
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MailSendFormInLeaseAgreement.fxml"));
                            Parent root = fxmlLoader.load();
                            MailSendFormInLeaseAgreementController mailSendFormInLeaseAgreementController = fxmlLoader.getController();
                            mailSendFormInLeaseAgreementController.setPaymentDetailsToSendMail(selectedPayment);
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
                                System.err.println("Error while loading Mail Send form: " + e.getMessage());
                                notification("An error occurred while loading Mail Send Form. Please try again or contact support.");
                            }

                        });


                        delete.setOnMouseClicked((MouseEvent event) -> {

                            PaymentTm selectedPayment = table.getSelectionModel().getSelectedItem();

                            try {
                                boolean result = paymentModel.checkIfThisPaymentIsFirstPaymentOrNot(selectedPayment);
                                if (result) {
                                    notification("This is First Made Payment Upon Renting Or Buying, Can't Delete");
                                    System.out.println("First payment");
                                    return;
                                } else {

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Confirmation Dialog");
                                    alert.setHeaderText("Please Confirm First");
                                    alert.setContentText("Are you sure you want to delete this selected payment?");

                                    ButtonType buttonYes = new ButtonType("Yes");
                                    ButtonType buttonCancel = new ButtonType("Cancel");

                                    alert.getButtonTypes().setAll(buttonYes, buttonCancel);

                                    Optional<ButtonType> res = alert.showAndWait();

                                    if (res.isPresent() && res.get() == buttonYes) {

                                            String response = paymentModel.deletePayment(selectedPayment);
                                            notification(response);
                                            loadTable();

                                    } else {
                                        table.getSelectionModel().clearSelection();
                                    }
                                }
                            }catch (SQLException | ClassNotFoundException e) {
                                e.printStackTrace();
                                System.err.println("Error while deleting the payment: " + e.getMessage());
                                notification("An error occurred while deleting the invoice no: "+selectedPayment.getInvoiceNo()+", Please try again or contact support.");
                            }

                        });

                        HBox manageBtn = new HBox(mail,delete);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(mail, new Insets(2, 2, 0, 3));
                        HBox.setMargin(delete, new Insets(2, 3, 0, 3));
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
            tableData = paymentModel.getAllPayments();
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
        paymentTypeCmb.getSelectionModel().selectFirst();
        sortCmb.getSelectionModel().selectFirst();
        invoiceNoTxt.setText("");
        table.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.parse("1990-10-10"));
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



