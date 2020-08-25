package com.dollarsbank.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dollarsbank.models.Customer;

@WebServlet("/accountinfo")
public class AccountInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AccountInfoServlet() {
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
			request.setAttribute("customer", customer);
			request.getRequestDispatcher("/WEB-INF/views/accountinfo.jsp").forward(request, response);
		}else {
			
			response.sendRedirect("login");
		}
	}

}
