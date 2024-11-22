package com.example.test.controller;

import com.example.test.dto.TenantDto;
import com.example.test.model.NotificationModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {

    @FXML
    private ListView<String> notificationList;

    private final NotificationModel notificationModel = new NotificationModel();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ObservableList<TenantDto> tenants = notificationModel.getTenantWhoNotDonePayment();

            ObservableList<String> notifications = FXCollections.observableArrayList();

            for(TenantDto x : tenants){
                notifications.add("Tenant ID: " + x.getTenantId() + ",   Name: " + x.getName()+",   Last Paid Month Is: "+x.getLastPaidMonth()+"\n->Rent For This Month Has Not Yet Been Paid.");
            }

            //notificationList.setItems(notPaidTenants);

            ObservableList<TenantDto> notPaidForEarlierMonthsTenants =  notificationModel.checkTenantsWhoHaveNotPaidForEarlierMonths();

            for(TenantDto x : notPaidForEarlierMonthsTenants){
                notifications.add("Tenant ID: " + x.getTenantId() + ",   Name: " + x.getName()+",   Last Paid Month Is: "+x.getLastPaidMonth()+"\n->Not Paid For Early Month.");
            }

            //notificationList.setItems(notPaidTenants);

            ObservableList<String> expiredAgreements = notificationModel.getExpiredAgreements();

            for(String  x : expiredAgreements){
                notifications.add(x);
            }

            //notificationList.setItems(notifications);

            ObservableList<String> expiredSoonAgreements = notificationModel.getSoonExpiredAgreements();

            for(String  x : expiredSoonAgreements){
                notifications.add(x);
            }

            //notificationList.setItems(notifications);

            ObservableList<String> notCompletedRequests = notificationModel.getInProcessMaintenanceRequest();

            for(String  x : notCompletedRequests){
                notifications.add(x);
            }

            ObservableList<String> notDamageCostPaidTenants = notificationModel.getWhoNotPaidDamageCost();

            for(String  x : notDamageCostPaidTenants){
                notifications.add(x);
            }

            notificationList.setItems(notifications);
        }

        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while calculating total expense: " + e.getMessage());
            notification("An error occurred while calculating total expense. Please try again or contact support.");
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





