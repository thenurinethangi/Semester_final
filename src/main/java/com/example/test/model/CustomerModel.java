package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerModel {


    public ObservableList<CustomerTm> getAllCustomers() throws SQLException, ClassNotFoundException {

        String sql = "select * from customer";
        ResultSet result  = CrudUtility.execute(sql);

        ObservableList<CustomerTm> allCustomers = FXCollections.observableArrayList();

        while(result.next()){

            String id = result.getString(1);
            String name = result.getString(2);
            String nic = result.getString(3);
            String address = result.getString(4);
            String phoneNo = result.getString(5);
            String jobTitle = result.getString(6);
            String livingArrangement = result.getString(7);

            CustomerTm customer = new CustomerTm(id,name,nic,address,phoneNo,jobTitle,livingArrangement);
            allCustomers.add(customer);
        }

        return allCustomers;
    }

    public ObservableList<String> getAllCustomersId() throws SQLException, ClassNotFoundException {

        String sql = "select customerId from customer order by customerId asc";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> customerIds = FXCollections.observableArrayList();
        customerIds.add("Select");

        while (result.next()){

            customerIds.add(result.getString("customerId"));
        }

        return customerIds;
    }

    public String deleteCustomer(CustomerTm selectedItem) throws SQLException, ClassNotFoundException {

        String sql = "delete from customer where customerId = ?";
        boolean result = CrudUtility.execute(sql,selectedItem.getCustomerId());

        return result ? "Successfully delete the customer" : "Failed to delete the customer";
    }

    public ObservableList<CustomerTm> searchCustomerAlreadyExistOrNot(String phoneNo) throws SQLException, ClassNotFoundException {

        String sql = "select * from customer where phoneNo = ?";
        ResultSet result = CrudUtility.execute(sql,phoneNo);

        ObservableList<CustomerTm> customerTms = FXCollections.observableArrayList();

        if(result.next()){

            String id = result.getString(1);
            String name = result.getString(2);
            String nic = result.getString(3);
            String address = result.getString(4);
            String customerPhoneNo = result.getString(5);
            String jobTitle = result.getString(6);
            String livingArrangement = result.getString(7);

            CustomerTm customer = new CustomerTm(id,name,nic,address,customerPhoneNo,jobTitle,livingArrangement);
            customerTms.add(customer);
        }

        return customerTms;
    }

    public ObservableList<String> getPhoneNoSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT distinct phoneNo FROM customer WHERE phoneNo LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> phoneNumbers = FXCollections.observableArrayList();

        while (result.next()) {
            phoneNumbers.add(result.getString("phoneNo"));
        }

        return phoneNumbers;
    }

    public ObservableList<String> getNicSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT nic FROM customer WHERE nic LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> nicNumbers = FXCollections.observableArrayList();

        while (result.next()) {
            nicNumbers.add(result.getString("nic"));
        }

        return nicNumbers;
    }

    public ObservableList<String> getNameSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT distinct name FROM customer WHERE name LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> names = FXCollections.observableArrayList();

        while (result.next()) {
            names.add(result.getString("name"));
        }

        return names;
    }

    public ObservableList<String> getJobTitlesSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT jobTitle FROM customer WHERE jobTitle LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> jobTitles = FXCollections.observableArrayList();

        while (result.next()) {
            jobTitles.add(result.getString("jobTitle"));
        }

        return jobTitles;
    }

    public ObservableList<CustomerTm> getCustomerById(String customerId) throws SQLException, ClassNotFoundException {

        String sql = "select * from customer where customerId = ?";
        ResultSet result = CrudUtility.execute(sql,customerId);

        ObservableList<CustomerTm> customerTms = FXCollections.observableArrayList();

        if(result.next()){

            String id = result.getString(1);
            String name = result.getString(2);
            String nic = result.getString(3);
            String address = result.getString(4);
            String customerPhoneNo = result.getString(5);
            String jobTitle = result.getString(6);
            String livingArrangement = result.getString(7);

            CustomerTm customer = new CustomerTm(id,name,nic,address,customerPhoneNo,jobTitle,livingArrangement);
            customerTms.add(customer);
        }
        return customerTms;
    }

    public String searchCustomerAlreadyExistOrNotByNic(String nic) throws SQLException, ClassNotFoundException {

        String sql = "select customerId from  customer where  nic =?";
        ResultSet result = CrudUtility.execute(sql,nic);

        String id = "";

        if(result.next()){

            id = result.getString("customerId");
        }

        return id;
    }
}











