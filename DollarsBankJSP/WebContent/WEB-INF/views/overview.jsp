<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.dollarsbank.models.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/css/mainstyle.css"%></style>
<title>Account Overview</title>
</head>
<body class="customerMain">
	<div>
		<nav>
            <div class="bank">Dollars Bank</div>
                <ul class="nav">
                    <li><a href='<%=request.getContextPath()%>/myaccount' class='link'>Account Overview</a></li>
                    <li><a href='<%=request.getContextPath()%>/transaction' class='link'>Deposit/Withdraw</a></li>
                    <li><a href='<%=request.getContextPath()%>/transfer' class='link'>Transfer</a></li>
                    <li><a href='<%=request.getContextPath()%>/transactions' class='link'>Recent Transactions</a></li>
                    <li><a href='<%=request.getContextPath()%>/accountinfo' class='link'>Account Information</a></li>
                    <li><a href='<%=request.getContextPath()%>/logout' class='link'>Log Out</a></li>
                </ul>
        </nav>
	</div>
	<div>
		<h3>Hello, ${customer.name}</h3>
		
			<%! @SuppressWarnings("unchecked") %>
			<% List<Account> accounts = (ArrayList<Account>) request.getAttribute("accounts");
 
		    for(Account account : accounts)
		    {
		    	%><div class='accountcontainer'>
					<div class='balance'>
						<p>Account ID: <%out.print(account.getAccountId()); %> </p>
						<p>Account Type: <%out.print(account.getAccountType());%></p> 
	                    <p>Balance: $<%out.print(account.getAccountBalance());%></p>
	                </div>
            	</div>
            	<%
		    }
		 
			%>
			
			<div class='accountcontainer'>
				<a href="new-account">OPEN NEW ACCOUNT</a>
			</div>
    </div>
</body>
</html>