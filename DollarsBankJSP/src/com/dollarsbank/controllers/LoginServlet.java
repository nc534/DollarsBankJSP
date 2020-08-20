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

@WebServlet("/")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DollarsBankDao dao;

	public void init() {
		dao = new DollarsBankDao();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//get the username and password inputted in text box
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		
		//create user bean
		Customer customer = new Customer();
		
		//set the username and password to this user
		customer.setUserId(userName);
		customer.setPassword(password);
		
		try {
			
			//check if username and password match the records
			if(dao.findCustomer(customer)) {
				
				//create a session for current user
				HttpSession session = request.getSession();
				session.setAttribute("customer", userName);
				
				//send user to user account page
				response.sendRedirect("customer");
		
			}else {
				
				//send user to login page with error message
				request.setAttribute("errorMessage", "Wrong username and password. If you don't have an account, please register.");
				request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);  
			}
			
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}