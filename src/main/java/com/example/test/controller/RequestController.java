package com.example.test.controller;

import com.example.test.dto.tm.CustomerTm;
import com.example.test.dto.tm.RequestTm;
import com.example.test.model.RequestModel;
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
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class RequestController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private Button addNewRentRequestBtn;

    @FXML
    private ComboBox<Integer> tableRowsCmb;

    @FXML
    private Button deletebtn;

    @FXML
    private ComboBox<String> sortByCmb;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private ComboBox<?> employeeIdCmb;

    @FXML
    private ComboBox<?> positionCmb;

    @FXML
    private TextField nameTxt;

    @FXML
    private ListView<?> nameList;

    @FXML
    private ListView<?> addressList;

    @FXML
    private TextField addressTxt;

    @FXML
    private Button addNewBuyRequestBtn;

    @FXML
    private TableView<RequestTm> table;
    @FXML
    private TableColumn<RequestTm, String> requestIdColumn;
    @FXML
    private TableColumn<RequestTm, String> customerIdColumn;
    @FXML
    private TableColumn<RequestTm, String> rentOrBuyColumn;
    @FXML
    private TableColumn<RequestTm, String> houseTypeColumn;
    @FXML
    private TableColumn<RequestTm, String> leaseTurnDesireColumn;
    @FXML
    private TableColumn<RequestTm, String> allDocumentsProvidedColumn;
    @FXML
    private TableColumn<RequestTm, String> qualifiedCustomerOrNotColumn;
    @FXML
    TableColumn<RequestTm, String> agreesToAllTermsAndConditionsColumn;
    @FXML
    TableColumn<RequestTm, String> isPaymentsCompletedColumn;
    @FXML
    TableColumn<RequestTm, String> customerRequestStatusColumn;
    @FXML
    TableColumn<RequestTm, String> requestStatusColumn;
    @FXML
    TableColumn<RequestTm, String> houseIdColumn;
    @FXML
    TableColumn<RequestTm, String> actionColumn;


    private final RequestModel requestModel = new RequestModel();
    private ObservableList<RequestTm> tableData;

    public RequestController() throws SQLException, ClassNotFoundException {
    }


    @FXML
    void addNewBuyRequestOnAction(ActionEvent event) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewBuyRequest.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void addNewRentRequestOnAction(ActionEvent event) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewRentRequest.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void addressTxtKeyReleased(KeyEvent event) {

    }


    @FXML
    void addressTxtOnMouseClicked(MouseEvent event) {

    }


    @FXML
    void deleteOnAction(ActionEvent event) { //delete eka fully hadanna

        RequestTm selectedRequest = table.getSelectionModel().getSelectedItem();

        if(selectedRequest==null){
            return;
        }

        if(selectedRequest.getQualifiedCustomerOrNot().equals("Yes")){

            if(selectedRequest.getRequestStatus().equals("Completed") || selectedRequest.getCustomerRequestStatus().equals("confirmed") || selectedRequest.getIsPaymentsCompleted().equals("Yes")){
                if(selectedRequest.getRequestStatus().equals("Completed")){

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("This Is Completed Request, Can't Delete");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();
                    return;
                }
                else if(selectedRequest.getIsPaymentsCompleted().equals("Yes")){

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("This Request Has Completed Payment, Can't Delete");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();
                    return;

                }
                else{

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("This Is Confirmed Request, Can't Delete");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();
                    return;
                }


            }

        }
        else{

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this request?");
            Optional<ButtonType> response = alert.showAndWait();

            deleteTheSelectedRequest(response,selectedRequest);
            //delete request
        }

        if(selectedRequest.getQualifiedCustomerOrNot().equals("Yes") && selectedRequest.getAllDocumentsProvided().equals("Yes") && selectedRequest.getAgreesToAllTermsAndConditions().equals("Yes")){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This customer is qualified, has agreed to all terms and conditions," +
                                                                    " and has provided all necessary documents. This request can be important" +
                                                                    " Are you sure you want to delete this request?");
            Optional<ButtonType> response = alert.showAndWait();

            deleteTheSelectedRequest(response,selectedRequest);

        }
        else if(selectedRequest.getAllDocumentsProvided().equals("Yes") && selectedRequest.getQualifiedCustomerOrNot().equals("Yes")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This customer is qualified, has agreed to all terms and conditions," +
                                                                        " This request can be important to business, Are you sure you want to delete this request?");
                Optional<ButtonType> response = alert.showAndWait();

                deleteTheSelectedRequest(response,selectedRequest);
            }

        else if(selectedRequest.getAgreesToAllTermsAndConditions().equals("Yes") && selectedRequest.getQualifiedCustomerOrNot().equals("Yes")){

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This customer is qualified, has agreed to all terms and conditions, " +
                                                                        "This request can be important to business, Are you sure you want to delete this request?");
                Optional<ButtonType> response = alert.showAndWait();

                deleteTheSelectedRequest(response,selectedRequest);


        }


    }


    public void deleteTheSelectedRequest(Optional<ButtonType> response,RequestTm selectedRequest){

        if(response.isPresent() && response.get()==ButtonType.OK){

            try {
                String result = requestModel.deleteSelectedRequest(selectedRequest);
                Notifications notifications = Notifications.create();
                notifications.title("Notification");
                notifications.text(result);
                notifications.hideCloseButton();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.CENTER);
                notifications.darkStyle();
                notifications.showInformation();

                loadTable();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @FXML
    void editOnAction(ActionEvent event) {

        RequestTm selectedRequest = table.getSelectionModel().getSelectedItem();

        if(selectedRequest==null){
            return;
        }

        if(selectedRequest.getRequestStatus().equals("Completed")){
            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("This Is Completed Request, Can't Edit");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();
            return;
        }

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/EditRentRequest.fxml"));
            Parent root = fxmlLoader.load();
            EditRentRequestController editRentRequestController = fxmlLoader.getController();
            editRentRequestController.setSelectedRequestData(selectedRequest);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void employeeIdCmbOnAction(ActionEvent event) {

    }

    @FXML
    void nameTxtKeyReleased(KeyEvent event) {

    }

    @FXML
    void nameTxtOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void positionCmbOnAction(ActionEvent event) {

    }


    @FXML
    void refreshOnAction(ActionEvent event) {

        loadTable();
    }


    @FXML
    void searchOnAction(ActionEvent event) {

    }


    @FXML
    void sortByCmbOnAction(ActionEvent event) {

    }


    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<RequestTm> requestTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            requestTms.add(tableData.get(i));
        }

        table.setItems(requestTms);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setColumnToTable();
        setValuesToTableRowCmb();
    }


    public void setColumnToTable(){

        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        rentOrBuyColumn.setCellValueFactory(new PropertyValueFactory<>("rentOrBuy"));
        houseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("houseType"));
        leaseTurnDesireColumn.setCellValueFactory(new PropertyValueFactory<>("leaseTurnDesire"));
        allDocumentsProvidedColumn.setCellValueFactory(new PropertyValueFactory<>("allDocumentsProvided"));
        qualifiedCustomerOrNotColumn.setCellValueFactory(new PropertyValueFactory<>("qualifiedCustomerOrNot"));
        agreesToAllTermsAndConditionsColumn.setCellValueFactory(new PropertyValueFactory<>("agreesToAllTermsAndConditions"));
        isPaymentsCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("isPaymentsCompleted"));
        customerRequestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("customerRequestStatus"));
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));


        Callback<TableColumn<RequestTm, String>, TableCell<RequestTm, String>> cellFoctory = (TableColumn<RequestTm, String> param) -> {

            final TableCell<RequestTm, String> cell = new TableCell<RequestTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\visibility.png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\building.png");
                        Image image3 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\ethics (1).png");
                        Image image4 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\rent.png");
                        ImageView viewDetails = new ImageView();
                        viewDetails.setImage(image1);
                        viewDetails.setFitHeight(20);
                        viewDetails.setFitWidth(20);

                        ImageView searchHouse = new ImageView();
                        searchHouse.setImage(image2);
                        searchHouse.setFitHeight(20);
                        searchHouse.setFitWidth(20);

                        ImageView process = new ImageView();
                        process.setImage(image3);
                        process.setFitHeight(20);
                        process.setFitWidth(20);

                        ImageView complete = new ImageView();
                        complete.setImage(image4);
                        complete.setFitHeight(20);
                        complete.setFitWidth(20);


                        viewDetails.setStyle(" -fx-cursor: hand ;");
                        searchHouse.setStyle(" -fx-cursor: hand ;");
                        process.setStyle(" -fx-cursor: hand ;");
                        complete.setStyle(" -fx-cursor: hand ;");


                        viewDetails.setOnMouseClicked((MouseEvent event) -> {

                            RequestTm request = table.getSelectionModel().getSelectedItem();

                           if(request.getRentOrBuy().equals("Rent")){

                               try{
                                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/RentRequestDetails.fxml"));
                                   Parent root = fxmlLoader.load();
                                   RentRequestDetailsController rentRequestDetailsController = fxmlLoader.getController();
                                   rentRequestDetailsController.setSelectedRequestData(request);
                                   Scene scene = new Scene(root);
                                   Stage stage = new Stage();
                                   stage.setScene(scene);
                                   stage.show();

                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }

                           else{

                               try{
                                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PurchaseRequestDetails.fxml"));
                                   Parent root = fxmlLoader.load();
                                   PurchaseRequestDetailsController purchaseRequestDetailsController = fxmlLoader.getController();
                                   purchaseRequestDetailsController.setSelectedRequestData(request);
                                   Scene scene = new Scene(root);
                                   Stage stage = new Stage();
                                   stage.setScene(scene);
                                   stage.show();

                               } catch (IOException e) {
                                   e.printStackTrace();
                               }

                           }

                        });

                        searchHouse.setOnMouseClicked((MouseEvent event) -> {

                            RequestTm request = table.getSelectionModel().getSelectedItem();

                            if(request.getRequestStatus().equals("Rejected") || request.getQualifiedCustomerOrNot().equals("No") || request.getAgreesToAllTermsAndConditions().equals("No") || request.getCustomerRequestStatus().equals("Canceled")){
                                return;
                            }

                            try{
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/RecommendedHouses.fxml"));
                                Parent root = fxmlLoader.load();
                                RecommendedHousesController recommendedHousesController = fxmlLoader.getController();
                                recommendedHousesController.setSelectedRequestData(request);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });

                        process.setOnMouseClicked((MouseEvent event) -> {

                            RequestTm request = table.getSelectionModel().getSelectedItem();

                            if(request.getRequestStatus().equals("Completed")){

                                if(request.getRentOrBuy().equals("Rent")){

                                    try {
                                        String response = requestModel.addNewTenant(request);
                                        loadTable();

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

                                else{
                                    System.out.println("Owner add will add lately,stay tune!");
                                }


                            }

                            else if(request.getRequestStatus().equals("Fixed")){

                                Notifications notifications = Notifications.create();
                                notifications.title("Notification");
                                notifications.text("Fixed Request");
                                notifications.hideCloseButton();
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.CENTER);
                                notifications.darkStyle();
                                notifications.showInformation();
                            }

                            else{
                                Notifications notifications = Notifications.create();
                                notifications.title("Notification");
                                notifications.text("Not A Completed Request");
                                notifications.hideCloseButton();
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.CENTER);
                                notifications.darkStyle();
                                notifications.showInformation();
                            }

                        });


                        complete.setOnMouseClicked((MouseEvent event) -> {

                            RequestTm request = table.getSelectionModel().getSelectedItem();

                            if(request.getRequestStatus().equals("Completed")){
                                return;
                            }

                            if(request.getCustomerRequestStatus().equals("Confirmed") && request.getAllDocumentsProvided().equals("Yes") && request.getAgreesToAllTermsAndConditions().equals("Yes") && request.getIsPaymentsCompleted().equals("Yes") && request.getQualifiedCustomerOrNot().equals("Yes") && !request.getHouseId().equals("-")){

                                try {
                                    String response = requestModel.makeRequestStatusCompleted(request);
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
                            else if(request.getRequestStatus().equals("Rejected")){
                                Notifications notifications = Notifications.create();
                                notifications.title("Notification");
                                notifications.text("Rejected Request");
                                notifications.hideCloseButton();
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.CENTER);
                                notifications.darkStyle();
                                notifications.showInformation();
                            }
                            else if(request.getCustomerRequestStatus().equals("Canceled")){
                                Notifications notifications = Notifications.create();
                                notifications.title("Notification");
                                notifications.text("The customer has canceled this request,Please mark this request as rejected");
                                notifications.hideCloseButton();
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.CENTER);
                                notifications.darkStyle();
                                notifications.showInformation();
                            }
                            else if(request.getQualifiedCustomerOrNot().equals("No")){
                                Notifications notifications = Notifications.create();
                                notifications.title("Notification");
                                notifications.text("Not A Qualified Customer,Please mark this request as rejected");
                                notifications.hideCloseButton();
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.CENTER);
                                notifications.darkStyle();
                                notifications.showInformation();
                            }
                            else if(request.getAgreesToAllTermsAndConditions().equals("No")){
                                Notifications notifications = Notifications.create();
                                notifications.title("Notification");
                                notifications.text("Customer Not Agreed With All Terms & Conditions,Please mark this request as rejected");
                                notifications.hideCloseButton();
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.CENTER);
                                notifications.darkStyle();
                                notifications.showInformation();
                            }
                            else{
                                Notifications notifications = Notifications.create();
                                notifications.title("Notification");
                                notifications.text("Can't Make This Request Complete, Because This Request Not Complete All Requires");
                                notifications.hideCloseButton();
                                notifications.hideAfter(Duration.seconds(5));
                                notifications.position(Pos.CENTER);
                                notifications.darkStyle();
                                notifications.showInformation();
                            }


                        });

                        HBox manageBtn = new HBox(viewDetails,searchHouse,complete,process);


                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3); // Adjust spacing between buttons
                        manageBtn.setPadding(new Insets(2)); // Add padding around the HBox

                        HBox.setMargin(viewDetails, new Insets(2, 2, 0, 3));
                        HBox.setMargin(searchHouse, new Insets(2, 3, 0, 3));
                        HBox.setMargin(complete, new Insets(2, 3, 0, 3));
                        HBox.setMargin(process, new Insets(2, 3, 0, 3));

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
            tableData = requestModel.getAllRequests();
            System.out.println("Number of requests: " + tableData.size());
            table.setItems(tableData);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"problem in sql");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"class not found");
            alert.showAndWait();
        }

    }


    public void setValuesToTableRowCmb(){

        int count = 0;
        ObservableList<Integer> tableRows = FXCollections.observableArrayList();

        for (RequestTm x : tableData){
            count++;
            tableRows.add(count);

        }

        tableRowsCmb.setItems(tableRows);
        tableRowsCmb.getSelectionModel().selectLast();
    }

}
