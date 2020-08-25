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
import com.dollarsbank.models.Account;
import com.dollarsbank.models.Customer;
import com.dollarsbank.models.Transaction;

@WebServlet("/transaction")
public class DepositWithdrawServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DepositWithdrawServlet() {
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
			request.getRequestDispatcher("/WEB-INF/views/deposit_withdraw.jsp").forward(request, response);
		}else {
			
			response.sendRedirect("login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean accountExists = false;
		DollarsBankDao dao = new DollarsBankDao();
		
		HttpSession session = request.getSession(false);
		Customer customer = (Customer) session.getAttribute("customer");
		
        String transaction = request.getParameter("transaction");
		String account_type = request.getParameter("account");
		double transaction_amount = Double.parseDouble(request.getParameter("transaction_amount"));
		
		Account account = new Account();
		
		account.setAccountType(account_type);
		
		accountExists = dao.accountExists(customer.getUserId(), account_type);
		
		double account_balance = dao.getAccountInfo(customer.getCustomerId(), account_type, 0).getAccountBalance();
		boolean validWithdraw = true;
		
		if(transaction.equals("withdraw") && transaction_amount > account_balance) {
			validWithdraw = false;
			request.setAttribute("errorMessage", "You do not have enough funds in your " + account_type + " account to withdraw.");
			request.getRequestDispatcher("/WEB-INF/views/deposit_withdraw.jsp").forward(request, response);
		}
		
		if(accountExists && validWithdraw) {
			dao.addTransaction(customer.getUserId(), account_type, transaction, 0, 0, transaction_amount);
			
			int account_id = dao.getAccountInfo(customer.getCustomerId(), account_type, 0).getAccountId();
			
			List<Transaction> transactions = dao.getTransactions(customer.getUserId());

			//latest transaction
			//note that it's desc
            int transaction_id = transactions.get(0).getTransactionId();
            
            dao.updateBalance(transaction_amount, account_id, transaction_id);
            
            request.setAttribute("accounts", dao.getAccounts(customer.getUserId()));
			request.getRequestDispatcher("/WEB-INF/views/overview.jsp").forward(request, response);
		}else {
			request.setAttribute("errorMessage", "You do not have a " + account_type + " account.");
			request.getRequestDispatcher("/WEB-INF/views/deposit_withdraw.jsp").forward(request, response); 
		}

	}

}
