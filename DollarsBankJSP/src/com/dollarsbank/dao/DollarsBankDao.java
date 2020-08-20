package com.dollarsbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dollarsbank.models.Customer;
import com.dollarsbank.utility.DollarsBankConnection;


public class DollarsBankDao {
	
	//method for registering new customers
    public int registerCustomer(Customer customer) throws ClassNotFoundException {

        String insert_customer_sql = "INSERT INTO customer (name, address, phone, userid, password)" +
                "VALUES (?, ?, ?, ?, ?)";

        int result = 0;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		try {
			
			Connection connection = DollarsBankConnection.getConnection();
		
			PreparedStatement preparedStatement = connection.prepareStatement(insert_customer_sql);
			
			preparedStatement.setString(1, customer.getName());
			preparedStatement.setString(2, customer.getAddress());
			preparedStatement.setString(3, customer.getPhone());
			preparedStatement.setString(4, customer.getUserId());
			preparedStatement.setString(5, customer.getPassword());
			
			result = preparedStatement.executeUpdate();
		
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;

    }
    
    //method for checking if the customer already exists when registering
    public boolean checkCustomer(Customer customer) throws ClassNotFoundException {

        boolean customerExists = false;

        Class.forName("com.mysql.cj.jdbc.Driver");

        String find_customer_exists_sql = "SELECT * FROM customer WHERE userid = ? OR phone = ?";

        try {
			
			Connection connection = DollarsBankConnection.getConnection();
		
			PreparedStatement preparedStatement = connection.prepareStatement(find_customer_exists_sql);
			
			preparedStatement.setString(1, customer.getUserId());
			preparedStatement.setString(2, customer.getPhone());
			
			ResultSet rs = preparedStatement.executeQuery();
			
			customerExists = rs.next();
		
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerExists;

    }

	public boolean findCustomer(Customer customer) throws ClassNotFoundException {
		
		boolean customerExists = false;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		//Binary makes sure username and password are case sensitive
		String find_customer_sql = "SELECT * FROM customer WHERE BINARY userid = ? AND BINARY password = ?";
		
		try {
			
			Connection connection = DollarsBankConnection.getConnection();
		
			PreparedStatement preparedStatement = connection.prepareStatement(find_customer_sql);
			
			preparedStatement.setString(1, customer.getUserId());
			preparedStatement.setString(2, customer.getPassword());
			
			ResultSet rs = preparedStatement.executeQuery();
			
			customerExists = rs.next();
		
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerExists;
		
	}

}
