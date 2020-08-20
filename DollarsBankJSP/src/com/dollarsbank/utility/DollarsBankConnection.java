package com.dollarsbank.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.Driver;

public class DollarsBankConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/dollarsbank_db?useSSL=false";
	private static final String user = "root";
	private static final String password = "rootroot";
	
	public static Connection getConnection(){
      
		try {
			
			DriverManager.registerDriver(new Driver());
             return DriverManager.getConnection(URL, user, password);
             
      } catch (SQLException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
		
    }
	
}