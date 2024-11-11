package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.tm.EmployeeTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeModel {

    private Connection connection;

    public EmployeeModel() throws SQLException, ClassNotFoundException {

        connection = DBConnection.getInstance().getConnection();
    }


    public ObservableList<EmployeeTm> getAllEmployees() throws SQLException {

        String sql = "select * from employee";

        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet result = pst.executeQuery();

        ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

        while (result.next()){

            String employeeId = result.getString(1);
            String name = result.getString(2);
            String address = result.getString(3);
            String phoneNo = result.getString(4);
            double basicSalary = result.getDouble(5);
            double allowances = result.getDouble(6);
            String dob = result.getString(7);
            String position = result.getString(8);

            EmployeeTm employeeTm = new EmployeeTm(employeeId,name,address,phoneNo,basicSalary,allowances,dob,position);
            employeeTms.add(employeeTm);

        }
        return employeeTms;

    }

    public String deleteEmployee(EmployeeTm selectedItem) throws SQLException {

        String sql = "delete from employee where employeeId = ?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,selectedItem.getEmployeeId());

        int result = pst.executeUpdate();

        return (result>0) ? "successfully delete the employee" : "failed to delete the employee, try again later";

    }

    public ObservableList<String> getAllEmployeesId() throws SQLException, ClassNotFoundException {

        String sql = "select employeeId from employee";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> employeeIds = FXCollections.observableArrayList();
        employeeIds.add("Select");

        while (result.next()){

            employeeIds.add(result.getString("employeeId"));
        }

        return employeeIds;

    }

    public ObservableList<String> getAllDistinctPositions() throws SQLException, ClassNotFoundException {

        String sql = "select distinct position from employee";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> positions = FXCollections.observableArrayList();
        positions.add("Select");

        while (result.next()){

            positions.add(result.getString("position"));
        }

        return positions;

    }

    public ObservableList<String> getEmployeeAddresses(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT address FROM employee WHERE address LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> addresses = FXCollections.observableArrayList();

        while (result.next()) {
            addresses.add(result.getString("address"));
        }

        return addresses;

    }

    public ObservableList<String> getEmployeeNames(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT name FROM employee WHERE name LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> names = FXCollections.observableArrayList();

        while (result.next()) {
            names.add(result.getString("name"));
        }

        return names;
    }

    public ObservableList<EmployeeTm> getEmployeeById(String emId) throws SQLException, ClassNotFoundException {

        String sql = "select * from employee where employeeId = ?";
        ResultSet result = CrudUtility.execute(sql,emId);

        ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

        if(result.next()){

                String employeeId = result.getString(1);
                String name = result.getString(2);
                String address = result.getString(3);
                String phoneNo = result.getString(4);
                double basicSalary = result.getDouble(5);
                double allowances = result.getDouble(6);
                String dob = result.getString(7);
                String position = result.getString(8);

                EmployeeTm employeeTm = new EmployeeTm(employeeId,name,address,phoneNo,basicSalary,allowances,dob,position);
                employeeTms.add(employeeTm);

        }

        return employeeTms;
    }

    public ObservableList<EmployeeTm> getEmployeeByName(String name) throws SQLException, ClassNotFoundException {

        String sql = "select * from employee where name = ?";
        ResultSet result = CrudUtility.execute(sql,name);

        ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

            while(result.next()) {
                String employeeId = result.getString(1);
                String emName = result.getString(2);
                String address = result.getString(3);
                String phoneNo = result.getString(4);
                double basicSalary = result.getDouble(5);
                double allowances = result.getDouble(6);
                String dob = result.getString(7);
                String position = result.getString(8);

                EmployeeTm employeeTm = new EmployeeTm(employeeId, emName, address, phoneNo, basicSalary, allowances, dob, position);
                employeeTms.add(employeeTm);
            }

        return employeeTms;
    }


    public ObservableList<EmployeeTm> getEmployeeByAddress(String address) throws SQLException, ClassNotFoundException {

        String sql = "select * from employee where address = ?";
        ResultSet result = CrudUtility.execute(sql,address);

        ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

        while(result.next()) {
            String employeeId = result.getString(1);
            String emName = result.getString(2);
            String emAddress = result.getString(3);
            String phoneNo = result.getString(4);
            double basicSalary = result.getDouble(5);
            double allowances = result.getDouble(6);
            String dob = result.getString(7);
            String position = result.getString(8);

            EmployeeTm employeeTm = new EmployeeTm(employeeId, emName, emAddress, phoneNo, basicSalary, allowances, dob, position);
            employeeTms.add(employeeTm);
        }

        return employeeTms;
    }

    public ObservableList<EmployeeTm> getEmployeeByPosition(String position) throws SQLException, ClassNotFoundException {

        String sql = "select * from employee where position = ?";
        ResultSet result = CrudUtility.execute(sql,position);

        ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

        while(result.next()) {
            String employeeId = result.getString(1);
            String emName = result.getString(2);
            String emAddress = result.getString(3);
            String phoneNo = result.getString(4);
            double basicSalary = result.getDouble(5);
            double allowances = result.getDouble(6);
            String dob = result.getString(7);
            String emPosition = result.getString(8);

            EmployeeTm employeeTm = new EmployeeTm(employeeId, emName, emAddress, phoneNo, basicSalary, allowances, dob, emPosition);
            employeeTms.add(employeeTm);
        }

        return employeeTms;
    }
}








