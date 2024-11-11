package com.example.test.controller;

import com.example.test.dto.tm.CustomerTm;
import com.example.test.dto.tm.EmployeeTm;
import com.example.test.model.CustomerModel;
import com.example.test.validation.UserInputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<CustomerTm> table;

    @FXML
    private TableColumn<CustomerTm, String> clientIdColumn;

    @FXML
    private TableColumn<CustomerTm, String> nameColumn;

    @FXML
    private TableColumn<CustomerTm, String> nicColumn;

    @FXML
    private TableColumn<CustomerTm, String> addressColumn;

    @FXML
    private TableColumn<CustomerTm, String> phoneNoColumn;

    @FXML
    private TableColumn<CustomerTm, String> jobTitleColumn;

    @FXML
    private TableColumn<CustomerTm, String> livingArrangementColumn;

    @FXML
    private Button addNewClientbtn;

    @FXML
    private ComboBox<Integer> tableRowCmb;

    @FXML
    private Button deletebtn;

    @FXML
    private ComboBox<String> sortByCmb;

    @FXML
    private Button searchbtn;

    @FXML
    private Button refreshbtn;

    @FXML
    private TextField nictxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField jobTitleTxt;

    @FXML
    private ListView<String> nicList;

    @FXML
    private ListView<String> jobTitleList;

    @FXML
    private ComboBox<String> livingArrangementCmb;

    @FXML
    private ComboBox<String> customerIdCmb;

    @FXML
    private TextField phoneNoTxt;

    @FXML
    private Button searchPhoneNoBtn;

    @FXML
    private ListView<String> phoneNoList;

    @FXML
    private ListView<String> nameList;


    private ObservableList<CustomerTm> tableData;
    private final CustomerModel customerModel = new CustomerModel();
    private ObservableList<String> phoneNumbers = FXCollections.observableArrayList();
    private ObservableList<String> nicNumbers = FXCollections.observableArrayList();
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<String> jobTitles = FXCollections.observableArrayList();


    @FXML
    void addNewClientOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewCustomer.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }



    @FXML
    void customerIdCmbOnAction(ActionEvent event) {

    }


    @FXML
    void deleteOnAction(ActionEvent event) {//should change this method after request implement

        CustomerTm selectedItem = table.getSelectionModel().getSelectedItem();

        if(selectedItem==null){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("You should first select customer to delete");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

        }
        else{

            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the customer?");
            Optional<ButtonType> options = a1.showAndWait();

            if (options.isPresent() && options.get() == ButtonType.OK) {

                try {
                    String response = customerModel.deleteCustomer(selectedItem);
                    loadTable();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                    alert.showAndWait();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            table.getSelectionModel().clearSelection();
        }

    }


    @FXML
    void editOnAction(ActionEvent event) {

        CustomerTm selectedItem = table.getSelectionModel().getSelectedItem();

        if(selectedItem==null){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("You should first select customer to update");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

        }
        else{

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewCustomer.fxml"));
                Parent root = fxmlLoader.load();
                AddNewCustomerController addNewCustomerController = fxmlLoader.getController();
                addNewCustomerController.selectedCustomerData(selectedItem);
                Scene scene = new Scene(root);
                Stage s1 = new Stage();
                s1.setScene(scene);
                s1.show();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        table.getSelectionModel().clearSelection();

    }


    @FXML
    void jobTitleListOnMouseClicked(MouseEvent event) {

        jobTitleTxt.setText(jobTitleList.getSelectionModel().getSelectedItem());
        jobTitleList.getItems().clear();

    }


    @FXML
    void jobTitleTxtOnKeyReleased(KeyEvent event) {

        String input = jobTitleTxt.getText();


        try {
            jobTitles = customerModel.getJobTitlesSuggestions(input);
            jobTitleList.setItems(jobTitles);
        }

        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

        if(input.isEmpty()){
            jobTitles.clear();
        }

    }


    @FXML
    void livingArrangementCmbOnAction(ActionEvent event) {

    }


    @FXML
    void nameListOnMouseClicked(MouseEvent event) {

        nameTxt.setText(nameList.getSelectionModel().getSelectedItem());
        nameList.getItems().clear();

    }


    @FXML
    void nameTxtOnKeyReleased(KeyEvent event) {

        String input = nameTxt.getText();


        try {
            customerNames = customerModel.getNameSuggestions(input);
            nameList.setItems(customerNames);
        }

        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

        if(input.isEmpty()){
            customerNames.clear();
        }

    }


    @FXML
    void nicListOnMouseClicked(MouseEvent event) {

        nictxt.setText(nicList.getSelectionModel().getSelectedItem());
       // nicNumbers.clear();
        nicList.getItems().clear();

    }



    @FXML
    void nicTxtOnKeyReleased(KeyEvent event) {

        String input = nictxt.getText();


        try {
            nicNumbers = customerModel.getNicSuggestions(input);
            nicList.setItems(nicNumbers);
        }

        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

        if(input.isEmpty()){
            nicNumbers.clear();
        }

    }



    @FXML
    void phoneNoListOnMouseClicked(MouseEvent event) {

        phoneNoTxt.setText(phoneNoList.getSelectionModel().getSelectedItem());
        phoneNoList.getItems().clear();
    }



    @FXML
    void phoneNoTxtOnKeyReleased(KeyEvent event) {

        String input = phoneNoTxt.getText();


        try {
            phoneNumbers = customerModel.getPhoneNoSuggestions(input);
            phoneNoList.setItems(phoneNumbers);
        }

        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

        if(input.isEmpty()){
            phoneNumbers.clear();
        }

    }



    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();
    }



    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<CustomerTm> searchedEmployees;

        String customerId = customerIdCmb.getSelectionModel().getSelectedItem();

        if(customerId!=null && !customerId.equals("Select")) {

            try {
                searchedEmployees = customerModel.getCustomerById(customerId);
                if (searchedEmployees.isEmpty()) {
                    return;
                } else {
                    table.setItems(searchedEmployees);
                }

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Problem at sql query");
                alert.showAndWait();
            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Problem at found class");
                alert.showAndWait();
            }

        }

    }



    @FXML
    void searchPhoneNoOnAction(ActionEvent event) {

        String phoneNo = phoneNoTxt.getText();

        if(phoneNo.isEmpty()){
            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Enter Phone Number to search");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();
            return;
        }

        boolean b1 = UserInputValidation.checkPhoneNoValidation(phoneNo);

        if(!b1){
            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Not a valid phone no");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

            phoneNoTxt.setText("");
        }

        else{
            try {
                ObservableList<CustomerTm> customersSearchByPhoneNo = customerModel.searchCustomerAlreadyExistOrNot(phoneNo);

                if(customersSearchByPhoneNo.isEmpty()){

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("No registered customer in this phone number, Please Add to the system as a new customer");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewCustomer.fxml"));
                        Parent root = fxmlLoader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                }

                else{

                    Notifications notifications = Notifications.create();
                    notifications.title("Notification");
                    notifications.text("Registered phone no");
                    notifications.hideCloseButton();
                    notifications.hideAfter(Duration.seconds(5));
                    notifications.position(Pos.CENTER);
                    notifications.darkStyle();
                    notifications.showInformation();

                    table.setItems(customersSearchByPhoneNo);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            phoneNoTxt.setText("");
        }

    }


    @FXML
    void sortByCmbOnAction(ActionEvent event) {

        String sortType = sortByCmb.getSelectionModel().getSelectedItem();
        ObservableList<CustomerTm> customerTms = tableData;

        if(sortType==null){
            return;
        }

        if(sortType.equals("Customer Id asc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getCustomerId().compareTo(customerTms.get(i + 1).getCustomerId())>0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

        else if(sortType.equals("Customer Id desc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getCustomerId().compareTo(customerTms.get(i + 1).getCustomerId())<0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

        else if(sortType.equals("Name asc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getName().compareTo(customerTms.get(i + 1).getName())>0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

        else if(sortType.equals("Name desc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getName().compareTo(customerTms.get(i + 1).getName())<0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

        else if(sortType.equals("NIC asc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getNic().compareTo(customerTms.get(i + 1).getNic())>0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

        else if(sortType.equals("NIC desc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getNic().compareTo(customerTms.get(i + 1).getNic())<0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

        else if(sortType.equals("Living Arrangement asc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getLivingArrangement().compareTo(customerTms.get(i + 1).getLivingArrangement())>0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

        else if(sortType.equals("Living Arrangement desc")){

            for(int j = 0; j < customerTms.size(); j++) {
                for (int i = 0; i < customerTms.size()-1; i++) {
                    if (customerTms.get(i).getLivingArrangement().compareTo(customerTms.get(i + 1).getLivingArrangement())<0) {
                        CustomerTm temp = customerTms.get(i);
                        customerTms.set(i, customerTms.get(i + 1));
                        customerTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(customerTms);
        }

    }

    @FXML
    void tableOnSort(ActionEvent event) {

    }


    @FXML
    void tableRowCmbOnAction(ActionEvent event) {

        Integer value = tableRowCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<CustomerTm> customerTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            customerTms.add(tableData.get(i));
        }

        table.setItems(customerTms);

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumnsValues();
        loadTable();
        setCustomerIdCmbValues();
        setLivingArrangementCmbValues();
        setRowCmbValues();
        setSortByCmbValues();

    }


    public void setTableColumnsValues(){

        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nicColumn.setCellValueFactory(new PropertyValueFactory<>("nic"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNoColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        livingArrangementColumn.setCellValueFactory(new PropertyValueFactory<>("livingArrangement"));

    }


    public void loadTable(){

        try {
            tableData = customerModel.getAllCustomers();
            table.setItems(tableData);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"problem in sql");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"class not found");
            alert.showAndWait();
        }

    }


    public void setCustomerIdCmbValues(){

        try {
            ObservableList<String> customerIds = customerModel.getAllCustomersId();
            customerIdCmb.setItems(customerIds);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

    }


    public void setLivingArrangementCmbValues(){

        ObservableList<String> livingArrangement = FXCollections.observableArrayList("Select","Single Living Arrangement","Couple Living Arrangement","Family Living Arrangement");
        livingArrangementCmb.setItems(livingArrangement);

    }


    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (CustomerTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowCmb.setItems(rows);
        tableRowCmb.getSelectionModel().selectLast();

    }


    public void setSortByCmbValues(){

        ObservableList<String> sortTypes = FXCollections.observableArrayList();
        sortTypes.addAll("Sort By","Customer Id asc","Customer Id desc","Name asc","Name desc","NIC asc","NIC desc","Living Arrangement asc","Living Arrangement desc");
        sortByCmb.setItems(sortTypes);

    }


    public void clean(){

        loadTable();
        setCustomerIdCmbValues();
        setRowCmbValues();
        sortByCmb.getSelectionModel().selectFirst();
        customerIdCmb.getSelectionModel().selectFirst();
        livingArrangementCmb.getSelectionModel().selectFirst();
        nameTxt.setText("");
        nictxt.setText("");
        jobTitleTxt.setText("");
        phoneNoTxt.setText("");

    }

}
