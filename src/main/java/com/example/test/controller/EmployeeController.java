package com.example.test.controller;

import com.example.test.dto.tm.EmployeeTm;
import com.example.test.model.EmployeeModel;
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    public ListView<String> addressList;
    @FXML
    public ListView<String> nameList;
    @FXML
    private Button editbtn;

    @FXML
    private TableView<EmployeeTm> table;

    @FXML
    private TableColumn<EmployeeTm, String> employeeIdColumn;

    @FXML
    private TableColumn<EmployeeTm, String> nameColumn;

    @FXML
    private TableColumn<EmployeeTm, String> addressColumn;

    @FXML
    private TableColumn<EmployeeTm, Integer> phoneNoColumn;

    @FXML
    private TableColumn<EmployeeTm, Double> basicSalaryColumn;

    @FXML
    private TableColumn<EmployeeTm, Double> allowancesColumn;

    @FXML
    private TableColumn<EmployeeTm, String> dobColumn;

    @FXML
    private TableColumn<EmployeeTm, String> positionColumn;

    @FXML
    private Button addNewEmployeeBtn;

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
    private ComboBox<String> employeeIdCmb;

    @FXML
    private ComboBox<String> positionCmb;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField addressTxt;

    @FXML
    private TextField searchTxt;

    private EmployeeModel employeeModel;
    private ObservableList<EmployeeTm> tableData;
    private ObservableList<String> addresses;
    private ObservableList<String> names;
    private String employeeId;
    private String position;


    public EmployeeController() {

        try {
            employeeModel = new EmployeeModel();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while initializing employee controller: " + e.getMessage());
            notification("An error occurred while loading Employee page. Please try again or contact support.");
        }

    }


    @FXML
    void addNewEmployeeOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewEmployee.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while saving new floor: " + e.getMessage());
            notification("An error occurred while saving the new floor. Please try again or contact support.");
        }


    }


    @FXML
    void deleteOnAction(ActionEvent event) {

        EmployeeTm selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        } else {
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete selected employee?");
            a1.getButtonTypes().setAll(yesButton, cancelButton);
            Optional<ButtonType> options = a1.showAndWait();

            if (options.isPresent() && options.get() == yesButton) {

                try {
                    String response = employeeModel.deleteEmployee(selectedItem);
                    notification(response);

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error while deleting the employee: " + e.getMessage());
                    notification("An error occurred while deleting the employee id: " + selectedItem.getEmployeeId() + ", Please try again or contact support.");
                }

            }

            table.getSelectionModel().clearSelection();
            loadTable();
        }

    }


    @FXML
    void editOnAction(ActionEvent event) {

        EmployeeTm selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        } else {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewEmployee.fxml"));
                Parent root = fxmlLoader.load();
                AddNewEmployeeController addNewEmployeeController = fxmlLoader.getController();
                addNewEmployeeController.setSelectedItemData(selectedItem);
                Scene scene = new Scene(root);
                Stage s1 = new Stage();
                s1.setScene(scene);
                s1.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while loading Add New Employee Page: " + e.getMessage());
                notification("An error occurred while loading Add New Employee Page. Please try again or contact support.");
            }
        }
    }


    @FXML
    void getSelectedRow(MouseEvent event) {

        EmployeeTm selectedItem = table.getSelectionModel().getSelectedItem();

    }


    @FXML
    void addressTxtKeyReleased(KeyEvent keyEvent) {

        String input = addressTxt.getText();

        try {
            addresses = employeeModel.getEmployeeAddresses(input);
            addressList.setItems(addresses);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while suggesting employee addresses: " + e.getMessage());
            notification("An error occurred while suggesting employee addresses. Please try again or contact support.");
        }

        if (input.isEmpty()) {
            addresses.clear();
        }

    }

    @FXML
    public void addressTxtOnMouseClicked(MouseEvent event) {

        addressTxt.setText(addressList.getSelectionModel().getSelectedItem());
        addresses.clear();

    }


    @FXML
    public void nameTxtKeyReleased(KeyEvent keyEvent) {

        String input = nameTxt.getText();

        try {
            names = employeeModel.getEmployeeNames(input);
            nameList.setItems(names);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while suggesting employee names: " + e.getMessage());
            notification("An error occurred while suggesting employee names. Please try again or contact support.");
        }

        if (input.isEmpty()) {
            names.clear();
        }
    }


    @FXML
    void nameTxtOnMouseClicked(MouseEvent event) {

        nameTxt.setText(nameList.getSelectionModel().getSelectedItem());
        names.clear();

    }


    @FXML
    void employeeIdCmbOnAction(ActionEvent event) {

        employeeId = employeeIdCmb.getSelectionModel().getSelectedItem();
        System.out.println(employeeId);

    }


    @FXML
    void positionCmbOnAction(ActionEvent event) {

        position = positionCmb.getSelectionModel().getSelectedItem();
        System.out.println(position);

    }


    @FXML
    void refreshOnAction(ActionEvent event) {

        clean();
    }


    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<EmployeeTm> searchedEmployees = FXCollections.observableArrayList();

        String selectedEmployeeId = employeeIdCmb.getValue();
        String selectedName = nameTxt.getText();
        String selectedAddress = addressTxt.getText();
        String selectedPosition = positionCmb.getValue();

        boolean employeeIdSelected = selectedEmployeeId != null && !selectedEmployeeId.equals("Select");
        boolean nameSelected = selectedName != null && !selectedName.isEmpty();
        boolean addressSelected = selectedAddress != null && !selectedAddress.isEmpty();
        boolean positionSelected = selectedPosition != null && !selectedPosition.equals("Select");

        if (employeeIdSelected) {

            ObservableList<EmployeeTm> employeesById = getEmployeeById(selectedEmployeeId);

            if (employeesById.isEmpty()) {
                table.setItems(employeesById);
            } else {
                searchedEmployees.addAll(employeesById);

                if (nameSelected) {
                    ObservableList<EmployeeTm> filteredByName = filterEmployeesByName(searchedEmployees, selectedName);
                    searchedEmployees.clear();
                    searchedEmployees.addAll(filteredByName);
                }

                if (addressSelected) {
                    ObservableList<EmployeeTm> filteredByAddress = filterEmployeesByAddress(searchedEmployees, selectedAddress);
                    searchedEmployees.clear();
                    searchedEmployees.addAll(filteredByAddress);
                }

                if (positionSelected) {
                    ObservableList<EmployeeTm> filteredByPosition = filterEmployeesByPosition(searchedEmployees, selectedPosition);
                    searchedEmployees.clear();
                    searchedEmployees.addAll(filteredByPosition);
                }

                table.setItems(searchedEmployees);
            }

        } else if (nameSelected || addressSelected || positionSelected) {

            ObservableList<EmployeeTm> allEmployees = tableData;
            searchedEmployees.addAll(allEmployees);

            if (nameSelected) {
                searchedEmployees = filterEmployeesByName(searchedEmployees, selectedName);
            }

            if (addressSelected) {
                searchedEmployees = filterEmployeesByAddress(searchedEmployees, selectedAddress);
            }

            if (positionSelected) {
                searchedEmployees = filterEmployeesByPosition(searchedEmployees, selectedPosition);
            }

            table.setItems(searchedEmployees);

        } else {
            ObservableList<EmployeeTm> allEmployees = tableData;
            table.setItems(allEmployees);
        }
    }



    public ObservableList<EmployeeTm> getEmployeeById(String employeeId) {

        ObservableList<EmployeeTm> filteredEmployees = FXCollections.observableArrayList();
        for (EmployeeTm employee : tableData) {
            if (employee.getEmployeeId().equals(employeeId)) {
                filteredEmployees.add(employee);
            }
        }
        return filteredEmployees;
    }


    public ObservableList<EmployeeTm> filterEmployeesByName(ObservableList<EmployeeTm> employees, String name) {

        ObservableList<EmployeeTm> filteredEmployees = FXCollections.observableArrayList();
        for (EmployeeTm employee : employees) {
            if (employee.getName().equalsIgnoreCase(name)) {
                filteredEmployees.add(employee);
            }
        }
        return filteredEmployees;
    }


    public ObservableList<EmployeeTm> filterEmployeesByAddress(ObservableList<EmployeeTm> employees, String address) {

        ObservableList<EmployeeTm> filteredEmployees = FXCollections.observableArrayList();
        for (EmployeeTm employee : employees) {
            if (employee.getAddress().equalsIgnoreCase(address)) {
                filteredEmployees.add(employee);
            }
        }
        return filteredEmployees;
    }


    public ObservableList<EmployeeTm> filterEmployeesByPosition(ObservableList<EmployeeTm> employees, String position) {

        ObservableList<EmployeeTm> filteredEmployees = FXCollections.observableArrayList();
        for (EmployeeTm employee : employees) {
            if (employee.getPosition().equalsIgnoreCase(position)) {
                filteredEmployees.add(employee);
            }
        }
        return filteredEmployees;
    }



    @FXML
    void sortCmbOnAction(ActionEvent event) {

        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<EmployeeTm> employeeTms = tableData;

        if(sortType==null){
            return;
        }

        if(sortType.equals("Employee ID (Ascending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getEmployeeId().compareTo(employeeTms.get(i + 1).getEmployeeId())>0) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }
        else if(sortType.equals("Employee ID (Descending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getEmployeeId().compareTo(employeeTms.get(i + 1).getEmployeeId())<0) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }
        else if(sortType.equals("Name (Ascending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getName().compareTo(employeeTms.get(i + 1).getName())>0) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }
        else if(sortType.equals("Name (Descending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getName().compareTo(employeeTms.get(i + 1).getName())<0) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);

        }

        else if(sortType.equals("Date of Birth (Descending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getDob().compareTo(employeeTms.get(i + 1).getDob())<0) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }

        else if(sortType.equals("Date of Birth (Ascending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getDob().compareTo(employeeTms.get(i + 1).getDob())>0) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);

        }
        else if(sortType.equals("Basic Salary (Ascending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getBasicSalary() > employeeTms.get(i + 1).getBasicSalary()) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }

        else if(sortType.equals("Basic Salary (Descending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getBasicSalary() < employeeTms.get(i + 1).getBasicSalary()) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }

        else if(sortType.equals("Allowance (Ascending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getAllowances() > employeeTms.get(i + 1).getAllowances()) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }

        else if(sortType.equals("Allowance (Descending)")){

            for(int j = 0; j < employeeTms.size(); j++) {
                for (int i = 0; i < employeeTms.size()-1; i++) {
                    if (employeeTms.get(i).getAllowances() < employeeTms.get(i + 1).getAllowances()) {
                        EmployeeTm temp = employeeTms.get(i);
                        employeeTms.set(i, employeeTms.get(i + 1));
                        employeeTms.set((i + 1), temp);

                    }
                }
            }
            table.setItems(employeeTms);
        }
    }


    @FXML
    void tableRowsCmbOnAction(ActionEvent event) {

        Integer value = tableRowsCmb.getSelectionModel().getSelectedItem();

        if(value==null){
            return;
        }

        ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            employeeTms.add(tableData.get(i));
        }

        table.setItems(employeeTms);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumnsValues();
        loadTable();
        setEmployeeIdCmbValues();
        setPositionCmbValues();
        setRowCmbValues();
        setSortCmbValues();
        tableSearch();

    }


    public void tableSearch() {

        FilteredList<EmployeeTm> filteredData = new FilteredList<>(tableData, b -> true);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (employee.getEmployeeId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getPhoneNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(employee.getBasicSalary()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(employee.getAllowances()).contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getDob().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getPosition().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<EmployeeTm> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }



    public void setTableColumnsValues(){

        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNoColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        basicSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
        allowancesColumn.setCellValueFactory(new PropertyValueFactory<>("allowances"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

    }

    public void loadTable(){

        try {
            tableData = employeeModel.getAllEmployees();
            table.setItems(tableData);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while loading table data: " + e.getMessage());
            notification("An error occurred while loading table data. Please try again or contact support.");
        }

    }


    public void setEmployeeIdCmbValues(){

        try {
            ObservableList<String> employeeIds = employeeModel.getAllEmployeesId();
            employeeIdCmb.setItems(employeeIds);
            employeeIdCmb.getSelectionModel().selectFirst();
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting values to employee combo box: " + e.getMessage());
            notification("An error occurred while setting values to employee combo box. Please try again or contact support.");
        }

    }


    public void setPositionCmbValues(){

        try {
            ObservableList<String> allDistinctPositions = employeeModel.getAllDistinctPositions();
            positionCmb.setItems(allDistinctPositions);
            positionCmb.getSelectionModel().selectFirst();

        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error while setting values to position combo box: " + e.getMessage());
            notification("An error occurred while setting values to position combo box. Please try again or contact support.");
        }

    }


    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (EmployeeTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }


    public void setSortCmbValues(){

        ObservableList<String> sortTypes = FXCollections.observableArrayList();
        sortTypes.addAll("Sort By", "Employee ID (Ascending)", "Employee ID (Descending)", "Name (Ascending)", "Name (Descending)", "Basic Salary (Ascending)", "Basic Salary (Descending)", "Allowance (Ascending)", "Allowance (Descending)", "Date of Birth (Ascending)", "Date of Birth (Descending)");
        sortCmb.setItems(sortTypes);
        sortCmb.getSelectionModel().selectFirst();

    }


    public void clean(){

        loadTable();
        setEmployeeIdCmbValues();
        setPositionCmbValues();
        setRowCmbValues();
        sortCmb.getSelectionModel().select(0);
        positionCmb.getSelectionModel().selectFirst();
        employeeIdCmb.getSelectionModel().selectFirst();
        nameTxt.setText("");
        addressTxt.setText("");
        table.getSelectionModel().getSelectedItem();
        searchTxt.clear();


    }


    public void notification(String message){

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text(message);
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();
    }

}

