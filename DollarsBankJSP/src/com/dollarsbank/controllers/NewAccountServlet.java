package com.dollarsbank.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dollarsbank.dao.DollarsBankDao;
import com.dollarsbank.models.Account;
import com.dollarsbank.models.Customer;

@WebServlet("/new-account")
public class NewAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public NewAccountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/newaccount.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Boolean accountExists = false;
		DollarsBankDao dao = new DollarsBankDao();
		
		HttpSession session = request.getSession(false);
		Customer customer = (Customer) session.getAttribute("customer");
		
		//to prevent from accessing page after logging out
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);
		
		String account_type = request.getParameter("account");
		double initial_deposit = Double.parseDouble(request.getParameter("initial_deposit"));
		
		Account account = new Account();
		
		account.setAccountType(account_type);
		account.setInitialDeposit(initial_deposit);
		
		accountExists = dao.accountExists(customer.getUserId(), account_type);
		
		if(!accountExists) {
			dao.addAccount(customer, account);
			dao.addTransaction(customer.getUserId(), account_type, "initial_deposit", 0, 0, initial_deposit);
			request.setAttribute("accounts", dao.getAccounts(customer.getUserId()));
			request.getRequestDispatcher("/WEB-INF/views/overview.jsp").forward(request, response);  
		}else {
			request.setAttribute("errorMessage", "You already have a " + account_type + " account.");
			request.getRequestDispatcher("/WEB-INF/views/newaccount.jsp").forward(request, response); 
		}
		
	}

}
