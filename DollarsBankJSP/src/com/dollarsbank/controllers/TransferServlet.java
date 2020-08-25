package com.dollarsbank.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dollarsbank.dao.DollarsBankDao;
import com.dollarsbank.models.Customer;
import com.dollarsbank.models.Transaction;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TransferServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		Customer customer = (Customer) session.getAttribute("customer");
		
		//to prevent from accessing page after logging out
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);
		
        if (customer != null) {
			request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
		}else {
			
			response.sendRedirect("login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DollarsBankDao dao = new DollarsBankDao();
		
		HttpSession session = request.getSession(false);
		Customer customer = (Customer) session.getAttribute("customer");
		
		String account_from = request.getParameter("account_from");
		String account_to = request.getParameter("account_to");
		String phone = request.getParameter("phone");
		int account_from_id = dao.getAccountInfo(dao.getCustomerInfo(customer.getUserId(), null).getCustomerId(), account_from, 0).getAccountId();
		int account_to_id = Integer.parseInt(request.getParameter("other_account"));
		double transaction_amount = Double.parseDouble(request.getParameter("transaction_amount"));
		boolean accountFromExists = dao.accountExists(customer.getUserId(), account_from);
		boolean accountToExists = dao.accountExists(customer.getUserId(), account_to);
		
		double account_balance = dao.getAccountInfo(customer.getCustomerId(), account_from, 0).getAccountBalance();
		boolean validTransfer = true;
		
		if(account_from == account_to && account_from_id == account_to_id) {
			request.setAttribute("errorMessage", "You cannot transfer funds to the same account.");
			request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
		}
		
		if(accountFromExists) {
			if(transaction_amount > account_balance) {
				validTransfer = false;
				request.setAttribute("errorMessage", "You do not have enough funds in your " + account_from + " account to transfer.");
				request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
			}
		}
		
		if(accountFromExists && validTransfer) {
			
			if(account_to.equals("other")) {
				
				boolean otherCustomerAccountExists = dao.otherCustomerAccount(phone, account_to_id);

				if(!otherCustomerAccountExists) {
					request.setAttribute("errorMessage", "No customer was found with the phone number " + phone + " and account id " + account_to_id);
					request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
				}else if(otherCustomerAccountExists && !phone.equals(dao.getCustomerInfo(customer.getUserId(), null).getPhone())){
					
					Customer otherCustomer = dao.getCustomerInfo(null, phone);
					String account_type = dao.getAccountInfo(otherCustomer.getCustomerId(), null, account_to_id).getAccountType();
					
					dao.addTransaction(customer.getUserId(), account_from, "transfer", account_from_id, account_to_id, transaction_amount);
					dao.addTransaction(otherCustomer.getUserId(), account_type, "transfer", account_from_id, account_to_id, transaction_amount);
					
					List<Transaction> transactionFrom = dao.getTransactions(customer.getUserId());

		            int transaction_id_from = transactionFrom.get(0).getTransactionId();

		            dao.updateBalanceFrom(transaction_amount, account_from_id, transaction_id_from);

		            dao.updateBalanceTo(transaction_amount, account_to_id);
		            
		            request.setAttribute("accounts", dao.getAccounts(customer.getUserId()));
					request.getRequestDispatcher("/WEB-INF/views/overview.jsp").forward(request, response);
					
				}else {
					request.setAttribute("errorMessage", "Please select the correct accounts when transferring between your accounts.");
					request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
				}
				
			}else if(accountToExists && account_from != account_to){
				int transfer_to = dao.getAccountInfo(customer.getCustomerId(), account_to, 0).getAccountId();
	            int transfer_from = dao.getAccountInfo(customer.getCustomerId(), account_from, 0).getAccountId();
	            
	            dao.addTransaction(customer.getUserId(), account_from, "transfer", transfer_from, transfer_to, transaction_amount);

	            List<Transaction> transaction = dao.getTransactions(customer.getUserId());

	            int transaction_id = transaction.get(0).getTransactionId();

	            dao.updateBalanceFrom(transaction_amount, transfer_from, transaction_id);
	            dao.updateBalanceTo(transaction_amount, transfer_to);
	            
	            request.setAttribute("accounts", dao.getAccounts(customer.getUserId()));
				request.getRequestDispatcher("/WEB-INF/views/overview.jsp").forward(request, response);
			}else {
				request.setAttribute("errorMessage", "You do not have a " + account_to + " account.");
				request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response); 
			}
			
		}else {
			request.setAttribute("errorMessage", "You do not have a " + account_from + " account.");
			request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response); 
		}
		
	}

}
