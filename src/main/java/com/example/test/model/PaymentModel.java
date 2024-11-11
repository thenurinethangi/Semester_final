package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.tm.PaymentTm;
import com.example.test.dto.tm.TenantTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaymentModel {

    private  final TenantModel tenantModel = new TenantModel();

    public boolean addNewFirstPayment(String tenantId, double amount, String paymentType) throws SQLException, ClassNotFoundException {

        String invoiceNo = generateNewPaymentId();
        System.out.println("Invoice :"+invoiceNo);

        String today = String.valueOf(LocalDate.now());
        System.out.println("Local date now: "+today);

        String sql = "insert into payment values(?,?,?,?,?,?)";
        boolean result = CrudUtility.execute(sql,invoiceNo,amount,today,paymentType,tenantId,1);

        return result;
    }


    public String generateNewPaymentId() throws SQLException, ClassNotFoundException {

        String sql = "select invoiceNo from payment order by invoiceNo desc limit 1";
        ResultSet result = CrudUtility.execute(sql);

        LocalDate date = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = date.format(dateFormatter);
        System.out.println(dateString);

        if(result.next()){

            String lastId = result.getString("invoiceNo");
            String subStr = lastId.substring(10);
            int id = Integer.parseInt(subStr);
            id+=1;
            String newId = String.format("I%04d", id);

            String invoiceNo = dateString+"-"+newId;
            System.out.println(invoiceNo);
            return invoiceNo;

        }

        return dateString+"-"+"I0001";

    }

    public ObservableList<PaymentTm> getAllPayments() throws SQLException, ClassNotFoundException {

        String sql = "select * from payment";
        ResultSet result = CrudUtility.execute(sql);

        ObservableList<PaymentTm> allPayments = FXCollections.observableArrayList();

        while(result.next()){

            String invoiceNo = result.getString(1);
            double amount = result.getDouble(2);
            String date  = result.getString(3);
            String paymentType = result.getString(4);
            String tenantId = result.getString(5);

            PaymentTm payment = new PaymentTm(invoiceNo,amount,date,paymentType,tenantId);
            allPayments.add(payment);
        }

        return  allPayments;
    }

    public ObservableList<TenantTm> getAllTenantIds() throws SQLException, ClassNotFoundException {

        ObservableList<TenantTm> allTenants = tenantModel.getAllTenants();
        return allTenants;
    }

    public ObservableList<String> getInvoiceNoSuggestions(String input) throws SQLException, ClassNotFoundException {

        String sql = "SELECT invoiceNo FROM payment WHERE invoiceNo LIKE ?";
        ResultSet result = CrudUtility.execute(sql, input + "%");

        ObservableList<String> invoices = FXCollections.observableArrayList();

        while (result.next()) {
            invoices.add(result.getString("invoiceNo"));
        }

        return invoices;
    }

    public boolean checkIfThisPaymentIsFirstPaymentOrNot(PaymentTm selectedPayment) throws SQLException, ClassNotFoundException {

        String sql = "select isFirstPayment from payment where invoiceNo = ?";
        ResultSet result = CrudUtility.execute(sql,selectedPayment.getInvoiceNo());

        boolean isFirstPayment = false;

        if(result.next()) {
            isFirstPayment = result.getBoolean("isFirstPayment");

        }

        return isFirstPayment;
    }

    public String deletePayment(PaymentTm selectedPayment) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            String sql = "delete from payment where invoiceNo = ?";
            boolean result = CrudUtility.execute(sql, selectedPayment.getInvoiceNo());

            if(!result){
                connection.rollback();
               return "Something Went Wrong, Failed To Delete Payment, Try Again Later";
            }

            boolean isUpdate = tenantModel.updateLastPaymentMonth(selectedPayment);
            if(!isUpdate){
                connection.rollback();
                return "Something Went Wrong, Failed To Delete Payment, Try Again Later";
            }

            connection.commit();
            return "Successfully Delete The Payment";
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
}






