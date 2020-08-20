package com.dollarsbank.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dollarsbank.dao.*;
import com.dollarsbank.models.Customer;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DollarsBankDao dao = new DollarsBankDao();

    public RegisterServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//set user fields with parameters from registration text box
		String userId = request.getParameter("username");
		String pw = request.getParameter("password");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		Boolean customerExists = false;
		
		Customer customer = new Customer();
		
			customer.setUserId(userId);
			customer.setPassword(pw);
			customer.setName(name);
			customer.setPhone(phone);
			customer.setAddress(address);
		
		//check if user's username or email is already taken
		try {
			customerExists = dao.checkCustomer(customer);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//only try to insert new user if the check that user doesn't already exist passes
		if (customerExists == false) {
			try {
				dao.registerCustomer(customer);
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		//if the username or email is already taken, display error without adding new user, else, new user is created and sent back to login
		if (customerExists == true) {
			
			request.setAttribute("errorMessage", "Username or Phone already exists");
			request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response); 
			
		}else {
		
			request.setAttribute("Message", "Account Created");
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);  
			
		}

	}

}