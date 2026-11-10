/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerceAdmin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Ezekiel
 */
public class DBConfiguration {
 public static Connection getConnection() throws ClassNotFoundException, SQLException{
        //Step 1: Define Database Credentials
        String url = "jdbc:mysql://localhost:3306/ecommerce";
        String userName = "root";
        String password = "January2nd2001";
        //Step 2: Load your JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
     //Step 3: Get Connection
     //Step 4: Use the connection
        return DriverManager.getConnection(url, userName, password);
    }    
}
