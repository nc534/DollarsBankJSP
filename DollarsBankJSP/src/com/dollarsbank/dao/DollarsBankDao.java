package com.dollarsbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dollarsbank.models.Account;
import com.dollarsbank.models.Customer;
import com.dollarsbank.models.Transaction;
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
	
	public Customer getCustomerInfo(String userId) {
		
		Customer customer = new Customer();
		
        String customer_info_sql = "SELECT * FROM customer WHERE userid = ?";

        try{
        	
        	Connection connection = DollarsBankConnection.getConnection();
    		
			PreparedStatement preparedStatement = connection.prepareStatement(customer_info_sql);
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setUserId(rs.getString("userid"));
            customer.setPassword(rs.getString("password"));
            customer.setName(rs.getString("name"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(rs.getString("address"));

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
	
	public void addAccount(Customer customer, Account account) {
		
		int customer_id = getCustomerInfo(customer.getUserId()).getCustomerId();
		boolean accountCreated;

        String add_account_sql = "Insert into account (customer_id, account_type, account_creation, initial_deposit, account_balance)" +
                "values (?, ?, now(), ?, initial_deposit)";

        try{
        	
        	Connection connection = DollarsBankConnection.getConnection();
    		
			PreparedStatement preparedStatement = connection.prepareStatement(add_account_sql);
            preparedStatement.setInt(1, customer_id);
            preparedStatement.setString(2, account.getAccountType());
            preparedStatement.setDouble(3, account.getInitialDeposit());
            accountCreated = preparedStatement.execute();

            if(accountCreated){
                getAccountInfo(customer_id, account.getAccountType());
            }

        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
	
	public Account getAccountInfo(int customer_id, String account){

        String find_account_sql = "SELECT * from account where customer_id = ? AND account_type = ?";

        Account currentAccount = new Account();

        try{

        	Connection connection = DollarsBankConnection.getConnection();
    		
			PreparedStatement preparedStatement = connection.prepareStatement(find_account_sql);
            preparedStatement.setInt(1, customer_id);
            preparedStatement.setString(2, account);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            currentAccount.setAccountId(rs.getInt("account_id"));
            currentAccount.setAccountType(rs.getString("account_type"));
            currentAccount.setAccountCreation(rs.getTimestamp("account_creation"));
            currentAccount.setInitialDeposit(rs.getDouble("initial_deposit"));
            currentAccount.setAccountBalance(rs.getDouble("account_balance"));

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return currentAccount;

    }
	
	public List<Account> getAccounts(String userId) {
		 int customer_id = getCustomerInfo(userId).getCustomerId();

	        String get_transactions_sql = "SELECT * from account where customer_id = ?";

	        List<Account> accountList = new ArrayList<>();

	        try{

	        	Connection connection = DollarsBankConnection.getConnection();
	    		PreparedStatement preparedStatement = connection.prepareStatement(get_transactions_sql);
	            preparedStatement.setInt(1, customer_id);

	            ResultSet rs = preparedStatement.executeQuery();

	            while(rs.next()) {
	                Account account = new Account();

	                account.setAccountId(rs.getInt("account_id"));
	                account.setAccountType(rs.getString("account_type"));
	                account.setAccountCreation(rs.getTimestamp("account_creation"));
	                account.setInitialDeposit(rs.getDouble("initial_deposit"));
	                account.setAccountBalance(rs.getDouble("account_balance"));

	                accountList.add(account);
	                System.out.println(accountList);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return accountList;
		
	}
	
	public void addTransaction(String userId, String account_type, String transaction_type, int transfer_from, int transfer_to, double transaction_amount) {

		Connection connection = DollarsBankConnection.getConnection();
		PreparedStatement preparedStatement = null;
		
		int account_id = getAccountInfo(getCustomerInfo(userId).getCustomerId(), account_type).getAccountId();
        boolean transactionCreated;
        String add_transaction_sql;

        if(transaction_type.equals("initial_deposit")) {

            add_transaction_sql = "INSERT into transaction(account_id, transaction_date, transaction_type, transaction_amount) " +
                    "values (" + account_id + ",  " +
                    "(select account_creation from account where account_id = " + account_id + "), ?, ?)";

            try {
            	
            	preparedStatement = connection.prepareStatement(add_transaction_sql);
                preparedStatement.setString(1, transaction_type);
                preparedStatement.setDouble(2, transaction_amount);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            add_transaction_sql = "insert into transaction(account_id, transaction_date, transaction_type, transfer_from, transfer_to, transaction_amount) " +
            "values (?,  now(), ?," + transfer_from + "," + transfer_to + ", ?)";

            try{

                preparedStatement = connection.prepareStatement(add_transaction_sql);
                preparedStatement.setInt(1, account_id);
                preparedStatement.setString(2, transaction_type);
                preparedStatement.setDouble(3, transaction_amount);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            assert preparedStatement != null;
            transactionCreated = preparedStatement.execute();

            if (transactionCreated) {
                getTransactions(getCustomerInfo(userId).getUserId());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
	
	public List<Transaction> getTransactions(String userId) {

        int customer_id = getCustomerInfo(userId).getCustomerId();

        String get_transactions_sql = "SELECT c.customer_id, transaction_id, a.account_id, account_type, transaction_date, transfer_from, transfer_to, transaction_type," +
                "transfer_from, transfer_to," +
                "transaction_amount FROM transaction t " +
                "INNER JOIN account a " +
                "ON t.account_id = a.account_id " +
                "INNER JOIN customer c " +
                "ON a.customer_id = c.customer_id " +
                "WHERE c.customer_id = ? " +
                "ORDER BY transaction_date DESC " +
                "LIMIT 5;";

        List<Transaction> transactionList = new ArrayList<>();

        try{

        	Connection connection = DollarsBankConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(get_transactions_sql);
            preparedStatement.setInt(1, customer_id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                Transaction transaction = new Transaction();

                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setAccountId(rs.getInt("account_id"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setTransferFrom(rs.getInt("transfer_from"));
                transaction.setTransferTo(rs.getInt("transfer_to"));
                transaction.setTransactionAmount(rs.getDouble("transaction_amount"));

                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

}
