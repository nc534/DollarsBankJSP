package com.dollarsbank.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dollarsbank.dao.DollarsBankDao;

@WebServlet("/myaccount")
public class OverviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public OverviewServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
				//do not create a new session if session is null
				HttpSession session = request.getSession(false);
				String customer = (String) session.getAttribute("customer");
				
				//to prevent from accessing page after logging out
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		        response.setDateHeader("Expires", 0);
		        
		        DollarsBankDao dao = new DollarsBankDao();
				
		        //if the user's session is not null, go to the user account page, else, go to login page
				if (customer != null) {
					request.getSession().setAttribute("customer", dao.getCustomerInfo(customer));
					request.setAttribute("accounts", dao.getAccounts(customer));
					request.getRequestDispatcher("/WEB-INF/views/overview.jsp").forward(request, response);
				
				}else {
					
					response.sendRedirect("login");
				}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
