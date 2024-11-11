package com.example.test.controller;

import com.example.test.dto.tm.CustomerTm;
import com.example.test.dto.tm.RequestTm;
import com.example.test.dto.tm.TenantTm;
import com.example.test.model.TenantModel;
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
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TenantController implements Initializable {

    @FXML
    private Button editbtn;

    @FXML
    private TableView<TenantTm> table;

    @FXML
    private TableColumn<TenantTm, String> tenantIdColumn;

    @FXML
    private TableColumn<TenantTm, String> nameColumn;

    @FXML
    private TableColumn<TenantTm, String> phoneNoColumn;

    @FXML
    private TableColumn<TenantTm, Integer> membersCountColumn;

    @FXML
    private TableColumn<TenantTm, String> startDateColumn;

    @FXML
    private TableColumn<TenantTm, Double> monthlyRentColumn;

    @FXML
    private TableColumn<TenantTm, String> lastPaidMonthColumn;

    @FXML
    private TableColumn<TenantTm, String> houseIdColumn;

    @FXML
    private TableColumn<TenantTm, String> actionColumn;

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
    private ComboBox<String> monthsCmb;

    @FXML
    private TextField nameTxt;

    @FXML
    private ListView<String> nameList;

    @FXML
    private TextField membersCountTxt;

    @FXML
    private ComboBox<String> tenantIdCmb;

    @FXML
    private ComboBox<String> houseIdCmb;

    @FXML
    private TextField searchTxt;

    private final TenantModel tenantModel = new TenantModel();
    private ObservableList<TenantTm> tableData;
    private ObservableList<String> names = FXCollections.observableArrayList();



    @FXML
    void deleteOnAction(ActionEvent event) {//no need

    }

    @FXML
    void editOnAction(ActionEvent event) {//no need

    }

    @FXML
    void getSelectedRow(MouseEvent event) {//no need

    }


    @FXML
    void nameTxtKeyReleased(KeyEvent event) {

        String input = nameTxt.getText();


        try {
            names = tenantModel.getNameSuggestions(input);
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
    void nameListOnMouseClicked(MouseEvent event) {

        nameTxt.setText(nameList.getSelectionModel().getSelectedItem());
        nameList.getItems().clear();
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

        ObservableList<TenantTm> tenantTms = FXCollections.observableArrayList();

        for (int i=0; i<value; i++){
            tenantTms.add(tableData.get(i));
        }

        table.setItems(tenantTms);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTableColumnsValue();
        setRowCmbValues();
        setTenantIdCmbValues();
        setMonthsCmbValues();
        setHouseIdCmbValues();

    }

    public void setHouseIdCmbValues(){

        try {
            ObservableList<String> houseIds = tenantModel.getHouseIds();
            houseIdCmb.setItems(houseIds);
            houseIdCmb.getSelectionModel().selectFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public void setMonthsCmbValues(){

        ObservableList<String> months = FXCollections.observableArrayList( "Select","January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");

        monthsCmb.setItems(months);
        monthsCmb.getSelectionModel().selectFirst();

    }

    public void setTenantIdCmbValues(){

        ObservableList<String> tenantIds = FXCollections.observableArrayList();
        tenantIds.add("Select");

        for(TenantTm x : tableData){
          tenantIds.add(x.getTenantId());
        }

        tenantIdCmb.setItems(tenantIds);
        tenantIdCmb.getSelectionModel().selectFirst();

    }

    public void setRowCmbValues(){

        ObservableList<Integer> rows = FXCollections.observableArrayList();
        int count = 0;

        for (TenantTm x : tableData){
            count++;
            rows.add(count);

        }

        tableRowsCmb.setItems(rows);
        tableRowsCmb.getSelectionModel().selectLast();

    }

    public void setTableColumnsValue(){

        tenantIdColumn.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNoColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        membersCountColumn.setCellValueFactory(new PropertyValueFactory<>("membersCount"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentStartDate"));
        monthlyRentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyRent"));
        lastPaidMonthColumn.setCellValueFactory(new PropertyValueFactory<>("lastPaidMonth"));
        houseIdColumn.setCellValueFactory(new PropertyValueFactory<>("houseId"));


        Callback<TableColumn<TenantTm, String>, TableCell<TenantTm, String>> cellFoctory = (TableColumn<TenantTm, String> param) -> {

            final TableCell<TenantTm, String> cell = new TableCell<TenantTm, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Image image1 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\visibility.png");
                        Image image2 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\editText.png");
                        Image image3 = new Image("C:\\Users\\Laptop World\\IdeaProjects\\test\\src\\main\\resources\\image\\accounting (3).png");

                        ImageView viewDetails = new ImageView();
                        viewDetails.setImage(image1);
                        viewDetails.setFitHeight(20);
                        viewDetails.setFitWidth(20);

                        ImageView edit = new ImageView();
                        edit.setImage(image2);
                        edit.setFitHeight(20);
                        edit.setFitWidth(20);

                        ImageView paymentHistory = new ImageView();
                        paymentHistory.setImage(image3);
                        paymentHistory.setFitHeight(20);
                        paymentHistory.setFitWidth(20);


                        viewDetails.setStyle(" -fx-cursor: hand ;");
                        edit.setStyle(" -fx-cursor: hand ;");
                        paymentHistory.setStyle(" -fx-cursor: hand ;");


                        viewDetails.setOnMouseClicked((MouseEvent event) -> {

                            TenantTm SelectedTenant = table.getSelectionModel().getSelectedItem();

                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TenantDetails.fxml"));
                                Parent root = fxmlLoader.load();
                                TenantDetailsController tenantDetailsController = fxmlLoader.getController();
                                tenantDetailsController.setSelectedTenantDetails(SelectedTenant);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        });

                        edit.setOnMouseClicked((MouseEvent event) -> {

                            TenantTm SelectedTenant = table.getSelectionModel().getSelectedItem();

                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/EditTenant.fxml"));
                                Parent root = fxmlLoader.load();
                                TenantEditController tenantEditController = fxmlLoader.getController();
                                tenantEditController.setSelectedTenantDetails(SelectedTenant);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                        });

                        paymentHistory.setOnMouseClicked((MouseEvent event) -> {

                            System.out.println("hi thenuri");

                        });


                        HBox manageBtn = new HBox(viewDetails,edit,paymentHistory);


                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(3);
                        manageBtn.setPadding(new Insets(2));

                        HBox.setMargin(viewDetails, new Insets(2, 2, 0, 3));
                        HBox.setMargin(edit, new Insets(2, 3, 0, 3));
                        HBox.setMargin(paymentHistory, new Insets(2, 3, 0, 3));
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
            tableData = tenantModel.getAllTenants();
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
        monthsCmb.getSelectionModel().selectFirst();
        sortCmb.getSelectionModel().selectFirst();
        nameTxt.setText("");
        membersCountTxt.setText("");
        table.getSelectionModel().clearSelection();

    }

}




