package com.example.test.controller;

import com.example.test.logindata.LoginDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
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
    private Label userNameLabel;

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
    private ImageView notificationIcon;


    @FXML
    void purchaseAgreementOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Purchase Agreement");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PurchaseAgreement.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Purchase Agreement Page: " + e.getMessage());
            notification("An error occurred while loading the Purchase Agreement Page. Please try again or contact support.");
        }

    }

    @FXML
    void leaseAgreementOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Lease Agreement");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LeaseAgreement.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Lease Agreement Page: " + e.getMessage());
            notification("An error occurred while loading the Lease Agreement Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Customer Page: " + e.getMessage());
            notification("An error occurred while loading the Customer Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Dashboard Page: " + e.getMessage());
            notification("An error occurred while loading the Dashboard Page. Please try again or contact support.");
        }

    }

    @FXML
    void exitOnAction(ActionEvent event) {

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm before exit on Application");
        alert.setHeaderText("Are you sure you want to exit from this application?");
        alert.setContentText("Click OK to exit or click Cancel to abort");
        alert.getButtonTypes().setAll(yesButton, cancelButton);

        Optional<ButtonType> option = alert.showAndWait();

        if(option.isPresent() && option.get()==yesButton){
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Expense Page: " + e.getMessage());
            notification("An error occurred while loading the Expense Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Floor Page: " + e.getMessage());
            notification("An error occurred while loading the Floor Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Maintenance Request Page: " + e.getMessage());
            notification("An error occurred while loading the Maintenance Request Page. Please try again or contact support.");
        }

    }

    @FXML
    void notificationOnAction(ActionEvent event) {

    }

    @FXML
    void ownerOnAction(ActionEvent event) {

        try {
            pageLabel.setText("Home Owner");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Owner.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Home Owner Page: " + e.getMessage());
            notification("An error occurred while loading the Home Owner Page. Please try again or contact support.");
        }
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Payment Page: " + e.getMessage());
            notification("An error occurred while loading the Payment Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Request Page: " + e.getMessage());
            notification("An error occurred while loading the Request Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Return House Page: " + e.getMessage());
            notification("An error occurred while loading the Return House Page. Please try again or contact support.");
        }

    }

    @FXML
    void searchOnAction(ActionEvent event) {

    }

    @FXML
    void statusCheckOnAction(ActionEvent event) {

        try {
            pageLabel.setText("House Inspection");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HouseStatusCheck.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the  House Inspection Page: " + e.getMessage());
            notification("An error occurred while loading the House Inspection Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Tenant Page: " + e.getMessage());
            notification("An error occurred while loading the Tenant Page. Please try again or contact support.");
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
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Unit Page: " + e.getMessage());
            notification("An error occurred while loading the Unit Page. Please try again or contact support.");
        }
    }

    @FXML
    public void analysisOnAction(ActionEvent event) {
    }

    @FXML
    public void logOutOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            stage1.initStyle(StageStyle.UNDECORATED);
            stage1.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the SignIn Page: " + e.getMessage());
            notification("An error occurred while loading the SignIn Page. Please try again or contact support.");
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d");
        String formattedDate = formatter.format(date);

        dateLabel.setText(formattedDate);

        String userName = LoginDetails.getUserName();
        String subUserName = userName.trim();
        userNameLabel.setText(subUserName);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mainPnae.getChildren().clear();
            mainPnae.getChildren().add(anchorPane);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Dashboard: " + e.getMessage());
            notification("An error occurred while loading the Dashboard. Please try again or contact support.");
        }

    }

    @FXML
    public void getSearchTxtRecommenderOnAction(KeyEvent keyEvent) {

    }


    @FXML
    public void searchTxtOnMouseEntered(MouseEvent event) {

        searchtxt.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                String page = searchtxt.getText();
                System.out.println(page);


                if(page.equals("Customer") || page.equals("customer")){
                    customerOnAction(new ActionEvent());
                }

                else if(page.equals("Employee") || page.equals("employee")){
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Employee.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        mainPnae.getChildren().clear();
                        mainPnae.getChildren().add(anchorPane);

                        pageLabel.setText("Employee");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error while loading the Employee Page: " + e.getMessage());
                        notification("An error occurred while loading the Employee Page. Please try again or contact support.");
                    }
                }

                else if(page.equals("Unit") || page.equals("unit")){
                    unitOnAction(new ActionEvent());
                }

                else if(page.equals("Tenant") || page.equals("tenant")){
                    tenantOnAction(new ActionEvent());
                }

                else if(page.equals("Owner") || page.equals("owner")){
                    ownerOnAction(new ActionEvent());
                }

                else if(page.equals("Request") || page.equals("request")){
                    requestOnAction(new ActionEvent());
                }

                else if(page.equals("Payment") || page.equals("payment")){
                    paymentOnAction(new ActionEvent());
                }

                else if(page.equals("Floor") || page.equals("floor")){
                    floorOnAction(new ActionEvent());
                }

                else if(page.equals("House Type") || page.equals("house type") || page.equals("House type") || page.equals("house Type")){
                    try {
                        pageLabel.setText("House Type");

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HouseType.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        HouseTypeController houseTypeController = fxmlLoader.getController();
                        houseTypeController.setHouseTypeTextInvisible();
                        mainPnae.getChildren().clear();
                        mainPnae.getChildren().add(anchorPane);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error while loading the House Type Page: " + e.getMessage());
                        notification("An error occurred while loading the House Type Page. Please try again or contact support.");
                    }

                }

                else if(page.equals("Expense") || page.equals("expense")){
                    expenceOnAction(new ActionEvent());
                }

                else if(page.equals("Maintenance Request") || page.equals("maintenance request") || page.equals("Maintenance request") || page.equals("maintenance Request") || page.equals("Maintenance")){
                    maintenanceOnAction(new ActionEvent());
                }

                else if(page.equals("Status Check") || page.equals("status check") || page.equals("House Inspection") || page.equals("house inspection")){
                    statusCheckOnAction(new ActionEvent());
                }

                else if(page.equals("Dashboard") || page.equals("dashboard")){
                    dashboarOnAction(new ActionEvent());
                }

                else if(page.equals("Return") || page.equals("return")){
                    returnOnAction(new ActionEvent());
                }

                else if(page.equals("Lease Agreement") || page.equals("lease agreement") || page.equals("Lease agreement") || page.equals("lease Agreement")){
                    leaseAgreementOnAction(new ActionEvent());
                }

                else if(page.equals("Purchase Agreement") || page.equals("purchase agreement") || page.equals("Purchase agreement") || page.equals("purchase Agreement")){
                    purchaseAgreementOnAction(new ActionEvent());
                }
                searchtxt.clear();
            }
        });

    }


    @FXML
    public void notificationOnMouseClicked(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Notification.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the Notification Page: " + e.getMessage());
            notification("An error occurred while loading the Notification Page. Please try again or contact support.");
        }
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
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading the User Profile Page: " + e.getMessage());
            notification("An error occurred while loading the User Profile Page. Please try again or contact support.");
        }
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










