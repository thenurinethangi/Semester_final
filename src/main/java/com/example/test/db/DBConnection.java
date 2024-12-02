package com.example.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection connection;
    private static DBConnection dbConnection;

    private DBConnection(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grandview_residences", "root", "Ijse@1234");
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {

        if(dbConnection==null){
            dbConnection = new DBConnection();
        }

        return dbConnection;

    }

    public Connection getConnection(){

        return connection;
    }
}
