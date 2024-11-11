package com.example.test.controller;

import com.example.test.dto.tm.PaymentTm;
import com.example.test.dto.tm.TenantTm;
import com.example.test.model.PaymentModel;
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
            throw new RuntimeException(e);
        }

    }

    @FXML
    void deleteOnAction(ActionEvent event) {//no need

    }

    @FXML
    void editOnAction(ActionEvent event) {//no need

    }

    @FXML
    void getSelectedRow(MouseEvent event) {//no need

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

        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
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
    }


    public void setTenantIdCmbValues(){

        ObservableList<String> tenantIds = FXCollections.observableArrayList();
        tenantIds.add("Select");
        try {
            ObservableList<TenantTm> allTenants = paymentModel.getAllTenantIds();

            for (TenantTm x: allTenants){
                tenantIds.add(x.getTenantId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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


    }

    public  void setPaymentTypeCmbValues(){

        ObservableList<String> paymentTypes = FXCollections.observableArrayList("Select","Monthly Rent Payment","Full House Purchase Payment","Security Deposit","Property Damage Charges");
        paymentTypeCmb.setItems(paymentTypes);

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
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SendMail.fxml"));
                            Parent root = fxmlLoader.load();
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.setX(50);
                            stage.setY(50);
                            stage.show();

                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                        });

                        delete.setOnMouseClicked((MouseEvent event) -> {

                            PaymentTm selectedPayment = table.getSelectionModel().getSelectedItem();

                            try {
                                boolean result = paymentModel.checkIfThisPaymentIsFirstPaymentOrNot(selectedPayment);
                                if(result){
                                    System.out.println("First payment");
                                    return;
                                }
                                else{
                                    String response = paymentModel.deletePayment(selectedPayment);

                                    Notifications notifications = Notifications.create();
                                    notifications.title("Notification");
                                    notifications.text(response);
                                    notifications.hideCloseButton();
                                    notifications.hideAfter(Duration.seconds(5));
                                    notifications.position(Pos.CENTER);
                                    notifications.darkStyle();
                                    notifications.showInformation();
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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

    }

}



