package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.ExpenseDto;
import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.ExpenseTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExpenseModel {
    private final MaintenanceRequestModel maintenanceRequestModel = new MaintenanceRequestModel();

    public boolean addNewExpense(TenantDto tenant) throws SQLException, ClassNotFoundException {

        String newExpenseNo = generateNewExpenseId();

        String sql = "INSERT INTO expence (expenceNo, description, Amount, date, isActiveExpense) VALUES (?, ?, ?, ?, ?)";
        boolean result = CrudUtility.execute(sql,newExpenseNo,"Security deposit refunded expense",tenant.getSecurityPaymentRemain(), String.valueOf(LocalDate.now()),1);

        return result;
    }

    public String generateNewExpenseId() throws SQLException, ClassNotFoundException {

        String sql = "select expenceNo from expence order by expenceNo desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){

            String lastId = result.getString("expenceNo");
            String subStr = lastId.substring(4);
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

    public ObservableList<ExpenseTm> getAllExpenses() throws SQLException, ClassNotFoundException {

        String sql = "select * from expence where isActiveExpense = 1";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<ExpenseTm> allExpenses = FXCollections.observableArrayList();

        while(result.next()){

           String expenseNo = result.getString(1);
           String description = result.getString(2);
           double amount = result.getDouble(3);
           String requestId = result.getString(4);
           if(requestId==null){
              requestId = "N/A";
           }
           String date = result.getString(5);

            ExpenseTm expenseTm = new ExpenseTm(expenseNo,description,amount,requestId,date);
            allExpenses.add(expenseTm);
        }
       return allExpenses;
    }

    public ObservableList<String> getAllDistinctMaintenanceRequestNos() throws SQLException, ClassNotFoundException {

        String sql = "SELECT DISTINCT requestNo FROM expence WHERE requestNo IS NOT NULL";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<String> requestNos = FXCollections.observableArrayList();
        requestNos.add("Select");

        while (result.next()){

            requestNos.add(result.getString("requestNo"));
        }

        return requestNos;
    }

    public String deleteExpense(ExpenseTm selectedExpense) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try{

            boolean isMakeNotComplete = maintenanceRequestModel.setRequestNotComplete(selectedExpense);

            if(!isMakeNotComplete){
                connection.rollback();
                return "Something Went Wrong With Deleting The Selected Expense, Try Again Later";
            }

            String sql = "UPDATE expence SET isActiveExpense = ? WHERE expenceNo = ?";
            boolean result = CrudUtility.execute(sql,0,selectedExpense.getExpenseNo());

            if(!result){
                connection.rollback();
                return "Something Went Wrong With Deleting The Selected Expense, Try Again Later";
            }

            connection.commit();
            return "Successfully Deleted The Expense No: "+selectedExpense.getExpenseNo();

        }
        catch (Exception e){
           connection.rollback();
           e.printStackTrace();
        }
        finally {
           connection.setAutoCommit(true);
        }

        return "0";
    }

    public ObservableList<String> getRequestIdSuggestions(String input) throws SQLException, ClassNotFoundException {

        return maintenanceRequestModel.getRequestIdSuggestions(input);
    }

    public boolean checkEnteredRequestIdValid(String maintenanceRequestNo) throws SQLException, ClassNotFoundException {

        return maintenanceRequestModel.checkEnteredRequestIdValid(maintenanceRequestNo);
    }

    public String addNewExpenseForMaintenanceRequest(ExpenseDto newExpense) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            String sql = "INSERT INTO expence (expenceNo, description, Amount, requestNo, date, isActiveExpense) VALUES (?, ?, ?, ?, ?, ?)";
            boolean result = CrudUtility.execute(sql, newExpense.getExpenseNo(), newExpense.getDescription(), newExpense.getAmount(), newExpense.getMaintenanceRequestNo(), newExpense.getDate(), 1);

            if(!result){
               connection.rollback();
               return "Something Went Wrong With Adding A New Expense, Please Try Again Later";
            }

            boolean isUpdateTheCost = maintenanceRequestModel.setActualCost(newExpense);

            if(!isUpdateTheCost){
                connection.rollback();
                return "Something Went Wrong With Adding A New Expense, Please Try Again Later";
            }

            connection.commit();
            return "Successfully Added New Expense";
        }
        catch (Exception e){
            connection.rollback();
            e.printStackTrace();
        }
        finally {
            connection.setAutoCommit(true);
        }

        return "0";
    }

    public double getTotalExpense() throws SQLException, ClassNotFoundException {

        String sql = "select sum(Amount) from expence";
        ResultSet result = CrudUtility.execute(sql);

        if(result.next()){
           return result.getDouble("sum(Amount)");
        }
        return 0;
    }
}






