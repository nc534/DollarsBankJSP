package com.dollarsbank.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dollarsbank.dao.DollarsBankDao;
import com.dollarsbank.models.Customer;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TransactionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		Customer customer = (Customer) session.getAttribute("customer");
		
		DollarsBankDao dao = new DollarsBankDao();
		
		//to prevent from accessing page after logging out
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);
		
        if (customer != null) {
			request.setAttribute("transactions", dao.getTransactions(customer.getUserId()));
			request.getRequestDispatcher("/WEB-INF/views/transactions.jsp").forward(request, response);
        }else {
			
			response.sendRedirect("login");
		}
	}


}
