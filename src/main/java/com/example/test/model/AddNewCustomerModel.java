package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.CustomerDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewCustomerModel {
    public String generateNextCustomerId() throws SQLException, ClassNotFoundException {

        String sql = "select customerId from customer order by customerId desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("customerId");
            String subStr = lastId.substring(1);
            System.out.println(subStr);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("C%04d", id);

        }
        else{
            return "C0001";
        }
    }

    public String updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE customer SET name = ?, nic = ?, address = ?, phoneNo = ?, jobTitle = ?, livingArrangement = ?, email = ? WHERE customerId = ?";

        boolean result = CrudUtility.execute(sql,customerDto.getName(),customerDto.getNic(),customerDto.getAddress(),customerDto.getPhoneNo(),customerDto.getJobTitle(),
                customerDto.getLivingArrangement(),customerDto.getEmail(),customerDto.getCustomerId());

        return result ? "successfully update the customer" : "failed to update, please try again later";
    }

    public String addCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {

        String sql = "insert into customer values(?,?,?,?,?,?,?,?)";

        boolean result = CrudUtility.execute(sql,customerDto.getCustomerId(),customerDto.getName(),customerDto.getNic(),customerDto.getAddress(),customerDto.getPhoneNo(),
                customerDto.getJobTitle(), customerDto.getLivingArrangement(),customerDto.getEmail());

        return result ? "successfully add the new customer" : "failed to add the new customer, please try again later";
    }
}




