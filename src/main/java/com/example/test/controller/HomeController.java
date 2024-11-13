package com.example.test.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {


    @FXML
    public Button logOutbtn;
    @FXML
    public Button analysisbtn;

    @FXML
    private AnchorPane pane1;

    @FXML
    private Pane pane2;

    @FXML
    private Button userbtn;

    @FXML
    private Button notificationbtn;

    @FXML
    private Label pageLabel;

    @FXML
    private TextField searchtxt;

    @FXML
    private Label dateLabel;

    @FXML
    private Button exitbtn;

    @FXML
    private Pane pane3;

    @FXML
    private Button dashboardbtn;

    @FXML
    private Button requestbtn;

    @FXML
    private Button customerbtn;

    @FXML
    private Button unitbtn;

    @FXML
    private Button tenantbtn;

    @FXML
    private Button ownerbtn;

    @FXML
    private Button contractbtn;

    @FXML
    private Button maintenancebtn;

    @FXML
    private Button complainbtn;

    @FXML
    private Button statusCheckbtn;

    @FXML
    private Button paymentbtn;

    @FXML
    private Button expencebtn;

    @FXML
    private Button returnbtn;

    @FXML
    private Button floorbtn;

    @FXML
    private Button c13;

    @FXML
    private Button c14;

    @FXML
    private AnchorPane mainPnae;


    @FXML
    void complainOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Employee");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Employee.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void contractOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Lease Agreement");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LeaseAgreement.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void customerOnAction(ActionEvent event) {

        try {
            dashboardbtn.setFocusTraversable(true);
            pageLabel.setText("Customer");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Customer.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void dashboarOnAction(ActionEvent event) {

        try {
            dashboardbtn.setFocusTraversable(true);
            pageLabel.setText("Dashboard");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void exitOnAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm before exit on Application");
        alert.setHeaderText("Are you sure you want to exit from this application?");
        alert.setContentText("Click OK to exit or click Cancel to abort");

        Optional<ButtonType> option = alert.showAndWait();

        if(option.isPresent() && option.get()==ButtonType.OK){
            System.exit(0);
        }
        else{
            return;
        }
    }

    @FXML
    void expenceOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Expense");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Expense.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void floorOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Floor");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Floor.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            FloorController floorController = fxmlLoader.getController();
            floorController.setFloorTextInvisible();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void maintenanceOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Maintenance Request");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MaintenanceRequest.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void notificationOnAction(ActionEvent event) {

    }

    @FXML
    void ownerOnAction(ActionEvent event) {

    }

    @FXML
    void paymentOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Payment");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Payment.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void requestOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Request");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Request.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void returnOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Return House");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ReturnHouse.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void searchOnAction(ActionEvent event) {

    }

    @FXML
    void statusCheckOnAction(ActionEvent event) {

        try {
            pageLabel.setText("House inspect check");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HouseStatusCheck.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void tenantOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Tenant");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Tenant.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void unitOnAction(ActionEvent event) {

        try {
            dashboardbtn.setFocusTraversable(true);
            pageLabel.setText("Unit");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Unit.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void analysisOnAction(ActionEvent event) {
    }

    @FXML
    public void logOutOnAction(ActionEvent event) {
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d");
        String formattedDate = formatter.format(date);

        dateLabel.setText(formattedDate);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    public void getSearchTxtRecommenderOnAction(KeyEvent keyEvent) {

//        String searchText = searchtxt.getText();
//        System.out.println(searchText);
//
//        ObservableList<String> listItems = list.getItems();
//        if (listItems != null) {
//            listItems.add(searchText);
//        }
//        else{
//            System.out.println("hi");
//        }


    }


    @FXML
    public void userOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfile.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}










