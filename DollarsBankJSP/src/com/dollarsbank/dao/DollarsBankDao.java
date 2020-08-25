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
	
	public Customer getCustomerInfo(String userId, String phone) {
		
		Customer customer = new Customer();
		
        String customer_info_sql = "SELECT * FROM customer WHERE userid = ? or phone = ?";

        try{
        	
        	Connection connection = DollarsBankConnection.getConnection();
    		
			PreparedStatement preparedStatement = connection.prepareStatement(customer_info_sql);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, phone);
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
		
		int customer_id = getCustomerInfo(customer.getUserId(), null).getCustomerId();
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
                getAccountInfo(customer_id, account.getAccountType(), 0);
            }

        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
	
	public boolean accountExists(String userId, String account_type) {
        boolean accountExists = false;

        String account_exists_sql = "SELECT * FROM account a INNER JOIN customer c ON c.customer_id = a.customer_id WHERE " +
                "userid = ? AND account_type = ?";

        try {

        	Connection connection = DollarsBankConnection.getConnection();
    		
			PreparedStatement preparedStatement = connection.prepareStatement(account_exists_sql);

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, account_type);

            ResultSet rs = preparedStatement.executeQuery();

            accountExists = rs.next();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return  accountExists;
    }
	
	public Account getAccountInfo(int customer_id, String account, int account_id){

        String find_account_sql = "SELECT * from account where customer_id = ? AND (account_type = ? OR account_id = ?)";

        Account currentAccount = new Account();

        try{

        	Connection connection = DollarsBankConnection.getConnection();
    		
			PreparedStatement preparedStatement = connection.prepareStatement(find_account_sql);
            preparedStatement.setInt(1, customer_id);
            preparedStatement.setString(2, account);
            preparedStatement.setInt(3, account_id);
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
		 int customer_id = getCustomerInfo(userId, null).getCustomerId();

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
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return accountList;
		
	}
	
	public void addTransaction(String userId, String account_type, String transaction_type, int transfer_from, int transfer_to, double transaction_amount) {

		Connection connection = DollarsBankConnection.getConnection();
		PreparedStatement preparedStatement = null;
		
		int account_id = getAccountInfo(getCustomerInfo(userId, null).getCustomerId(), account_type, 0).getAccountId();
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
                getTransactions(getCustomerInfo(userId, null).getUserId());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
	
	public List<Transaction> getTransactions(String userId) {

        int customer_id = getCustomerInfo(userId, null).getCustomerId();

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
	
	public boolean updateBalance(double transaction_amount, int account_id, int transaction_id) {

        boolean updated = false;

        String update_balance_sql = "UPDATE account a INNER JOIN transaction t " +
                "ON a.account_id = t.account_id " +
                "SET account_balance = " +
                "case " +
                "   when transaction_type = 'deposit' then account_balance + " + transaction_amount +
                "   when transaction_type = 'withdraw' then account_balance - " + transaction_amount +
                "   else account_balance " +
                "end " +
                "where a.account_id = ? AND transaction_id = ?";

        try {
        	
        	Connection connection = DollarsBankConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(update_balance_sql);

            preparedStatement.setInt(1, account_id);
            preparedStatement.setInt(2, transaction_id);

            //will return false since it is an update
            updated = preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }
	
	public boolean updateBalanceFrom(double transaction_amount, int account_id, int transaction_id){
        boolean updated = false;

        String update_balance_from_sql = "UPDATE account a INNER JOIN transaction t " +
                "ON a.account_id = t.transfer_from " +
                "SET account_balance = account_balance - " + transaction_amount +
                " WHERE a.account_id = ? AND transaction_id = ?";

        try {
        	
        	Connection connection = DollarsBankConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(update_balance_from_sql);

            preparedStatement.setInt(1, account_id);
            preparedStatement.setInt(2, transaction_id);

            System.out.println(preparedStatement);
            updated = preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public boolean updateBalanceTo(double transaction_amount, int account_id){
        boolean updated = false;

        String update_balance_to_sql = "UPDATE account a " +
                "SET account_balance = account_balance + " + transaction_amount +
                " WHERE account_id = ?";

        try {
        	
        	Connection connection = DollarsBankConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(update_balance_to_sql);

            preparedStatement.setInt(1, account_id);

            System.out.println(preparedStatement);
            updated = preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }
    
    public boolean otherCustomerAccount(String phone, int account_id) {
    	boolean otherAccountExists = false;
    	
    	 String check_other_customer_sql = "SELECT * from customer c " + 
    	 		"inner join account a " + 
    	 		"on c.customer_id = a.customer_id " + 
    	 		"where a.account_id = ? and phone = ?";
    	
    	 try {
         	
         	Connection connection = DollarsBankConnection.getConnection();
     		PreparedStatement preparedStatement = connection.prepareStatement(check_other_customer_sql);

             preparedStatement.setString(2, phone);
             preparedStatement.setInt(1, account_id);

             ResultSet rs = preparedStatement.executeQuery();
 			
             otherAccountExists = rs.next();
             
         }catch (SQLException e) {
             e.printStackTrace();
         }
    	
    	return otherAccountExists;
    }

}
