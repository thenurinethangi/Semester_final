package com.example.test.controller;

import com.example.test.dto.RequestDto;
import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.RequestTm;
import com.example.test.model.RecommendedHousesModel;
import com.example.test.model.RentRequestDetailsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;

public class RecommendedHousesController {

    @FXML
    private ListView<String> availableHousesList;

    @FXML
    private ListView<String> recommendedHousesList;

    private RequestTm requestTm;
    private String estimatedMonthlyBudgetForRent;
    private ObservableList<String> messageOne = FXCollections.observableArrayList("No Recommended Houses Since There are Available Houses");
    private ObservableList<String> messageTwo = FXCollections.observableArrayList("No Available Houses For This Request");
    private ObservableList<String> messageThree = FXCollections.observableArrayList("No Recommended Houses For This Request");
    private final RentRequestDetailsModel rentRequestDetailsModel = new RentRequestDetailsModel();
    private final RecommendedHousesModel recommendedHousesModel;
    private ObservableList<UnitDto> availableUnitsDtos;
    private ObservableList<UnitDto> recommendedUnitsDto;
    private ObservableList<String> recommendedUnits;
    private ObservableList<String> availableUnits;


    {
        try {
            recommendedHousesModel = new RecommendedHousesModel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void availableHousesListOnMouseClicked(MouseEvent event) {

        String selectedAvailableHouse = availableHousesList.getSelectionModel().getSelectedItem();

        if(selectedAvailableHouse==null || selectedAvailableHouse.equals("No Available Houses For This Request")){
            return;
        }
        else{

            if(!requestTm.getHouseId().equals("-")){
                try {
                    boolean result = recommendedHousesModel.makePreviousHouseAvailable(requestTm);
                    if(!result){
                        Notifications notifications = Notifications.create();
                        notifications.title("Notification");
                        notifications.text("Something went wrong, try again later");
                        notifications.hideCloseButton();
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.CENTER);
                        notifications.darkStyle();
                        notifications.showInformation();
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            int index = 0;

            for (int i = 0; i < availableUnits.size(); i++) {
                if(availableUnits.get(i).equals(selectedAvailableHouse)){
                    index = i;
                    break;
                }
            }

            try {
                String response = recommendedHousesModel.updateHouseId(availableUnitsDtos.get(index),requestTm);
                Notifications notifications = Notifications.create();
                notifications.title("Notification");
                notifications.text(response);
                notifications.hideCloseButton();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.CENTER);
                notifications.darkStyle();
                notifications.showInformation();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void recommendedHousesListOnMouseClicked(MouseEvent event) {

        String selectedRecommendedHouse = recommendedHousesList.getSelectionModel().getSelectedItem();

        // Check if selectedRecommendedHouse is null or one of the messages
        if (selectedRecommendedHouse == null ||
                selectedRecommendedHouse.equals("No Recommended Houses Since There are Available Houses") ||
                selectedRecommendedHouse.equals("No Recommended Houses For This Request")) {
            return;
        } else {

            if(!requestTm.getHouseId().equals("-")){
                try {
                    boolean result = recommendedHousesModel.makePreviousHouseAvailable(requestTm);
                    if(!result){
                        Notifications notifications = Notifications.create();
                        notifications.title("Notification");
                        notifications.text("Something went wrong, try again later");
                        notifications.hideCloseButton();
                        notifications.hideAfter(Duration.seconds(5));
                        notifications.position(Pos.CENTER);
                        notifications.darkStyle();
                        notifications.showInformation();
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            int index = 0;

            // Find the index of the selected recommended house
            for (int i = 0; i < recommendedUnits.size(); i++) {
                if (recommendedUnits.get(i).equals(selectedRecommendedHouse)) {
                    index = i;
                    break;
                }
            }

            try {
                // Update the house ID with the selected recommended unit
                String response = recommendedHousesModel.updateHouseId(recommendedUnitsDto.get(index), requestTm);
                Notifications notifications = Notifications.create();
                notifications.title("Notification");
                notifications.text(response);
                notifications.hideCloseButton();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.CENTER);
                notifications.darkStyle();
                notifications.showInformation();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void setSelectedRequestData(RequestTm request) {

        requestTm = request;

        if(requestTm.getRentOrBuy().equals("Rent")){
            getSelectedRequestData();
            getAvailableRentHouses();
        }
        else{
            getAvailableSellHouses();
        }
    }



    public void getSelectedRequestData(){

        try {
            RequestDto requestDto = rentRequestDetailsModel.getRentRequestDetails(requestTm.getRequestId());
            estimatedMonthlyBudgetForRent = requestDto.getEstimatedMonthlyBudgetForRent();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void getAvailableRentHouses(){

        try {
            availableUnits = recommendedHousesModel.getAvailableRentHouses(requestTm,estimatedMonthlyBudgetForRent);
            availableUnitsDtos = recommendedHousesModel.getAvailableRentHousesAsUnitDto(requestTm,estimatedMonthlyBudgetForRent);

            if(availableUnits.isEmpty()){
                availableHousesList.setItems(messageTwo);
                getRecommendedRentHouses();
            }
            else{
                availableHousesList.setItems(availableUnits);
                recommendedHousesList.setItems(messageOne);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public void getAvailableSellHouses(){

        try {
            availableUnits = recommendedHousesModel.getAvailableSellHouses(requestTm);
            availableUnitsDtos = recommendedHousesModel.getAvailableSellHousesAsUnitDto(requestTm);

            if(availableUnits.isEmpty()){
                availableHousesList.setItems(messageTwo);
                getRecommendedSellHouses();
            }
            else{
                availableHousesList.setItems(availableUnits);
                recommendedHousesList.setItems(messageOne);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public void getRecommendedRentHouses(){

        try {
            recommendedUnits = recommendedHousesModel.getRecommendedRentHouses(requestTm);
            recommendedUnitsDto = recommendedHousesModel.getRecommendedRentHousesAsUnitDto(requestTm);

            if(recommendedUnits.isEmpty()){
                recommendedHousesList.setItems(messageThree);
            }
            else {
                recommendedHousesList.setItems(recommendedUnits);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    public void getRecommendedSellHouses(){


    }

}





