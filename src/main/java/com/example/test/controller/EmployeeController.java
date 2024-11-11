package com.example.test.controller;

import com.example.test.dto.tm.EmployeeTm;
import com.example.test.dto.tm.UnitTm;
import com.example.test.model.EmployeeModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private EmployeeModel employeeModel;
    private ObservableList<EmployeeTm> tableData;
    private ObservableList<String> addresses;
    private ObservableList<String> names;
    private String employeeId;
    private String position;


    public EmployeeController(){

        try {
            employeeModel = new EmployeeModel();
        }
        catch (Exception e){
            e.printStackTrace();
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
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }



    @FXML
    void deleteOnAction(ActionEvent event) {

        EmployeeTm selectedItem = table.getSelectionModel().getSelectedItem();

        if(selectedItem==null){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("You should first select employee to delete");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

        }
        else{

            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the employee?");
            Optional<ButtonType> options = a1.showAndWait();

            if (options.isPresent() && options.get() == ButtonType.OK) {

                try {
                    String response = employeeModel.deleteEmployee(selectedItem);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, response);
                    alert.showAndWait();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            table.getSelectionModel().clearSelection();
            loadTable();
        }

    }


    @FXML
    void editOnAction(ActionEvent event) {

        EmployeeTm selectedItem = table.getSelectionModel().getSelectedItem();

        if(selectedItem==null){

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("You should first select employee to update");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

        }
        else{

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddNewEmployee.fxml"));
                Parent root = fxmlLoader.load();
                AddNewEmployeeController addNewEmployeeController = fxmlLoader.getController();
                addNewEmployeeController.setSelectedItemData(selectedItem);
                Scene scene = new Scene(root);
                Stage s1 = new Stage();
                s1.setScene(scene);
                s1.show();
            }
            catch (IOException e){
                e.printStackTrace();
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
        }

        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

        if(input.isEmpty()){
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
        }

        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

        if(input.isEmpty()){
            names.clear();
        }
    }


    @FXML
    void nameTxtOnMouseClicked(MouseEvent event){

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

        //table.setItems(tableData);
        clean();
    }


    @FXML
    void searchOnAction(ActionEvent event) {

        ObservableList<EmployeeTm> searchedEmployees = FXCollections.observableArrayList();

        if(employeeId!=null && !employeeId.equals("Select")){

            try {
                ObservableList<EmployeeTm> employer = employeeModel.getEmployeeById(employeeId);
                if(employer.isEmpty()){
                    return;
                }
                else{
                    table.setItems(employer);
                }

            }
            catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
                alert.showAndWait();
            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
                alert.showAndWait();
            }

        }
        else if(!nameTxt.getText().isEmpty()){

            try {
                ObservableList<EmployeeTm> employer = employeeModel.getEmployeeByName(nameTxt.getText());
                if(employer.isEmpty()){
                    return;
                }
                else{
                    table.setItems(employer);
                }

            }
            catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
                alert.showAndWait();
            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
                alert.showAndWait();
            }

        }

        else if(!addressTxt.getText().isEmpty()){

            try {
                ObservableList<EmployeeTm> employer = employeeModel.getEmployeeByAddress(addressTxt.getText());
                if(employer.isEmpty()){
                    return;
                }
                else{
                    table.setItems(employer);
                }

            }
            catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
                alert.showAndWait();
            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
                alert.showAndWait();
            }

        }

        else if(position!=null && !position.equals("Select")){

            try {
                ObservableList<EmployeeTm> employer = employeeModel.getEmployeeByPosition(position);
                if(employer.isEmpty()){
                    return;
                }
                else{
                    table.setItems(employer);
                }

            }
            catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
                alert.showAndWait();
            } catch (ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
                alert.showAndWait();
            }

        }

    }


    @FXML
    void sortCmbOnAction(ActionEvent event) {

        String sortType = sortCmb.getSelectionModel().getSelectedItem();
        ObservableList<EmployeeTm> employeeTms = tableData;

        if(sortType==null){
            return;
        }

        if(sortType.equals("employee Id asc")){

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
        else if(sortType.equals("employee Id desc")){

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
        else if(sortType.equals("name asc")){

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
        else if(sortType.equals("name desc")){

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

        else if(sortType.equals("dob desc")){

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

        else if(sortType.equals("dob asc")){

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
        else if(sortType.equals("basic salary asc")){

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

        else if(sortType.equals("basic salary desc")){

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

        else if(sortType.equals("allowance asc")){

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

        else if(sortType.equals("allowance desc")){

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
        System.out.println(value);

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
            throw new RuntimeException(e);
        }

    }


    public void setEmployeeIdCmbValues(){

        try {
            ObservableList<String> employeeIds = employeeModel.getAllEmployeesId();
            employeeIdCmb.setItems(employeeIds);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
        }

    }


    public void setPositionCmbValues(){

        try {
            ObservableList<String> allDistinctPositions = employeeModel.getAllDistinctPositions();
            positionCmb.setItems(allDistinctPositions);

        }catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at sql query");
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Problem at found class");
            alert.showAndWait();
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
        sortTypes.addAll("Sort By","employee Id asc","employee Id desc","name asc","name desc","basic salary asc","basic salary desc","allowance asc","allowance desc","dob asc","dob desc");
        sortCmb.setItems(sortTypes);

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

    }

}

