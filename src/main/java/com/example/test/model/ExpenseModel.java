package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.dto.ExpenseDto;
import com.example.test.dto.TenantDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseModel {
    public boolean addNewExpense(TenantDto tenant) throws SQLException, ClassNotFoundException {

        String newExpenseNo = generateNewExpenseId();

        String sql = "INSERT INTO expence (expenceNo, description, Amount) VALUES (?, ?, ?)";
        boolean result = CrudUtility.execute(sql,newExpenseNo,"Security deposit refunded expense",tenant.getSecurityPaymentRemain());

        return result;
    }

    public String generateNewExpenseId() throws SQLException, ClassNotFoundException {

        String sql = "select expenceNo from expence order by expenceNo desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("expenceNo");
            String subStr = lastId.substring(3);
            System.out.println("sub string: "+subStr);
            int id = Integer.parseInt(subStr);
            id+=1;
            return String.format("EXP-%05d", id);

        }
        else{
            return "EXP-00001";
        }

    }

    public ExpenseDto getLastExpenseDetails() throws SQLException, ClassNotFoundException {

        String sql = "select * from expence order by expenceNo desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        ExpenseDto expenseDto = new ExpenseDto();

        if(result.next()){

            String expenseNo = result.getString("expenceNo");
            double amount = result.getDouble("Amount");

            expenseDto.setExpenseNo(expenseNo);
            expenseDto.setAmount(amount);
        }

        return expenseDto;
    }
}
