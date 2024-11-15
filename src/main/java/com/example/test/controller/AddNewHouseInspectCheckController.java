package com.example.test.controller;

import com.example.test.dto.HouseStatusCheckDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.UnitDto;
import com.example.test.model.HouseStatusCheckModel;
import com.example.test.model.TenantModel;
import com.example.test.model.UnitModel;
import com.example.test.validation.UserInputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewHouseInspectCheckController implements Initializable {

    @FXML
    private TextField tenantIdTxt;

    @FXML
    private ComboBox<String> livingRoomStatusCmb;

    @FXML
    private ComboBox<String> kitchenStatusCmb;

    @FXML
    private ComboBox<String> bedRoomStatusCmb;

    @FXML
    private ComboBox<String> bathRoomStatusCmb;

    @FXML
    private ComboBox<String> totalHouseStatusCmb;

    @FXML
    private TextField costForRepairTxt;

    @FXML
    private Label tenantNameLabel;

    @FXML
    private Label houseIdLabel;

    @FXML
    private Label houseTypeLabel;

    @FXML
    private Label costForRepairErrorLabel;

    ObservableList<String> houseStatus = FXCollections.observableArrayList("Select","Excellent","Good","Moderate","Damaged");
    private final TenantModel tenantModel = new TenantModel();
    private final UnitModel unitModel = new UnitModel();
    private TenantDto tenant;
    private final HouseStatusCheckModel houseStatusCheckModel = new HouseStatusCheckModel();

    public AddNewHouseInspectCheckController() throws SQLException, ClassNotFoundException {
    }


    @FXML
    void addBtnOnAction(ActionEvent event) {

        if( tenant==null || tenant.getTenantId()==null){
            return;
        }

        String livingRoomStatus = livingRoomStatusCmb.getSelectionModel().getSelectedItem();
        String kitchenStatus = kitchenStatusCmb.getSelectionModel().getSelectedItem();
        String bedRoomStatus = bedRoomStatusCmb.getSelectionModel().getSelectedItem();
        String bathRoomStatus = bathRoomStatusCmb.getSelectionModel().getSelectedItem();
        String totalHouseStatus = totalHouseStatusCmb.getSelectionModel().getSelectedItem();
        String costForRepair = costForRepairTxt.getText();
        boolean repairCostValidation = UserInputValidation.checkDecimalValidation(costForRepair);


        if(livingRoomStatus.isEmpty() || livingRoomStatus.equals("Select") || kitchenStatus.isEmpty() || kitchenStatus.equals("Select") ||
                bedRoomStatus.isEmpty() || bedRoomStatus.equals("Select") || bathRoomStatus.isEmpty() || bathRoomStatus.equals("Select") || totalHouseStatus.isEmpty() || totalHouseStatus.equals("Select")){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Select the status of all house parts in order to proceed with saving the new inspection");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();
            return;
        }

        else if(!costForRepair.isEmpty() && !repairCostValidation){

            costForRepairErrorLabel.setText("This is not a valid input for this field");
            return;
        }

        if(totalHouseStatus.equals("Damaged") && costForRepair.isEmpty()){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Please Enter Estimated Cost Of Repair For This Damaged House");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

            costForRepairTxt.setStyle("-fx-border-color: #E53935");
            return;
        }
        else{
            costForRepairTxt.setStyle("-fx-border-color: #a4b0be");
        }

        HouseStatusCheckDto houseStatusCheckDto = new HouseStatusCheckDto();

        houseStatusCheckDto.setLivingRoomStatus(livingRoomStatus);
        houseStatusCheckDto.setBedRoomsStatus(bedRoomStatus);
        houseStatusCheckDto.setBathRoomsStatus(bathRoomStatus);
        houseStatusCheckDto.setTotalHouseStatus(totalHouseStatus);
        houseStatusCheckDto.setKitchenStatus(kitchenStatus);
        houseStatusCheckDto.setHouseId(tenant.getHouseId());
        houseStatusCheckDto.setTenantId(tenant.getTenantId());

        if(!totalHouseStatus.equals("Damaged")){

            houseStatusCheckDto.setEstimatedCostForRepair("N/A");
            houseStatusCheckDto.setIsPaymentDone("N/A");
        }
        else{

            houseStatusCheckDto.setEstimatedCostForRepair(costForRepair);
            houseStatusCheckDto.setIsPaymentDone("Not Yet");
        }


        try {
            String response = houseStatusCheckModel.addNewHouseStatusCheck(houseStatusCheckDto);

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text(response);
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

            clean();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cancelBtnOnAction(ActionEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void totalHouseStatusCmbOnAction(ActionEvent event) {

        if(!totalHouseStatusCmb.getSelectionModel().getSelectedItem().equals("Damaged")){
            costForRepairTxt.setDisable(true);
        }
        else{
            costForRepairTxt.setDisable(false);
        }
    }


    @FXML
    void searchIconClicked(MouseEvent event) {

        String tenantId = tenantIdTxt.getText();

        if (!tenantId.isEmpty()) {
            try {
                tenant = tenantModel.getMoreTenantDetails(tenantId);

                if (tenant.getName() == null) {

                    clean();

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("Please Enter Correct Tenant ID");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();
                    return;
                }

                tenantNameLabel.setText(tenant.getName());
                houseIdLabel.setText(tenant.getHouseId());
                UnitDto unit = unitModel.getHouseDetailsByHouseId(tenant.getHouseId());
                houseTypeLabel.setText(unit.getHouseType());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        totalHouseStatusCmb.setItems(houseStatus);
        totalHouseStatusCmb.getSelectionModel().selectFirst();
        livingRoomStatusCmb.setItems(houseStatus);
        livingRoomStatusCmb.getSelectionModel().selectFirst();
        bedRoomStatusCmb.setItems(houseStatus);
        bedRoomStatusCmb.getSelectionModel().selectFirst();
        bathRoomStatusCmb.setItems(houseStatus);
        bathRoomStatusCmb.getSelectionModel().selectFirst();
        kitchenStatusCmb.setItems(houseStatus);
        kitchenStatusCmb.getSelectionModel().selectFirst();
    }


    public void clean(){

        tenantIdTxt.clear();
        tenantNameLabel.setText("");
        houseIdLabel.setText("");
        houseTypeLabel.setText("");
        totalHouseStatusCmb.getSelectionModel().selectFirst();
        livingRoomStatusCmb.getSelectionModel().selectFirst();
        bedRoomStatusCmb.getSelectionModel().selectFirst();
        bathRoomStatusCmb.getSelectionModel().selectFirst();
        kitchenStatusCmb.getSelectionModel().selectFirst();
        costForRepairTxt.setDisable(false);
        costForRepairTxt.clear();
    }

}
