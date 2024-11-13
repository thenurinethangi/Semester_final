package com.example.test.controller;

import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.dto.tm.PaymentTm;
import com.example.test.dto.tm.TenantTm;
import com.example.test.dto.tm.UnitTm;
import com.example.test.model.LeaseAgreementModel;
import com.example.test.model.PaymentModel;
import com.example.test.model.UnitModel;
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
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class LeaseAgreementController implements Initializable {

    @FXML
    private TableView<LeaseAgreementTm> table;

    @FXML
    private TableColumn<LeaseAgreementTm, String> leaseIdColumn;

    @FXML
    private TableColumn<LeaseAgreementTm, String> tenantIdColumn;

    @FXML
    private TableColumn<LeaseAgreementTm, String> houseIdColumn;

    @FXML
    private TableColumn<LeaseAgreementTm, String> leaseTurnColumn;

    @FXML
    private TableColumn<LeaseAgreementTm, String> startDateColumn;

    @FXML
    private TableColumn<LeaseAgreementTm, String> endDateColumn;

    @FXML
    private TableColumn<LeaseAgreementTm, String> statusColumn;

    @FXML
    private TableColumn<LeaseAgreementTm, String> actionColumn;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private ComboBox<String> sortCmb;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private ComboBox<String> leaseTurnCmb;

    @FXML
    private ComboBox<String> leaseIdCmb;

    @FXML
    private ComboBox<String> statusCmb;

    @FXML
    private ComboBox<String> tenantIdCmb;

    @FXML
    private ComboBox<String> houseIdCmb;

    @FXML
    private TextField searchTxt;


    private ObservableList<LeaseAgreementTm> tableData;
    private final LeaseAgreementModel leaseAgreementModel = new LeaseAgreementModel();
    private final PaymentModel paymentModel = new PaymentModel();
    private final UnitModel unitModel = new UnitModel();

    public LeaseAgreementController() throws SQLException, ClassNotFoundException {
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

        ObservableList<LeaseAgreementTm> leaseAgreementTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            leaseAgreementTms.add(tableData.get(i));
        }

        table.setItems(leaseAgreementTms);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumns();
        setTableColumnsValue();
        setRowCmbValues();
        setTenantIdCmbValues();
        setHouseIdCmbValues();
        setLeaseTurnCmbValues();
        setStatusCmbValues();
        setLeaseIdCmbValues();
    }


    public void setStatusCmbValues(){

        ObservableList<String> statuses = FXCollections.observableArrayList("Select","Active","Expired","Canceled");
        statusCmb.setItems(statuses);

    }

    public void setLeaseTurnCmbValues(){

        ObservableList<String> leaseTurns = FXCollections.observableArrayList("Select","6 Months","12 Months","18 Months","2 Year");
        leaseTurnCmb.setItems(leaseTurns);

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


    public void setLeaseIdCmbValues(){

        ObservableList<String> leaseIds = FXCollections.observableArrayList();
        leaseIds.add("Select");

        for (LeaseAgreementTm x : tableData){
            leaseIds.add(x.getLeaseId());
        }

        leaseIdCmb.setItems(leaseIds);
        leaseIdCmb.getSelectionModel().selectFirst();

    }


    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (LeaseAgreementTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();
    }


    public void setTableColumns(){

        leaseIdColumn.setCellValueFactory(new PropertyValueFactory<>("leaseId"));
        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));
        leaseTurnColumn.setCellValueFactory(new PropertyValueFactory<>("leaseTurn"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    public void setTableColumnsValue(){

        Callback<TableColumn<LeaseAgreementTm, String>, TableCell<LeaseAgreementTm, String>> cellFoctory = (TableColumn<LeaseAgreementTm, String> param) -> {

            final TableCell<LeaseAgreementTm, String> cell = new TableCell<LeaseAgreementTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        Button status = new Button();
                        status.setStyle("-fx-background-radius: 100; -fx-text-fill: #FFFFFF;");

                        LeaseAgreementTm currentAgreement = getTableView().getItems().get(getIndex());

                        boolean isNearToEndLease = false;

                        String end = currentAgreement.getEndDate();
                        LocalDate endDate = LocalDate.parse(end);
                        System.out.println("end: "+endDate);

                        LocalDate currentDate = LocalDate.now();
                        System.out.println("Current date: "+currentDate);

                        long daysDifference = ChronoUnit.DAYS.between(currentDate, endDate);

                        if (daysDifference >= 0 && daysDifference <= 30) {
                            System.out.println("The dates are near (within one month).");
                            isNearToEndLease = true;
                        } else {
                            System.out.println("The dates are not near (not within one month).");
                            isNearToEndLease = false;
                        }


                        if (currentAgreement.getStatus().equalsIgnoreCase("Canceled")) {
                            status.setStyle("-fx-background-color: #2f3542; -fx-background-radius: 100; -fx-text-fill: #FFFFFF;");

                        }else if (currentAgreement.getStatus().equalsIgnoreCase("Expired") || isNearToEndLease) {
                            status.setStyle("-fx-background-color: #E53935; -fx-background-radius: 100; -fx-text-fill: #FFFFFF;");

                        }
                        else if (currentAgreement.getStatus().equalsIgnoreCase("Active")) {
                            status.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 100; -fx-text-fill: #FFFFFF;");

                        } else {
                            status.setStyle("-fx-background-color: #FFC107; -fx-background-radius: 100; -fx-text-fill: #FFFFFF;");
                        }


                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\email (2).png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\contract(1).png");
                        Image image3 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\reply(1).png");


                        ImageView mail = new ImageView();
                        mail.setImage(image1);
                        mail.setFitHeight(19);
                        mail.setFitWidth(19);

                        ImageView reSign = new ImageView();
                        reSign.setImage(image2);
                        reSign.setFitHeight(20);
                        reSign.setFitWidth(20);

                        ImageView returnHouse = new ImageView();
                        returnHouse.setImage(image3);
                        returnHouse.setFitHeight(20);
                        returnHouse.setFitWidth(20);

                        mail.setStyle(" -fx-cursor: hand ;");
                        reSign.setStyle(" -fx-cursor: hand ;");
                        returnHouse.setStyle(" -fx-cursor: hand ;");


                        mail.setOnMouseClicked((MouseEvent event) -> {

                            LeaseAgreementTm selectedLeaseAgreement = table.getSelectionModel().getSelectedItem();

                            if(selectedLeaseAgreement.getStatus().equals("Canceled") || selectedLeaseAgreement.getStatus().equals("Deleted")) {

                                return;
                            }

                            String subject = "";
                            String message = "";
                            if(selectedLeaseAgreement.getStatus().equals("Expired")){

                                subject = "Regarding the expiration of the lease turn. \n";

                                message = "We would like to formally inform you that your lease has expired.\nThe lease term commenced on "+ selectedLeaseAgreement.getStartDate()+ " , and concluded on " + selectedLeaseAgreement.getEndDate()+".\nWe kindly request that you either sign the lease again or vacate the premises.\nPlease take the necessary actions promptly.\nThank you for your attention to this matter!\n\n\n" +
                                        "The GrandView Residences,  \n" +
                                        "Colombo 08.";
                            }

                            try{
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MailSendFormInLeaseAgreement.fxml"));
                                Parent root = fxmlLoader.load();
                                MailSendFormInLeaseAgreementController mailSendFormInLeaseAgreementController = fxmlLoader.getController();
                                mailSendFormInLeaseAgreementController.setSelectedTenantDetailsToSendMail(selectedLeaseAgreement,subject,message);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.initStyle(StageStyle.UNDECORATED);
                                stage.setX(70);
                                stage.setY(50);
                                stage.show();

                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        });

                        reSign.setOnMouseClicked((MouseEvent event) -> {

                            LeaseAgreementTm selectedLeaseAgreement = table.getSelectionModel().getSelectedItem();

                            if(selectedLeaseAgreement.getStatus().equals("Expired")){

                                try{
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ReSignLeaseAgreement.fxml"));
                                    Parent root = fxmlLoader.load();
                                    ReSignLeaseAgreementController reSignLeaseAgreementController = fxmlLoader.getController();
                                    reSignLeaseAgreementController.setSelectedAgreementDetails(selectedLeaseAgreement);
                                    Scene scene = new Scene(root);
                                    Stage stage = new Stage();
                                    stage.setScene(scene);
                                    stage.show();

                                }
                                catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                        });

                        returnHouse.setOnMouseClicked((MouseEvent event) -> {

                            LeaseAgreementTm selectedLeaseAgreement = table.getSelectionModel().getSelectedItem();

                            if(selectedLeaseAgreement.getStatus().equals("Expired")){

                                try{
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HouseReturnConformation.fxml"));
                                    Parent root = fxmlLoader.load();
                                    HouseReturnConformationController houseReturnConformationController = fxmlLoader.getController();
                                    houseReturnConformationController.setSelectedAgreementDetailsToReturn(selectedLeaseAgreement);
                                    Scene scene = new Scene(root);
                                    Stage stage = new Stage();
                                    stage.setScene(scene);
                                    stage.show();

                                }
                                catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                        });

                        HBox manageBtn = new HBox(status,mail,reSign,returnHouse);

                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(status, new Insets(2, 3, 0, 3));
                        HBox.setMargin(mail, new Insets(2, 2, 0, 3));
                        HBox.setMargin(reSign, new Insets(2, 3, 0, 3));
                        HBox.setMargin(returnHouse, new Insets(2, 3, 0, 3));
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
            tableData = leaseAgreementModel.getAllAgreements();
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
        setHouseIdCmbValues();
        setLeaseIdCmbValues();
        sortCmb.getSelectionModel().selectFirst();
        statusCmb.getSelectionModel().selectFirst();
        leaseTurnCmb.getSelectionModel().selectFirst();
        table.getSelectionModel().clearSelection();

    }

    public void deleteOnAction(ActionEvent event) {
    }
}
