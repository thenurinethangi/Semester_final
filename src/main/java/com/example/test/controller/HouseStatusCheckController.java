package com.example.test.controller;

import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.*;
import com.example.test.model.HouseStatusCheckModel;
import com.example.test.model.PaymentModel;
import com.example.test.model.TenantModel;
import com.example.test.model.UnitModel;
import com.example.test.validation.UserInputValidation;
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
    private final UnitModel unitModel = new UnitModel();
    private final TenantModel tenantModel = new TenantModel();


    public HouseStatusCheckController() throws SQLException, ClassNotFoundException {
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
        }
        catch (Exception e){
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
    }


    public void setPaymentDoneCmbValues(){

        ObservableList<String> isPaymentDone = FXCollections.observableArrayList("Select","N/A","Paid","Reduced from Security Deposit","Not Yet");
        paymentDoneCmb.setItems(isPaymentDone);

    }

    public void setHouseStatusCmbValues(){

        ObservableList<String> finalHouseStatus = FXCollections.observableArrayList("Select","Excellent","Good","Moderate","Damaged");
        houseStatusCmb.setItems(finalHouseStatus);

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
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
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
                                        throw new RuntimeException(e);
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
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }

                                if(selectedHouseCheck.getIsPaymentDone().equals("Not Yet") && isEnough) {

                                    try {
                                        String response = tenantModel.reduceRepairCostFromSecurityCharge(selectedHouseCheck.getTenantId(), costOfRepair);

                                        Notifications notifications = Notifications.create();
                                        notifications.title("Notification");
                                        notifications.text(response);
                                        notifications.hideCloseButton();
                                        notifications.hideAfter(Duration.seconds(5));
                                        notifications.position(Pos.CENTER);
                                        notifications.darkStyle();
                                        notifications.showInformation();

                                        if(response.equals("Repair costs were successfully deducted from the security deposit.")){
                                            boolean isChangeTheStatus = houseStatusCheckModel.changeStatus(selectedHouseCheck.getCheckNumber(),"Reduced from Security Deposit");
                                            loadTable();

                                            TenantDto tenantDto = tenantModel.getMoreTenantDetails(selectedHouseCheck.getTenantId());

                                            Notifications notification = Notifications.create();
                                            notification.title("Notification");
                                            notification.text("Tenant :"+tenantDto.getTenantId()+", deducted amount is: "+costOfRepair+ " Remaining Security Deposit Is: "+ tenantDto.getSecurityPaymentRemain());
                                            notification.hideCloseButton();
                                            notification.hideAfter(Duration.seconds(5));
                                            notification.position(Pos.CENTER);
                                            notification.darkStyle();
                                            notification.showInformation();

                                            //send mail here to tenant
                                        }

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    } catch (ClassNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
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
        setHouseStatusCmbValues();
        setCheckNoCmbValues();
        sortCmb.getSelectionModel().selectFirst();
        houseStatusCmb.getSelectionModel().selectFirst();
        paymentDoneCmb.getSelectionModel().selectFirst();
        table.getSelectionModel().clearSelection();

    }
}




