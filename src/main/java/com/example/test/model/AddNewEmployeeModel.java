package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.EmployeeDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewEmployeeModel {
    public String updateEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE employee SET name = ?, address = ?, phoneNo = ?, basicSalary = ?, allowances = ?, dob = ?, position = ? WHERE employeeId = ?";

        boolean result = CrudUtility.execute(sql,employeeDto.getName(),employeeDto.getAddress(),employeeDto.getPhoneNo(),employeeDto.getBasicSalary(),
                employeeDto.getAllowances(),employeeDto.getDob(),employeeDto.getPosition(),employeeDto.getEmployeeId());

        return result ? "successfully update the employee" : "failed to update, please try again later";

    }

    public String generateNextEmployeeId() throws SQLException, ClassNotFoundException {

        String sql = "select employeeId from employee order by employeeId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("employeeId");
            String subStr = lastId.substring(2);
            System.out.println(subStr);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("EM%03d", id);

        }
        else{
            return "EM001";
        }
    }

    public String addNewEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {

        String sql = "insert into employee values(?,?,?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,employeeDto.getEmployeeId(),employeeDto.getName(),employeeDto.getAddress(),employeeDto.getPhoneNo(),employeeDto.getBasicSalary(),
                employeeDto.getAllowances(),employeeDto.getDob(),employeeDto.getPosition());

        return result ? "successfully add new employee" : "failed add new employee, please try again later";

    }
}










